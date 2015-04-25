package com.feelsafe;

import com.feelsafe.contacts.ContactsDatabase;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.widget.Toast;

public class Utilities {
	
	Context context;
	public Camera camera;
	Parameters params;
	Boolean isFlashOn=false;
	MediaPlayer sound;
	SettingsDatabase sdb;
	ContactsDatabase cdb;
	
	public Utilities(Context context,SettingsDatabase sdb) {
		this.context=context;
		this.sdb=sdb;
	}
	

	public void getCamera() {
	    if (camera == null) {
	        try {
	            camera = Camera.open();
	            params = camera.getParameters();
	        } catch (RuntimeException e) {
	        	Toast.makeText(context,"Camera Failed to Open: "+e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	    }
	}
	 
	
	public boolean hasFlash(){
		try{
			 Boolean Temp=context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
			 //Toast.makeText(getActivity().getApplicationContext(),"deviceHasFlash", Toast.LENGTH_LONG).show();
			 return Temp;
		}catch(RuntimeException e) {
        	Toast.makeText(context,"HasFlash: "+e.getMessage(), Toast.LENGTH_LONG).show();
        	return true;
        }
	}
	
	public boolean isFlashOn(){
		Boolean temp=params.getFlashMode().equals(android.hardware.Camera.Parameters.FLASH_MODE_ON);
		Toast.makeText(context,"Flash: "+temp, Toast.LENGTH_LONG).show();
		return temp;
		
	}
	
	public void turnOnFlash() {
	        if (camera == null || params == null) {
	            return;
	        }
	        params = camera.getParameters();
	        params.setFlashMode(Parameters.FLASH_MODE_TORCH);
	        camera.setParameters(params);
	        camera.startPreview();
	        isFlashOn = true;
	}
    
	public void turnOffFlash() {
	        if (camera == null || params == null) {
	            return;
	        }
	        params = camera.getParameters();
	        params.setFlashMode(Parameters.FLASH_MODE_OFF);
	        camera.setParameters(params);
	        camera.stopPreview();
	        isFlashOn = false;
	}
	
	public void sirenStart(){
		String s2=sdb.getSiren();
		if(s2.equals("dog")){
			sound = MediaPlayer.create(context,R.raw.dog_bark);
			sound.setLooping(true);
			sound.start();
		}
		if(s2.equals("police")){
			sound = MediaPlayer.create(context,R.raw.policesiren);
			sound.setLooping(true);
			sound.start();
		}
		if(s2.equals("scream")){
			sound = MediaPlayer.create(context,R.raw.woman_scream);
			sound.setLooping(true);
			sound.start();
		}
	}
	
	public void sirenStop(){
		if(sound!=null){
			sound.stop();
		}
	}
	
	
}
