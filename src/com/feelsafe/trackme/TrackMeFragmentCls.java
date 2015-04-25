package com.feelsafe.trackme;

import java.util.List;

import com.feelsafe.R;
import com.feelsafe.SettingsDatabase;
import com.feelsafe.SmsSendReport;
import com.feelsafe.contacts.ContactsDatabase;
import com.feelsafe.locations.GPSTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TrackMeFragmentCls extends Fragment{
	private int hours;
	private int minutes;
	private int seconds;
	String mess;
	private EditText hoursBox;
	private EditText minutesBox;
	private EditText secondsBox;
	CheckBox contact1,contact2;
	Button startButton;
	public static NotificationManager mNotificationManager;
	public static int HELLO_ID = 1;
	View rootView;
	SettingsDatabase sdb;
	ContactsDatabase db;
	SmsSendReport ssr;
	GPSTracker gps;
	double latitude;
    double longitude;
   
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	   rootView =inflater.inflate(R.layout.fragment_trackme, container,false);
	   
	   sdb=new SettingsDatabase(getActivity().getApplicationContext());
	   db=new ContactsDatabase(getActivity().getApplicationContext());
	   ssr=new SmsSendReport(getActivity().getApplicationContext());
	   gps = new GPSTracker(getActivity());
	   	hoursBox = (EditText)rootView.findViewById(R.id.hoursBox1);
	   	minutesBox = (EditText)rootView.findViewById(R.id.minutesBox1);
	   	secondsBox = (EditText)rootView.findViewById(R.id.secondsBox1);
	     startButton = (Button)rootView.findViewById(R.id.trackmeStartButton);
	     contact1=(CheckBox) rootView.findViewById(R.id.contactCheckBox01);
	     contact2=(CheckBox) rootView.findViewById(R.id.contactCheckBox02);
	     
	     if(db.getPrimeContactsCount()<2){
	    	 startButton.setVisibility(4);
	    	 contact1.setText("Set Prime Contacts");
	    	 contact2.setText("and come back");
	     }else{
	    	 List<String> list = db.getPrimeContactNames();
	    	 startButton.setVisibility(2);
	    	 contact1.setText(list.get(0));
	    	 contact2.setText(list.get(1));
	    	 
	     }
	    
	     contact1.setChecked(sdb.getcontact1status().equals("true"));
	     contact2.setChecked(sdb.getcontact2status().equals("true"));
	     
	     contact1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(contact1.isChecked()){
					sdb.setcontact1status("true");
				}else{
					sdb.setcontact1status("false");
				}
			}
		});
	     
	     contact2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(contact2.isChecked()){
						sdb.setcontact2status("true");
					}else{
						sdb.setcontact2status("false");
					}
				}
			});
	     
	     
	     startButton.setOnClickListener(new View.OnClickListener() {
	   
	   	  public void onClick(View v) {
	   		  
	   		  if(contact1.isChecked() || contact2.isChecked()){
	   			  
	   		  
	   		  if(!(hoursBox.getText().toString().equals("") 
	   				  && minutesBox.getText().toString().equals("")
	   				  && secondsBox.getText().toString().equals(""))) {
	   			  
	   			gps = new GPSTracker(getActivity()); 
	   			  //gps turn on check
	   			if(gps.canGetLocation()){
                    
                    latitude = gps.getLatitude();
                     longitude = gps.getLongitude();
                   
	   			  final Intent startCountdown = new Intent(getActivity(), CountdownActivity.class);
			   
	   			  if(hoursBox.getText().toString().equals(""))
	   				  hoursBox.setText("0");
			    
	   			  if(minutesBox.getText().toString().equals(""))
	   				  minutesBox.setText("0");
			    
	   			  if(secondsBox.getText().toString().equals(""))
	   				  secondsBox.setText("0");
					    		
	   			  hours = Integer.parseInt(hoursBox.getText().toString());
	   			  minutes = Integer.parseInt(minutesBox.getText().toString());	
	   			  seconds = Integer.parseInt(secondsBox.getText().toString());
			    
	   			  startCountdown.putExtra("hours", hours);
	   			  startCountdown.putExtra("minutes", minutes);
	   			  startCountdown.putExtra("seconds", seconds);
	   			  
	   			  
	   			  //min time for interval check 
	   			  int interval=Integer.parseInt(sdb.getTrackMeInterval().replace("min",""));
	   			  if(hours==0 && minutes<interval){
	   				  Toast.makeText(getActivity().getApplicationContext(), "Minimum "+interval+"min. required, change the trackme interval settings for less time.", Toast.LENGTH_LONG).show();
	   				  return;
	   			  }
		   	
	   			//Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
	   			 // AlertDialog.Builder alert1= new AlertDialog.Builder(getActivity());
	   			 // alert1.setTitle("MESSAGE");
	   			 // alert1.setCancelable(false);
	   			  
	   			final AlertDialog dialogDetails;
	   			LayoutInflater inflater = LayoutInflater.from(getActivity());
	   			final View dialogview = inflater.inflate(R.layout.dialog_trackme_enter_message, null);
	   			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
	   			dialogbuilder.setView(dialogview);
	   			dialogDetails = dialogbuilder.create();
	   			dialogDetails.setCancelable(false);
	   			dialogDetails.setCanceledOnTouchOutside(false);
	   			dialogDetails.show();
	   			TextView title= (TextView) dialogview.findViewById(R.id.dialogHeading_Enter_SMS);
	   			final EditText input=(EditText) dialogview.findViewById(R.id.dialogeditText_Enter_SMS);
	   			Button okButton=(Button)dialogview.findViewById(R.id.dialog_Enter_SMS_OK_button1);
	   			Button cancelButton=(Button)dialogview.findViewById(R.id.dialog_enter_sms_cancel_button1);
	   			title.setText("Enter Message");
	   			okButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mess=input.getText().toString();
						//Toast.makeText(getActivity(), mess, Toast.LENGTH_LONG).show();
						if(mess.length()>0){
							sdb.setTrackMeSMS(mess);
							mess=mess+". Frm nw u'll get my location msg for every "+sdb.getTrackMeInterval()+" -@Location:FeelSafe App";
							//======sending Message to contacts===========
							if(contact1.isChecked()){
								String number=db.getContact(contact1.getText().toString());
								if(number.length()>0){
							ssr.sendSMS(number, mess);
								}
							}
							if(contact2.isChecked()){
								String number=db.getContact(contact2.getText().toString());
								if(number.length()>0){
								ssr.sendSMS(number, mess);
								}
							}
							//=====================================
							startActivity(startCountdown);
				   			getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);	
				   			String ns = Context.NOTIFICATION_SERVICE;
				   			  mNotificationManager = (NotificationManager)getActivity().getSystemService(ns);
				   			  CharSequence tickerText = "Track-Me Mode Started";
				   			  int icon = R.drawable.ic_launcher;
				       
				   			  long when = System.currentTimeMillis();

				   			  Notification notification = new Notification(icon, tickerText, when);
				   			  
				   			  CharSequence contentTitle = "Track-Me Mode Active";
				   			  CharSequence contentText = "Open Feel safe App to View/stop";
				   			  //Intent notificationIntent = new Intent(getActivity(), CountdownActivity.class);
				   			  //passed a null intent in pending Intent for not opening the activity again
				   			  PendingIntent pIntent = PendingIntent.getActivity(getActivity(), 0, new Intent(), HELLO_ID);
				   			  notification.setLatestEventInfo(getActivity(), contentTitle, contentText, pIntent);
				   			  //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				       
				   			  mNotificationManager.notify(HELLO_ID, notification);
				   			  dialogDetails.dismiss();
						}
						else{
							Toast.makeText(getActivity(), "Message cannot be left blank", Toast.LENGTH_SHORT).show();
						}
					}
				});
	   			cancelButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialogDetails.dismiss();
					}
				});  
	   			
	   			
	   				}else{
	   					// can't get location
	   					// GPS or Network is not enabled
	   					// Ask user to enable GPS/network in settings
	   					gps.showSettingsAlert();
	   				}
	   			//end of gps if else
	   			
	   			
	   		  }else{
	   			  Toast.makeText(getActivity(), "Please Enter Time", Toast.LENGTH_SHORT).show();
	   		  }
	   		  
	   		  }// end of contact is checked if
	   		  else{
	   			Toast.makeText(getActivity(), "Please Select a Contact", Toast.LENGTH_SHORT).show(); 
	   		  }
	   	  }
	   	 });
	  return rootView;
   }
   public void setMessageInDB(String mess) {
	sdb.setTrackMeSMS(mess);
   }
   public void onResume(){
 
   	super.onResume();
   	
   	hoursBox.setText("");
   	minutesBox.setText("");
   	secondsBox.setText("");
   	
   	//gps=new GPSTracker(getActivity().getApplicationContext());
   	
   	if(db.getPrimeContactsCount()<2){
   	 startButton.setVisibility(4);
   	 contact1.setText("Set Prime Contacts");
   	 contact2.setText("and come back");
    }else{
    	startButton.setVisibility(2);	
   	 List<String> list = db.getPrimeContactNames();
   	 
   	 contact1.setText(list.get(0));
   	 contact2.setText(list.get(1));
   	 
    }
   	
   	
   }
	
   
   @Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	try {
		getActivity().unregisterReceiver(ssr.sendBroadcastReceiver);
        getActivity().unregisterReceiver(ssr.deliveryBroadcastReciever);
		
	} catch (Exception e) {
		// TODO: handle exception
	}
		 
	
}
   

}
