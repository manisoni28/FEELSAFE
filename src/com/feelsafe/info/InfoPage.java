package com.feelsafe.info;

import com.feelsafe.R;
import com.feelsafe.tutorials.OnInstall;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class InfoPage extends Activity {
	
	Button tuts,story,tips,credits;
	LinearLayout ls,lc,ltp,lt,bg;
 @Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.info_page_layout);
	tuts=(Button) findViewById(R.id.tutorialButton);
	story=(Button) findViewById(R.id.storiesButton);
	tips=(Button) findViewById(R.id.tipsButton);
	credits=(Button) findViewById(R.id.creditsButton);
	ls=(LinearLayout) findViewById(R.id.stories_layout);
	lc=(LinearLayout) findViewById(R.id.credits_layout);
	ltp=(LinearLayout) findViewById(R.id.tips_layout);
	lt=(LinearLayout) findViewById(R.id.tutorial_layout);
	bg=(LinearLayout) findViewById(R.id.info_page_bg_layout);
	
	final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanim);
	final Animation animation_tuts = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanim_tuts);
	final Animation animation_credits = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanim_credits);
	final Animation animation_stories = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanim_stories);
	tuts.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			  lc.setVisibility(8);
			    ltp.setVisibility(8);
			    ls.setVisibility(8);
			    bg.setBackgroundColor(Color.parseColor("#9b59b6"));
			tuts.startAnimation(animation_tuts);
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent i = new Intent(InfoPage.this,OnInstall.class);
					i.putExtra("fromInfo", true);
					startActivity(i);
					overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
					finish();
				}
			}, 1000);
		
		}
	});
	
	story.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), "Coming soon..", Toast.LENGTH_LONG).show();	
			bg.setBackgroundColor(Color.parseColor("#f1c40f"));
		    lc.setVisibility(8);
		    ltp.setVisibility(8);
		    lt.setVisibility(8);
		    story.startAnimation(animation_stories);
		    Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent i = new Intent(InfoPage.this,StoriesPage.class);
					startActivity(i);
					overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
					finish();
				}
			}, 1000);
		   
		}
	});
	tips.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//bg.setBackgroundColor(Color.parseColor("#e74c3c"));
		 	lc.setVisibility(8);
		    ls.setVisibility(8);
		    lt.setVisibility(8);
		    tips.startAnimation(animation);
		    Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent i = new Intent(InfoPage.this,StoriesPage.class);
					startActivity(i);
					finish();
				}
			}, 2000);
		}
	});
	credits.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			bg.setBackgroundColor(Color.parseColor("#3498db"));
			ltp.setVisibility(8);
		    ls.setVisibility(8);
		    lt.setVisibility(8);
		credits.startAnimation(animation_credits);
		
	    Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(InfoPage.this,CreditsPage.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
				finish();
			}
		}, 1000);
		}
	});
	
}
 
}
