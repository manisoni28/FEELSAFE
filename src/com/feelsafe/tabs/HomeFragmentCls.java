package com.feelsafe.tabs;

import java.util.ArrayList;
import java.util.List;

import com.feelsafe.R;
import com.feelsafe.SettingsDatabase;
import com.feelsafe.Utilities;
import com.feelsafe.contacts.ContactsDatabase;
import com.feelsafe.locations.GPSTracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;

public class HomeFragmentCls extends Fragment{
	Button panicModeButton,settingsButton,gpsonoffbutton;
	ToggleButton torchButton,sirenButton;
	TextView warningtext,gpsText,primesText,whatsappText;
	MediaPlayer mpclick,sound;
	Camera camera;
	Parameters params;
	Boolean isFlashOn=false;
	SettingsDatabase sdb;
	ContactsDatabase cdb;
	Utilities ut;
	GPSTracker gps;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		 
	   
		 //declaring buttons and id's
		 panicModeButton = (Button) rootView.findViewById(R.id.panicModeButton);
		 gpsonoffbutton = (Button) rootView.findViewById(R.id.gpsonoffbutton);
		 torchButton=(ToggleButton) rootView.findViewById(R.id.torchButton);
		 sirenButton=(ToggleButton) rootView.findViewById(R.id.sirenButton);
		 settingsButton=(Button) rootView.findViewById(R.id.settingsButton);
		 warningtext=(TextView) rootView.findViewById(R.id.warningtextView);
		 gpsText=(TextView) rootView.findViewById(R.id.gpstextView);
		 whatsappText=(TextView) rootView.findViewById(R.id.whatsapptextView);
		 primesText=(TextView) rootView.findViewById(R.id.primetextView);
		 
		 
		 sdb=new SettingsDatabase(getActivity().getApplicationContext());
		 cdb=new ContactsDatabase(getActivity().getApplicationContext());
		 ut =new Utilities(getActivity(), sdb);
		 gps=new GPSTracker(getActivity());
		 
		 gpsText.setVisibility(8);
		 
		 //====warning text views visibility handling==============
		 
		 boolean warninvisibility=false;
		 if(appInstalledOrNot("com.whatsapp")){
			 whatsappText.setVisibility(4);
		 }else{
			 warninvisibility=true;
		 }
		 if(gps.canGetLocation()){
			 gpsonoffbutton.setText("ON");
			 gpsonoffbutton.setTextColor(Color.parseColor("#27ae60"));
		 }else{
			 gpsonoffbutton.setText("OFF");
			 gpsonoffbutton.setTextColor(Color.parseColor("#c0392b"));
		 }
		 if(cdb.getPrimeContactsCount()>0){
			 primesText.setVisibility(8);
		 }else{
			 warninvisibility=true;
		 }
		 if(!warninvisibility){
			 warningtext.setVisibility(4);
		 }
		 
		 gpsonoffbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
			}
		});
		 
		 
		 //========================================================
		 //this is put in trank me mode running and started panic mode
		 // checkk the extra boolean if its set
		 // that means it is started from track me mode
		 // so start panic mode.
		 Intent i = getActivity().getIntent();
		 Boolean start_panic_mode= i.getBooleanExtra("panicmode", false);
		 if(start_panic_mode){
			 PanicModeCls pc = new PanicModeCls(getActivity(), cdb, sdb);
			 pc.startPanicMode();
		 }
		 //=======================================================
		 
		 //panic button on touch 
		 panicModeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mpclick = MediaPlayer.create(getActivity().getApplicationContext(),R.raw.click );
				mpclick.start();
				//Toast.makeText(getActivity().getBaseContext(), "Panic Mode"+sdb.getcontact1status()+" 2:"+sdb.getcontact2status(), Toast.LENGTH_SHORT).show();
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		    	alert.setTitle("Start Panic Mode ?");
		    	alert.setMessage("Are you sure you want to start panic mode ? \n*may cost SMS or data charges.\n\ncurrent Settings:\nMessaging mode: "+sdb.getPanicModeMessage()+"\nInterval :"+sdb.getPanicModeInterval());
		    	alert.setPositiveButton("Start", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						PanicModeCls pc = new PanicModeCls(getActivity(), cdb, sdb);
						pc.startPanicMode();
					}
				});
		    	alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					 dialog.dismiss();
					}
				});
		    	alert.show();
				
			}
		});
		 
