package com.feelsafe.trackme;

import java.util.List;

import com.feelsafe.MainActivity;
import com.feelsafe.R;
import com.feelsafe.SettingsDatabase;
import com.feelsafe.SmsSendReport;
import com.feelsafe.Utilities;
import com.feelsafe.contacts.ContactsDatabase;
import com.feelsafe.locations.GPSTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CountdownActivity extends Activity   {
	
	public static TextView countdown_text;
	public static Button finishButton,panicModeButton;
	public static ToggleButton flashButton,sirenButton;
	MediaPlayer mpclick;
	SmsSendReport ssr;
	
	public static int hours;
	public static int minutes;
	public static int seconds;
	
	int PANIC_TYPE=19,CANCEL_TYPE=0;
	
	public static boolean timerFinished;
	private Timer countdown = null;
	private static int HELLO_ID = TrackMeFragmentCls.HELLO_ID;
	
	SettingsDatabase sdb;
	ContactsDatabase db;
	Utilities ut;
	//=======GPS ==========
		 GPSTracker gps;   
		 double latitude;
		 double longitude;
		
	//==================contact details===========
	String contact1Name,contact2Name;
	String contact1Number,contact2Number;
	Boolean contact1IsChecked,contact2IsChecked;
	List<String> list;
	//========================================
	
	
	public void onCreate(Bundle savedInstanceState) {
		
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.countdown_track_me);
	        
	        
	        // gps initialisation=========
 			gps=new GPSTracker(this);
 			 latitude = gps.getLatitude();
              longitude = gps.getLongitude();
  //====database initialisation======
	sdb= new SettingsDatabase(this);
	db= new ContactsDatabase(this);
	//==================================
	ssr = new SmsSendReport(this);
	ut = new Utilities(this, sdb);
	//=====prime contacts values=============
	list=db.getPrimeContactNames();
	contact1Name=list.get(0);
	contact2Name=list.get(1);
	contact1Number=db.getContact(list.get(0));
	contact2Number=db.getContact(list.get(1));
	contact1IsChecked=sdb.getcontact1status().equals("true");
	contact2IsChecked=sdb.getcontact2status().equals("true");
	//=====================================================
	        
	        countdown_text = (TextView) findViewById(R.id.countdown_text);
	        finishButton= (Button) findViewById(R.id.finish_button);
	        panicModeButton= (Button) findViewById(R.id.timerPanicModebutton1);
	        flashButton=(ToggleButton) findViewById(R.id.timerFlashButton);
	        sirenButton=(ToggleButton) findViewById(R.id.timerSirenButton);
	        
	         hours = getIntent().getIntExtra("hours", 0);
	         minutes = getIntent().getIntExtra("minutes", 0);
	         seconds = getIntent().getIntExtra("seconds", 0);
	        
	        long  time = ((hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000));

	        countdown = new Timer(time,1000, this,ssr);
	        timerFinished = false;
	        
	        countdown.start();
	         
	        finishButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(!timerFinished){
						String message = "Are you sure you want to cancel the Track-Me Mode?";
						confirmCancel(message,CANCEL_TYPE);
					}
					else{
						end(); }
				}	
	        });
	        
	        flashButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mpclick = MediaPlayer.create(getApplicationContext(),R.raw.click );
					mpclick.start();
					if(ut.hasFlash()){
						ut.getCamera();
						if(flashButton.isChecked()){
							ut.turnOnFlash();
						}else{
							ut.turnOffFlash();
						} 
						//Toast.makeText(getActivity().getApplicationContext(),"Succesfully in if clause", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(getApplicationContext(),"Your device doesnot support Flash", Toast.LENGTH_LONG).show();
					}
				}
			});
	        
	        sirenButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(sirenButton.isChecked()){
						ut.sirenStart();
					}else{
						ut.sirenStop();
					}
				}
			});
	        
	        panicModeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if(!timerFinished){
						String message = "Are you sure you want to start PANIC MODE? \n\nThis also stops Track-me mode.";
						confirmCancel(message,PANIC_TYPE);
					}
					else{
						end(); }
				}	
	        });
	        
	        
		}

	@Override
	public void onBackPressed() {
		if(!timerFinished){
			String message = "Are you sure you want to cancel the Track-Me Mode?";
			confirmCancel(message,CANCEL_TYPE);
		}
		else{
			end();
		}
	}
	
	public void confirmCancel(String message,final int type){
		AlertDialog.Builder confirmation = new AlertDialog.Builder(this);
	    confirmation.setMessage(message)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   if(contact1IsChecked){
							//Toast.makeText(getApplicationContext(),"Ending SmS to:"+contact1Number+"("+contact1Name+")\n"+"Message:"+sdb.getTrackMeSMS()+"."+latitude+","+longitude, Toast.LENGTH_LONG).show();
     						ssr.sendSMS(contact1Number, "The user has cancelled track me mode. at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; -@Location:FeelSafe App");
						}
						
						if(contact2IsChecked){
							//Toast.makeText(getApplicationContext(),"Ending SmS to:"+contact2Number+"("+contact2Name+")\n"+"Message:"+sdb.getTrackMeSMS()+"."+latitude+","+longitude, Toast.LENGTH_SHORT).show();
							ssr.sendSMS(contact2Number, "The user has cancelled track me mode. at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; -@Location:FeelSafe App");
						}
						if(type==CANCEL_TYPE){
							end();
						}else{
							Intent i = new Intent(CountdownActivity.this,MainActivity.class);
							i.putExtra("panicmode", true);
							startActivity(i);
							end();
						}
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	               }
	           });
	    AlertDialog alert = confirmation.create();
	    alert.show();
	}
	
	private void end(){
		countdown.cancel();
		
		if(timerFinished) {
			Timer.r.stop();
			Timer.v.cancel();
		} else {
			Toast.makeText(CountdownActivity.this, "Timer cancelled", Toast.LENGTH_SHORT).show();
		}
		TrackMeFragmentCls.mNotificationManager.cancel(HELLO_ID);
		finish();
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mpclick!=null){
			mpclick.stop();
		}
	}
	
	 @Override
	 public void onDestroy() {
	 	// TODO Auto-generated method stub
	 	super.onDestroy();
	 	if(ssr!=null){
	 		 unregisterReceiver(ssr.sendBroadcastReceiver);
	         unregisterReceiver(ssr.deliveryBroadcastReciever);
	 	}
	 }
}

