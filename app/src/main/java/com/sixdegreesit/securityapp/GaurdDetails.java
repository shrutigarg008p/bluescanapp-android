package com.sixdegreesit.securityapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.db.DesignationData;
import com.android.db.Guard;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sixdegreesit.adapter.CustomAdapter;
import com.sixdegreesit.manager.GaurdManager;
import com.sixdegreesit.manager.LoginManager;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.ConnectionDetector;
import com.sixdegreesit.utils.KEYS;
import com.sixdegreesit.utils.SharedPrefrenceManager;

/**
 * Created by Sanjay on 30/09/15.
 */
public class GaurdDetails extends BaseActivity implements View.OnClickListener{

	Spinner spinner;
	List<DesignationData> spinnerpos;
	List<DesignationData> designationList;
	List<String> DesignationName = new ArrayList<String>();
	CustomAdapter customAdapter;
	int check = 0;
	public static String DESIGNATION="";
	
    Intent intent = null;
    String siteId = "", gaurdId = "", gaurdImage="";
    ImageView userImageView,ivOk;
    TextView tvName, tvAddress, tvMobile, tvLangaugeKnown;
    List<Guard> guardList=null;
    BroadcastReceiver broadcastReceiver=null;
    Button inspectButton,gaurdProfileButton;
    public ImageLoader imageLoader; 
	private DisplayImageOptions options;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.gaurd_details);
        
        spinner=(Spinner)findViewById(R.id.spinner);
		designationList = new LoginManager(getApplicationContext()).getAllDesignationDataReturn();
	
	
		
		
        intent = getIntent();
        if (intent != null) {
            siteId = intent.getStringExtra(KEYS.SITE_ID);
            gaurdId = intent.getStringExtra(KEYS.GUARD_ID);
            gaurdImage=intent.getStringExtra(KEYS.GUARD_IMAGE);
            Log.i("GaurdDetails","siteId="+siteId+"---gaurdId="+gaurdId);
        }
        
        imageLoader = ImageLoader.getInstance();	
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_user_new)
		.showImageForEmptyUrl(R.drawable.default_user_new).cacheInMemory()
		.cacheOnDisc().build();

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        mCustomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView title = (TextView) mCustomView.findViewById(R.id.title_text);
        title.setText("Details");
        ImageView menuImageView=(ImageView)mCustomView.findViewById(R.id.menu_ImageView);
        menuImageView.setVisibility(View.INVISIBLE);
        ImageView syncImageView=(ImageView)mCustomView.findViewById(R.id.sync_ImageView);
        syncImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				popupOptionMenu(v);
				if (new ConnectionDetector(GaurdDetails.this).isConnectingToInternet()) {
           		 new  TaskSyncData().execute();
				} else {
					Toast.makeText(GaurdDetails.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				} 
			}
		});
        ImageView logoutImageView=(ImageView)mCustomView.findViewById(R.id.logout_ImageView);
        logoutImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutDailogBox(GaurdDetails.this);
			}
		});
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
       

        userImageView = (ImageView) findViewById(R.id.iv_user);
        if (!gaurdImage.equals("")) {
			imageLoader.init(ImageLoaderConfiguration.createDefault(GaurdDetails.this));
			imageLoader.displayImage(gaurdImage, userImageView,options,null);
		}else{
			 userImageView.setImageResource(R.drawable.default_user_new);
		}
        ivOk = (ImageView) findViewById(R.id.ivOk);
        ivOk.setOnClickListener(this);
        inspectButton=(Button)findViewById(R.id.button_inspect);
        inspectButton.setOnClickListener(this);
        gaurdProfileButton=(Button)findViewById(R.id.button_gaurdProfile);
        gaurdProfileButton.setOnClickListener(this);

        tvName = (TextView) findViewById(R.id.tvUsername);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvLangaugeKnown = (TextView) findViewById(R.id.tvLangaugesKnown);

        GaurdManager gaurdManager=new GaurdManager(GaurdDetails.this);
        guardList=new ArrayList<Guard>();
        guardList=gaurdManager.getGuardDataByGuardId(gaurdId);
        if (guardList!=null){
            if(guardList.size()>0) {
                tvName.setText(guardList.get(0).getFirst_name() + " " + guardList.get(0).getLast_name());
                
             
               
                tvAddress.setText(guardList.get(0).getAddress());
                tvMobile.setText(guardList.get(0).getMobile());
                tvLangaugeKnown.setText(guardList.get(0).getLanguage_known());
            }
        }
        
    	setSpiner();
        
        if (SharedPrefrenceManager.getMemberDetailsFromSP(GaurdDetails.this).get(KEYS.ATTENDENCE_MODE).equals("on")) {
        	inspectButton.setVisibility(View.GONE);
        	ivOk.setVisibility(View.VISIBLE);
		}else{
			inspectButton.setVisibility(View.VISIBLE);
			ivOk.setVisibility(View.GONE);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivOk:
                intent = new Intent(GaurdDetails.this, AndroidCameraExample.class);
                intent.putExtra(KEYS.GUARD_ID, gaurdId);
                startActivity(intent);
                break;
            case R.id.button_inspect:
                intent=new Intent(GaurdDetails.this,Inspection.class);
                intent.putExtra(KEYS.SITE_ID, siteId);
                intent.putExtra(KEYS.GUARD_ID, gaurdId);
                intent.putExtra(KEYS.SITE_VISITID, SharedPrefrenceManager.getMemberDetailsFromSP(GaurdDetails.this).get(KEYS.SITE_VISITID));
                intent.putExtra(KEYS.TYPE,"guard");
                startActivity(intent);
                break;
            case R.id.button_gaurdProfile:
            	 intent = new Intent(GaurdDetails.this, GaurdProfile.class);
                 intent.putExtra(KEYS.GUARD_ID, gaurdId);
                 startActivity(intent);
            	break;
        }
    }
    
    int citypos=0;
    String Designationfinal11="";
	public void setSpiner()
	{
		
		  Designationfinal11=guardList.get(0).getDesignation();
        //  Toast.makeText(getApplicationContext(), "post11="+Designationfinal11, Toast.LENGTH_LONG).show();
		
		for (int x = 0; x < designationList.size(); x++)
		{
			DesignationName.add(designationList.get(x).getDesignation());
			if (Designationfinal11.equalsIgnoreCase(designationList.get(x).getDesignation()))
			{
			//Toast.makeText(getApplicationContext(), "if="+designationList.get(x).getDesignation(), Toast.LENGTH_LONG).show();
				citypos=x;
			}
		}
		
       // Toast.makeText(getApplicationContext(), "citypos="+citypos, Toast.LENGTH_LONG).show();

		
		customAdapter=new CustomAdapter(getApplicationContext(),DesignationName);
		spinner.setAdapter(customAdapter);
		
        spinner.setSelection(citypos);
        
        
       
        
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{

				if(check == 0) {

					String str = (String) DesignationName.get(position);

					 DESIGNATION=Designationfinal11;
					 KEYS.desination=Designationfinal11;
					//Toast.makeText(getApplicationContext(), "first="+DESIGNATION, Toast.LENGTH_LONG).show();
				}

				if(++check > 1) {

					String str = (String) DesignationName.get(position);

					DESIGNATION=str;
					KEYS.desination=str;
					//Toast.makeText(getApplicationContext(), "second="+DESIGNATION, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub


			}
		});

	}


}

