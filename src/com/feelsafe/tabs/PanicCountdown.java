package com.feelsafe.tabs;

import java.util.List;

import com.feelsafe.MainActivity;
import com.feelsafe.R;
import com.feelsafe.SettingsDatabase;
import com.feelsafe.SmsSendReport;
import com.feelsafe.Utilities;
import com.feelsafe.contacts.ContactsDatabase;
import com.feelsafe.contacts.RowItem;
import com.feelsafe.locations.GPSTracker;
import com.feelsafe.trackme.CountdownActivity;
import com.google.android.gms.internal.di;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PanicCountdown extends Activity{

	
	ToggleButton flashButton,sirenButton;
	Button safeButton;
	TextView countdownTextView;
	MediaPlayer mpclick;
	
	 int PANIC_ID=277;
	 int message_cycles=0;
	Context context;
	//==Database========
	SettingsDatabase sdb;
	ContactsDatabase cdb;
	List<String> contactsList;
	int intervalMinutes=5;
	//====other classes======
	Utilities ut;
	SmsSendReport ssr;
	
	 private long startTime = 0L;
	 private Handler myHandler = new Handler();
	 long timeInMillies = 0L;
	 long timeSwap = 0L;
	 long finalTime = 0L;
	 int prevMinute=0;
	 
	//=======GPS ==========
	 GPSTracker gps;   
	 double latitude;
	 double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.countdown_panic);
		
		context=this;
		
		flashButton=(ToggleButton)findViewById(R.id.pmCountdownFlashButton);
		sirenButton=(ToggleButton) findViewById(R.id.pmCountdownSirenButton);
		countdownTextView=(TextView)findViewById(R.id.pmCountdownTimerText);
		safeButton=(Button)findViewById(R.id.pmCountdownStopButton);
		
		cdb=new ContactsDatabase(context);
		sdb= new SettingsDatabase(context);
		contactsList = cdb.getContactNumberList();
		intervalMinutes=Integer.parseInt(sdb.getPanicModeInterval().replace("min",""));
		
		ut = new Utilities(context, sdb);
		ssr = new SmsSendReport(context);
		gps = new GPSTracker(context);
		longitude = gps.getLongitude();
		latitude=gps.getLatitude();
		//send initial message 
		for(int i=0;i<contactsList.size();i++){
			   String number=contactsList.get(i);
			   String message="Help me, I am in Emergency. I am at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; message sent by FeelSafe.";
			   //Toast.makeText(context, "message to "+number, Toast.LENGTH_SHORT).show();
			   ssr.sendSMS(number,message);
		}
		
		
		startTime = SystemClock.uptimeMillis();
	    myHandler.postDelayed(updateTimerMethod, 0);
		
		
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
		
		safeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				confirmCancel();
			}
		});
		
		
	}
	
	
	private Runnable updateTimerMethod = new Runnable() {

		  public void run() {
		   timeInMillies = SystemClock.uptimeMillis() - startTime;
		   finalTime = timeSwap + timeInMillies;
		   
		   int seconds = (int) (finalTime / 1000);
		   int minutes = seconds / 60;
		   int hours=minutes/60;
		   seconds = seconds % 60;
		   int milliseconds = (int) (finalTime % 1000);
		   countdownTextView.setText(hours+":" + String.format("%02d", minutes) + ":"
		     + String.format("%02d", seconds) + ":"
		     + String.format("%03d", milliseconds));
		   
		   if(prevMinute!=minutes && minutes%intervalMinutes==0){ //1min passed
			   message_cycles=message_cycles+1;
			   gps=new GPSTracker(context);
			   longitude = gps.getLongitude();
			   latitude=gps.getLatitude();
			   prevMinute=minutes;
			   for(int i=0;i<contactsList.size();i++){
				   String number=contactsList.get(i);
				   //Toast.makeText(context, "message to "+number, Toast.LENGTH_SHORT).show();
				 ssr.sendSMS(number, "Help me, I am in Emergency. I am at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; message sent by FeelSafe.");
			   }
			   makeNotification(message_cycles,PANIC_ID+message_cycles);
			   
		   }
		   
		   myHandler.postDelayed(this, 0);
		   
		  }

		 };
		 
		 
		 
	public void confirmCancel(){
				AlertDialog.Builder confirmation = new AlertDialog.Builder(this);
				confirmation.setTitle("Are you safe?");
			    confirmation.setMessage("Are you sure, you want to cancel PANIC MODE?")
			           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {
			            	   for(int i=0;i<contactsList.size();i++){
			            		   timeSwap += timeInMillies;
			       			       myHandler.removeCallbacks(updateTimerMethod);
			       			    gps=new GPSTracker(context);
			       			    longitude = gps.getLongitude();
			       			    latitude=gps.getLatitude();
			    				   String number=contactsList.get(i);
			    				   //Toast.makeText(context, "Safe message to "+number, Toast.LENGTH_SHORT).show();
			    				   String message1="I am Safe now, Thank you. at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; -@Location:FeelSafe App";
			    				   ssr.sendSMS(number,message1);
			    				   finish();
			    			   }
			            	   NotificationManager mNotificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		    				   for(int j=0;j<=message_cycles;j++){
		    					   mNotificationManager.cancel(PANIC_ID+j);
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
	
	public void makeNotification(int count,int id){
		NotificationCompat.Builder  mBuilder = 
  		      new NotificationCompat.Builder(context);	
			int time = count*intervalMinutes;
  		      mBuilder.setContentTitle("Messages Sent to Contacts");
  		      mBuilder.setContentText(""+time+"min. finsihed and messages are sent");
  		      mBuilder.setTicker(""+time+"min. finsihed and messages are sent");
  		      mBuilder.setSmallIcon(R.drawable.ic_launcher);
  		      Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
  		      mBuilder.setSound(alarmSound);
  		      long[] pattern = {100,500,100,500};
  		      mBuilder.setVibrate(pattern);
  		      mBuilder.setAutoCancel(true);
  		      mBuilder.setGroup("FeelSafe");
  		      
  		      NotificationManager mNotificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
  		    	      /* notificationID allows you to update the notification later on. */
  		    	      mNotificationManager.notify(id, mBuilder.build());
	}


	public void onBackPressed() {
		final AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Cannot Go back");
		alert.setMessage("You cannot go back to FeelSafe App in Panic Mode.\n* you can press home button to use phone normally.\n* or stop Panic mode if you are Safe.");
		alert.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		alert.show();
	};
	
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
	 	try {
	 		 unregisterReceiver(ssr.sendBroadcastReceiver);
	         unregisterReceiver(ssr.deliveryBroadcastReciever);
		} catch (Exception e) {
			// TODO: handle exception
		}
	 	
	 }
}
