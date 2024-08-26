package com.sixdegreesit.manager;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.ServerCalls;
import com.sixdegreesit.utils.SharedPrefrenceManager;

public class InspectionManager extends BaseManager {
    
	private Context context;
	String serviceUrl="",responseString="";
	String TAG = getClass().getSimpleName();

    public InspectionManager(Context context) {
        super();
        this.context = context;
    }
    
    public String saveSiteGuardInspection(String type,String id,String latitude,String longitude,String questionAnsArr,
    		String datetime,String siteVisitId) {
		serviceUrl = SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)+"/services/?";
		Log.i("saveSiteGuardInspection", "serviceUrl="+SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.BASE_URL)
				+"/services/?param=saveSiteGuardInspection&type="+type+"&id="+id+"&lat="+latitude+"&long="+longitude
				+"&datetime="+datetime+"&site_visiting_id="+siteVisitId+"&questionAnsArr="+questionAnsArr);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("param", new StringBody("saveSiteGuardInspection"));			
			reqEntity.addPart("type", new StringBody(type));
			reqEntity.addPart("id", new StringBody(id));
			reqEntity.addPart("lat", new StringBody(latitude));
			reqEntity.addPart("long", new StringBody(longitude));
			reqEntity.addPart("questionAnsArr", new StringBody(questionAnsArr));
			reqEntity.addPart("datetime",new StringBody(datetime));
			reqEntity.addPart("site_visiting_id",new StringBody(siteVisitId));
			responseString = ServerCalls.makeConnection(serviceUrl, reqEntity,
					SharedPrefrenceManager.getMemberDetailsFromSP(context).get(KEYS.SESSION_TOKEN));
			Log.i(TAG, "saveSiteGuardInspection responseString==" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseInspectionInfo(responseString);
	}    
    
    private String parseInspectionInfo(String jsonResponse) {
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
     
}
