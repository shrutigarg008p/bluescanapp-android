package com.sixdegreesit.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.android.db.DaoSession;
import com.android.db.DesignationData;
import com.android.db.DesignationDataDao;
import com.android.db.GaurdMaster;
import com.android.db.GaurdMasterDao;
import com.android.db.MasterQuestions;
import com.android.db.MasterQuestionsDao;
import com.android.db.SiteMaster;
import com.android.db.SiteMasterDao;
import com.android.db.User;
import com.android.db.UserDao;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.ServerCalls;
import com.sixdegreesit.utils.SharedPrefrenceManager;

@SuppressLint("DefaultLocale")
public class LoginManager extends BaseManager{
	
    private Context context;
    String serviceUrl="",responseString="";
    String TAG = getClass().getSimpleName();
    
    public LoginManager(Context context) {
        super();
        this.context = context;
    }
    
    public String validateUserNameandPassword(String userName, String password,String apns_token) {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("param", new StringBody("login"));			
			reqEntity.addPart("user_name", new StringBody(userName));
			reqEntity.addPart("password", new StringBody(password));
			if(!apns_token.equals(""))
				reqEntity.addPart("apns_token", new StringBody(apns_token));
			else
				reqEntity.addPart("apns_token", new StringBody("123445578900"));
			
			   Log.i("loginrequest", "login serviceUrl-->" + serviceUrl + "&param=" + "login"
	                    + "&user_name=" +userName + "&password="+password+"&apns_token="+apns_token);

			
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "validateUserNameandPassword responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveUserInfo(responseString);
	}    
  
