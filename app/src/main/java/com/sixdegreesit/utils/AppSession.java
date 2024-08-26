package com.sixdegreesit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class AppSession {
	private static String TAG = AppSession.class.getSimpleName();
	private SharedPreferences sharedPref;
	private Editor editor;

	private static final String SHARED = "BlueScan_Preferences";
	private static final String LOGIN_ID = "login_id";
	private static final String LOGIN_PASSWORD = "password";
	private static final String IS_REMEMBAR = "is_remembar";

	public AppSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		editor = sharedPref.edit();
	}

	public void storeRemembarLoginData(String[] loginData) {
		editor.putString(LOGIN_ID, loginData[0]);
		editor.putString(LOGIN_PASSWORD, loginData[1]);
		editor.putString(IS_REMEMBAR, loginData[2]);
		editor.commit();
		Log.v(TAG, ".....storeRemembarLoginData.....");
	}

	public void resetRemembarLoginData() {
		editor.putString(LOGIN_ID, null);
		editor.putString(LOGIN_PASSWORD, null);
		editor.putString(IS_REMEMBAR, null);
		editor.commit();
		Log.v(TAG, "......resetRemembarLoginData......");
	}

	public String[] getRemembarLoginData() {

		String[] loginData = new String[] { "", "", "" };
		String login_id = sharedPref.getString(LOGIN_ID, "");
		String password = sharedPref.getString(LOGIN_PASSWORD, "");
		String is_remembar = sharedPref.getString(IS_REMEMBAR, "");
		Log.v(TAG, ".....getRemembarLoginData.....");

		loginData[0] = login_id;
		loginData[1] = password;
		loginData[2] = is_remembar;

		return loginData;
	}

}