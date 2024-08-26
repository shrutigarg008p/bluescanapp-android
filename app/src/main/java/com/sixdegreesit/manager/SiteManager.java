package com.sixdegreesit.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

import com.android.db.DaoSession;
import com.android.db.Guard;
import com.android.db.GuardDao;
import com.android.db.Questions;
import com.android.db.QuestionsDao;
import com.android.db.Site;
import com.android.db.SiteDao;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.ServerCalls;
import com.sixdegreesit.utils.SharedPrefrenceManager;

public class SiteManager extends BaseManager {
    
	private Context context;
	String serviceUrl="",responseString="";
	String TAG = getClass().getSimpleName();

    public SiteManager(Context context) {
        super();
        this.context = context;
    }
    
    public String getSiteOrGuardDetailsByQRCode(String qrCode,String latitude,String longitude,String attendance_mode,
    		String datetime) {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		Log.i("getSiteOrGuardDetailsByQRCode", "serviceUrl="+SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?"
				+"param=getSiteOrGuardDetailsByQRCode&qr_code="+qrCode+"&latitude="+latitude+"&longitude="+longitude
				+"&attendance_mode="+attendance_mode+"&datetime="+datetime);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("param", new StringBody("getSiteOrGuardDetailsByQRCode"));			
			reqEntity.addPart("qr_code", new StringBody(qrCode));
			reqEntity.addPart("latitude", new StringBody(latitude));
			reqEntity.addPart("longitude", new StringBody(longitude));
			reqEntity.addPart("attendance_mode", new StringBody(attendance_mode));
			reqEntity.addPart("datetime", new StringBody(datetime));
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "getSiteOrGuardDetailsByQRCode responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveSiteGuardDetailInfo(responseString);
	}    
    
