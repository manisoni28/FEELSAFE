package com.feelsafe.tutorials;

import com.feelsafe.R;
import com.feelsafe.info.TutorialsPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class OnInstall extends Activity{
	Button next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opening_welcome);
		final Button next=(Button)findViewById(R.id.nextButtonOnInstall);
		Intent j= getIntent();
		final boolean fromInfo=j.getBooleanExtra("fromInfo", false);
		int timeToAppear=5000;
		if(fromInfo){
		  timeToAppear=1000;
		}
		next.setVisibility(4);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				next.setVisibility(0);
			}
		}, timeToAppear);
		
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(OnInstall.this,TutorialsPage.class);
				if(fromInfo){
					i.putExtra("onInstall", false);
				}else{
					i.putExtra("onInstall", true);
				}
				startActivity(i);
				finish();
			}
		});
		
	}
}
