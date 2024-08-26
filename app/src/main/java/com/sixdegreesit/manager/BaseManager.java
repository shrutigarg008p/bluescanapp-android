package com.sixdegreesit.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.db.DaoMaster;
import com.android.db.DaoMaster.DevOpenHelper;
import com.android.db.DaoSession;

public class BaseManager {
	
	public static final String LOG_TAG = "BaseManager";

	public static DaoSession getDBSessoin(Context context) {
		long startTime = System.currentTimeMillis();
		if (context != null) {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "SecurityApp.sqlite", null);
			SQLiteDatabase db = helper.getWritableDatabase();
			DaoMaster daoMaster = new DaoMaster(db);
			DaoSession daoSession = daoMaster.newSession();
			if (daoSession != null) {
				return daoSession;
			} else {
				Log.i(LOG_TAG, "getDBSessoin:  - Application context is null");
				return null;
			}
		}
		Log.i(LOG_TAG,"getDBSessoin: Execution time - "+(System.currentTimeMillis() - startTime));
		return null;
	}

	public void closeDatabase(Context context) {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(
				context, "SecurityApp.sqlite", null);
		helper.close();
	}

}
