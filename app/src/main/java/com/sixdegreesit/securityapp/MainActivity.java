package com.sixdegreesit.securityapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.db.DaoSession;
import com.android.db.DesignationData;
import com.android.db.GaurdMaster;
import com.android.db.Guard;
import com.android.db.GuardDao;
import com.android.db.MasterQuestions;
import com.android.db.Questions;
import com.android.db.QuestionsDao;
import com.android.db.Site;
import com.android.db.SiteDao;
import com.android.db.SiteMaster;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sixdegreesit.adapter.CustomAdapter;
import com.sixdegreesit.manager.BaseManager;
import com.sixdegreesit.manager.LoginManager;
import com.sixdegreesit.manager.SiteManager;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.ConnectionDetector;
import com.sixdegreesit.utils.GPSTracker;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.SharedPrefrenceManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	BroadcastReceiver broadcastReceiver=null;   

	Intent intent=null;
	ImageView ivScan;   
	int check = 0;
	public static String DESIGNATION="";
	Button submitButton,customInspectionButton;
	EditText qrCodeEditText;
	String qrCodeString="",siteIdAttendence="",siteName="",companyName="";
	TextView tvName,tvSiteName,tvMsg;
	// Spinner spinner;
	List<DesignationData> spinnerpos;
	List<DesignationData> designationList;
	List<String> DesignationName = new ArrayList<String>();
	CustomAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);

		//  spinner=(Spinner)findViewById(R.id.spinner);

		designationList = new LoginManager(getApplicationContext()).getAllDesignationDataReturn();

		for (int x = 0; x < designationList.size(); x++)

		{
			DesignationName.add(designationList.get(x).getDesignation());
		}

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar, null);
		mCustomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		ImageView menuImageView=(ImageView)mCustomView.findViewById(R.id.menu_ImageView);
		menuImageView.setVisibility(View.INVISIBLE);
		TextView title = (TextView) mCustomView.findViewById(R.id.title_text);
		title.setText("Scan QR Code");
		ImageView syncImageView=(ImageView)mCustomView.findViewById(R.id.sync_ImageView);
		syncImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//				popupOptionMenu(v);
				if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
					new  TaskSyncData().execute();
				} else {
					Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				} 
			}
		});
		ImageView logoutImageView=(ImageView)mCustomView.findViewById(R.id.logout_ImageView);
		logoutImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutDailogBox(MainActivity.this);
			}
		});
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

		tvMsg=(TextView)findViewById(R.id.tv_msg);
		tvName=(TextView)findViewById(R.id.tvName_main);
		tvSiteName=(TextView)findViewById(R.id.tvSiteName_main);
		tvName.setText(SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.USER_NAME));
		ivScan=(ImageView)findViewById(R.id.ivScanQR_main);
		ivScan.setOnClickListener(this);
		submitButton=(Button)findViewById(R.id.submitButton_main);
		submitButton.setOnClickListener(this);
		customInspectionButton=(Button)findViewById(R.id.customInspButton_main);
		customInspectionButton.setOnClickListener(this);
		qrCodeEditText=(EditText)findViewById(R.id.qrCodeEditText_main);     

		if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.ATTENDENCE_MODE).equals("on")) {
			customInspectionButton.setVisibility(View.GONE);
			//spinner.setVisibility(View.VISIBLE);

			//setSpiner();

			tvMsg.setVisibility(View.VISIBLE);
		}else{
			customInspectionButton.setVisibility(View.VISIBLE);
			tvMsg.setVisibility(View.GONE);
		}

		siteName=""+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_NAME);
		companyName=""+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.COMPANY_NAME);
		if(!siteName.equals(null)&&!companyName.equals(null)){
			if(!siteName.equals("null")&&!companyName.equals("null")){
				tvSiteName.setText("Site: "+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this)
				.get(KEYS.SITE_NAME)+"\nCompany: "+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this)
				.get(KEYS.COMPANY_NAME));
			}
		}
		if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.ATTENDENCE_MODE).equals("on")) {
			tvSiteName.setVisibility(View.VISIBLE);
		}else{
			tvSiteName.setVisibility(View.GONE);
		}

		if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.ATTENDENCE_MODE).equals("on")) {
			siteIdAttendence=""+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE);
			if(!siteIdAttendence.equals(null)&&!siteIdAttendence.equals("null")){
				if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE).equals("")) {
					qrCodeEditText.setHint("Enter Site QR Code");
				}else{
					qrCodeEditText.setHint("Enter Gaurd QR Code");
				}
			}else{
				qrCodeEditText.setHint("Enter Site QR Code");
			}
		}


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
		if(broadcastReceiver!=null)
			unregisterReceiver(broadcastReceiver);
	}

	@Override
	public void onBackPressed() {
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.ivScanQR_main:
			qrCodeString="";
			IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
			integrator.addExtra("SCAN_WIDTH", 640);
			integrator.addExtra("SCAN_HEIGHT", 480);
			integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
			integrator.addExtra("PROMPT_MESSAGE", "Scan QR Code");
			integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
			break;
		case R.id.submitButton_main:
			qrCodeString=qrCodeEditText.getText().toString().trim();
			if (!qrCodeString.equals("")) {
				if(SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.ATTENDENCE_MODE).equals("on")){
					siteIdAttendence=""+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE);
					if(!siteIdAttendence.equals(null)&&!siteIdAttendence.equals("null")){
						if (qrCodeString.contains("G") || qrCodeString.contains("g")) 
						{
							List<GaurdMaster> gaurdMasters=new ArrayList<GaurdMaster>();
							gaurdMasters=new LoginManager(MainActivity.this).getGuardMasterDataBYQRCode(qrCodeString);
							if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
								GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
								new GetSiteOrGaurdDetailByQRCode().execute(qrCodeString,""+gpsTracker.getLatitude(),
										""+gpsTracker.getLongitude());
							}else if(gaurdMasters!=null && gaurdMasters.size()>0){
								qrCodeEditText.setText("");
								DaoSession daoSession=BaseManager.getDBSessoin(MainActivity.this);
								daoSession.getGuardDao().deleteAll();
								GuardDao guardDao=daoSession.getGuardDao();
								guardDao.insertOrReplace(new Guard(null, gaurdMasters.get(0).getUser_id(),
										gaurdMasters.get(0).getCompany_id(), gaurdMasters.get(0).getFirst_name(),
										gaurdMasters.get(0).getLast_name(),gaurdMasters.get(0).getPhone(),
										gaurdMasters.get(0).getMobile(), gaurdMasters.get(0).getL_address()+" "+
												gaurdMasters.get(0).getP_address(), gaurdMasters.get(0).getZip(), 
												"", "", "","", gaurdMasters.get(0).getPassword(), "", "", 
												gaurdMasters.get(0).getStatus(), gaurdMasters.get(0).getIs_deleted(),
												gaurdMasters.get(0).getCreated_by(),gaurdMasters.get(0).getCreated_date(),
												gaurdMasters.get(0).getQr_code(), gaurdMasters.get(0).getSite_ids(),gaurdMasters.get(0).getCompany_name(),
												SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.USER_ID),
												"",gaurdMasters.get(0).getImg_url(),gaurdMasters.get(0).getDesignation()));
								
								
								HashMap<String, String> hashMap=new HashMap<String, String>();
								hashMap.put(KEYS.GUARD_ID, gaurdMasters.get(0).getUser_id());
								SharedPrefrenceManager.setMemberDetailsInSP(MainActivity.this, hashMap);

								QuestionsDao questionsDao=daoSession.getQuestionsDao();
								daoSession.getQuestionsDao().deleteAll();
								List<MasterQuestions> masterQuestions=new ArrayList<MasterQuestions>();
								masterQuestions=new LoginManager(MainActivity.this).getQuestionMasterDataBYQRCode("G");
								if (masterQuestions!=null) {
									for (int i = 0; i < masterQuestions.size(); i++) {
										StringBuilder answer=new StringBuilder();
										String question_type=masterQuestions.get(i).getQuestion_type();
										String question_option=masterQuestions.get(i).getQuestion_option();
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
										questionsDao.insert(new Questions(masterQuestions.get(i).getQuestion_id(),
												masterQuestions.get(i).getQuestion(), masterQuestions.get(i).getCompany_id(), 
												masterQuestions.get(i).getIs_mandatory(), masterQuestions.get(i).getSequence(),
												question_type, question_option, masterQuestions.get(i).getRemark(), 
												masterQuestions.get(i).getIs_published(),answer.toString()));
									}
								}

								Intent intent = new Intent(MainActivity.this, GaurdDetails.class);
								intent.putExtra(KEYS.SITE_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_ID));
								intent.putExtra(KEYS.GUARD_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.GUARD_ID));
								intent.putExtra(KEYS.GUARD_IMAGE, gaurdMasters.get(0).getImg_url());
								startActivity(intent);

							}else {
								Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
										Toast.LENGTH_SHORT).show();
							} 
						}else {
							Toast.makeText(MainActivity.this,"Please enter gaurd QR code.",
									Toast.LENGTH_SHORT).show();
						}
					}else{
						if (qrCodeString.contains("S") || qrCodeString.contains("s")) {
							List<SiteMaster> siteMasters=new ArrayList<SiteMaster>();
							siteMasters=new LoginManager(MainActivity.this).getSiteMasterDataBYQRCode(qrCodeString);
							if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
								new GetSiteDetailByQRCode().execute();
							}
							else if(siteMasters.size()>0){
								HashMap<String, String> hashMap=new HashMap<String, String>();
								hashMap.put(KEYS.SITE_IDATTEDENCE, siteMasters.get(0).getSite_id());
								hashMap.put(KEYS.SITE_NAME, siteMasters.get(0).getSiteName());
								hashMap.put(KEYS.COMPANY_NAME,  siteMasters.get(0).getCompany_name());
								SharedPrefrenceManager.setMemberDetailsInSP(MainActivity.this, hashMap);

								qrCodeEditText.setText("");
								tvSiteName.setText("Site: "+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this)
								.get(KEYS.SITE_NAME)+"\nCompany: "+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this)
								.get(KEYS.COMPANY_NAME));
								siteIdAttendence=""+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE);
								if(!siteIdAttendence.equals(null)&&!siteIdAttendence.equals("null")){
									if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE).equals("")) {
										qrCodeEditText.setHint("Enter Site QR Code");
									}else{
										qrCodeEditText.setHint("Enter Gaurd QR Code");
									}
								}else{
									qrCodeEditText.setHint("Enter Site QR Code");
								}
							} 
							else {
								Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
										Toast.LENGTH_SHORT).show();
							} 
						} else {
							Toast.makeText(MainActivity.this,"Please enter site QR code.",
									Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
						GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
						new GetSiteOrGaurdDetailByQRCode().execute(qrCodeString,""+gpsTracker.getLatitude(),
								""+gpsTracker.getLongitude());
					}else if(qrCodeString.contains("S")||qrCodeString.contains("s")){
						qrCodeEditText.setText("");
						DaoSession daoSession=BaseManager.getDBSessoin(MainActivity.this);
						daoSession.getSiteDao().deleteAll();
						SiteDao siteDao=daoSession.getSiteDao();
						List<SiteMaster> siteMasters=new ArrayList<SiteMaster>();
						siteMasters=new LoginManager(MainActivity.this).getSiteMasterDataBYQRCode(qrCodeString);
						if(siteMasters!=null&&siteMasters.size()>0){
							siteDao.insertOrReplace(new Site(siteMasters.get(0).getSite_id(), siteMasters.get(0).getAddress(),
									siteMasters.get(0).getZipcode(), siteMasters.get(0).getCity(), siteMasters.get(0).getEmail_id(),
									siteMasters.get(0).getContact_person(), siteMasters.get(0).getContact_number(), 
									siteMasters.get(0).getBranch_id(), siteMasters.get(0).getBranch_name(), siteMasters
									.get(0).getCustomer_name(),siteMasters.get(0).getCompany_name(), SharedPrefrenceManager
									.getMemberDetailsFromSP(MainActivity.this).get(KEYS.USER_ID),""));									
							HashMap<String, String> hashMap=new HashMap<String, String>();
							hashMap.put(KEYS.SITE_ID, siteMasters.get(0).getSite_id());
							SharedPrefrenceManager.setMemberDetailsInSP(MainActivity.this, hashMap);

							QuestionsDao questionsDao=daoSession.getQuestionsDao();
							daoSession.getQuestionsDao().deleteAll();
							List<MasterQuestions> masterQuestions=new ArrayList<MasterQuestions>();
							masterQuestions=new LoginManager(MainActivity.this).getQuestionMasterDataBYQRCode(qrCodeString);
							if (masterQuestions!=null) {
								for (int i = 0; i < masterQuestions.size(); i++) {
									StringBuilder answer=new StringBuilder();
									String question_type=masterQuestions.get(i).getQuestion_type();
									String question_option=masterQuestions.get(i).getQuestion_option();
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
									questionsDao.insert(new Questions(masterQuestions.get(i).getQuestion_id(),
											masterQuestions.get(i).getQuestion(), masterQuestions.get(i).getCompany_id(), 
											masterQuestions.get(i).getIs_mandatory(), masterQuestions.get(i).getSequence(),
											question_type, question_option, masterQuestions.get(i).getRemark(), 
											masterQuestions.get(i).getIs_published(),answer.toString()));
								}
							}

							if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.ATTENDENCE_MODE).equals("on")) {
								Toast.makeText(MainActivity.this, "QR code is not valid.",Toast.LENGTH_SHORT).show();
							}else{
								intent=new Intent(MainActivity.this,Inspection.class);
								intent.putExtra(KEYS.SITE_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_ID));
								intent.putExtra(KEYS.GUARD_ID, "");
								intent.putExtra(KEYS.SITE_VISITID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_VISITID));
								intent.putExtra(KEYS.TYPE,"site");
								startActivity(intent);
							}
						}else{
							Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
									Toast.LENGTH_SHORT).show();
						}
					}else if(qrCodeString.contains("G")||qrCodeString.contains("g")){
						qrCodeEditText.setText("");
						DaoSession daoSession=BaseManager.getDBSessoin(MainActivity.this);
						daoSession.getGuardDao().deleteAll();
						GuardDao guardDao=daoSession.getGuardDao();
						List<GaurdMaster> gaurdMasters=new ArrayList<GaurdMaster>();
						gaurdMasters=new LoginManager(MainActivity.this).getGuardMasterDataBYQRCode(qrCodeString);
						if(gaurdMasters!=null&&gaurdMasters.size()>0){
							guardDao.insertOrReplace(new Guard(null, gaurdMasters.get(0).getUser_id(),
									gaurdMasters.get(0).getCompany_id(), gaurdMasters.get(0).getFirst_name(),
									gaurdMasters.get(0).getLast_name(),gaurdMasters.get(0).getPhone(),
									gaurdMasters.get(0).getMobile(), gaurdMasters.get(0).getL_address()+" "+
											gaurdMasters.get(0).getP_address(), gaurdMasters.get(0).getZip(), 
											"", "", "","", gaurdMasters.get(0).getPassword(), "", "", 
											gaurdMasters.get(0).getStatus(), gaurdMasters.get(0).getIs_deleted(),
											gaurdMasters.get(0).getCreated_by(),gaurdMasters.get(0).getCreated_date(),
											gaurdMasters.get(0).getQr_code(), gaurdMasters.get(0).getSite_ids(),gaurdMasters.get(0).getCompany_name(),
											SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.USER_ID),
											"",gaurdMasters.get(0).getImg_url(),gaurdMasters.get(0).getDesignation()));
							HashMap<String, String> hashMap=new HashMap<String, String>();
							hashMap.put(KEYS.GUARD_ID, gaurdMasters.get(0).getUser_id());
							SharedPrefrenceManager.setMemberDetailsInSP(MainActivity.this, hashMap);

							QuestionsDao questionsDao=daoSession.getQuestionsDao();
							daoSession.getQuestionsDao().deleteAll();
							List<MasterQuestions> masterQuestions=new ArrayList<MasterQuestions>();
							masterQuestions=new LoginManager(MainActivity.this).getQuestionMasterDataBYQRCode("G");
							if (masterQuestions!=null) {
								for (int i = 0; i < masterQuestions.size(); i++) {
									StringBuilder answer=new StringBuilder();
									String question_type=masterQuestions.get(i).getQuestion_type();
									String question_option=masterQuestions.get(i).getQuestion_option();
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
									questionsDao.insert(new Questions(masterQuestions.get(i).getQuestion_id(),
											masterQuestions.get(i).getQuestion(), masterQuestions.get(i).getCompany_id(), 
											masterQuestions.get(i).getIs_mandatory(), masterQuestions.get(i).getSequence(),
											question_type, question_option, masterQuestions.get(i).getRemark(), 
											masterQuestions.get(i).getIs_published(),answer.toString()));
								}
							}

							Intent intent = new Intent(MainActivity.this, GaurdDetails.class);
							intent.putExtra(KEYS.SITE_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_ID));
							intent.putExtra(KEYS.GUARD_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.GUARD_ID));
							intent.putExtra(KEYS.GUARD_IMAGE, gaurdMasters.get(0).getImg_url());
							startActivity(intent);
						}else{
							Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
									Toast.LENGTH_SHORT).show();
						}
					}else {
						Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
								Toast.LENGTH_SHORT).show();
					}   
				}                
			}else {
				Toast.makeText(MainActivity.this, "Please enter or scan QR code.",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.customInspButton_main:
			Intent intent = new Intent(MainActivity.this, CustomInspection.class);
			startActivity(intent);
			break;
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (result != null && result.getContents()!=null) {
			qrCodeString = result.getContents();
			if (qrCodeString != null|| !qrCodeString.equals("")) {

				if(SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.ATTENDENCE_MODE).equals("on")){
					siteIdAttendence=""+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE);
					if(!siteIdAttendence.equals(null)&&!siteIdAttendence.equals("null")){
						if (qrCodeString.contains("G") || qrCodeString.contains("g")) {
							List<GaurdMaster> gaurdMasters=new ArrayList<GaurdMaster>();
							gaurdMasters=new LoginManager(MainActivity.this).getGuardMasterDataBYQRCode(qrCodeString);
							if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
								GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
								new GetSiteOrGaurdDetailByQRCode().execute(qrCodeString,""+gpsTracker.getLatitude(),
										""+gpsTracker.getLongitude());
							}else if(gaurdMasters!=null && gaurdMasters.size()>0){
								qrCodeEditText.setText("");
								DaoSession daoSession=BaseManager.getDBSessoin(MainActivity.this);
								daoSession.getGuardDao().deleteAll();
								GuardDao guardDao=daoSession.getGuardDao();
								guardDao.insertOrReplace(new Guard(null, gaurdMasters.get(0).getUser_id(),
										gaurdMasters.get(0).getCompany_id(), gaurdMasters.get(0).getFirst_name(),
										gaurdMasters.get(0).getLast_name(),gaurdMasters.get(0).getPhone(),
										gaurdMasters.get(0).getMobile(), gaurdMasters.get(0).getL_address()+" "+
												gaurdMasters.get(0).getP_address(), gaurdMasters.get(0).getZip(), 
												"", "", "","", gaurdMasters.get(0).getPassword(), "", "", 
												gaurdMasters.get(0).getStatus(), gaurdMasters.get(0).getIs_deleted(),
												gaurdMasters.get(0).getCreated_by(),gaurdMasters.get(0).getCreated_date(),
												gaurdMasters.get(0).getQr_code(), gaurdMasters.get(0).getSite_ids(),gaurdMasters.get(0).getCompany_name(),
												SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.USER_ID),
												"",gaurdMasters.get(0).getImg_url(),gaurdMasters.get(0).getDesignation()));
								HashMap<String, String> hashMap=new HashMap<String, String>();
								hashMap.put(KEYS.GUARD_ID, gaurdMasters.get(0).getUser_id());
								SharedPrefrenceManager.setMemberDetailsInSP(MainActivity.this, hashMap);

								QuestionsDao questionsDao=daoSession.getQuestionsDao();
								daoSession.getQuestionsDao().deleteAll();
								List<MasterQuestions> masterQuestions=new ArrayList<MasterQuestions>();
								masterQuestions=new LoginManager(MainActivity.this).getQuestionMasterDataBYQRCode("G");
								if (masterQuestions!=null) {
									for (int i = 0; i < masterQuestions.size(); i++) {
										StringBuilder answer=new StringBuilder();
										String question_type=masterQuestions.get(i).getQuestion_type();
										String question_option=masterQuestions.get(i).getQuestion_option();
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
										questionsDao.insert(new Questions(masterQuestions.get(i).getQuestion_id(),
												masterQuestions.get(i).getQuestion(), masterQuestions.get(i).getCompany_id(), 
												masterQuestions.get(i).getIs_mandatory(), masterQuestions.get(i).getSequence(),
												question_type, question_option, masterQuestions.get(i).getRemark(), 
												masterQuestions.get(i).getIs_published(),answer.toString()));
									}
								}

								Intent intent1 = new Intent(MainActivity.this, GaurdDetails.class);
								intent1.putExtra(KEYS.SITE_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_ID));
								intent1.putExtra(KEYS.GUARD_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.GUARD_ID));
								intent1.putExtra(KEYS.GUARD_IMAGE, gaurdMasters.get(0).getImg_url());
								startActivity(intent1);

							}else {
								Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
										Toast.LENGTH_SHORT).show();
							} 
						}else {
							Toast.makeText(MainActivity.this,"Please enter gaurd QR code.",
									Toast.LENGTH_SHORT).show();
						}
					}else{
						if (qrCodeString.contains("S") || qrCodeString.contains("s")) {
							List<SiteMaster> siteMasters=new ArrayList<SiteMaster>();
							siteMasters=new LoginManager(MainActivity.this).getSiteMasterDataBYQRCode(qrCodeString);
							if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
								new GetSiteDetailByQRCode().execute();
							}
							else if(siteMasters.size()>0){
								HashMap<String, String> hashMap=new HashMap<String, String>();
								hashMap.put(KEYS.SITE_IDATTEDENCE, siteMasters.get(0).getSite_id());
								hashMap.put(KEYS.SITE_NAME, siteMasters.get(0).getSiteName());
								hashMap.put(KEYS.COMPANY_NAME,  siteMasters.get(0).getCompany_name());
								SharedPrefrenceManager.setMemberDetailsInSP(MainActivity.this, hashMap);

								qrCodeEditText.setText("");
								tvSiteName.setText("Site: "+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this)
								.get(KEYS.SITE_NAME)+"\nCompany: "+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this)
								.get(KEYS.COMPANY_NAME));
								siteIdAttendence=""+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE);
								if(!siteIdAttendence.equals(null)&&!siteIdAttendence.equals("null")){
									if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE).equals("")) {
										qrCodeEditText.setHint("Enter Site QR Code");
									}else{
										qrCodeEditText.setHint("Enter Gaurd QR Code");
									}
								}else{
									qrCodeEditText.setHint("Enter Site QR Code");
								}
							} 
							else {
								Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
										Toast.LENGTH_SHORT).show();
							} 
						} else {
							Toast.makeText(MainActivity.this,"Please enter site QR code.",
									Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					if (new ConnectionDetector(MainActivity.this).isConnectingToInternet()) {
						GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
						new GetSiteOrGaurdDetailByQRCode().execute(qrCodeString,""+gpsTracker.getLatitude(),
								""+gpsTracker.getLongitude());
					}else if(qrCodeString.contains("S")||qrCodeString.contains("s")){
						qrCodeEditText.setText("");
						DaoSession daoSession=BaseManager.getDBSessoin(MainActivity.this);
						daoSession.getSiteDao().deleteAll();
						SiteDao siteDao=daoSession.getSiteDao();
						List<SiteMaster> siteMasters=new ArrayList<SiteMaster>();
						siteMasters=new LoginManager(MainActivity.this).getSiteMasterDataBYQRCode(qrCodeString);
						if(siteMasters!=null&&siteMasters.size()>0){
							siteDao.insertOrReplace(new Site(siteMasters.get(0).getSite_id(), siteMasters.get(0).getAddress(),
									siteMasters.get(0).getZipcode(), siteMasters.get(0).getCity(), siteMasters.get(0).getEmail_id(),
									siteMasters.get(0).getContact_person(), siteMasters.get(0).getContact_number(), 
									siteMasters.get(0).getBranch_id(), siteMasters.get(0).getBranch_name(), siteMasters
									.get(0).getCustomer_name(),siteMasters.get(0).getCompany_name(), SharedPrefrenceManager
									.getMemberDetailsFromSP(MainActivity.this).get(KEYS.USER_ID),""));									
							HashMap<String, String> hashMap=new HashMap<String, String>();
							hashMap.put(KEYS.SITE_ID, siteMasters.get(0).getSite_id());
							SharedPrefrenceManager.setMemberDetailsInSP(MainActivity.this, hashMap);

							QuestionsDao questionsDao=daoSession.getQuestionsDao();
							daoSession.getQuestionsDao().deleteAll();
							List<MasterQuestions> masterQuestions=new ArrayList<MasterQuestions>();
							masterQuestions=new LoginManager(MainActivity.this).getQuestionMasterDataBYQRCode(qrCodeString);
							if (masterQuestions!=null) {
								for (int i = 0; i < masterQuestions.size(); i++) {
									StringBuilder answer=new StringBuilder();
									String question_type=masterQuestions.get(i).getQuestion_type();
									String question_option=masterQuestions.get(i).getQuestion_option();
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
									questionsDao.insert(new Questions(masterQuestions.get(i).getQuestion_id(),
											masterQuestions.get(i).getQuestion(), masterQuestions.get(i).getCompany_id(), 
											masterQuestions.get(i).getIs_mandatory(), masterQuestions.get(i).getSequence(),
											question_type, question_option, masterQuestions.get(i).getRemark(), 
											masterQuestions.get(i).getIs_published(),answer.toString()));
								}
							}

							if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.ATTENDENCE_MODE).equals("on")) {
								Toast.makeText(MainActivity.this, "QR code is not valid.",Toast.LENGTH_SHORT).show();
							}else{
								intent=new Intent(MainActivity.this,Inspection.class);
								intent.putExtra(KEYS.SITE_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_ID));
								intent.putExtra(KEYS.GUARD_ID, "");
								intent.putExtra(KEYS.SITE_VISITID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_VISITID));
								intent.putExtra(KEYS.TYPE,"site");
								startActivity(intent);
							}
						}else{
							Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
									Toast.LENGTH_SHORT).show();
						}
					}else if(qrCodeString.contains("G")||qrCodeString.contains("g")){
						qrCodeEditText.setText("");
						DaoSession daoSession=BaseManager.getDBSessoin(MainActivity.this);
						daoSession.getGuardDao().deleteAll();
						GuardDao guardDao=daoSession.getGuardDao();
						List<GaurdMaster> gaurdMasters=new ArrayList<GaurdMaster>();
						gaurdMasters=new LoginManager(MainActivity.this).getGuardMasterDataBYQRCode(qrCodeString);
						if(gaurdMasters!=null&&gaurdMasters.size()>0){
							guardDao.insertOrReplace(new Guard(null, gaurdMasters.get(0).getUser_id(),
									gaurdMasters.get(0).getCompany_id(), gaurdMasters.get(0).getFirst_name(),
									gaurdMasters.get(0).getLast_name(),gaurdMasters.get(0).getPhone(),
									gaurdMasters.get(0).getMobile(), gaurdMasters.get(0).getL_address()+" "+
											gaurdMasters.get(0).getP_address(), gaurdMasters.get(0).getZip(), 
											"", "", "","", gaurdMasters.get(0).getPassword(), "", "", 
											gaurdMasters.get(0).getStatus(), gaurdMasters.get(0).getIs_deleted(),
											gaurdMasters.get(0).getCreated_by(),gaurdMasters.get(0).getCreated_date(),
											gaurdMasters.get(0).getQr_code(), gaurdMasters.get(0).getSite_ids(),gaurdMasters.get(0).getCompany_name(),
											SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.USER_ID),
											"",gaurdMasters.get(0).getImg_url(),gaurdMasters.get(0).getDesignation()));
							HashMap<String, String> hashMap=new HashMap<String, String>();
							hashMap.put(KEYS.GUARD_ID, gaurdMasters.get(0).getUser_id());
							SharedPrefrenceManager.setMemberDetailsInSP(MainActivity.this, hashMap);

							QuestionsDao questionsDao=daoSession.getQuestionsDao();
							daoSession.getQuestionsDao().deleteAll();
							List<MasterQuestions> masterQuestions=new ArrayList<MasterQuestions>();
							masterQuestions=new LoginManager(MainActivity.this).getQuestionMasterDataBYQRCode("G");
							if (masterQuestions!=null) {
								for (int i = 0; i < masterQuestions.size(); i++) {
									StringBuilder answer=new StringBuilder();
									String question_type=masterQuestions.get(0).getQuestion_type();
									String question_option=masterQuestions.get(0).getQuestion_option();
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
									questionsDao.insert(new Questions(masterQuestions.get(i).getQuestion_id(),
											masterQuestions.get(i).getQuestion(), masterQuestions.get(i).getCompany_id(), 
											masterQuestions.get(i).getIs_mandatory(), masterQuestions.get(i).getSequence(),
											question_type, question_option, masterQuestions.get(i).getRemark(), 
											masterQuestions.get(i).getIs_published(),answer.toString()));
								}
							}

							Intent intent1 = new Intent(MainActivity.this, GaurdDetails.class);
							intent1.putExtra(KEYS.SITE_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_ID));
							intent1.putExtra(KEYS.GUARD_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.GUARD_ID));
							intent1.putExtra(KEYS.GUARD_IMAGE, gaurdMasters.get(0).getImg_url());
							startActivity(intent1);
						}else{
							Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
									Toast.LENGTH_SHORT).show();
						}
					}else {
						Toast.makeText(MainActivity.this,getResources().getString(R.string.network_errror),
								Toast.LENGTH_SHORT).show();
					}   
				}                

			}else {
				Toast.makeText(MainActivity.this, "Scan Fail, Not able to scan properly",Toast.LENGTH_SHORT).show();
			}
		}
	}


	public class GetSiteOrGaurdDetailByQRCode extends AsyncTask<String,Void,String>{
		ProgressDialog loginDialog;
		String returnResponse=null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog=ProgressDialog.show(MainActivity.this,"","Please Wait");
		}

		@Override
		protected String doInBackground(String... params) {
			SiteManager siteManager = new SiteManager(MainActivity.this);            
			returnResponse=siteManager.getSiteOrGuardDetailsByQRCode(params[0],params[1],params[2],
					SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.ATTENDENCE_MODE),
					getCurrentDate("yyyy-MM-dd kk:mm:ss"));
			return returnResponse;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			loginDialog.dismiss();
			if (returnResponse.equals("100")) {            	
				qrCodeEditText.setText("");
				if (qrCodeString.contains("S") || qrCodeString.contains("s")){
					if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.ATTENDENCE_MODE).equals("on")) {
						Toast.makeText(MainActivity.this, "QR code is not valid.",Toast.LENGTH_SHORT).show();
					}else{
						intent=new Intent(MainActivity.this,Inspection.class);
						intent.putExtra(KEYS.SITE_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_ID));
						intent.putExtra(KEYS.GUARD_ID, "");
						intent.putExtra(KEYS.SITE_VISITID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_VISITID));
						intent.putExtra(KEYS.TYPE,"site");
						startActivity(intent);
					}                	
				}else {
					Intent intent = new Intent(MainActivity.this, GaurdDetails.class);
					intent.putExtra(KEYS.SITE_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_ID));
					intent.putExtra(KEYS.GUARD_ID, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.GUARD_ID));
					intent.putExtra(KEYS.GUARD_IMAGE, SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.GUARD_IMAGE));
					startActivity(intent);
				}             
			}else{
				Toast.makeText(MainActivity.this, returnResponse,Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class GetSiteDetailByQRCode extends AsyncTask<String,Void,String>{
		ProgressDialog loginDialog;
		String returnResponse=null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog=ProgressDialog.show(MainActivity.this,"","Please Wait");
		}

		@Override
		protected String doInBackground(String... params) {
			SiteManager siteManager = new SiteManager(MainActivity.this);            
			returnResponse=siteManager.getSiteDataByQRCode(qrCodeString);
			return returnResponse;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			loginDialog.dismiss();
			if (returnResponse.equals("100")) {     
				qrCodeEditText.setText("");
				tvSiteName.setText("Site: "+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this)
				.get(KEYS.SITE_NAME)+"\nCompany: "+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this)
				.get(KEYS.COMPANY_NAME));
				siteIdAttendence=""+SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE);
				if(!siteIdAttendence.equals(null)&&!siteIdAttendence.equals("null")){
					if (SharedPrefrenceManager.getMemberDetailsFromSP(MainActivity.this).get(KEYS.SITE_IDATTEDENCE).equals("")) {
						qrCodeEditText.setHint("Enter Site QR Code");
					}else{
						qrCodeEditText.setHint("Enter Gaurd QR Code");
					}
				}else{
					qrCodeEditText.setHint("Enter Site QR Code");
				}
			}else{
				Toast.makeText(MainActivity.this, returnResponse,Toast.LENGTH_SHORT).show();
			}
		}

	}

	/*public void setSpiner()
	{
		customAdapter=new CustomAdapter(getApplicationContext(),DesignationName);
		spinner.setAdapter(customAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()

		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{

				if(check == 0) {

					String str = (String) DesignationName.get(position);

					DESIGNATION=str;
					//Toast.makeText(MainActivity.this, "first"+DESIGNATION, Toast.LENGTH_LONG).show();
				}

				if(++check > 1) {

					String str = (String) DesignationName.get(position);

					DESIGNATION=str;
					//Toast.makeText(MainActivity.this, ""+DESIGNATION, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub


			}
		});

	}*/




}
