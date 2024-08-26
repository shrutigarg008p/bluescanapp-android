package com.sixdegreesit.securityapp;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.sixdegreesit.manager.LoginManager;
import com.sixdegreesit.securityapp.GoogleAnalyticsApp.TrackerName;
import com.sixdegreesit.utils.AppSession;
import com.sixdegreesit.utils.ConnectionDetector;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.SharedPrefrenceManager;

public class LoginActivity extends Activity implements OnClickListener {

	Button loginButton;
	EditText userNameEditText, passwordEditText, etServerURl;
	BroadcastReceiver broadcastReceiver = null;
	String serverURL = "http://app.bluescan.me";
	//String serverURL = "http://bluescandev.6degreesit.com";
	
	//String serverURL = "http://192.168.0.142/security_app";
	TextView tvVersion,tvAttendenceMode;
	ToggleButton toggleButton;
	CheckBox rememberCheckBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Tracker t = ((GoogleAnalyticsApp) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Login");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		loginButton = (Button) findViewById(R.id.submitButton);
		userNameEditText = (EditText) findViewById(R.id.userNameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordNameEditText);
		//etServerURl = (EditText) findViewById(R.id.etServer);
		//etServerURl.setText("http://secure.6degreesit.com/");
		//etServerURl.setText("http://192.168.0.142/security_app");
		
		//http://192.168.0.142/security_app/services?
		
//		etServerURl.setVisibility(View.GONE);
//		etServerURl.setText("http://securityapp.wsisrdev.com");
//		etServerURl.setText("http://app.bluescan.me");
		loginButton.setOnClickListener(this);
		
		tvAttendenceMode=(TextView)findViewById(R.id.tv_attendenceMode);
		
		HashMap<String, String> hashMap=new HashMap<String, String>();
		hashMap.put(KEYS.ATTENDENCE_MODE, "off");
		SharedPrefrenceManager.setMemberDetailsInSP(LoginActivity.this, hashMap);
		tvAttendenceMode.setText("Attendance Mode is OFF");
		
		toggleButton = (ToggleButton)findViewById(R.id.attendenceModeToggleButton);		
		toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	HashMap<String, String> hashMap=new HashMap<String, String>();
            	if (isChecked) {     
            	    hashMap.put(KEYS.ATTENDENCE_MODE, "on");
            	    tvAttendenceMode.setText("Attendance Mode is ON");
				} else {
					hashMap.put(KEYS.ATTENDENCE_MODE, "off");
					tvAttendenceMode.setText("Attendance Mode is OFF");
				}
               SharedPrefrenceManager.setMemberDetailsInSP(LoginActivity.this, hashMap);
            }
        });

		try {
			PackageManager manager = LoginActivity.this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					LoginActivity.this.getPackageName(), 0);
			String version = info.versionName;
			tvVersion = (TextView) findViewById(R.id.tv_version_login);
			tvVersion.setText("Version: " + version);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		  // Get Location Manager and check for GPS & Network location services
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
              !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
          // Build the alert dialog
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle("SecurityApp will use your location");
          builder.setMessage("Please enable Location Services and GPS");
          builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialogInterface, int i) {
            // Show location settings when the user acknowledges the alert dialog
        	  startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));          
            }
          });
          Dialog alertDialog = builder.create();
          alertDialog.setCanceledOnTouchOutside(false);
          alertDialog.show();
        }
        
        rememberCheckBox = (CheckBox) findViewById(R.id.remembars_me);
        
        AppSession appSession = new AppSession(LoginActivity.this);
  		String[] arr = appSession.getRemembarLoginData();
  		if (arr[2] != null && !arr[2].equals("") && arr[2].equals("true")) {
  			 String[] rememberArray=appSession.getRemembarLoginData();
  			 userNameEditText.setText((String) rememberArray[0]);
  			 passwordEditText.setText((String) rememberArray[1]);
  			 rememberCheckBox.setChecked(true);
  		}
        

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
		registerReceiver(broadcastReceiver, intentFilter);

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(LoginActivity.this).reportActivityStart(this);
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(LoginActivity.this).reportActivityStop(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (broadcastReceiver != null)
			unregisterReceiver(broadcastReceiver);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submitButton:
			//serverURL = etServerURl.getText().toString().trim();
			if (serverURL.length() == 0) {
				Toast.makeText(LoginActivity.this, "Please enter URL.",Toast.LENGTH_SHORT).show();
			} else if (!Patterns.WEB_URL.matcher(serverURL).matches()) {
				Toast.makeText(LoginActivity.this, "Please enter valid URL.",Toast.LENGTH_SHORT).show();
			} else if (userNameEditText.getText().toString().length() > 0 && passwordEditText.getText().toString().length() > 0) {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put(KEYS.BASE_URL, serverURL);
				SharedPrefrenceManager.setMemberDetailsInSP(LoginActivity.this,hashMap);
				if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
					new TaskUserLogin().execute(userNameEditText.getText()
							.toString(), passwordEditText.getText().toString());
				} else {
					Toast.makeText(LoginActivity.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(LoginActivity.this,"Please enter username and password.",Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	public class TaskUserLogin extends AsyncTask<String, Void, String> {
		ProgressDialog loginDialog;
		String returnResponse = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog = ProgressDialog.show(LoginActivity.this, "","Please Wait..");
		}
		
		@Override
		protected String doInBackground(String... params) {
			LoginManager loginManager = new LoginManager(LoginActivity.this);
			returnResponse = loginManager.validateUserNameandPassword(params[0], params[1], "12e4q3323242353427423978");
			return returnResponse;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			loginDialog.dismiss();
			if (returnResponse.equals("100")) {
				 AppSession appSession = new AppSession(LoginActivity.this);
					if (rememberCheckBox.isChecked()) {
						String[] arr = new String[] { userNameEditText.getText().toString(),passwordEditText.getText().toString(),
								"true" };
						appSession.storeRemembarLoginData(arr);
					} else {
						appSession.resetRemembarLoginData();
					}
				if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
					new TaskCmpnyData().execute();
				} else {
					Toast.makeText(LoginActivity.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(LoginActivity.this, returnResponse,Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public class TaskCmpnyData extends AsyncTask<String, String, String>{
		ProgressDialog loginDialog;
		String returnResponse = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog = ProgressDialog.show(LoginActivity.this, "","Please Wait..");
		}
		
		@Override
		protected String doInBackground(String... params) {
			LoginManager loginManager = new LoginManager(LoginActivity.this);
			returnResponse = loginManager.getAllCompanyData();
			return returnResponse;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			loginDialog.dismiss();
			
			if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
				new TaskdesignationData().execute();
			} else {
				Toast.makeText(LoginActivity.this,getResources().getString(R.string.network_errror),
						Toast.LENGTH_SHORT).show();
			}
			
		/*	Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			finish();*/
		}
	}
	
	public class TaskdesignationData extends AsyncTask<String, String, String>{
		ProgressDialog loginDialog;
		String returnResponse = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog = ProgressDialog.show(LoginActivity.this, "","Please Wait..");
		}
		
		@Override
		protected String doInBackground(String... params) {
			LoginManager loginManager = new LoginManager(LoginActivity.this);
			returnResponse = loginManager.getAllDesignationData();
			return returnResponse;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			loginDialog.dismiss();
			
		
			
	Intent intent = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
}