    private String saveUserInfo(String jsonResponse) {
		String responseCode = "";
		try {
			if (jsonResponse != null && !jsonResponse.equals(null)) {
				Object object = new JSONTokener(jsonResponse).nextValue();
				if (object instanceof JSONObject) {
					JSONObject jsonObject = new JSONObject(jsonResponse);
					DaoSession daoSession=getDBSessoin(context);
					daoSession.getUserDao().deleteAll();
					UserDao userDao=daoSession.getUserDao();
					if (jsonObject.has("responseCode")) {
						responseCode = jsonObject.getString("responseCode");
						if (responseCode.equalsIgnoreCase("100")) {
							responseString = responseCode;
							if (jsonObject.has("responseData")) {								
								JSONObject responseObject=jsonObject.getJSONObject("responseData");
								 String userId = responseObject.optString("user_id");
						         String email = responseObject.optString("email");
						         String status = responseObject.optString("status");
						         String companyId = responseObject.optString("company_id");
						         String name = responseObject.optString("name");
						         String roleId = responseObject.optString("role_id");
						         String roleName = responseObject.optString("role_name");
						         String code = responseObject.optString("code");
						         String regionId = responseObject.optString("region_id");
						         String branchId = responseObject.optString("branch_id");
						         String siteId = responseObject.optString("site_id");
						         String userTasksID = responseObject.optString("user_tasks_ids");
						         String userTasks = responseObject.optString("user_tasks");
						         String sessionToken = responseObject.optString("session_token");
						         HashMap<String, String> hashMap = new HashMap<String, String>();
						         hashMap.put(KEYS.SESSION_TOKEN, sessionToken);
						         hashMap.put(KEYS.USER_ID, userId);
						         hashMap.put(KEYS.USER_NAME, name);
						         SharedPrefrenceManager.setMemberDetailsInSP(context, hashMap) ;
						         userDao.insertOrReplace(new User(userId, email, status, companyId, name, roleId, roleName,
						        		 code, regionId, branchId, siteId, userTasksID, userTasks, sessionToken));
						         userDao.getDatabase().close();
							}else{
								responseString = jsonObject.getString("responseData");
							}
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
  
    public String logoutUser(String userId) {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("param", new StringBody("logoutUser"));			
			reqEntity.addPart("user_id", new StringBody(userId));			
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "logoutUser responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseLogoutInfo(responseString);
	}    
    
    
    public String saveCustomInspection(String lat,String lng,String desc,String datetime) {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		Log.i(TAG, "saveCustomInspection Service==" +serviceUrl+"" );
		try {			
			reqEntity.addPart("param", new StringBody("saveCustomInspection"));			
			reqEntity.addPart("lat", new StringBody(lat));	
			reqEntity.addPart("long", new StringBody(lng));	
			reqEntity.addPart("description", new StringBody(desc));	
			reqEntity.addPart("datetime",new StringBody(datetime));
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "saveCustomInspection responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseLogoutInfo(responseString);
	}    
    
    private String parseLogoutInfo(String jsonResponse) {
  		String responseCode = "";
  		try {
  			if (jsonResponse != null && !jsonResponse.equals(null)) {
  				Object object = new JSONTokener(jsonResponse).nextValue();
  				if (object instanceof JSONObject) {
  					JSONObject jsonObject = new JSONObject(jsonResponse);
  					if (jsonObject.has("responseCode")) {
  						responseCode = jsonObject.getString("responseCode");
  						if (responseCode.equalsIgnoreCase("100")) {
  							responseString = responseCode;  							
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
    
    
    public String getAllCompanyData() {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		Log.i(TAG, "getAllCompanyData Service==" +serviceUrl+"getAllCompanyData" );
		try {			
			reqEntity.addPart("param", new StringBody("getAllCompanyData"));			
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "getAllCompanyData responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseCompanyData(responseString);
	}   
    
    private String parseCompanyData(String jsonResponse) {
  		String responseCode = "";
  		try {
  			if (jsonResponse != null && !jsonResponse.equals(null)) {
  				Object object = new JSONTokener(jsonResponse).nextValue();
  				if (object instanceof JSONObject) {
  					JSONObject jsonObject = new JSONObject(jsonResponse);
  					if (jsonObject.has("responseCode")) {
  						responseCode = jsonObject.getString("responseCode");
  						if (responseCode.equalsIgnoreCase("100")) {
  							responseString = responseCode; 
  							DaoSession daoSession=getDBSessoin(context);
  							daoSession.getGaurdMasterDao().deleteAll();
  							daoSession.getSiteMasterDao().deleteAll();
  							daoSession.getMasterQuestionsDao().deleteAll();
  							GaurdMasterDao gaurdMasterDao=daoSession.getGaurdMasterDao();
  							SiteMasterDao siteMasterDao=daoSession.getSiteMasterDao();
  							MasterQuestionsDao masterQuestionsDao=daoSession.getMasterQuestionsDao();
  							if (jsonObject.has("responseData")) {
								JSONObject jsonObject2=jsonObject.getJSONObject("responseData");
								
								if (jsonObject2.has("siteList")) {
									JSONArray jsonArray=jsonObject2.getJSONArray("siteList");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3=jsonArray.getJSONObject(i);
										String siteQRCode="";
										if (jsonObject3.has("siteData")) {
											JSONObject siteObject=jsonObject3.getJSONObject("siteData");
											String qr_code = siteObject.optString("qr_code");
											siteQRCode=qr_code;
											String site_id = siteObject.optString("site_id");
											String address = siteObject.optString("address");
											String zipcode = siteObject.optString("zipcode");
											String city = siteObject.optString("city");
											String contact_person = siteObject.optString("contact_person");
											String contact_number = siteObject.optString("contact_number");
											String email_id = siteObject.optString("email_id");
											String branch_id = siteObject.optString("branch_id");
											String branch_name = siteObject.optString("branch_name");
											String customer_name = siteObject.optString("customer_name");
											String company_name = siteObject.optString("company_name");
											String guard = siteObject.optString("guard");
											String site_title = siteObject.optString("site_title");
											
											SiteMaster siteMaster=new SiteMaster(site_id, qr_code, address, zipcode,
												city, contact_person, contact_number, email_id, branch_id, branch_name,
												customer_name, company_name, guard,site_title);
											siteMasterDao.insertOrReplace(siteMaster);
										}
										
										if (jsonObject3.has("questionData")) {
											JSONArray jsonArray2=jsonObject3.getJSONArray("questionData");
											for (int j = 0; j < jsonArray2.length(); j++) {
												JSONObject queObject=jsonArray2.getJSONObject(j);
												String question_id = queObject.optString("question_id");
												String question = queObject.optString("question");
												String company_id = queObject.optString("company_id");
												String is_mandatory = queObject.optString("is_mandatory");
												String sequence = queObject.optString("sequence");
												String question_type = queObject.optString("question_type");
												String question_option = queObject.optString("question_option");
												String remark = queObject.optString("remark");
												String is_published = queObject.optString("is_published");
												
												MasterQuestions masterQuestions=new MasterQuestions(question_id, question,
													company_id, is_mandatory, sequence, question_type, question_option,
													remark, is_published, "", siteQRCode);
												masterQuestionsDao.insert(masterQuestions);
											}
										}
									}
								}
								
								if (jsonObject2.has("guardList")) {
									JSONArray jsonArray=jsonObject2.getJSONArray("guardList");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject3=jsonArray.getJSONObject(i);
										if (jsonObject3.has("guardData")) {
											JSONObject guardObject=jsonObject3.getJSONObject("guardData");
											String guard_id = guardObject.optString("guard_id");
									        String company_id = guardObject.optString("company_id");
									        String first_name = guardObject.optString("first_name");
									        String last_name = guardObject.optString("last_name");
									        String father_name = guardObject.optString("father_name");
									        String dob = guardObject.optString("dob");
									        String age = guardObject.optString("age");
									        String phone = guardObject.optString("phone");
									        String mobile = guardObject.optString("mobile");
									        String l_address = guardObject.optString("l_address");
									        String p_address = guardObject.optString("p_address");
									        String latitude = guardObject.optString("latitude");
									        String longitude = guardObject.optString("longitude");
									        String zip = guardObject.optString("zip");
									        String user_name = guardObject.optString("user_name");
									        String password = guardObject.optString("password");
									        String email = guardObject.optString("email");
									        String status = guardObject.optString("status");
									        String is_deleted = guardObject.optString("is_deleted");
									        String created_by = guardObject.optString("created_by");
									        String created_date = guardObject.optString("created_date");
									        String qr_code = guardObject.optString("qr_code");
									        String session_token = guardObject.optString("session_token");
									        String apns_token = guardObject.optString("apns_token");
									        String img_url = guardObject.optString("img_url");
									        String company_name = guardObject.optString("company_name");
									        String site_ids = guardObject.optString("site_ids");
									        String designation = guardObject.optString("designation");
									        
									        GaurdMaster gaurdMaster=new GaurdMaster(guard_id, company_id, first_name,
									        	last_name, father_name, dob, age, phone, mobile, l_address, p_address,
									        	latitude, longitude, zip, user_name, password, email, status, is_deleted,
									        	created_by, BaseActivity.dateFromString("yyyy-MM-dd",created_date),
									        	qr_code, session_token, apns_token, img_url,company_name,site_ids,designation);
									        gaurdMasterDao.insertOrReplace(gaurdMaster);
									      
										}
									}
								}
								
								if (jsonObject2.has("guardQuestionData")) {
									JSONArray jsonArray=jsonObject2.getJSONArray("guardQuestionData");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject queObject=jsonArray.getJSONObject(i);
										String question_id = queObject.optString("question_id");
										String question = queObject.optString("question");
										String company_id = queObject.optString("company_id");
										String is_mandatory = queObject.optString("is_mandatory");
										String sequence = queObject.optString("sequence");
										String question_type = queObject.optString("question_type");
										String question_option = queObject.optString("question_option");
										String remark = queObject.optString("remark");
										String is_published = queObject.optString("is_published");
										
										MasterQuestions masterQuestions=new MasterQuestions(question_id, question,
												company_id, is_mandatory, sequence, question_type, question_option,
												remark, is_published, "", "G");
										masterQuestionsDao.insertOrReplace(masterQuestions);
									}
								}
								
							}
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
    
    
    
    
    public String getAllDesignationData() {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		Log.i(TAG, "getAllDesignationData Service==" +serviceUrl+"designation" );
		try {			
			reqEntity.addPart("param", new StringBody("designation"));			
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "getAllDesignationData responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseDesignationData(responseString);
	}   
    
    
    private String parseDesignationData(String jsonResponse) {
  		String responseCode = "";
  		try {
  			if (jsonResponse != null && !jsonResponse.equals(null)) {
  				Object object = new JSONTokener(jsonResponse).nextValue();
  				if (object instanceof JSONObject) {
  					JSONObject jsonObject = new JSONObject(jsonResponse);
  					DaoSession daoSession=getDBSessoin(context);
  					daoSession.getDesignationDataDao().deleteAll();
  					DesignationDataDao designationDataDao=daoSession.getDesignationDataDao();
  					//daoSession.getUserDao().deleteAll();
  					//UserDao userDao=daoSession.getUserDao();
  					if (jsonObject.has("responseCode")) {
  						responseCode = jsonObject.getString("responseCode");
  						if (responseCode.equalsIgnoreCase("100")) {
  							responseString = responseCode;
  							if (jsonObject.has("responseData")) {								
  								JSONArray array=jsonObject.getJSONArray("responseData");
  								for(int l=0;l<array.length();l++){
  								
  									Log.i("testing"+l,""+array.getString(l));
  									
  									 String Designation = array.getString(l);
  									DesignationData data= new DesignationData(Designation);
  									designationDataDao.insertOrReplaceInTx(data);
  									
  			
  								}
  								
  						
  							}else{
  								responseString = jsonObject.getString("responseData");
  							}
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
    
    
  
    
    public List<DesignationData> getAllDesignationDataReturn()
    {
        DaoSession daoSession = getDBSessoin(context);
        DesignationDataDao designationDataDao=daoSession.getDesignationDataDao();
        try {
            List<DesignationData> alldesignationdata=new ArrayList<DesignationData>();
            alldesignationdata=designationDataDao.loadAll();
            return alldesignationdata;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
        	designationDataDao.getDatabase().close();
        }
    }
    
    public List<SiteMaster> getSiteMasterDataBYQRCode(String qrCode){
    	DaoSession daoSession=getDBSessoin(context);
		SiteMasterDao siteMasterDao=daoSession.getSiteMasterDao();
		List<SiteMaster> siteMasters=new ArrayList<SiteMaster>();
		siteMasters=siteMasterDao.queryBuilder().where(SiteMasterDao.Properties.Qr_code.eq(qrCode.toUpperCase())).list();
		return siteMasters;
    }
    
  
    public List<GaurdMaster> getGuardMasterDataBYQRCode(String qrCode){
    	DaoSession daoSession=getDBSessoin(context);
    	GaurdMasterDao gaurdMasterDao=daoSession.getGaurdMasterDao();
		List<GaurdMaster> gaurdMasters=new ArrayList<GaurdMaster>();
		gaurdMasters=gaurdMasterDao.queryBuilder().where(GaurdMasterDao.Properties.Qr_code.eq(qrCode.toUpperCase())).list();
		return gaurdMasters;
    }
    
    public List<MasterQuestions> getQuestionMasterDataBYQRCode(String qrCode){
    	DaoSession daoSession=getDBSessoin(context);
    	MasterQuestionsDao masterQuestionsDao=daoSession.getMasterQuestionsDao();
		List<MasterQuestions> masterQuestions=new ArrayList<MasterQuestions>();
		masterQuestions=masterQuestionsDao.queryBuilder().where(MasterQuestionsDao.Properties.Qrcode.eq(qrCode.toUpperCase())).list();
		return masterQuestions;
    }
    
}