    private String saveSiteGuardDetailInfo(String jsonResponse) {
		String responseCode = "";
		try {
			if (jsonResponse != null && !jsonResponse.equals(null)) {
				Object object = new JSONTokener(jsonResponse).nextValue();
				if (object instanceof JSONObject) {
					JSONObject jsonObject = new JSONObject(jsonResponse);
					DaoSession daoSession=getDBSessoin(context);
					daoSession.getSiteDao().deleteAll();
					daoSession.getGuardDao().deleteAll();
					daoSession.getQuestionsDao().deleteAll();
					SiteDao siteDao=daoSession.getSiteDao();
					GuardDao guardDao=daoSession.getGuardDao();
					QuestionsDao questionsDao=daoSession.getQuestionsDao();
					if (jsonObject.has("responseCode")) {
						responseCode = jsonObject.getString("responseCode");
						if (responseCode.equalsIgnoreCase("100")) {
							responseString = responseCode;
							if (jsonObject.has("responseData")) {								
								JSONObject responseObject=jsonObject.getJSONObject("responseData");
								String siteVisitingId=responseObject.optString("site_visiting_id");
								HashMap<String, String> hashMap1=new HashMap<String, String>();
								hashMap1.put(KEYS.SITE_VISITID, siteVisitingId);
								SharedPrefrenceManager.setMemberDetailsInSP(context, hashMap1);
								if (responseObject.has("siteData")) {
									 JSONObject siteObject=responseObject.getJSONObject("siteData");
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
									 siteDao.insertOrReplace(new Site(site_id, address, zipcode, city, email_id,
										contact_person, contact_number, branch_id, branch_name, customer_name, 
										company_name, SharedPrefrenceManager.getMemberDetailsFromSP(context)
										.get(KEYS.USER_ID),siteVisitingId));									
									 HashMap<String, String> hashMap=new HashMap<String, String>();
									 hashMap.put(KEYS.SITE_ID, site_id);
									 SharedPrefrenceManager.setMemberDetailsInSP(context, hashMap);
								}
								
								if (responseObject.has("guardData")) {
									JSONObject guardObject=responseObject.getJSONObject("guardData");
									String guard_id = guardObject.optString("guard_id");
							        String company_id = guardObject.optString("company_id");
							        String first_name = guardObject.optString("first_name");
							        String last_name = guardObject.optString("last_name");
							        String phone = guardObject.optString("phone");
							        String mobile = guardObject.optString("mobile");
							        String address = guardObject.optString("address");
							        String zip = guardObject.optString("zip");
							        String post = guardObject.optString("post");
							        String technical_qualification = guardObject.optString("technical_qualification");
							        String language_known = guardObject.optString("language_known");
							        String experience = guardObject.optString("experience");
							        String passcode = guardObject.optString("passcode");
							        String pf_no = guardObject.optString("pf_no");
							        String esi_no = guardObject.optString("esi_no");
							        String status = guardObject.optString("status");
							        String is_deleted = guardObject.optString("is_deleted");
							        String created_by = guardObject.optString("created_by");
							        String created_date = guardObject.optString("created_date");
							        String qr_code = guardObject.optString("qr_code");
							        String company_name = guardObject.optString("company_name");
							        String site_id = guardObject.optString("site_id");
							        String img_url = guardObject.optString("img_url");
							        String designation = guardObject.optString("designation");
							        
							        guardDao.insertOrReplace(new Guard(null, guard_id, company_id, first_name, last_name, 
							        		phone, mobile, address, zip, post, technical_qualification, language_known, 
							        		experience, passcode, pf_no, esi_no, status, is_deleted, created_by, 
							        		BaseActivity.dateFromString("yyyy-MM-dd",created_date),qr_code, site_id,
							        		company_name,SharedPrefrenceManager.getMemberDetailsFromSP(context)
							        		.get(KEYS.USER_ID),siteVisitingId,img_url,designation));
							        HashMap<String, String> hashMap=new HashMap<String, String>();
							        hashMap.put(KEYS.GUARD_ID, guard_id);
							        hashMap.put(KEYS.GUARD_IMAGE, img_url);
							        SharedPrefrenceManager.setMemberDetailsInSP(context, hashMap);
								}
								
								if (responseObject.has("questionData")) {
									JSONArray jsonArray=responseObject.getJSONArray("questionData");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject jsonObject2=jsonArray.getJSONObject(i);
										String question_id = jsonObject2.optString("question_id");
										String question = jsonObject2.optString("question");
										String company_id = jsonObject2.optString("company_id");
										String is_mandatory = jsonObject2.optString("is_mandatory");
										String sequence = jsonObject2.optString("sequence");
										String question_type = jsonObject2.optString("question_type");
										String question_option = jsonObject2.optString("question_option");
										String remark = jsonObject2.optString("remark");
										String is_published = jsonObject2.optString("is_published");
										StringBuilder answer=new StringBuilder();
										if (question_type.equalsIgnoreCase("select")||question_type.equalsIgnoreCase("radio")) {
											if (!question_option.equals("")) {
												String[] arr=question_option.split(",");
												for (int j = 0; j < arr.length; j++) {
													if (j==0) {
														answer.append("false");
													} else {
														answer.append(","+"false");
													}
												}
											}
										}
										questionsDao.insertOrReplace(new Questions(question_id, question, company_id, 
												is_mandatory, sequence, question_type, question_option, remark, is_published,answer.toString()));										
									}
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
   
    public List<Questions> getQuestionData() {
        DaoSession daoSession = getDBSessoin(context);
        QuestionsDao questionsDao= daoSession.getQuestionsDao();
        List<Questions> questions = null;
        try {
        	questions = questionsDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	questionsDao.getDatabase().close();
        }
        return questions;
    }
    
    public List<Questions> getQuestionByQueId(String queId) {
        DaoSession daoSession = getDBSessoin(context);
        QuestionsDao questionsDao= daoSession.getQuestionsDao();
        List<Questions> questions = null;
        try {
        	questions = questionsDao.queryBuilder().where(QuestionsDao.Properties.Question_id.eq(queId)).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	questionsDao.getDatabase().close();
        }
        return questions;
    }
    
    public void updateAnswerByQueId(String queId,String answer) {
        DaoSession daoSession = getDBSessoin(context);
        QuestionsDao questionsDao= daoSession.getQuestionsDao();
        List<Questions> questions=getQuestionByQueId(queId);
        try {
			Iterator<Questions> iterator = questions.iterator();
			while (iterator.hasNext()) {
				Questions questionObject = (Questions) iterator.next();
				questionObject.setAnswer(answer);
				questionsDao.insertOrReplace(questionObject);
			}
		}catch (Exception e) {
            e.printStackTrace();
        } finally {
        	questionsDao.getDatabase().close();
        }
    }
    
    
    public String getSiteDataByQRCode(String qrCode) {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("param", new StringBody("getSiteDataByQRCode"));			
			reqEntity.addPart("qr_code", new StringBody(qrCode));
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "getSiteDataByQRCode responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveSiteDataByQRCode(responseString);
	}  
    
    private String saveSiteDataByQRCode(String jsonResponse) {
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
  							if (jsonObject.has("responseData")) {								
  								JSONObject responseObject=jsonObject.getJSONObject("responseData");
  								if (responseObject.has("siteData")) {
  									 JSONObject siteObject=responseObject.getJSONObject("siteData");
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
  									 String site_name=siteObject.optString("site_name");								
  									 HashMap<String, String> hashMap=new HashMap<String, String>();
  									 hashMap.put(KEYS.SITE_IDATTEDENCE, site_id);
  									 hashMap.put(KEYS.SITE_NAME, site_name);
  									 hashMap.put(KEYS.COMPANY_NAME, company_name);
  									 SharedPrefrenceManager.setMemberDetailsInSP(context, hashMap);
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

}
