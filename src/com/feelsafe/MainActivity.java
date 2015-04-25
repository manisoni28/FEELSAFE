package com.feelsafe;


import com.feelsafe.contacts.Contacts;
import com.feelsafe.info.InfoPage;
import com.feelsafe.tabs.TabsPagerAdapter;
import com.feelsafe.tutorials.OnInstall;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface.OnClickListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        // for setting the table on installation and redirecting to home page in the beginning
        db=this.openOrCreateDatabase("db_contacts.db", 0, null);
        if(!isTableExists(db, "table_settings")){
        	db.execSQL("create table if not exists table_contacts(name VARCHAR,contact VARCHAR,image TEXT,is_prime BOOLEAN,ID INTEGER PRIMARY KEY);");
        }
        if(!isTableExists(db, "table_settings")){
        	db.execSQL("create table if not exists table_settings(attr VARCHAR,value VARCHAR);");
        	ContentValues contentValues = new ContentValues();
    	    contentValues.put("attr","siren");
    	    contentValues.put("value","police");
    	    db.insert("table_settings", null, contentValues);
    	    contentValues.put("attr","panic_interval");
    	    contentValues.put("value","15min");
    	    db.insert("table_settings", null, contentValues);
    	    contentValues.put("attr","panic_mode");
    	    contentValues.put("value","SMS");
    	    db.insert("table_settings", null, contentValues);
    	    contentValues.put("attr","timer_interval");
    	    contentValues.put("value","5min");
    	    db.insert("table_settings", null, contentValues);
    	    contentValues.put("attr","SMS_alert_sound");
    	    contentValues.put("value","yes");
    	    db.insert("table_settings", null, contentValues);
    	    contentValues.put("attr","Track_me_SMS");
    	    contentValues.put("value","I started tracke me mode. am at location");
    	    db.insert("table_settings", null, contentValues);
    	    contentValues.put("attr","contact1");
    	    contentValues.put("value","false");
    	    db.insert("table_settings", null, contentValues);
    	    contentValues.put("attr","contact2");
    	    contentValues.put("value","false");
    	    db.insert("table_settings", null, contentValues);
    	    Intent i = new Intent(MainActivity.this,OnInstall.class);
    	    startActivity(i);
    	    finish();
        }/*else{
        	Cursor cursor = db.rawQuery("SELECT * FROM table_settings",null);
        	while(cursor.moveToNext()){
        		Toast.makeText(getApplicationContext(), "attr: "+cursor.getString(0)+" value: "+cursor.getString(1), Toast.LENGTH_LONG).show();
        	}
        } */
        
        
        // if the app is setup and is not opened for the first time
        //then continue with tabs initialisation
        //tabs initialisation
        viewPager = (ViewPager)findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        
        //setting tabs
        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("").setIcon(R.drawable.tab_icon_location).setTabListener((TabListener) this));
        actionBar.addTab(actionBar.newTab().setText("").setIcon(R.drawable.tab_icon_home).setTabListener((TabListener) this));
        actionBar.addTab(actionBar.newTab().setText("").setIcon(R.drawable.tab_icon_trackme).setTabListener((TabListener) this));
        
        if(!appInstalledOrNot("com.whatsapp")){
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);
        	alert.setTitle("WhatsApp not installed");
        	alert.setMessage("Whatsapp is needed for the full functionality of the app.Kindly install whatsapp.");
        	alert.setPositiveButton("install", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//Toast.makeText(getApplicationContext(), "install link coming soon", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=com.whatsapp"));
					startActivity(intent);
				}
			});
        	alert.setNegativeButton("close", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				 dialog.dismiss();
				}
			});
        	alert.show();
        	
        }
        
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
        
        viewPager.setCurrentItem(1);      // to set the 2 tab as the default tab
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.contacts_actionButton) {
        	Intent i1=new Intent(MainActivity.this,Contacts.class);
			startActivity(i1);
			overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        	//Toast.makeText(getApplicationContext(), "Contacts button", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.tutorial_actionButton){
        	Intent i1=new Intent(MainActivity.this,InfoPage.class);
			startActivity(i1);
        }
        return super.onOptionsItemSelected(item);
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
	
	
	 private boolean appInstalledOrNot(String uri)
     {
         PackageManager pm = getPackageManager();
         boolean app_installed = false;
         try
         {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                app_installed = true;
         }
         catch (PackageManager.NameNotFoundException e)
         {
                app_installed = false;
         }
         return app_installed ;
     }
	 
	 boolean isTableExists(SQLiteDatabase db, String tableName)
	 {
	     if (tableName == null || db == null || !db.isOpen())
	     {
	         return false;
	     }
	     Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
	     if (!cursor.moveToFirst())
	     {
	         return false;
	     }
	     int count = cursor.getInt(0);
	     cursor.close();
	     return count > 0;
	 }
}