//============================Torch button=============================================================
		 
		 torchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mpclick = MediaPlayer.create(getActivity().getApplicationContext(),R.raw.click );
				mpclick.start();
				if(ut.hasFlash()){
					ut.getCamera();
					if(torchButton.isChecked()){
						ut.turnOnFlash();
					}else{
						ut.turnOffFlash();
					} 
					//Toast.makeText(getActivity().getApplicationContext(),"Succesfully in if clause", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getActivity().getApplicationContext(),"Your device doesnot support Flash", Toast.LENGTH_LONG).show();
				}
			}
		});
		 
//=========================Siren Button================================================================		 
	     
		 
		 //siren button on touch
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
		//=========================Settings Button================================================================
		 
		 	settingsButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final AlertDialog dialogDetails;
					LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
					final View dialogview = inflater.inflate(R.layout.settings_dialog_layout, null);
					AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
					dialogbuilder.setTitle("Settings");
					dialogbuilder.setView(dialogview);
					dialogDetails = dialogbuilder.create();
					dialogDetails.show();
					
					
					final Spinner sirenSpinner=(Spinner) dialogview.findViewById(R.id.sirenSpinner);
					List<String> list = new ArrayList<String>();
					list.add("dog");
					list.add("police");
					list.add("scream");
					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
						android.R.layout.simple_spinner_dropdown_item, list);
					dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					sirenSpinner.setAdapter(dataAdapter);
					sirenSpinner.setSelection(dataAdapter.getPosition(sdb.getSiren()));
					sirenSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							String s=String.valueOf(sirenSpinner.getSelectedItem());
							sdb.setSiren(s);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					 });
					
					//=========panic Mode Interval=====================
					
					final Spinner panicIntervalSpinner=(Spinner) dialogview.findViewById(R.id.panicModeIntervalspinner);
					List<String> pintevallist = new ArrayList<String>();
					pintevallist.add("5min");
					pintevallist.add("15min");
					pintevallist.add("30min");
					pintevallist.add("1hr");
					ArrayAdapter<String> dataAdapterPinterval = new ArrayAdapter<String>(getActivity().getApplicationContext(),
						android.R.layout.simple_spinner_dropdown_item, pintevallist);
					dataAdapterPinterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					panicIntervalSpinner.setAdapter(dataAdapterPinterval);
					panicIntervalSpinner.setSelection(dataAdapterPinterval.getPosition(sdb.getPanicModeInterval()));
					panicIntervalSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							String s=String.valueOf(panicIntervalSpinner.getSelectedItem());
							sdb.setPanicModeInterval(s);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					 });
					
					//=========panic Mode Message=====================
					
					final Spinner panicMessageSpinner=(Spinner) dialogview.findViewById(R.id.panicModeMessageSpinner);
					List<String> pmessagelist = new ArrayList<String>();
					pmessagelist.add("SMS");
					pmessagelist.add("whatsapp");
					ArrayAdapter<String> dataAdapterPmessage = new ArrayAdapter<String>(getActivity().getApplicationContext(),
						android.R.layout.simple_spinner_dropdown_item, pmessagelist);
					dataAdapterPmessage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					panicMessageSpinner.setAdapter(dataAdapterPmessage);
					panicMessageSpinner.setSelection(dataAdapterPmessage.getPosition(sdb.getPanicModeMessage()));
					panicMessageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							String s=String.valueOf(panicMessageSpinner.getSelectedItem());
							sdb.setPanicModeMessage(s);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					 });
					
					//=========TrackMe Mode Message=====================
					
					final Spinner trackmeIntervalSpinner=(Spinner) dialogview.findViewById(R.id.trackmeModeSpinner);
					List<String> tmintervallist = new ArrayList<String>();
					tmintervallist.add("5min");
					tmintervallist.add("10min");
					tmintervallist.add("15min");
					tmintervallist.add("30min");
					ArrayAdapter<String> dataAdapterTMInterval = new ArrayAdapter<String>(getActivity().getApplicationContext(),
						android.R.layout.simple_spinner_dropdown_item, tmintervallist);
					dataAdapterTMInterval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					trackmeIntervalSpinner.setAdapter(dataAdapterTMInterval);
					trackmeIntervalSpinner.setSelection(dataAdapterTMInterval.getPosition(sdb.getTrackMeInterval()));
					trackmeIntervalSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							String s=String.valueOf(trackmeIntervalSpinner.getSelectedItem());
							sdb.setTrackMeInterval(s);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					 });
					//=========SMS Alert Tone on/off=====================
					
					final Spinner smsAlertSpinner=(Spinner) dialogview.findViewById(R.id.smsAlertSpinner);
					List<String> alertlist = new ArrayList<String>();
					alertlist.add("yes");
					alertlist.add("no");
					ArrayAdapter<String> dataAdapterSMSAlert = new ArrayAdapter<String>(getActivity().getApplicationContext(),
						android.R.layout.simple_spinner_dropdown_item, alertlist);
					dataAdapterSMSAlert.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					smsAlertSpinner.setAdapter(dataAdapterSMSAlert);
					smsAlertSpinner.setSelection(dataAdapterSMSAlert.getPosition(sdb.getSMSAlert()));
					smsAlertSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							String s=String.valueOf(smsAlertSpinner.getSelectedItem());
							sdb.setSMSAlert(s);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					 });
					
			//===============OK Button============================================		
					Button okButton = (Button) dialogview.findViewById(R.id.okSettinfsButton);
					okButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							//Toast.makeText(getActivity().getApplicationContext(),"siren:"+sdb.getSiren()+" PI:"+sdb.getPanicModeInterval()+" PMM:"+sdb.getPanicModeMessage()+" TMI:"+sdb.getTrackMeInterval()+" SMS:"+sdb.getSMSAlert(), Toast.LENGTH_LONG).show();
							dialogDetails.dismiss();
						}
					});
				}
			});
		 
		 
		 
		 //=======================================================================================================
		return rootView;
		
	}
	
	
	 @Override
	public void onResume() {
	        super.onResume();
	         
	        // on resume turn on the flash
	        if(ut.hasFlash())
	            ut.turnOnFlash();
	        
	        	gps= new GPSTracker(getActivity());
	        cdb=new ContactsDatabase(getActivity());
	        boolean warninvisibility=false;
			 if(appInstalledOrNot("com.whatsapp")){
				 whatsappText.setVisibility(4);
			 }else{
				 warninvisibility=true;
			 }
			 if(gps.canGetLocation()){
				 gpsonoffbutton.setText("ON");
				 gpsonoffbutton.setTextColor(Color.parseColor("#27ae60"));
			 }else{
				 gpsonoffbutton.setText("OFF");
				 gpsonoffbutton.setTextColor(Color.parseColor("#c0392b"));
			 }
			 if(cdb.getPrimeContactsCount()>0){
				 primesText.setVisibility(8);
			 }else{
				 warninvisibility=true;
			 }
			 if(!warninvisibility){
				 warningtext.setVisibility(4);
			 }
	    }
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		if(mpclick!=null){
			mpclick.stop();
		}
		
		 if (ut.camera != null) {
		        ut.camera.release();
		        ut.camera = null;
		    }
		
	}
	
	private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = getActivity().getPackageManager();
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
	
}
