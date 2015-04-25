package com.feelsafe.trackme;

import java.util.List;

import com.feelsafe.SettingsDatabase;
import com.feelsafe.SmsSendReport;
import com.feelsafe.contacts.ContactsDatabase;
import com.feelsafe.locations.GPSTracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Timer extends CountDownTimer{
	
	public static Ringtone r;
	public static Vibrator v;
	private static int HELLO_ID = TrackMeFragmentCls.HELLO_ID;
	private Context mContext;
	private PowerManager pm;
	int i,intervalsec;
	SettingsDatabase sdb;
	ContactsDatabase db;
	TrackMeFragmentCls tm;
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
	
	
	public Timer(long millisInFuture, long countDownInterval, Context mContext,SmsSendReport ssr) {
			super(millisInFuture, countDownInterval);
			
			this.mContext = mContext;
			this.ssr=ssr;
			// gps initialisation=========
			gps=new GPSTracker(mContext);
			 latitude = gps.getLatitude();
             longitude = gps.getLongitude();
			//====database initialisation======
			sdb= new SettingsDatabase(mContext);
			db= new ContactsDatabase(mContext);
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
	
	@Override
	public void onFinish() {
		
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		r = RingtoneManager.getRingtone(mContext, notification);
		pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Countdown Timer");
		final long[] pattern = {1,300,75,300,75,300,75,300,3000,300,75,300,75,300,75,300};
		
		
		Intent TimerFinishedDialog = new Intent(mContext,TimerFinishedDialog.class);
		TimerFinishedDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		TimerFinishedDialog.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		CountdownActivity.countdown_text.setText("Finished.");
		CountdownActivity.finishButton.setText("Ok");
		CountdownActivity.timerFinished = true;
		
		wl.acquire();
		
		mContext.getApplicationContext().startActivity(TimerFinishedDialog);
		TrackMeFragmentCls.mNotificationManager.cancel(HELLO_ID);
		
		v.vibrate(pattern, -1);
		r.play();
		
		wl.release();
	}
	
	@Override
	public void onTick(long millisUntilFinished) {
		

		long remainingMs = millisUntilFinished;
		
		// Display meaningful time remaining instead of milliseconds.
		Integer h = new Integer((int) remainingMs/3600000);
		long remaining = remainingMs - (h*3600000);
		Integer m = new Integer((int) remaining/60000);
		remaining = remaining - (m*60000);
		Integer s = new Integer((int) (remaining /1000));
		i=((h*60*60)+(m*60)+s)*1000;
		
		String remainingH;
		String remainingM;
		String remainingS;
		
		if(h.equals(new Integer(0)))
			 remainingH = "00";
		else
			if(h<10)
				remainingH = "0"+h.toString();
			else
			remainingH = h.toString();
		
		if(m.equals(new Integer(0)))
			 remainingM = "00";
		else{
			if(m < 10)
				remainingM = "0"+m.toString();
			else
				remainingM = m.toString();
		}
		
		if(s<10)
			remainingS = "0"+s.toString();
		else
			remainingS = s.toString();
		
			
		if(remainingH.equals("00")){
			CountdownActivity.countdown_text.setText(remainingM+":"+remainingS);
		}
		else{
			CountdownActivity.countdown_text.setText(remainingH+":"+remainingM+":"+remainingS);
		}
		String interval=sdb.getTrackMeInterval();
		if(interval.equals("5min")){
			//intervalsec=300000;
			intervalsec=300000;
		}
		else if(interval.equals("10min")){
			intervalsec=600000;
		}
		else if(interval.equals("15min")){
			intervalsec=900000;
		}
		else if(interval.equals("30min")){
			intervalsec=1800000;
		}
		if(i%intervalsec==0){
			
			if(contact1IsChecked){
				//Toast.makeText(mContext,"SmS to:"+contact1Number+"("+contact1Name+")\n"+"Message:"+sdb.getTrackMeSMS()+".I am at Location https://maps.google.com/maps?q="+latitude+","+longitude, Toast.LENGTH_SHORT).show();
				ssr.sendSMS(contact1Number, "sent a Location message. at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; -@Location:FeelSafe App");
			}
			
			if(contact2IsChecked){
				//Toast.makeText(mContext,"SmS to:"+contact2Number+"("+contact2Name+")\n"+"Message:"+sdb.getTrackMeSMS()+".I am at Location https://maps.google.com/maps?q="+latitude+","+longitude, Toast.LENGTH_SHORT).show();
				ssr.sendSMS(contact2Number, "sent a Location message. at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; -@Location:FeelSafe App");
			}
			
			
		}
	}
	
	
	
	
}

