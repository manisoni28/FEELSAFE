package com.feelsafe.locations;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.feelsafe.MainActivity;
import com.feelsafe.R;
import com.feelsafe.contacts.Contacts;
import com.feelsafe.contacts.ContactsDatabase;
import com.feelsafe.contacts.Validation;

public class LocationFragmentCls extends Fragment{
	
	Button b1,b2,b3;
	GPSTracker gps;
	RadioGroup rg;
	EditText e1;
	RadioButton r1,r2;
	String num,mess,fmess;
	double latitude;
    double longitude;
	@Override
	   public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		   View rootView =inflater.inflate(R.layout.fragment_location, container,false);
		    b1=(Button)rootView.findViewById(R.id.buttonShow);
	        b2=(Button)rootView.findViewById(R.id.buttonSend);
	        b3=(Button)rootView.findViewById(R.id.buttonContact);
	        e1=(EditText)rootView.findViewById(R.id.editTextnumber);
	        rg=(RadioGroup)rootView.findViewById(R.id.radioGroup1);
	        r1=(RadioButton)rootView.findViewById(R.id.radio0);
	        r2=(RadioButton)rootView.findViewById(R.id.radio1);
	        r1.setChecked(true);
	        r2.setChecked(false);
	        b3.setVisibility(0);
	        e1.setVisibility(4);
	        
	        r1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(r1.isChecked()){
						e1.setText("");
						e1.setVisibility(4);
						b3.setVisibility(0);
					}else{
						e1.setVisibility(4);
						b3.setVisibility(4);
					}
					
				}
			});
	        r2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(r2.isChecked()){
						
						b3.setVisibility(4);
						e1.setVisibility(4);
					}else{
						b3.setVisibility(0);
					}
				}
			});
	      
	     b3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				AlertDialog.Builder alert1= new AlertDialog.Builder(getActivity());
				alert1.setTitle("SELECT CONTACT NUMBER");
				alert1.setCancelable(true);
				alert1.setPositiveButton("App contacts",new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						final AlertDialog dialogDetails1;
						LayoutInflater inflater = LayoutInflater.from(getActivity());
						final View dialogview = inflater.inflate(R.layout.selectcontactfromlistdialog, null);
						AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
						dialogbuilder.setTitle("SELECT CONTACT NUMBER");
						dialogbuilder.setView(dialogview);
						dialogDetails1 = dialogbuilder.create();
						//dialogDetails1.setCancelable(false);
						//dialogDetails1.setCanceledOnTouchOutside(false);
						dialogDetails1.show();
						ContactsDatabase cdb = new ContactsDatabase(getActivity());
						ArrayList<String> items = cdb.getContacts();
						ListView l=(ListView)dialogview.findViewById(R.id.listViewselectcontact);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,items);
						l.setAdapter(adapter);
						l.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {
								// TODO Auto-generated method stub
								 String value=(String) parent.getItemAtPosition(position);
								 Toast.makeText(getActivity(),"Selected "+ value, Toast.LENGTH_SHORT).show();
							     Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(value);
							     while(m.find()) {
							       value=m.group(1);    
							     }
								dialogDetails1.dismiss();
								e1.setVisibility(0);
								e1.setText(value);
							}
							
						});
						Button cancel=(Button)dialogview.findViewById(R.id.buttoncancelcontact);
						cancel.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								dialogDetails1.dismiss();
							}
						});
						
					   }
			    });
				alert1.setNeutralButton("Enter manually", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						e1.setVisibility(0);
						b3.setVisibility(4);
						e1.setText("+91");
					}
				});
				alert1.setNegativeButton("Cancel", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						e1.setVisibility(4);
					}
				});
				alert1.show();
				}
	       });   
	     
	      b1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 // create class object
					gps = new GPSTracker(getActivity());
					 
	                // check if GPS enabled     
	                if(gps.canGetLocation()){
	                	Intent I=new Intent(getActivity(), maps.class);
	 	                startActivity(I);
	 	                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
	                        
	                }else{
	                    // can't get location
	                    // GPS or Network is not enabled
	                    // Ask user to enable GPS/network in settings
	                    gps.showSettingsAlert();
	                    return;
	                }
	                
				}
			});
	      b2.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("unused")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gps = new GPSTracker(getActivity());
				 
                // check if GPS enabled     
                if(gps.canGetLocation()){
                     
                	 latitude = gps.getLatitude();
                     longitude = gps.getLongitude();
                     
                     if(longitude==0.0 && latitude==0.0){
                    	 Toast.makeText(getActivity().getApplicationContext(), "Please wait until your GPS determines your Location", Toast.LENGTH_LONG).show();
                    	 return;
                     }
                     
                    // \n is for new line
                    //Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                    return;
                }
				if(r1.isChecked()){
					num=e1.getText().toString();
					mess="I've sent my location. at Location https://maps.google.com/maps?q="+latitude+","+longitude+"; -@Location:FeelSafe App";
					Validation vr = new Validation(getActivity());
					if(num.length()>3){
						if(vr.isPhoneNumberValid(num) ){
							sendSMS(num,mess);
							e1.setText("");
							e1.setVisibility(4);
						}else{
							Toast.makeText(getActivity(), "Number entered is not Valid", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(getActivity(), "Please enter a number to send message", Toast.LENGTH_SHORT).show();
					}
					
				}
				if(r2.isChecked()){
					Intent wa = new Intent(Intent.ACTION_SEND);
					wa.setType("text/plain");
					mess="I am at Location: http://maps.google.com/maps?q="+latitude+","+longitude+" . Message generated by FeelSafe app. please click the link to view the location";
					wa.setPackage("com.whatsapp");
					if(wa != null){
						wa.putExtra(Intent.EXTRA_TEXT, mess);
						startActivity(Intent.createChooser(wa, "share with"));
					}else{
						Toast.makeText(getActivity(), "Whats app not installed", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		  return rootView;
	   }
	
		public void sendSMS(String pn,String ms){
	    	PendingIntent pi= PendingIntent.getActivity(getActivity(), 0, new Intent(), 0);
	    	SmsManager sms = SmsManager.getDefault();
	    	sms.sendTextMessage(pn, null, ms, pi, null);
	    	Toast.makeText(getActivity(),"Message Sent Successfully", Toast.LENGTH_SHORT).show();
	    }
		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if(r1.isChecked()){
				
				b3.setVisibility(0);
			}else{
				e1.setVisibility(4);
				b3.setVisibility(4);
			}
			e1.setText("");
			
		}
}

