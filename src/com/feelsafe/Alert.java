package com.feelsafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Alert extends Activity{
	
	MediaPlayer sound;
	String number,message,name;
	 SQLiteDatabase db;
	 Cursor c;
	 Bitmap photo1 ;
	//ContactsDatabase db = new ContactsDatabase(getApplicationContext());
	
 @Override
 protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	if(savedInstanceState == null){
		Bundle  extras = getIntent().getExtras();
		number=extras.getString("number");
		number=number.substring(number.length()-10, number.length());
		message=extras.getString("message");
	}
	db=this.openOrCreateDatabase("db_contacts.db", 0, null);
    String name="";
    c= db.rawQuery("select name from table_contacts where contact like '%"+number+"%'", null);
    while(c.moveToNext()){
		name=c.getString(0);
	}
    
	/*c=db.rawQuery("select image from table_contacts where contact='"+number+"'",null);
	while(c.moveToNext()){
		byte[] imageByteArray = c.getBlob(0);
    	 photo1 = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.length);
	} */
	
	final AlertDialog dialogDetails;
	LayoutInflater inflater = LayoutInflater.from(this);
	final View dialogview = inflater.inflate(R.layout.alert_incoming_sms, null);
	AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
	//dialogbuilder.setTitle("edit Contact");
	dialogbuilder.setView(dialogview);
	dialogDetails = dialogbuilder.create();
	dialogDetails.setCancelable(false);
	dialogDetails.setCanceledOnTouchOutside(false);
	dialogDetails.show();
	

	
	final ImageView photo = (ImageView) dialogview.findViewById(R.id.incomingSMSAlertPhoto);
	final TextView nameEditText =(TextView) dialogview.findViewById(R.id.incomingSMSAlertName);
	Button callButton = (Button) dialogview.findViewById(R.id.incomingSMSAlertCallButton);
	Button messageButton = (Button) dialogview.findViewById(R.id.incomingSMSAlertMessageButton);
	Button cancelButton = (Button) dialogview.findViewById(R.id.incomingSMSAlertCancelButton);
	Button locationButton = (Button) dialogview.findViewById(R.id.incomingSMSAlertLocationButton);
	
	sound = MediaPlayer.create(this,R.raw.sos_alert);
	sound.setLooping(false);
	sound.start();
	
	
	//photo.setImageBitmap(photo1);
	
	if(name.length()<=1){
		nameEditText.setText(number);
	}else{
		nameEditText.setText(name);
	}
	
	c=db.rawQuery("select image from table_contacts where contact like '%"+number+"%'",null);
	while(c.moveToNext()){
		 byte[] imageByteArray = c.getBlob(0);
    	 photo1 = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.length);
    	 photo.setImageBitmap(photo1);
	}
	
	callButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sound.stop();
			//Toast toast = Toast.makeText(getApplicationContext(),"call "+number, Toast.LENGTH_LONG);
	        //toast.show();
	        Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));
			startActivity(callIntent);
		}
	});
	
	messageButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sound.stop();
			Toast toast = Toast.makeText(getApplicationContext(),"message "+number, Toast.LENGTH_LONG);
	        toast.show();
	        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.setType("vnd.android-dir/mms-sms");
			smsIntent.putExtra("address",number);
			startActivity(smsIntent);
		}
	});
	
	cancelButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sound.stop();
			dialogDetails.dismiss();
			finish();
		}
	});
	
	locationButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 sound.stop();
	            int startIndex=message.indexOf("https://");
	            int endIndex= message.indexOf(";");
	            String locationLink = message.substring(startIndex,endIndex);
	           
	            if(locationLink.contains("0.0,0.0")){
	            	Toast.makeText(getApplicationContext(),number+"'s GPS didnt send the location details. Wait for next Message", Toast.LENGTH_LONG).show();
	            }else{
	            Intent intent = new Intent();
	            intent.setAction(Intent.ACTION_VIEW);
	            intent.addCategory(Intent.CATEGORY_BROWSABLE);
	            intent.setData(Uri.parse(locationLink));
	            startActivity(intent);
	           // finish();
	            }
		}
	});
	
	
 }
 

 
}
