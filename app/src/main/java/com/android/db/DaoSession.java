package com.android.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.IdentityScopeType;

import com.android.db.User;
import com.android.db.Site;
import com.android.db.Guard;
import com.android.db.Questions;
import com.android.db.LogsData;
import com.android.db.SiteMaster;
import com.android.db.GaurdMaster;
import com.android.db.MasterQuestions;
import com.android.db.DesignationData;

import com.android.db.UserDao;
import com.android.db.SiteDao;
import com.android.db.GuardDao;
import com.android.db.QuestionsDao;
import com.android.db.LogsDataDao;
import com.android.db.SiteMasterDao;
import com.android.db.GaurdMasterDao;
import com.android.db.MasterQuestionsDao;
import com.android.db.DesignationDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig siteDaoConfig;
    private final DaoConfig guardDaoConfig;
    private final DaoConfig questionsDaoConfig;
    private final DaoConfig logsDataDaoConfig;
    private final DaoConfig siteMasterDaoConfig;
    private final DaoConfig gaurdMasterDaoConfig;
    private final DaoConfig masterQuestionsDaoConfig;
    private final DaoConfig designationDataDaoConfig;

    private final UserDao userDao;
    private final SiteDao siteDao;
    private final GuardDao guardDao;
    private final QuestionsDao questionsDao;
    private final LogsDataDao logsDataDao;
    private final SiteMasterDao siteMasterDao;
    private final GaurdMasterDao gaurdMasterDao;
    private final MasterQuestionsDao masterQuestionsDao;
    private final DesignationDataDao designationDataDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        siteDaoConfig = daoConfigMap.get(SiteDao.class).clone();
        siteDaoConfig.initIdentityScope(type);

        guardDaoConfig = daoConfigMap.get(GuardDao.class).clone();
        guardDaoConfig.initIdentityScope(type);

        questionsDaoConfig = daoConfigMap.get(QuestionsDao.class).clone();
        questionsDaoConfig.initIdentityScope(type);

        logsDataDaoConfig = daoConfigMap.get(LogsDataDao.class).clone();
        logsDataDaoConfig.initIdentityScope(type);

        siteMasterDaoConfig = daoConfigMap.get(SiteMasterDao.class).clone();
        siteMasterDaoConfig.initIdentityScope(type);

        gaurdMasterDaoConfig = daoConfigMap.get(GaurdMasterDao.class).clone();
        gaurdMasterDaoConfig.initIdentityScope(type);

        masterQuestionsDaoConfig = daoConfigMap.get(MasterQuestionsDao.class).clone();
        masterQuestionsDaoConfig.initIdentityScope(type);

        designationDataDaoConfig = daoConfigMap.get(DesignationDataDao.class).clone();
        designationDataDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        siteDao = new SiteDao(siteDaoConfig, this);
        guardDao = new GuardDao(guardDaoConfig, this);
        questionsDao = new QuestionsDao(questionsDaoConfig, this);
        logsDataDao = new LogsDataDao(logsDataDaoConfig, this);
        siteMasterDao = new SiteMasterDao(siteMasterDaoConfig, this);
        gaurdMasterDao = new GaurdMasterDao(gaurdMasterDaoConfig, this);
        masterQuestionsDao = new MasterQuestionsDao(masterQuestionsDaoConfig, this);
        designationDataDao = new DesignationDataDao(designationDataDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(Site.class, siteDao);
        registerDao(Guard.class, guardDao);
        registerDao(Questions.class, questionsDao);
        registerDao(LogsData.class, logsDataDao);
        registerDao(SiteMaster.class, siteMasterDao);
        registerDao(GaurdMaster.class, gaurdMasterDao);
        registerDao(MasterQuestions.class, masterQuestionsDao);
        registerDao(DesignationData.class, designationDataDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        siteDaoConfig.getIdentityScope().clear();
        guardDaoConfig.getIdentityScope().clear();
        questionsDaoConfig.getIdentityScope().clear();
        logsDataDaoConfig.getIdentityScope().clear();
        siteMasterDaoConfig.getIdentityScope().clear();
        gaurdMasterDaoConfig.getIdentityScope().clear();
        masterQuestionsDaoConfig.getIdentityScope().clear();
        designationDataDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public SiteDao getSiteDao() {
        return siteDao;
    }

    public GuardDao getGuardDao() {
        return guardDao;
    }

    public QuestionsDao getQuestionsDao() {
        return questionsDao;
    }

    public LogsDataDao getLogsDataDao() {
        return logsDataDao;
    }

    public SiteMasterDao getSiteMasterDao() {
        return siteMasterDao;
    }

    public GaurdMasterDao getGaurdMasterDao() {
        return gaurdMasterDao;
    }

    public MasterQuestionsDao getMasterQuestionsDao() {
        return masterQuestionsDao;
    }

    public DesignationDataDao getDesignationDataDao() {
        return designationDataDao;
    }

}
