package com.feelsafe.info;

import com.feelsafe.MainActivity;
import com.feelsafe.R;
import com.feelsafe.contacts.Contacts;
import com.feelsafe.tabs.TabsPagerAdapter;
import com.feelsafe.tutorials.TutorialPagerAdapter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class TutorialsPage extends FragmentActivity implements ActionBar.TabListener{
	//WebView webView;
	//Button next,prev;
	private ViewPager viewPager;
    private TutorialPagerAdapter mAdapter;
    private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	/*	webView = (WebView) findViewById(R.id.tutoriallayoutwebView1);
		
		webView.loadUrl("file:///android_asset/Tutorial/01.html");
		webView.setWebViewClient(new WebViewClient() {
    	    @Override
    	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    	    	
    	    	if(url.contains("FeelSafe//closeIntent")){
    	    				webView.setVisibility(8);
    						// TODO Auto-generated method stub
    	    				Intent j= getIntent();
    	    				boolean onInstall=j.getBooleanExtra("onInstall", false);
    	    				if(onInstall){
    	    					Intent k = new Intent(TutorialsPage.this,MainActivity.class);
    	    					startActivity(k);
    	    					finish();
    	    				}else{
    	    					Intent i = new Intent(TutorialsPage.this,InfoPage.class);
        	    	    		startActivity(i);
        	    	    		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        	    	            finish();
    	    				}
    	    		return true;
    	           
    	        }
    	        //view.loadUrl(url);
    	        return super.shouldOverrideUrlLoading(view, url);
    	    }
    	});
		 */
		
		 // if the app is setup and is not opened for the first time
        //then continue with tabs initialisation
        //tabs initialisation
        viewPager = (ViewPager)findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
       
        
        //setting tabs
        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("1").setTabListener((TabListener) this));
        actionBar.addTab(actionBar.newTab().setText("2").setTabListener((TabListener) this));
        actionBar.addTab(actionBar.newTab().setText("3").setTabListener((TabListener) this));
        actionBar.addTab(actionBar.newTab().setText("4").setTabListener((TabListener) this));
        actionBar.addTab(actionBar.newTab().setText("5").setTabListener((TabListener) this));
        actionBar.addTab(actionBar.newTab().setText("6").setTabListener((TabListener) this));
        actionBar.addTab(actionBar.newTab().setText("7").setTabListener((TabListener) this));
        actionBar.addTab(actionBar.newTab().setText("8").setTabListener((TabListener) this));
        
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          	 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        
        viewPager.setCurrentItem(0);      // to set the 1 tab as the default tab 

	}
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutorials_menu, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.tutorials_cross_button) {
			// TODO Auto-generated method stub
			Intent j= getIntent();
			boolean onInstall=j.getBooleanExtra("onInstall", false);
			if(onInstall){
				Intent k = new Intent(TutorialsPage.this,MainActivity.class);
				startActivity(k);
				finish();
			}else{
				Intent i = new Intent(TutorialsPage.this,InfoPage.class);
	    		startActivity(i);
	    		overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
	            finish();
			}
        }
       
        return super.onOptionsItemSelected(item);
    }
	
}
