package com.sixdegreesit.manager;

import java.util.ArrayList;
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
import com.sixdegreesit.securityapp.GaurdDetails;
import com.sixdegreesit.securityapp.GaurdProfile;
import com.sixdegreesit.securityapp.MainActivity;
import com.sixdegreesit.utils.GaurdProfileDTO;
import com.sixdegreesit.utils.GaurdProfileDTO.ExperienceDTO;
import com.sixdegreesit.utils.GaurdProfileDTO.GaurdDTO;
import com.sixdegreesit.utils.GaurdProfileDTO.QualificationDTO;
import com.sixdegreesit.utils.GaurdProfileDTO.RatingDTO;
import com.sixdegreesit.utils.GaurdProfileDTO.ReviewDTO;
import com.sixdegreesit.utils.GaurdProfileDTO.SkillDTO;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.ServerCalls;
import com.sixdegreesit.utils.SharedPrefrenceManager;

public class GaurdManager extends BaseManager {
    
	private Context context;
	String serviceUrl="",responseString="";
	String TAG = getClass().getSimpleName();

    public GaurdManager(Context context) {
        super();
        this.context = context;
    }
    
    public String saveGuardAttendanceAndImage(String guard_id,String latitude,String longitude,String image,
    		String datetime,String site_id) {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		/*Log.i("saveGuardAttendanceAndImage", "serviceUrl="+ SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)
				+"/services/?param=saveGuardAttendanceAndImage&guard_id="+guard_id+"&latitude="+latitude+
				"&longitude="+longitude+"&datetime="+datetime+"&site_id="+site_id+"&image="+image);*/
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("param", new StringBody("saveGuardAttendanceAndImage"));			
			reqEntity.addPart("guard_id", new StringBody(guard_id));
			reqEntity.addPart("latitude", new StringBody(latitude));
			reqEntity.addPart("longitude", new StringBody(longitude));
			reqEntity.addPart("image", new StringBody(image));
			reqEntity.addPart("datetime",new StringBody(datetime));
			reqEntity.addPart("site_id", new StringBody(site_id));
			reqEntity.addPart("designation", new StringBody(KEYS.desination));
			
			Log.i("saveGuardAttendanceAndImage","&designation="+KEYS.desination);
			
			
			Log.i("saveGuardAttendanceAndImage", "serviceUrl="+ SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)
					+"/services/?param=saveGuardAttendanceAndImage&guard_id="+guard_id+"&latitude="+latitude+
					"&longitude="+longitude+"&datetime="+datetime+"&site_id="+site_id+"&designation="+KEYS.desination+"&image="+image);
			
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "saveGuardAttendanceAndImage responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseAttendenceInfo(responseString);
	}    
    
    private String parseAttendenceInfo(String jsonResponse) {
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
    
  
    public List<Guard> getGuardDataByGuardId(String guardId) {
        DaoSession daoSession = getDBSessoin(context);
        GuardDao guardDao = daoSession.getGuardDao();
        List<Guard> guardList = null;
        try {
            guardList = guardDao.queryBuilder().where(GuardDao.Properties.Guard_id.eq(guardId)).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            guardDao.getDatabase().close();
        }
        return guardList;
    }
    
    
    public String renderGuradProfileByGuardId(String guard_id) {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		Log.i("renderGuradProfileByGuardId", "serviceUrl="+ SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)
				+"/services/?param=renderGuradProfileByGuardId&guard_id="+guard_id);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("param", new StringBody("renderGuradProfileByGuardId"));			
			reqEntity.addPart("guard_id", new StringBody(guard_id));
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "renderGuradProfileByGuardId responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseGaurdProfileInfo(responseString);
	}    
    
    
    private String parseGaurdProfileInfo(String jsonResponse) {
  		String responseCode = "";
  		
  		List<GaurdDTO> gaurdDTOs=null;
  		List<QualificationDTO> qualificationDTOs=null;
  		List<SkillDTO> skillDTOs=null;
  		List<ExperienceDTO> experienceDTOs=null;
  		List<ReviewDTO> reviewDTOs=null;
  		List<RatingDTO> ratingDTOs=null;
  		try {
  			if (jsonResponse != null && !jsonResponse.equals(null)) {
  				Object object = new JSONTokener(jsonResponse).nextValue();
  				if (object instanceof JSONObject) {
  					JSONObject jsonObject = new JSONObject(jsonResponse);
  					if (jsonObject.has("responseCode")) {
  						responseCode = jsonObject.getString("responseCode");
  						if (responseCode.equalsIgnoreCase("100")) {
  							responseString = responseCode;  	
  							JSONObject responseDataObject=jsonObject.getJSONObject("responseData");
  							
  							gaurdDTOs=new ArrayList<GaurdProfileDTO.GaurdDTO>();
  							if (responseDataObject.has("guardData")) {
								JSONObject gaurdObject=responseDataObject.getJSONObject("guardData");
								String employee_id=gaurdObject.optString("employee_id");
								String user_id=gaurdObject.optString("user_id");
								String qualification_id=gaurdObject.optString("qualification_id");
								String language_known=gaurdObject.optString("language_known");
								String status=gaurdObject.optString("status");
								String is_deleted=gaurdObject.optString("is_deleted");
								String created_by=gaurdObject.optString("created_by");
								String created_date=gaurdObject.optString("created_date");
								String ref_name_1=gaurdObject.optString("ref_name_1");
								String ref_add_1=gaurdObject.optString("ref_add_1");
								String ref_des_1=gaurdObject.optString("ref_des_1");
								String ref_name_2=gaurdObject.optString("ref_name_2");
								String ref_add_2=gaurdObject.optString("ref_add_2");
								String ref_des_2=gaurdObject.optString("ref_des_2");
								String bank_name=gaurdObject.optString("bank_name");
								String branch_name=gaurdObject.optString("branch_name");
								String ifsc_code=gaurdObject.optString("ifsc_code");
								String account_number=gaurdObject.optString("account_number");
								String pf_account_number=gaurdObject.optString("pf_account_number");
								String esic_account_number=gaurdObject.optString("esic_account_number");
								String aadhar_card_verification=gaurdObject.optString("aadhar_card_verification");
								String police_verification=gaurdObject.optString("police_verification");
								String education_verification=gaurdObject.optString("education_verification");
								String experience_verification=gaurdObject.optString("experience_verification");
								String license_verification=gaurdObject.optString("license_verification");
								String aadhar_card_no=gaurdObject.optString("aadhar_card_no");
								String esic_smart_card_number=gaurdObject.optString("esic_smart_card_number");
								String guard_id=gaurdObject.optString("guard_id");
								String company_id=gaurdObject.optString("company_id");
								String first_name=gaurdObject.optString("first_name");
								String last_name=gaurdObject.optString("last_name");
								String aadhar_card_verification_img_url=gaurdObject.optString("aadhar_card_verification_img_url");
								String police_verification_img_url=gaurdObject.optString("police_verification_img_url");
								String education_verification_img_url=gaurdObject.optString("education_verification_img_url");
								String experience_verification_img_url=gaurdObject.optString("experience_verification_img_url");
								String license_verification_img_url=gaurdObject.optString("license_verification_img_url");
								String phone=gaurdObject.optString("phone");
								String mobile=gaurdObject.optString("mobile");
								String address=gaurdObject.optString("address");
								String company_name=gaurdObject.optString("company_name");
								String img_url=gaurdObject.optString("img_url");
								String site_id=gaurdObject.optString("site_id");								
								gaurdDTOs.add(new GaurdDTO(employee_id, user_id, qualification_id, language_known, status,
									is_deleted, created_by, created_date, ref_name_1, ref_add_1, ref_des_1, ref_name_2, 
									ref_add_2, ref_des_2, bank_name, branch_name, ifsc_code, account_number, pf_account_number,
									esic_account_number, aadhar_card_verification, police_verification, education_verification,
									experience_verification, license_verification, aadhar_card_no, esic_smart_card_number, 
									guard_id, company_id, first_name, last_name, aadhar_card_verification_img_url,
									police_verification_img_url, education_verification_img_url, experience_verification_img_url,
									license_verification_img_url, phone, mobile, address, company_name, img_url, site_id));
							}
  							
  							qualificationDTOs=new ArrayList<GaurdProfileDTO.QualificationDTO>();
  							if (responseDataObject.has("qualificationData")) {
								JSONArray jsonArray=responseDataObject.getJSONArray("qualificationData");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject2=jsonArray.getJSONObject(i);
									String employee_id=jsonObject2.optString("employee_id");
									String qualification_id=jsonObject2.optString("qualification_id");
									String qualification_name=jsonObject2.optString("qualification_name");
									String is_qualification=jsonObject2.optString("is_qualification");
									qualificationDTOs.add(new QualificationDTO(employee_id, qualification_id, qualification_name, is_qualification));
								}
							}
  							
  							skillDTOs=new ArrayList<GaurdProfileDTO.SkillDTO>();
  							if (responseDataObject.has("skillData")) {
  								JSONArray jsonArray=responseDataObject.getJSONArray("skillData");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject2=jsonArray.getJSONObject(i);
									String employee_skill_id=jsonObject2.optString("employee_skill_id");
									String employee_id=jsonObject2.optString("employee_id");
									String skill_id=jsonObject2.optString("skill_id");
									String skill_name=jsonObject2.optString("skill_name");
									skillDTOs.add(new SkillDTO(employee_skill_id, employee_id, skill_id, skill_name));								
								}
  							}
  							
  							experienceDTOs=new ArrayList<GaurdProfileDTO.ExperienceDTO>();
  							if (responseDataObject.has("experienceData")) {
  								JSONArray jsonArray=responseDataObject.getJSONArray("experienceData");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject2=jsonArray.getJSONObject(i);
									String employee_experience_id=jsonObject2.optString("employee_experience_id");
									String employee_id=jsonObject2.optString("employee_id");
									String start_date=jsonObject2.optString("start_date");
									String end_date=jsonObject2.optString("end_date");
									String company_id=jsonObject2.optString("company_id");
									String designation=jsonObject2.optString("designation");
									String salary_drawn=jsonObject2.optString("salary_drawn");
									String leaving_reason=jsonObject2.optString("leaving_reason");
									String verified_by=jsonObject2.optString("verified_by");
									String company_name=jsonObject2.optString("company_name");
									String exp_duration_year=jsonObject2.optString("exp_duration_year");
									String exp_duration_month=jsonObject2.optString("exp_duration_month");
									experienceDTOs.add(new ExperienceDTO(employee_experience_id, employee_id, start_date,
										end_date, company_id, designation, salary_drawn, leaving_reason, verified_by, company_name,
										exp_duration_year, exp_duration_month));
								}
  							}
  							
  							reviewDTOs=new ArrayList<GaurdProfileDTO.ReviewDTO>();
  							if (responseDataObject.has("reviewData")) {
  								JSONArray jsonArray=responseDataObject.getJSONArray("reviewData");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject2=jsonArray.getJSONObject(i);
									String review_id=jsonObject2.optString("review_id");
									String employee_id=jsonObject2.optString("employee_id");
									String review_by=jsonObject2.optString("review_by");
									String review_text=jsonObject2.optString("review_text");
									String review_date=jsonObject2.optString("review_date");
									String rating=jsonObject2.optString("rating");
									String fo_name=jsonObject2.optString("fo_name");
									String img_url=jsonObject2.optString("img_url");
									reviewDTOs.add(new ReviewDTO(review_id, employee_id, review_by, review_text, 
											review_date, rating, fo_name, img_url));
								}
  							}
  							
  							ratingDTOs=new ArrayList<GaurdProfileDTO.RatingDTO>();
  							if (responseDataObject.has("ratingData")) {
  								JSONArray jsonArray=responseDataObject.getJSONArray("ratingData");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject2=jsonArray.getJSONObject(i);
									String discipline_rating=jsonObject2.optString("discipline_rating");
									String punctuality_rating=jsonObject2.optString("punctuality_rating");
									String fitness_rating=jsonObject2.optString("fitness_rating");
									String cleverness_rating=jsonObject2.optString("cleverness_rating");
									String cleanliness_rating=jsonObject2.optString("cleanliness_rating");
									ratingDTOs.add(new RatingDTO(discipline_rating, punctuality_rating, fitness_rating,
											cleverness_rating, cleanliness_rating));
								}
  							}
  							String avgRating=responseDataObject.optString("avgRating");
  							String numberOfReviews=responseDataObject.optString("numberOfReviews");  							
  							GaurdProfile.gaurdProfileDTOs.add(new GaurdProfileDTO(gaurdDTOs, qualificationDTOs, skillDTOs,
  									experienceDTOs, reviewDTOs, ratingDTOs, numberOfReviews,avgRating));
  							
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
