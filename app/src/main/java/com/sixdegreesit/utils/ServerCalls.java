package com.sixdegreesit.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;


public class ServerCalls {
	static String LOG_TAG = "Server Calls";

	/* method for server call */
	public static String makeConnection(String serviceUrl,
			MultipartEntity entity, String token) {
		String response = "", urlStr = "";		
		try {
			urlStr = serviceUrl;
			Log.i("ServerCalls", "serviceUrl="+serviceUrl);
			HttpClient httpClient;
			httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(urlStr);
			httpPost.setEntity(entity);
			if (token!=null&&!token.equals(""))
				httpPost.addHeader("token", token);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			if(response!=null)
				Log.i("ServerCalls", "response="+response);
			entity = null;
		} catch (UnsupportedEncodingException e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		}
		return response;
	}

}
