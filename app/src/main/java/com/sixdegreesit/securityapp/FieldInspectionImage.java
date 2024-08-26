package com.sixdegreesit.securityapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sixdegreesit.manager.SiteManager;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.ConnectionDetector;
import com.sixdegreesit.utils.BaseActivity.TaskSyncData;

public class FieldInspectionImage extends BaseActivity implements View.OnClickListener{  
    
    Intent intent = null;   
    TextView titleTextView,questionTextView;
    Button doneButton,addImage;
    
    public static ArrayList<String> f = new ArrayList<String>();
    File[] listFile;   
    // For camera
    Bitmap mBitmap = null;
    private Uri fileUri;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Salaria";
    GridView gridView;
    BroadcastReceiver broadcastReceiver=null;
    
    String questionId="",question="";
    int groupPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_guard_image);     

        intent=getIntent();
        if (intent!=null) {
			questionId=intent.getStringExtra("questionId");
			question=intent.getStringExtra("question");
			groupPosition=intent.getIntExtra("groupPosition", 0);
		}
        
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        mCustomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        titleTextView.setText("Images");
        ImageView menuImageView = (ImageView) mCustomView.findViewById(R.id.menu_ImageView);
        menuImageView.setVisibility(View.INVISIBLE);    
        ImageView syncImageView=(ImageView)mCustomView.findViewById(R.id.sync_ImageView);
        syncImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				popupOptionMenu(v);
				if (new ConnectionDetector(FieldInspectionImage.this).isConnectingToInternet()) {
           		 new  TaskSyncData().execute();
				} else {
					Toast.makeText(FieldInspectionImage.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				} 
			}
		});
        ImageView logoutImageView=(ImageView)mCustomView.findViewById(R.id.logout_ImageView);
        logoutImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutDailogBox(FieldInspectionImage.this);
			}
		});
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);        
        
        questionTextView=(TextView)findViewById(R.id.questionTextView);
        questionTextView.setText(question);
        doneButton = (Button) findViewById(R.id.addGuardImageDoneButton);
        doneButton.setOnClickListener(this);
        addImage = (Button) findViewById(R.id.addGuardImageButton);
        addImage.setOnClickListener(this);

        getFromSdcard();
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this));
       
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StringBuilder stringBuilder=new StringBuilder();
        for (int i=0;i<f.size();i++) {
            if (i == 0)
                stringBuilder.append(f.get(i));
            else
                stringBuilder.append("," + f.get(i));
        }
        Inspection.questionsList.get(groupPosition).setAnswer(stringBuilder.toString());
        SiteManager siteManager=new SiteManager(FieldInspectionImage.this);
        siteManager.updateAnswerByQueId(questionId, stringBuilder.toString());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.addGuardImageDoneButton:
                StringBuilder stringBuilder=new StringBuilder();
                for (int i=0;i<f.size();i++){
                    if (i==0)
                        stringBuilder.append(f.get(i));
                    else
                        stringBuilder.append(","+f.get(i));
                }
                Inspection.questionsList.get(groupPosition).setAnswer(stringBuilder.toString());
                SiteManager siteManager=new SiteManager(FieldInspectionImage.this);
                siteManager.updateAnswerByQueId(questionId, stringBuilder.toString());
                finish();
                break;            
            case R.id.addGuardImageButton:
                captureImage();
                break;            
            default:
                break;
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 250));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(8, 8, 8, 8);
                Bitmap myBitmap = decodeFile(new File(f.get(position)),200, 200);
                imageView.setImageBitmap(myBitmap);
            } else {
                imageView = (ImageView) convertView;
            }
            return imageView;
        }
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        getFromSdcard();
        gridView.setAdapter(new ImageAdapter(this));
    }

    public void getFromSdcard() {
        f = new ArrayList<String>();
        File file = new File(android.os.Environment.getExternalStorageDirectory(),
                "Salaria/Site");
        if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getAbsolutePath());
            }
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
   
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    
    private static File getOutputMediaFile(int type) {
        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root + "/Salaria/Site");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "shippment_item" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
              previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
            } else {
            	Toast.makeText(FieldInspectionImage.this, "Sorry! Failed to capture image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),options);
            mBitmap = bitmap;
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
    }

}
