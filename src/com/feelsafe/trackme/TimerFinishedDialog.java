package com.feelsafe.trackme;

import java.util.List;

import com.feelsafe.MainActivity;
import com.feelsafe.R;
import com.feelsafe.SettingsDatabase;
import com.feelsafe.SmsSendReport;
import com.feelsafe.contacts.ContactsDatabase;
import com.feelsafe.locations.GPSTracker;
import com.feelsafe.tabs.PanicModeCls;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.widget.Toast;

public class TimerFinishedDialog extends Activity {
	MediaPlayer sound;
	SettingsDatabase sdb;
	ContactsDatabase db;
	CountDownTimer cdt;
	SmsSendReport ssr;
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

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         ssr= new SmsSendReport(this);
        final AlertDialog alert = showTimerFinishedDialog();
        sound = MediaPlayer.create(getApplicationContext(),R.raw.track_mode_finished);
		sound.setLooping(true);
		sound.start();
        alert.show();
        cdt = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            	int seconds=(int) (millisUntilFinished/1000);
            	if(seconds>=60){
            		alert.setMessage("TrackMe Timer finished. Are you Safe ?\n\nIf not responded in 2min PanicMode starts Automatically.\ntime remaining 00:01:"+(seconds-60));
            	}else{
               alert.setMessage("TrackMe Timer finished. Are you Safe ?\n\nIf not responded in 2min PanicMode starts Automatically.\ntime remaining 00:"+seconds);
            	}
            }

            @Override
            public void onFinish() {
                //info.setVisibility(View.GONE);
            	sound.stop();
            	sound = MediaPlayer.create(getApplicationContext(),R.raw.panic_mode_activated);
        		sound.setLooping(false);
        		sound.start();
            	alert.dismiss();
            	Toast.makeText(getApplicationContext(), "Panic Mode Activated", Toast.LENGTH_LONG).show();
            	Intent i = new Intent(TimerFinishedDialog.this,MainActivity.class);
            	i.putExtra("panicmode", true);
            	startActivity(i);
            	CountdownActivity.finishButton.performClick();
            	finish();
            	
            }
        }.start();
       /* for(int i=0;i<60;i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			alert.setMessage("Timer finished. time reaming :"+(60-i));
		} */
     // gps initialisation=========
     			gps=new GPSTracker(this);
     			 latitude = gps.getLatitude();
                  longitude = gps.getLongitude();
      //====database initialisation======
		sdb= new SettingsDatabase(this);
		db= new ContactsDatabase(this);
		//=====prime contacts values=============
		list=db.getPrimeContactNames();
		contact1Name=list.get(0);
		contact2Name=list.get(1);
		contact1Number=db.getContact(list.get(0));
		contact2Number=db.getContact(list.get(1));
		contact1IsChecked=sdb.getcontact1status().equals("true");
		contact2IsChecked=sdb.getcontact2status().equals("true");
		//=====================================================
	}
        
	private AlertDialog showTimerFinishedDialog() {
		ssr= new SmsSendReport(this);
		final AlertDialog alertDialog = new AlertDialog.Builder(this)
			.setTitle("Countdown Timer")
			.setMessage("TrackMe Timer finished. Are you Safe ?\n\nIf not responded in 2min PanicMode starts Automatically.\ntime remaining 00:")
		
			.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int whichButton) { 
					if(sound!=null)
					  sound.stop();
					cdt.cancel();
					CountdownActivity.finishButton.performClick();
					
					if(contact1IsChecked){
						//Toast.makeText(mContext,"SmS to:"+contact1Number+"("+contact1Name+")\n"+"Message:"+sdb.getTrackMeSMS()+".I am at Location https://maps.google.com/maps?q="+latitude+","+longitude, Toast.LENGTH_SHORT).show();
						ssr.sendSMS(contact1Number, "End of track me Mode. The user is safe and at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; -@Location:FeelSafe App");
					}
					
					if(contact2IsChecked){
						//Toast.makeText(mContext,"SmS to:"+contact2Number+"("+contact2Name+")\n"+"Message:"+sdb.getTrackMeSMS()+".I am at Location https://maps.google.com/maps?q="+latitude+","+longitude, Toast.LENGTH_SHORT).show();
						ssr.sendSMS(contact2Number, "End of track me Mode. The user is safe and at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; -@Location:FeelSafe App");
					}
					finish();
					
				}              
			})
			.setNegativeButton("New timer", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int whichButton) { 
					if(sound!=null)
						sound.stop();
					cdt.cancel();
					CountdownActivity.finishButton.performClick();
					Intent newTimer = new Intent(TimerFinishedDialog.this, MainActivity.class);
					startActivity(newTimer);
					finish();
				}              
			})
		.create();
		
	return alertDialog;
	}
	
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try{
	 		 unregisterReceiver(ssr.sendBroadcastReceiver);
	         unregisterReceiver(ssr.deliveryBroadcastReciever);
	 	}catch(Exception e){
	 		
	 	}
	} 
	  
}
