package com.sixdegreesit.securityapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sixdegreesit.adapter.ExperienceListAdapter;
import com.sixdegreesit.manager.GaurdManager;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.ConnectionDetector;
import com.sixdegreesit.utils.GaurdProfileDTO;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.GaurdProfileDTO.GaurdDTO;
import com.sixdegreesit.utils.GaurdProfileDTO.RatingDTO;

public class GaurdProfile extends BaseActivity{

	TextView tvName,tvEmployee,tvNative,tvLangaugeKnown,tvAvgRating,tvNumReview;
	String guardId="";
	Intent intent=null;
	public static List<GaurdProfileDTO> gaurdProfileDTOs=new ArrayList<GaurdProfileDTO>();
	ImageView gaurdImageView;
	ImageLoader imageLoader;
	private DisplayImageOptions options;
	RatingBar disciplineRatingBar,puntualityRatingBar,fitnessRatingBar,clevernessRatingBar,cleanlinessRatingBar;
	TextView tvDesciplineRating,tvPuntualityRating,tvFitnessRating,tvClevernessRating,tvCleanlinessRating;
	ImageView ivVerify1,ivVerify2,ivVerify3,ivVerify4,ivVerify5;
	ListView lvEmploymentDetails;
	ExperienceListAdapter experienceListAdapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.guardprofile);
		
		imageLoader = ImageLoader.getInstance();	
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.verified_default)
		.showImageForEmptyUrl(R.drawable.verified_default).cacheInMemory()
		.cacheOnDisc().build();
		
		intent=getIntent();
		if (intent!=null) {
			guardId=intent.getStringExtra(KEYS.GUARD_ID);
			Log.i("GaurdProfile", "guardId="+guardId);
		}
		
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
		title.setText("Profile");
		ImageView syncImageView=(ImageView)mCustomView.findViewById(R.id.sync_ImageView);
        syncImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (new ConnectionDetector(GaurdProfile.this).isConnectingToInternet()) {
           		 new  TaskSyncData().execute();
				} else {
					Toast.makeText(GaurdProfile.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				} 
			}
		});
        ImageView logoutImageView=(ImageView)mCustomView.findViewById(R.id.logout_ImageView);
        logoutImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutDailogBox(GaurdProfile.this);
			}
		});
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);	
		
		
		tvName=(TextView)findViewById(R.id.tv_name);
		tvEmployee=(TextView)findViewById(R.id.tv_currEmploye);
		tvNative=(TextView)findViewById(R.id.tv_nativePlace);
		tvLangaugeKnown=(TextView)findViewById(R.id.tv_langKnown);
		tvAvgRating=(TextView)findViewById(R.id.tv_avgRating);
		tvNumReview=(TextView)findViewById(R.id.tv_numReviews);
		gaurdImageView=(ImageView)findViewById(R.id.gaurdIV);
		lvEmploymentDetails=(ListView)findViewById(R.id.lvEmploymentDetails);
		
		
		disciplineRatingBar=(RatingBar)findViewById(R.id.ratingBar_discipline);
		puntualityRatingBar=(RatingBar)findViewById(R.id.ratingBar_punctuality);
		fitnessRatingBar=(RatingBar)findViewById(R.id.ratingBar_fitness);
		clevernessRatingBar=(RatingBar)findViewById(R.id.ratingBar_cleverness);
		cleanlinessRatingBar=(RatingBar)findViewById(R.id.ratingBar_cleanliness);
		tvDesciplineRating=(TextView)findViewById(R.id.tv_rating_discipline);
		tvPuntualityRating=(TextView)findViewById(R.id.tv_rating_punctuality);
		tvFitnessRating=(TextView)findViewById(R.id.tv_rating_fitness);
		tvClevernessRating=(TextView)findViewById(R.id.tv_rating_cleverness);
		tvCleanlinessRating=(TextView)findViewById(R.id.tv_rating_cleanliness);
		
		ivVerify1=(ImageView)findViewById(R.id.iv_ver_1);
		ivVerify2=(ImageView)findViewById(R.id.iv_ver_2);
		ivVerify3=(ImageView)findViewById(R.id.iv_ver_3);
		ivVerify4=(ImageView)findViewById(R.id.iv_ver_4);
		ivVerify5=(ImageView)findViewById(R.id.iv_ver_5);
		
		if (new ConnectionDetector(GaurdProfile.this).isConnectingToInternet()) {
      		 new  TaskGaurdProfile().execute();
		} else {
			Toast.makeText(GaurdProfile.this,getResources().getString(R.string.network_errror),
					Toast.LENGTH_SHORT).show();
		} 
		
	}
	
	
	public class TaskGaurdProfile extends AsyncTask<String,Void,String>{
		ProgressDialog loginDialog;
		String returnResponse=null,questionsArr="";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loginDialog = ProgressDialog.show(GaurdProfile.this, "", "Please Wait");
		}

		@Override
		protected String doInBackground(String... params) {
			gaurdProfileDTOs=new ArrayList<GaurdProfileDTO>();
			GaurdManager gaurdManager=new GaurdManager(GaurdProfile.this);
			returnResponse=gaurdManager.renderGuradProfileByGuardId(guardId);
			return returnResponse;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			loginDialog.dismiss();
			if (returnResponse.equals("100")){
				tvNumReview.setText(gaurdProfileDTOs.get(0).getNumberOfReviews()+" Reviews");
				tvAvgRating.setText(gaurdProfileDTOs.get(0).getAvgRating());	
				GaurdDTO gaurdDTO=gaurdProfileDTOs.get(0).getGaurdDTOs().get(0);
				if (gaurdDTO!=null) {
					tvName.setText(gaurdDTO.getFirst_name().toUpperCase()+" "+gaurdDTO.getLast_name().toUpperCase());
					tvEmployee.setText(gaurdDTO.getCompany_name());
					tvNative.setText(gaurdDTO.getAddress());
					tvLangaugeKnown.setText(gaurdDTO.getLanguage_known());
					
					if (!gaurdDTO.getImg_url().equals("")) {
						imageLoader.init(ImageLoaderConfiguration.createDefault(GaurdProfile.this));
						imageLoader.displayImage(gaurdDTO.getImg_url(), gaurdImageView,options);
					}		
					
					if (!gaurdDTO.getAadhar_card_verification_img_url().equals("")) {
						imageLoader.init(ImageLoaderConfiguration.createDefault(GaurdProfile.this));
						imageLoader.displayImage(gaurdDTO.getAadhar_card_verification_img_url(), ivVerify1,options);
					}
					if (!gaurdDTO.getPolice_verification_img_url().equals("")) {
						imageLoader.init(ImageLoaderConfiguration.createDefault(GaurdProfile.this));
						imageLoader.displayImage(gaurdDTO.getPolice_verification_img_url(), ivVerify2,options);
					}
					if (!gaurdDTO.getEducation_verification_img_url().equals("")) {
						imageLoader.init(ImageLoaderConfiguration.createDefault(GaurdProfile.this));
						imageLoader.displayImage(gaurdDTO.getEducation_verification_img_url(), ivVerify3,options);
					}
					if (!gaurdDTO.getExperience_verification_img_url().equals("")) {
						imageLoader.init(ImageLoaderConfiguration.createDefault(GaurdProfile.this));
						imageLoader.displayImage(gaurdDTO.getExperience_verification_img_url(), ivVerify4,options);
					}
					if (!gaurdDTO.getLicense_verification_img_url().equals("")) {
						imageLoader.init(ImageLoaderConfiguration.createDefault(GaurdProfile.this));
						imageLoader.displayImage(gaurdDTO.getLicense_verification_img_url(), ivVerify5,options);
					}
					
				}
				
				RatingDTO ratingDTO=gaurdProfileDTOs.get(0).getRatingDTOs().get(0);
				if (ratingDTO!=null) {
					disciplineRatingBar.setRating(Float.parseFloat(ratingDTO.getDiscipline_rating()));
					puntualityRatingBar.setRating(Float.parseFloat(ratingDTO.getPunctuality_rating()));
					fitnessRatingBar.setRating(Float.parseFloat(ratingDTO.getFitness_rating()));
					clevernessRatingBar.setRating(Float.parseFloat(ratingDTO.getCleverness_rating()));
					cleanlinessRatingBar.setRating(Float.parseFloat(ratingDTO.getCleanliness_rating()));
					tvDesciplineRating.setText(ratingDTO.getDiscipline_rating());
					tvPuntualityRating.setText(ratingDTO.getPunctuality_rating());
					tvFitnessRating.setText(ratingDTO.getFitness_rating());
					tvClevernessRating.setText(ratingDTO.getCleverness_rating());
					tvCleanlinessRating.setText(ratingDTO.getCleanliness_rating());
				}
				
				if (gaurdProfileDTOs.get(0).getExperienceDTOs()!=null) {
					experienceListAdapter=new ExperienceListAdapter(GaurdProfile.this, R.layout.item_empdetails,
							gaurdProfileDTOs.get(0).getExperienceDTOs());
					lvEmploymentDetails.setAdapter(experienceListAdapter);
				}
				
			}else{
				Toast.makeText(GaurdProfile.this,returnResponse,Toast.LENGTH_LONG).show();
			}
		}
	}
}
