package com.sixdegreesit.securityapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.db.LogsData;
import com.sixdegreesit.manager.LogDataManager;
import com.sixdegreesit.manager.LoginManager;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.ConnectionDetector;
import com.sixdegreesit.utils.GPSTracker;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.SharedPrefrenceManager;
import com.sixdegreesit.utils.BaseActivity.TaskSyncData;

public class CustomInspection extends BaseActivity implements View.OnClickListener{

    Button submitButton;
    EditText editText;
    String descString="";
    BroadcastReceiver broadcastReceiver=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_custins);

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        mCustomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView menuImageView=(ImageView)mCustomView.findViewById(R.id.menu_ImageView);
        menuImageView.setVisibility(View.INVISIBLE);
        ImageView syncImageView=(ImageView)mCustomView.findViewById(R.id.sync_ImageView);
        syncImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				popupOptionMenu(v);
				if (new ConnectionDetector(CustomInspection.this).isConnectingToInternet()) {
           		 new  TaskSyncData().execute();
				} else {
					Toast.makeText(CustomInspection.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				} 
			}
		});
        ImageView logoutImageView=(ImageView)mCustomView.findViewById(R.id.logout_ImageView);
        logoutImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutDailogBox(CustomInspection.this);
			}
		});
        TextView title = (TextView) mCustomView.findViewById(R.id.title_text);
        title.setText("Custom Inspection");
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        editText=(EditText)findViewById(R.id.et_desc);
        submitButton=(Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitButton:
                descString=editText.getText().toString().trim();
                if (descString.length()==0){
                    Toast.makeText(CustomInspection.this,"Please enter description.",Toast.LENGTH_LONG).show();
                }else{
                	GPSTracker gpsTracker = new GPSTracker(CustomInspection.this);
                    if (new ConnectionDetector(CustomInspection.this).isConnectingToInternet()) {
                    	  new SendCustomInspection().execute(""+gpsTracker.getLatitude(),""+gpsTracker.getLongitude(),descString);
					} else {
						String params="param=saveCustomInspection&lat="+gpsTracker.getLatitude()+"&long="+gpsTracker.getLongitude()
								+"&description="+descString+"&datetime="+getCurrentDate("yyyy-MM-dd kk:mm:ss");
						LogDataManager dataManager=new LogDataManager(CustomInspection.this);
						LogsData logsData=new LogsData(null, SharedPrefrenceManager.getMemberDetailsFromSP(CustomInspection.this).get(KEYS.USER_ID),
								params, "0", dateFromString("yyyy-MM-dd kk:mm:ss", getCurrentDate("yyyy-MM-dd kk:mm:ss")));
						dataManager.insertData(logsData);
						Toast.makeText(CustomInspection.this,"Inspection saved successfully, Please sync when network available.",Toast.LENGTH_LONG).show();
		                finish();
					}
                  
                }
                break;
        }
    }

    public class SendCustomInspection extends AsyncTask<String,Void,String> {
        ProgressDialog loginDialog;
        String returnResponse=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginDialog=ProgressDialog.show(CustomInspection.this,"","Please Wait");
        }

        @Override
        protected String doInBackground(String... params) {
           LoginManager loginManager=new LoginManager(CustomInspection.this);
            returnResponse=loginManager.saveCustomInspection(params[0],params[1],params[2],
            		getCurrentDate("yyyy-MM-dd kk:mm:ss"));
            return returnResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loginDialog.dismiss();
            if (returnResponse.equals("100")) {
                Toast.makeText(CustomInspection.this,"Inspection done successfully.",Toast.LENGTH_LONG).show();
                finish();
            }else if(returnResponse.equals(getResources().getString(R.string.network_errror))){
            	GPSTracker gpsTracker = new GPSTracker(CustomInspection.this);
            	String params="param=saveCustomInspection&lat="+gpsTracker.getLatitude()+"&long="+gpsTracker.getLongitude()
						+"&description="+descString+"&datetime="+getCurrentDate("yyyy-MM-dd kk:mm:ss");
				LogDataManager dataManager=new LogDataManager(CustomInspection.this);
				LogsData logsData=new LogsData(null, SharedPrefrenceManager.getMemberDetailsFromSP(CustomInspection.this).get(KEYS.USER_ID),
						params, "0", dateFromString("yyyy-MM-dd kk:mm:ss", getCurrentDate("yyyy-MM-dd kk:mm:ss")));
				dataManager.insertData(logsData);
				Toast.makeText(CustomInspection.this,"Inspection saved successfully, Please sync when network available.",Toast.LENGTH_LONG).show();
                finish();
            }else{
            	 Toast.makeText(CustomInspection.this,returnResponse,Toast.LENGTH_LONG).show();
            }
        }

    }

}
