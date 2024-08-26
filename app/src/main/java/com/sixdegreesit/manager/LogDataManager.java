package com.sixdegreesit.manager;

import java.util.Iterator;
import java.util.List;

import android.content.Context;

import com.android.db.DaoSession;
import com.android.db.LogsData;
import com.android.db.LogsDataDao;

public class LogDataManager extends BaseManager {
    
	private Context context;
	String serviceUrl="",responseString="";
	String TAG = getClass().getSimpleName();

    public LogDataManager(Context context) {
        super();
        this.context = context;
    }
    
    public void insertData(LogsData entity){
    	DaoSession daoSession = getDBSessoin(context);
        LogsDataDao logsDataDao= daoSession.getLogsDataDao();
        logsDataDao.insertOrReplace(entity);
        logsDataDao.getDatabase().close();
    }
    
    public List<LogsData> getLogDataByUserID(String userId) {
        DaoSession daoSession = getDBSessoin(context);
        LogsDataDao logsDataDao= daoSession.getLogsDataDao();
        List<LogsData> logsDatas = null;
        try {
        	logsDatas = logsDataDao.queryBuilder().where(LogsDataDao.Properties.UserId.eq(userId),
        			LogsDataDao.Properties.IsSynced.eq("0")).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	logsDataDao.getDatabase().close();
        }
        return logsDatas;
    }
    
    public List<LogsData> getLogDataByID(long id) {
        DaoSession daoSession = getDBSessoin(context);
        LogsDataDao logsDataDao= daoSession.getLogsDataDao();
        List<LogsData> logsDatas = null;
        try {
        	logsDatas = logsDataDao.queryBuilder().where(LogsDataDao.Properties.Id.eq(id)).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	logsDataDao.getDatabase().close();
        }
        return logsDatas;
    }
    
    public void updateIsSyncById(long id,String status) {
    	DaoSession daoSession = getDBSessoin(context);
        LogsDataDao logsDataDao= daoSession.getLogsDataDao();
        List<LogsData> logsDatas = getLogDataByID(id);
        try {
			Iterator<LogsData> iterator = logsDatas.iterator();
			while (iterator.hasNext()) {
				LogsData logsDataObject = (LogsData) iterator.next();
				logsDataObject.setIsSynced(status);
				logsDataDao.insertOrReplace(logsDataObject);
			}
		}catch (Exception e) {
            e.printStackTrace();
        } finally {
        	logsDataDao.getDatabase().close();
        }
    }
  
     
}
