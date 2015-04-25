package com.feelsafe.tabs;

import com.feelsafe.R;
import com.feelsafe.SettingsDatabase;
import com.feelsafe.contacts.Contacts;
import com.feelsafe.contacts.ContactsDatabase;
import com.feelsafe.locations.GPSTracker;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class PanicModeCls {
	Context context;
	ContactsDatabase cdb;
	SettingsDatabase sdb;
	GPSTracker gps;
	double latitude;
    double longitude;
    int PANIC_ID=277;
	
	public PanicModeCls(Context context,ContactsDatabase cdb,SettingsDatabase sdb){
		this.context = context;
		this.cdb=cdb;
		this.sdb=sdb;
		gps = new GPSTracker(context);
	}
	
    public boolean is_sms_mode(){
    	
    	if(sdb.getPanicModeMessage().equals("SMS")){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public void setPrimeContacts(){
    	Intent i = new Intent(context,Contacts.class);
    	context.startActivity(i);
    }
    
    private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try{
               pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
               app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
               app_installed = false;
        }
        return app_installed ;
    }
    
    public void alert_install_whatsapp(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(context);
    	alert.setTitle("WhatsApp not installed");
    	alert.setMessage("Whatsapp is not installed in your phone .please install whatsapp or change mode to SMS");
    	alert.setPositiveButton("install", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "install link coming soon", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=com.whatsapp"));
				context.startActivity(intent);
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
    
    
    public void sendWhatsAppMessage(double latitude,double longitude){
    	Intent sendIntent = new Intent();
    	sendIntent.setAction(Intent.ACTION_SEND);
    	if(latitude==0.0 && longitude==0.0){
    		sendIntent.putExtra(Intent.EXTRA_TEXT, "I am in emergency. I am at Location : --Err: The user GPS didnt send location."+"\n\n\n====FeelSafe App===\nThe user has sent an emergency message.\n\nCall or Message her/him immediately. \n\nyou can find her/his location by clicking the Above link.\nIncase of error link wait for next message\n\n=================");
    	}else {
    		sendIntent.putExtra(Intent.EXTRA_TEXT, "I am in emergency . I am at location \n \nhttps://maps.google.com/maps?q="+latitude+","+longitude+"\n\n\n====FeelSafe App===\nThe user has sent an emergency message.\n\nCall or Message her/him immediately. \n\nyou can find her/his location by clicking the Above link.\nIncase of error link wait for next message\n\n=================");	
		}
    	sendIntent.setType("text/plain");
    	sendIntent.setPackage("com.whatsapp");
    	context.startActivity(sendIntent);
    	Toast.makeText(context, "Select a Broadcast/Group/contact to send the Message", Toast.LENGTH_LONG).show();
    }
    
    public void startPanicMode(){
    	
    	if(gps.canGetLocation()){
    		//ready to launch to Panic Mode
    		
    		//get location details
    		  latitude = gps.getLatitude();
              longitude = gps.getLongitude();
              
    		
    		//get the mode of Message transfer
    		if(is_sms_mode()){
    			//check if prime contacts are seleted
    			int primes= cdb.getPrimeContactsCount();
    	    	if(primes<1){
    	    		setPrimeContacts();
    	    		Toast.makeText(context, "There are no Prime Contacts. please set them and start Panic mode Again.", Toast.LENGTH_LONG).show();
    	    		return;
    	    	}
    			 //Toast.makeText(context, "SMS mode Coming soon. try Whatsapp mode. change in the settings.", Toast.LENGTH_LONG).show();
    	    	 //start panic mode Countdown 
    	    	Intent i = new Intent(context,PanicCountdown.class);
    	    	context.startActivity(i);
    	    	startNotification();
    		}else{ //whatsapp mode
    			//first check if whatsapp is installed
    			if(appInstalledOrNot("com.whatsapp")){
    				sendWhatsAppMessage(latitude,longitude);
    			}else{  // if whatsapp not installed alert to install whatsapp
    				alert_install_whatsapp();
    				return;
    			}
    		}
    	}else{
    		gps.showSettingsAlert();
    		Toast.makeText(context, "Turn On GPS and Start Panic Mode Again", Toast.LENGTH_LONG).show();
    		return;
    	}
    }

    
    private void startNotification(){
    	NotificationCompat.Builder  mBuilder = 
    		      new NotificationCompat.Builder(context);	

    		      mBuilder.setContentTitle("Panic Mode Running..");
    		      mBuilder.setContentText("open Feel Safe app to view/stop panic mode");
    		      mBuilder.setTicker("Panic mode started.");
    		      mBuilder.setSmallIcon(R.drawable.ic_launcher);
    		      Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    		      mBuilder.setSound(alarmSound);
    		      long[] pattern = {100,500,100,500};
    		      mBuilder.setVibrate(pattern);
    		      mBuilder.setUsesChronometer(true);
    		      mBuilder.setAutoCancel(false);
    		      mBuilder.setGroup("FeelSafe");
    		      mBuilder.setPriority(Notification.PRIORITY_HIGH);
    		      
    		      NotificationManager mNotificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    		      Notification nondismissableNotification = mBuilder.build();  
    		      nondismissableNotification.flags = Notification.FLAG_ONGOING_EVENT;

    		    	      /* notificationID allows you to update the notification later on. */
    		    	      mNotificationManager.notify(PANIC_ID, nondismissableNotification);
    }
}
