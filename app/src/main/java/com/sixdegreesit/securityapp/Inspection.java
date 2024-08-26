package com.sixdegreesit.securityapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.db.LogsData;
import com.android.db.Questions;
import com.sixdegreesit.adapter.ExpandableListAdapter;
import com.sixdegreesit.adapter.GroupEntity;
import com.sixdegreesit.adapter.GroupEntity.GroupItemEntity;
import com.sixdegreesit.manager.InspectionManager;
import com.sixdegreesit.manager.LogDataManager;
import com.sixdegreesit.manager.SiteManager;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.ConnectionDetector;
import com.sixdegreesit.utils.GPSTracker;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.SharedPrefrenceManager;

public class Inspection extends BaseActivity implements OnClickListener{

	private ExpandableListView mExpandableListView;
	public static List<GroupEntity> mGroupCollection;
	public static List<Questions> questionsList=null;
	Intent intent=null;
	String siteId="",guardId="",type="",id="",siteVisitId="";
	Button sendButton,skipButton;
	ExpandableListAdapter expandableListAdapter=null;
	BroadcastReceiver broadcastReceiver=null;  
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_inspection);
		
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar, null);
		mCustomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		ImageView menuImageView = (ImageView) mCustomView.findViewById(R.id.menu_ImageView);
		menuImageView.setVisibility(View.INVISIBLE);
		TextView title = (TextView) mCustomView.findViewById(R.id.title_text);
		title.setText("Inspection");
		ImageView syncImageView=(ImageView)mCustomView.findViewById(R.id.sync_ImageView);
        syncImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				popupOptionMenu(v);
				if (new ConnectionDetector(Inspection.this).isConnectingToInternet()) {
           		 new  TaskSyncData().execute();
				} else {
					Toast.makeText(Inspection.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				} 
			}
		});
        ImageView logoutImageView=(ImageView)mCustomView.findViewById(R.id.logout_ImageView);
        logoutImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutDailogBox(Inspection.this);
			}
		});
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		 intent=getIntent();
		 if (intent!=null){
			 siteId=intent.getStringExtra(KEYS.SITE_ID);
			 type=intent.getStringExtra(KEYS.TYPE);
			 guardId=intent.getStringExtra(KEYS.GUARD_ID);
			 siteVisitId=intent.getStringExtra(KEYS.SITE_VISITID);
	     }
	
		 sendButton=(Button)findViewById(R.id.sendFieldInspectionButton);
		 sendButton.setOnClickListener(this);
		 skipButton=(Button)findViewById(R.id.skipButton);
		 skipButton.setOnClickListener(this);
		 mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		 setListAdapter();
		 
		 
		 mExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				if (mGroupCollection.get(groupPosition).questionType.equalsIgnoreCase("text")) {
					intent = new Intent(Inspection.this, FieldInspectionText.class);
					intent.putExtra("questionId", mGroupCollection.get(groupPosition).questionId);
					intent.putExtra("question", mGroupCollection.get(groupPosition).question);
					intent.putExtra("answer", mGroupCollection.get(groupPosition).answer);
					intent.putExtra("groupPosition", groupPosition);
	                startActivity(intent);					
				} else if (mGroupCollection.get(groupPosition).questionType.equalsIgnoreCase("image")) {
					intent = new Intent(Inspection.this, FieldInspectionImage.class);
					intent.putExtra("questionId", mGroupCollection.get(groupPosition).questionId);
					intent.putExtra("question", mGroupCollection.get(groupPosition).question);
					intent.putExtra("groupPosition", groupPosition);
	                startActivity(intent);		
					
				}
				return false;
			}
		});
		
		
		mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				String answers=Inspection.questionsList.get(groupPosition).getAnswer();
				String queOption=Inspection.questionsList.get(groupPosition).getQuestion_option();
				String[] array=answers.split(",");
				String[] queOptionArray=queOption.split(",");
				StringBuilder builder = null;
				if (mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).questionType.equalsIgnoreCase("select")) {
					if (mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).isChecked.equals("true")) {
						builder=new StringBuilder();
						for (int i = 0; i < array.length; i++) {
							if (mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).question.equals(queOptionArray[i])) {
								builder.append("false"+",");
								mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).setIsChecked("false");
							} else {
								builder.append(array[i]+",");
							}
						}
					} else {
						builder=new StringBuilder();
						for (int i = 0; i < array.length; i++) {
							if (mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).question.equals(queOptionArray[i])) {
								builder.append("true"+",");
								mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).setIsChecked("true");
							} else {
								builder.append(array[i]+",");
							}
						}
					}
					
				}else if(mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).questionType.equalsIgnoreCase("radio")){
						builder=new StringBuilder();
						for (int i = 0; i < queOptionArray.length; i++) {
							if (mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).question.equals(queOptionArray[i])) {
								builder.append("true"+",");
								mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).setIsChecked("true");
							} else {
								builder.append("false"+",");
								mGroupCollection.get(groupPosition).GroupItemCollection.get(i).setIsChecked("false");
							}
						}					
				}					
				Inspection.questionsList.get(groupPosition).setAnswer(builder.toString().substring(0, builder.toString().length()-1));
				SiteManager siteManager=new SiteManager(Inspection.this);
				siteManager.updateAnswerByQueId(mGroupCollection.get(groupPosition).GroupItemCollection.get(childPosition).questionId,
						builder.toString().substring(0, builder.toString().length()-1));
				expandableListAdapter.notifyDataSetChanged();
				return false;
			}
		});
		
		  IntentFilter intentFilter = new IntentFilter();
	        intentFilter.addAction("CLOSE_ALL");
	        broadcastReceiver=new BroadcastReceiver() {
	            @Override
	            public void onReceive(Context context, Intent intent) {
	                finish();
	            }
	        };
	        registerReceiver(broadcastReceiver, intentFilter);

	    }

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			if (broadcastReceiver != null)
				unregisterReceiver(broadcastReceiver);
		}
	
	public void setListAdapter(){
		questionsList=new ArrayList<Questions>();
		questionsList=new SiteManager(Inspection.this).getQuestionData();		
		mGroupCollection = new ArrayList<GroupEntity>();
		for (int i = 0; i < questionsList.size(); i++) {
			GroupEntity ge = new GroupEntity();			
			ge.question = questionsList.get(i).getQuestion();	
			ge.answer = questionsList.get(i).getAnswer();	
			ge.questionId = questionsList.get(i).getQuestion_id();	
			ge.questionType = questionsList.get(i).getQuestion_type();	
			ge.isMandatory=questionsList.get(i).getIs_mandatory();
			if (questionsList.get(i).getQuestion_type().equalsIgnoreCase("select")
					||questionsList.get(i).getQuestion_type().equalsIgnoreCase("radio")) {
				String queOption=questionsList.get(i).getQuestion_option();
				if (!queOption.equals(null) && !queOption.equals("")) {
					String[] queOptionArray=queOption.split(",");
					String[] answerArray=questionsList.get(i).getAnswer().split(",");
					for (int j = 0; j < queOptionArray.length; j++) {
						GroupItemEntity gi = ge.new GroupItemEntity();				
						gi.question =queOptionArray[j];
						gi.questionId =questionsList.get(i).getQuestion_id();
						gi.questionType =questionsList.get(i).getQuestion_type();
						gi.isMandatory=questionsList.get(i).getIs_mandatory();
						gi.isChecked=answerArray[j];
						ge.GroupItemCollection.add(gi);
					}
				}
			}			
			mGroupCollection.add(ge);
		}
		expandableListAdapter = new ExpandableListAdapter(Inspection.this,
					mExpandableListView, mGroupCollection);
		mExpandableListView.setAdapter(expandableListAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch (v.getId()){
         case R.id.sendFieldInspectionButton:
        	 GPSTracker gpsTracker=new GPSTracker(Inspection.this);
        	 if (type.equalsIgnoreCase("site")) {
        		 id=siteId;
        	 }else{
        		 id=guardId;
        	 }
        	 
        	 List<Questions> list=new SiteManager(Inspection.this).getQuestionData();
        	 JSONArray questionArray=new JSONArray();
        	 for (int i = 0; i < list.size(); i++) {
        		 try {
        			 JSONObject jsonObject=new JSONObject();
        			 if (list.get(i).getQuestion_type().equalsIgnoreCase("text")) {
						jsonObject.put("questionId", list.get(i).getQuestion_id());
						jsonObject.put("answer", list.get(i).getAnswer());			
						questionArray.put(jsonObject);
        			 }else if (list.get(i).getQuestion_type().equalsIgnoreCase("select")) {
 						jsonObject.put("questionId", list.get(i).getQuestion_id());
 						String[] queOption=list.get(i).getQuestion_option().split(",");
 						String[] answers=list.get(i).getAnswer().split(",");
 						StringBuilder stringBuilder=new StringBuilder();
 						for (int j = 0; j < answers.length; j++) {
							if (answers[j].equals("true")) {
								stringBuilder.append(queOption[j]+",");
							}
						}
 						if(!stringBuilder.toString().equals("")){
 							if(stringBuilder.toString().contains(","))
 								jsonObject.put("answer", stringBuilder.toString().substring(0, stringBuilder.toString().length()-1));
 							else
 								jsonObject.put("answer", stringBuilder.toString());
 						}else
 							jsonObject.put("answer", "");
 						
 						questionArray.put(jsonObject);
         			 }else if (list.get(i).getQuestion_type().equalsIgnoreCase("radio")) {
  						jsonObject.put("questionId", list.get(i).getQuestion_id());
  						String[] queOption=list.get(i).getQuestion_option().split(",");
  						String[] answers=list.get(i).getAnswer().split(",");
  						StringBuilder stringBuilder=new StringBuilder();
  						for (int j = 0; j < answers.length; j++) {
 							if (answers[j].equals("true")) {
 								stringBuilder.append(queOption[j]);
 								break;
 							}
 						}
  						if(!stringBuilder.toString().equals("")){
  							if(stringBuilder.toString().contains(","))
 								jsonObject.put("answer", stringBuilder.toString().substring(0, stringBuilder.toString().length()-1));
 							else
 								jsonObject.put("answer", stringBuilder.toString());
  						}else
  							jsonObject.put("answer","");
  						questionArray.put(jsonObject);
          			 }else{
          				if(!list.get(i).getAnswer().equals("")) {
          					String[] imagePathArray=list.get(i).getAnswer().split(",");
          					List<Bitmap> bitmaps = new ArrayList<Bitmap>();                       
          					for (int k=0; k<imagePathArray.length; k++){
          						bitmaps.add(BaseActivity.decodeFile(new File(imagePathArray[k]), 200, 200));
          					}                        
          					for (int l = 0; l < bitmaps.size(); l++) {
          						ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            	bitmaps.get(l).compress(Bitmap.CompressFormat.JPEG, 100, bao);
                            	byte[] ba = bao.toByteArray();
                            	String encodedString = Base64.encodeToString(ba, Base64.DEFAULT);
                            	jsonObject.put("questionId", list.get(i).getQuestion_id());
                            	jsonObject.put("answer", encodedString);
                            	questionArray.put(jsonObject);
          					}
          				}else{
          					jsonObject.put("questionId", list.get(i).getQuestion_id());
                        	jsonObject.put("answer", "");
                        	questionArray.put(jsonObject);
          				}
          			 }
        		 	} catch (JSONException e) {
						e.printStackTrace();
					}
			 }
        	 Log.i("Inspection", "questionArray="+questionArray.toString());
        	 if (new ConnectionDetector(Inspection.this).isConnectingToInternet()) {
        		//id, latitude, longitude,questionAnsArr
            	 new TaskSendInspection().execute(id,""+gpsTracker.getLatitude(),""+gpsTracker.getLongitude(),
            			 questionArray.toString());
			} else {
				String parameter="param=saveSiteGuardInspection&type="+type+"&id="+id+"&lat="+gpsTracker.getLatitude()
						+"&long="+gpsTracker.getLongitude()+"&datetime="+getCurrentDate("yyyy-MM-dd kk:mm:ss")
						+"&site_visiting_id="+siteVisitId+"&questionAnsArr="+questionArray.toString();
				LogDataManager dataManager=new LogDataManager(Inspection.this);
				LogsData logsData=new LogsData(null, SharedPrefrenceManager.getMemberDetailsFromSP(Inspection.this)
						.get(KEYS.USER_ID), parameter, "0", dateFromString("yyyy-MM-dd kk:mm:ss",
								getCurrentDate("yyyy-MM-dd kk:mm:ss")));
				dataManager.insertData(logsData);
				Toast.makeText(Inspection.this,"Inspection saved successfully, Please sync when network available.",Toast.LENGTH_LONG).show();
				File file = new File(android.os.Environment.getExternalStorageDirectory(),"Salaria/Site");
				if (file.isDirectory()) {
					String[] children = file.list();
					for (int i = 0; i < children.length; i++) {
						new File(file, children[i]).delete();
					}
				}
				intent = new Intent(Inspection.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
        	 
         break;
         case R.id.skipButton:
             intent=new Intent(Inspection.this, MainActivity.class);
             startActivity(intent);
             break;         
		 }
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		File file = new File(android.os.Environment.getExternalStorageDirectory(),"Salaria/Site");
		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				new File(file, children[i]).delete();
			}
		}
	}
	
	public class TaskSendInspection extends AsyncTask<String,Void,String>{
		ProgressDialog loginDialog;
		String returnResponse=null,questionsArr="";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog = ProgressDialog.show(Inspection.this, "", "Please Wait");
		}

		@Override
		protected String doInBackground(String... params) {
			InspectionManager inspectionManager=new InspectionManager(Inspection.this);
			questionsArr=params[3];
			returnResponse=inspectionManager.saveSiteGuardInspection(type, params[0], params[1], params[2],
					params[3],getCurrentDate("yyyy-MM-dd kk:mm:ss"),siteVisitId);
			return returnResponse;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			loginDialog.dismiss();
			if (returnResponse.equals("100")){
				Toast.makeText(Inspection.this,"Inspection saved successfully.",Toast.LENGTH_LONG).show();
				File file = new File(android.os.Environment.getExternalStorageDirectory(),"Salaria/Site");
				if (file.isDirectory()) {
					String[] children = file.list();
					for (int i = 0; i < children.length; i++) {
						new File(file, children[i]).delete();
					}
				}
				intent = new Intent(Inspection.this,MainActivity.class);
				startActivity(intent);
				finish();
			}else if(returnResponse.equals(getResources().getString(R.string.network_errror))){
				GPSTracker gpsTracker=new GPSTracker(Inspection.this);
				String parameter="param=saveSiteGuardInspection&type="+type+"&id="+id+"&lat="+gpsTracker.getLatitude()
						+"&long="+gpsTracker.getLongitude()+"&questionAnsArr="+questionsArr
						+"&datetime="+getCurrentDate("yyyy-MM-dd kk:mm:ss")+"&site_visiting_id="+siteVisitId;
				LogDataManager dataManager=new LogDataManager(Inspection.this);
				LogsData logsData=new LogsData(null, SharedPrefrenceManager.getMemberDetailsFromSP(Inspection.this)
						.get(KEYS.USER_ID), parameter, "0", dateFromString("yyyy-MM-dd kk:mm:ss",
								getCurrentDate("yyyy-MM-dd kk:mm:ss")));
				dataManager.insertData(logsData);
				Toast.makeText(Inspection.this,"Inspection saved successfully, Please sync when network available.",Toast.LENGTH_LONG).show();
				File file = new File(android.os.Environment.getExternalStorageDirectory(),"Salaria/Site");
				if (file.isDirectory()) {
					String[] children = file.list();
					for (int i = 0; i < children.length; i++) {
						new File(file, children[i]).delete();
					}
				}
				intent = new Intent(Inspection.this,MainActivity.class);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(Inspection.this,returnResponse,Toast.LENGTH_LONG).show();
			}
		}
	}
}
