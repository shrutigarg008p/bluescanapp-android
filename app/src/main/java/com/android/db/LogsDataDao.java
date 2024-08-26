package com.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;

import com.android.db.LogsData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LOGS_DATA.
*/
public class LogsDataDao extends AbstractDao<LogsData, Long> {

    public static final String TABLENAME = "LOGS_DATA";

    /**
     * Properties of entity LogsData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property Parameters = new Property(2, String.class, "parameters", false, "PARAMETERS");
        public final static Property IsSynced = new Property(3, String.class, "isSynced", false, "IS_SYNCED");
        public final static Property DateAdded = new Property(4, java.util.Date.class, "dateAdded", false, "DATE_ADDED");
    };


    public LogsDataDao(DaoConfig config) {
        super(config);
    }
    
    public LogsDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LOGS_DATA' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'USER_ID' TEXT," + // 1: userId
                "'PARAMETERS' TEXT," + // 2: parameters
                "'IS_SYNCED' TEXT," + // 3: isSynced
                "'DATE_ADDED' INTEGER);"); // 4: dateAdded
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LOGS_DATA'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, LogsData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String parameters = entity.getParameters();
        if (parameters != null) {
            stmt.bindString(3, parameters);
        }
 
        String isSynced = entity.getIsSynced();
        if (isSynced != null) {
            stmt.bindString(4, isSynced);
        }
 
        java.util.Date dateAdded = entity.getDateAdded();
        if (dateAdded != null) {
            stmt.bindLong(5, dateAdded.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public LogsData readEntity(Cursor cursor, int offset) {
        LogsData entity = new LogsData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // parameters
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // isSynced
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)) // dateAdded
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, LogsData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setParameters(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIsSynced(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDateAdded(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(LogsData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(LogsData entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
