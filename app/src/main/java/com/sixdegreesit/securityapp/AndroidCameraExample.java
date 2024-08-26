package com.sixdegreesit.securityapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.db.LogsData;
import com.sixdegreesit.manager.GaurdManager;
import com.sixdegreesit.manager.LogDataManager;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.CameraPreview;
import com.sixdegreesit.utils.ConnectionDetector;
import com.sixdegreesit.utils.GPSTracker;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.SharedPrefrenceManager;

public class AndroidCameraExample extends BaseActivity {
	private Camera mCamera;
	private CameraPreview mPreview;
	private PictureCallback mPicture;
	private Button capture;
	private Context myContext;
	private LinearLayout cameraPreview;
	private boolean cameraFront = false;
	BroadcastReceiver broadcastReceiver=null;
	Intent intent=null;
	String guardId="",encodedString ="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		myContext = this;
		initialize();

		intent=getIntent();
		if (intent!=null) {
			guardId=intent.getStringExtra(KEYS.GUARD_ID);
		}
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		broadcastReceiver=new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
		registerReceiver(broadcastReceiver, intentFilter);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(broadcastReceiver!=null)
			unregisterReceiver(broadcastReceiver);
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				break;
			}
		}
		return cameraId;
	}

	

	public void onResume() {
		super.onResume();
		if (!hasCamera(myContext)) {
			Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
		if (mCamera == null) {
			//if the front facing camera does not exist
			if (findFrontFacingCamera() < 0) {
				Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();				
			}			
			mCamera = Camera.open(findFrontFacingCamera());
			mPicture = getPictureCallback();
			mPreview.refreshCamera(mCamera);
		}
	}

	public void initialize() {
		cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
		mPreview = new CameraPreview(myContext, mCamera);
		cameraPreview.addView(mPreview);
		capture = (Button) findViewById(R.id.button_capture);
		capture.setOnClickListener(captrureListener);
	}

	public void chooseCamera() {
		//if the camera preview is the front
		if (cameraFront) {
			int cameraId = findFrontFacingCamera();
			if (cameraId >= 0) {
				mCamera = Camera.open(cameraId);				
				mPicture = getPictureCallback();			
				mPreview.refreshCamera(mCamera);
			}
		} 
	}

	@Override
	protected void onPause() {
		super.onPause();
		//when on Pause, release camera in order to be used from other applications
		releaseCamera();
	}

	private boolean hasCamera(Context context) {
		//check if the device has camera
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	private PictureCallback getPictureCallback() {
		PictureCallback picture = new PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				//make a new picture file
				File pictureFile = getOutputMediaFile();				
				if (pictureFile == null) {
					return;
				}
				try {
					//write the file
					FileOutputStream fos = new FileOutputStream(pictureFile);
					fos.write(data);
					fos.close();
					Bitmap bitmap=BaseActivity.decodeFile(pictureFile, 200, 200);
					if (bitmap!=null) {
						ByteArrayOutputStream bao = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
						byte[] ba = bao.toByteArray();
						encodedString = Base64.encodeToString(ba, Base64.DEFAULT);
						GPSTracker gpsTracker=new GPSTracker(AndroidCameraExample.this);
						if (new ConnectionDetector(AndroidCameraExample.this).isConnectingToInternet()) {
							//latitude, longitude, image
							new SendImageTask().execute(""+gpsTracker.getLatitude(),""+gpsTracker.getLongitude(),encodedString);
						} else {
							LogDataManager dataManager=new LogDataManager(AndroidCameraExample.this);
							String parameter="param=saveGuardAttendanceAndImage&guard_id="+guardId+"&site_id="+
							SharedPrefrenceManager.getMemberDetailsFromSP(AndroidCameraExample.this).get(KEYS.SITE_IDATTEDENCE)+"&latitude="+gpsTracker.getLatitude()
							+"&longitude="+gpsTracker.getLongitude()+"&datetime="+getCurrentDate("yyyy-MM-dd kk:mm:ss")+"&image="+encodedString;
							LogsData logsData=new LogsData(null, SharedPrefrenceManager.getMemberDetailsFromSP(AndroidCameraExample.this)
									.get(KEYS.USER_ID), parameter, "0", dateFromString("yyyy-MM-dd kk:mm:ss", getCurrentDate("yyyy-MM-dd kk:mm:ss")));
							dataManager.insertData(logsData);
							Toast.makeText(AndroidCameraExample.this,"Guard attendence saved successfully, Please sync when network available.",Toast.LENGTH_LONG).show();
							mPreview.refreshCamera(mCamera);
							finish();
						}		
					}else{
						Toast.makeText(AndroidCameraExample.this, "Sorry! Failed to capture image", Toast.LENGTH_LONG).show();
					}								
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}								
			}
		};
		return picture;
	}

	OnClickListener captrureListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mCamera.takePicture(null, null, mPicture);
		}
	};

	//make picture and save to a folder
	private static File getOutputMediaFile() {
		//make a new file directory inside the "sdcard" folder
		String IMAGE_PATH = Environment.getExternalStorageDirectory()+ "/Salaria";
		File mediaStorageDir = new File(IMAGE_PATH);
		
		//if this "JCGCamera folder does not exist
		if (!mediaStorageDir.exists()) {
			//if you cannot make this folder return
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		
		//take the current timeStamp
//		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		//and make a media file:
		mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_Gaurd" + ".jpg");
		return mediaFile;
	}

	private void releaseCamera() {
		// stop and release camera
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	public class SendImageTask extends AsyncTask<String,Void,String>{
		ProgressDialog loginDialog;
		String returnResponse=null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog = ProgressDialog.show(AndroidCameraExample.this, "", "Please Wait");
		}

		@Override
		protected String doInBackground(String... params) {
			GaurdManager gaurdManager=new GaurdManager(AndroidCameraExample.this);
			returnResponse=gaurdManager.saveGuardAttendanceAndImage(guardId, params[0], params[1], params[2],
					getCurrentDate("yyyy-MM-dd kk:mm:ss"),SharedPrefrenceManager
					.getMemberDetailsFromSP(AndroidCameraExample.this).get(KEYS.SITE_IDATTEDENCE));
			return returnResponse;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			loginDialog.dismiss();
			if (returnResponse.equals("100")){
				Toast.makeText(AndroidCameraExample.this,"Guard attendence marked successfully.",Toast.LENGTH_LONG).show();
				//refresh camera to continue preview
				mPreview.refreshCamera(mCamera);
				finish();
			}else if(returnResponse.equals(getResources().getString(R.string.network_errror))){
				GPSTracker gpsTracker=new GPSTracker(AndroidCameraExample.this);
				LogDataManager dataManager=new LogDataManager(AndroidCameraExample.this);
				String parameter="param=saveGuardAttendanceAndImage&guard_id="+guardId+"&site_id="+
				SharedPrefrenceManager.getMemberDetailsFromSP(AndroidCameraExample.this).get(KEYS.SITE_IDATTEDENCE)+"&latitude="+gpsTracker.getLatitude()
				+"&longitude="+gpsTracker.getLongitude()+"&datetime="+getCurrentDate("yyyy-MM-dd kk:mm:ss")+"&image="+encodedString;
				
				LogsData logsData=new LogsData(null, SharedPrefrenceManager.getMemberDetailsFromSP(AndroidCameraExample.this)
						.get(KEYS.USER_ID), parameter, "0", dateFromString("yyyy-MM-dd kk:mm:ss", getCurrentDate("yyyy-MM-dd kk:mm:ss")));
				dataManager.insertData(logsData);
				Toast.makeText(AndroidCameraExample.this,"Guard attendence saved successfully, Please sync when network available.",Toast.LENGTH_LONG).show();
				finish();
			}else{
				Toast.makeText(AndroidCameraExample.this,returnResponse,Toast.LENGTH_LONG).show();
			}
		}
	}
	
}