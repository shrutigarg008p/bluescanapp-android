package com.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;

import com.android.db.SiteMaster;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SITE_MASTER.
*/
public class SiteMasterDao extends AbstractDao<SiteMaster, String> {

    public static final String TABLENAME = "SITE_MASTER";

    /**
     * Properties of entity SiteMaster.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Site_id = new Property(0, String.class, "site_id", true, "SITE_ID");
        public final static Property Qr_code = new Property(1, String.class, "qr_code", false, "QR_CODE");
        public final static Property Address = new Property(2, String.class, "address", false, "ADDRESS");
        public final static Property Zipcode = new Property(3, String.class, "zipcode", false, "ZIPCODE");
        public final static Property City = new Property(4, String.class, "city", false, "CITY");
        public final static Property Contact_person = new Property(5, String.class, "contact_person", false, "CONTACT_PERSON");
        public final static Property Contact_number = new Property(6, String.class, "contact_number", false, "CONTACT_NUMBER");
        public final static Property Email_id = new Property(7, String.class, "email_id", false, "EMAIL_ID");
        public final static Property Branch_id = new Property(8, String.class, "branch_id", false, "BRANCH_ID");
        public final static Property Branch_name = new Property(9, String.class, "branch_name", false, "BRANCH_NAME");
        public final static Property Customer_name = new Property(10, String.class, "customer_name", false, "CUSTOMER_NAME");
        public final static Property Company_name = new Property(11, String.class, "company_name", false, "COMPANY_NAME");
        public final static Property Guard = new Property(12, String.class, "guard", false, "GUARD");
        public final static Property SiteName = new Property(13, String.class, "siteName", false, "SITE_NAME");
    };


    public SiteMasterDao(DaoConfig config) {
        super(config);
    }
    
    public SiteMasterDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SITE_MASTER' (" + //
                "'SITE_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: site_id
                "'QR_CODE' TEXT," + // 1: qr_code
                "'ADDRESS' TEXT," + // 2: address
                "'ZIPCODE' TEXT," + // 3: zipcode
                "'CITY' TEXT," + // 4: city
                "'CONTACT_PERSON' TEXT," + // 5: contact_person
                "'CONTACT_NUMBER' TEXT," + // 6: contact_number
                "'EMAIL_ID' TEXT," + // 7: email_id
                "'BRANCH_ID' TEXT," + // 8: branch_id
                "'BRANCH_NAME' TEXT," + // 9: branch_name
                "'CUSTOMER_NAME' TEXT," + // 10: customer_name
                "'COMPANY_NAME' TEXT," + // 11: company_name
                "'GUARD' TEXT," + // 12: guard
                "'SITE_NAME' TEXT);"); // 13: siteName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SITE_MASTER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SiteMaster entity) {
        stmt.clearBindings();
 
        String site_id = entity.getSite_id();
        if (site_id != null) {
            stmt.bindString(1, site_id);
        }
 
        String qr_code = entity.getQr_code();
        if (qr_code != null) {
            stmt.bindString(2, qr_code);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(3, address);
        }
 
        String zipcode = entity.getZipcode();
        if (zipcode != null) {
            stmt.bindString(4, zipcode);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(5, city);
        }
 
        String contact_person = entity.getContact_person();
        if (contact_person != null) {
            stmt.bindString(6, contact_person);
        }
 
        String contact_number = entity.getContact_number();
        if (contact_number != null) {
            stmt.bindString(7, contact_number);
        }
 
        String email_id = entity.getEmail_id();
        if (email_id != null) {
            stmt.bindString(8, email_id);
        }
 
        String branch_id = entity.getBranch_id();
        if (branch_id != null) {
            stmt.bindString(9, branch_id);
        }
 
        String branch_name = entity.getBranch_name();
        if (branch_name != null) {
            stmt.bindString(10, branch_name);
        }
 
        String customer_name = entity.getCustomer_name();
        if (customer_name != null) {
            stmt.bindString(11, customer_name);
        }
 
        String company_name = entity.getCompany_name();
        if (company_name != null) {
            stmt.bindString(12, company_name);
        }
 
        String guard = entity.getGuard();
        if (guard != null) {
            stmt.bindString(13, guard);
        }
 
        String siteName = entity.getSiteName();
        if (siteName != null) {
            stmt.bindString(14, siteName);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SiteMaster readEntity(Cursor cursor, int offset) {
        SiteMaster entity = new SiteMaster( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // site_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // qr_code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // address
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // zipcode
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // city
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // contact_person
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // contact_number
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // email_id
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // branch_id
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // branch_name
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // customer_name
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // company_name
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // guard
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // siteName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SiteMaster entity, int offset) {
        entity.setSite_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setQr_code(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAddress(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setZipcode(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCity(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setContact_person(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setContact_number(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setEmail_id(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBranch_id(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setBranch_name(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCustomer_name(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCompany_name(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setGuard(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setSiteName(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(SiteMaster entity, long rowId) {
        return entity.getSite_id();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(SiteMaster entity) {
        if(entity != null) {
            return entity.getSite_id();
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
