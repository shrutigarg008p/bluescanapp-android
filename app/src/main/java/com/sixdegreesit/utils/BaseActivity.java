package com.sixdegreesit.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.db.LogsData;
import com.sixdegreesit.manager.LogDataManager;
import com.sixdegreesit.manager.LoginManager;
import com.sixdegreesit.securityapp.LoginActivity;
import com.sixdegreesit.securityapp.R;

public class BaseActivity extends Activity{
	
	private View pview = null;;
	private PopupWindow pw; 
	
	
	 public void popupOptionMenu(View v) {
			if (pview == null) {
				LayoutInflater inflater = (LayoutInflater) BaseActivity.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				pview = inflater.inflate(R.layout.popup,(ViewGroup) findViewById(R.layout.activity_login));
				if (pview != null)
					pw = new PopupWindow(pview);
				pw.setOutsideTouchable(true);
				pw.setTouchable(true);				
				// pw.showAtLocation(pview, Gravity.AXIS_SPECIFIED,100,-160);
				pw.showAsDropDown(v);
				pw.update(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

				pw.setTouchInterceptor(new OnTouchListener() {
					@SuppressLint("ClickableViewAccessibility") 
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {							
							pw.dismiss();							
							return false;
						}
						return true;
					}
				});
				
				ImageView ivSync = (ImageView) pview.findViewById(R.id.ivSync);
				ImageView ivLogout = (ImageView) pview.findViewById(R.id.ivLogout);
			
				ivSync.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (new ConnectionDetector(BaseActivity.this).isConnectingToInternet()) {
		            		 new  TaskSyncData().execute();
						} else {
							Toast.makeText(BaseActivity.this,getResources().getString(R.string.network_errror),
									Toast.LENGTH_SHORT).show();
						} 
							pw.dismiss();
							pview = null;												
					}
				});
				
				ivLogout.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						logoutDailogBox(BaseActivity.this);	
						pw.dismiss();
						pview = null;
					}
				});
				
				
			} else {
				pw.dismiss();
				pview = null;
			}
		}
	 

    public void logoutDailogBox(Context mCtx) {
        final Dialog dialog = new Dialog(mCtx);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvNO = (TextView) window.findViewById(R.id.no_yesno);
        TextView tvYes = (TextView) window.findViewById(R.id.yes_yesno);
        tvNO.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) { 
                dialog.dismiss();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	if (new ConnectionDetector(BaseActivity.this).isConnectingToInternet()) {
            		 new  logoutServerCall().execute();
				} else {
					Toast.makeText(BaseActivity.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				}              
               dialog.dismiss();
            }
        });
        dialog.show();
    }
    
    public class logoutServerCall extends AsyncTask<String,Void,String>{    	
        ProgressDialog logoutDialog;
        String returnResponse=null;
        
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            logoutDialog.dismiss();
            HashMap<String, String> hashMap=new HashMap<String, String>();
            hashMap.put(KEYS.SITE_IDATTEDENCE, "null");
            hashMap.put(KEYS.SITE_NAME, "null");
            hashMap.put(KEYS.COMPANY_NAME, "null");
            SharedPrefrenceManager.setMemberDetailsInSP(BaseActivity.this, hashMap);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("CLOSE_ALL");
            BaseActivity.this.sendBroadcast(broadcastIntent);
            ((Activity)BaseActivity.this).finish();
            Intent intent=new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);            
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            logoutDialog=ProgressDialog.show(BaseActivity.this,"","Please wait..");
        }

        @Override
        protected String doInBackground(String... params) {
            LoginManager loginManager=new LoginManager(BaseActivity.this);
            returnResponse=loginManager.logoutUser(SharedPrefrenceManager.getMemberDetailsFromSP(BaseActivity.this).get(KEYS.USER_ID));
            return returnResponse;
        }
        
    }
    
    
    public class TaskSyncData extends AsyncTask<String,Void,String>{    	
        ProgressDialog logoutDialog;
        String returnResponse=null;
        List<LogsData> logsDatas=null;
        
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            logoutDialog.dismiss();
            if (logsDatas.size()==0) {
				Toast.makeText(BaseActivity.this, "No records found for sync.", Toast.LENGTH_LONG).show();
			}
                    
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            logoutDialog=ProgressDialog.show(BaseActivity.this,"","Please wait..");
        }

        @Override
        protected String doInBackground(String... params) {
            LogDataManager logDataManager=new LogDataManager(BaseActivity.this);
            logsDatas=new ArrayList<LogsData>();
            logsDatas =logDataManager.getLogDataByUserID(SharedPrefrenceManager
            		.getMemberDetailsFromSP(BaseActivity.this).get(KEYS.USER_ID));
            String serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(BaseActivity.this).get(KEYS.BASE_URL)+"/services/?";
            for (int i = 0; i < logsDatas.size(); i++) {
            	String param=logsDatas.get(i).getParameters();
            	Log.i("TaskSyncData", "param="+param);
            	String[] arr=param.split("&");
            	MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            	for (int j = 0; j < arr.length; j++) {
            		try {			
            			String[] keyValue=arr[j].split("=");
            			if(keyValue.length<2)
            				reqEntity.addPart(keyValue[0], new StringBody(""));
            			else
            			reqEntity.addPart(keyValue[0], new StringBody(keyValue[1]));
            			
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
				}
            	returnResponse = ServerCalls.makeConnection(serviceUrl, reqEntity,
    					SharedPrefrenceManager.getMemberDetailsFromSP(BaseActivity.this).get(KEYS.SESSION_TOKEN));
            	parseResponseInfo(returnResponse,logsDatas.get(i).getId());
			}
            return returnResponse;
        }
        
    }
    
    private String parseResponseInfo(String jsonResponse, long id) {
  		String responseCode = "",responseString="";
  		try {
  			if (jsonResponse != null && !jsonResponse.equals(null)) {
  				Object object = new JSONTokener(jsonResponse).nextValue();
  				if (object instanceof JSONObject) {
  					JSONObject jsonObject = new JSONObject(jsonResponse);
  					if (jsonObject.has("responseCode")) {
  						responseCode = jsonObject.getString("responseCode");
  						if (responseCode.equalsIgnoreCase("100")) {
  							responseString = responseCode; 
  							LogDataManager dataManager=new LogDataManager(BaseActivity.this);
  							dataManager.updateIsSyncById(id, "1");
  						} else {
  							responseString = jsonObject.getString("responseData");
  						}
  					}else {
  						responseString=jsonObject.getString("responseData");
  					}
  				} else {
  					responseString=jsonResponse;
  				}
  			} else {
  				responseString = "Please check your internet connection.";
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		return responseString;
  	}	
    public static String getCurrentDate(String format){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Log.i("getCurrentDate", "Current Time: "+sdf.format(c.getTime()));
        return sdf.format(c.getTime());
    }
    
    public static Date dateFromString(String formatString, String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString,Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());
        format.setLenient(false);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    public static Bitmap decodeFile(File f, int REQUIRED_WIDTH,
                                    int REQUIRED_HEIGHT) {
        try {
            ExifInterface exif = new ExifInterface(f.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            int angle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }
            Matrix mat = new Matrix();
            mat.postRotate(angle);
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // Find the correct scale value. It should be the power of 2.
            int REQUIRED_SIZE = 100;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            if (width_tmp > height_tmp) {
                REQUIRED_SIZE = REQUIRED_HEIGHT;
                REQUIRED_HEIGHT = REQUIRED_WIDTH;
                REQUIRED_WIDTH = REQUIRED_SIZE;
            }
            while (true) {
                if (width_tmp / 2 < REQUIRED_WIDTH
                        && height_tmp / 2 < REQUIRED_HEIGHT)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inPurgeable = true;
            Bitmap correctBmp = BitmapFactory.decodeStream(new FileInputStream(
                    f), null, o2);
            correctBmp = Bitmap.createBitmap(correctBmp, 0, 0,
                    correctBmp.getWidth(), correctBmp.getHeight(), mat, true);
            return correctBmp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
