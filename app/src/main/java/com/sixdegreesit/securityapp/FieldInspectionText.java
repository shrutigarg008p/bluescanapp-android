package com.sixdegreesit.securityapp;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sixdegreesit.manager.SiteManager;
import com.sixdegreesit.utils.BaseActivity;
import com.sixdegreesit.utils.ConnectionDetector;
import com.sixdegreesit.utils.BaseActivity.TaskSyncData;

public class FieldInspectionText extends BaseActivity {
 
    Intent intent = null;
    String questionId="",question="",answer="";
    TextView titleTextView,questionTextView;
    EditText remarkEditText;
    Button saveGuardRemarkButton;
    int groupPosition=0;
    BroadcastReceiver broadcastReceiver=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_guard_remark);
        
        intent=getIntent();
        if (intent!=null) {
			questionId=intent.getStringExtra("questionId");
			question=intent.getStringExtra("question");
			answer=intent.getStringExtra("answer");
			groupPosition=intent.getIntExtra("groupPosition", 0);
		}

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        mCustomView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        titleTextView.setText("Remark");
        ImageView menuImageView = (ImageView) mCustomView.findViewById(R.id.menu_ImageView);
        menuImageView.setVisibility(View.INVISIBLE);
        ImageView syncImageView=(ImageView)mCustomView.findViewById(R.id.sync_ImageView);
        syncImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				popupOptionMenu(v);
				if (new ConnectionDetector(FieldInspectionText.this).isConnectingToInternet()) {
           		 new  TaskSyncData().execute();
				} else {
					Toast.makeText(FieldInspectionText.this,getResources().getString(R.string.network_errror),
							Toast.LENGTH_SHORT).show();
				} 
			}
		});
        ImageView logoutImageView=(ImageView)mCustomView.findViewById(R.id.logout_ImageView);
        logoutImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutDailogBox(FieldInspectionText.this);
			}
		});
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
      
        
        questionTextView=(TextView)findViewById(R.id.questionTextView);
        remarkEditText = (EditText) findViewById(R.id.remarkEditText);
        saveGuardRemarkButton = (Button) findViewById(R.id.saveRemarkButton);
        questionTextView.setText(question);
        remarkEditText.setText(answer);
        
        saveGuardRemarkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String remark=remarkEditText.getText().toString().trim();
				if (remark.length()==0) {
					Toast.makeText(FieldInspectionText.this, "Please enter remark.", Toast.LENGTH_LONG).show();
				} else {
					SiteManager siteManager=new SiteManager(FieldInspectionText.this);
					siteManager.updateAnswerByQueId(questionId, remark);
					Inspection.mGroupCollection.get(groupPosition).setAnswer(remark);
					Inspection.questionsList.get(groupPosition).setAnswer(remark);
					finish();
				}
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
   
}