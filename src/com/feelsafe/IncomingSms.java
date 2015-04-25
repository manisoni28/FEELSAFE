package com.feelsafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSms extends BroadcastReceiver {
    
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
   

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		// Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();


        try {
            
            if (bundle != null) {
                
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                
                for (int i = 0; i < pdusObj.length; i++) {
                    
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
              
                    if(is_valid_panicAlert(message)){
                    Intent i1 = new Intent(context,Alert.class); //Same as above two lines
                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i1.putExtra("number", senderNum);
                    i1.putExtra("message", message);
                    //i1.putExtra("name", name);
                    context.startActivity(i1);
                    }
                    if(is_valid_TrackMeAlert(message)){
                        Intent i1 = new Intent(context,LocationAlert.class); //Same as above two lines
                        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i1.putExtra("number", senderNum);
                        i1.putExtra("message", message);
                        //i1.putExtra("name", name);
                        context.startActivity(i1);
                        }
                } // end for loop
              } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
	}    
	
	 public boolean is_valid_panicAlert(String message){
		 boolean b=message.contains("I am in Emergency. I am at Location https://maps.google.com/maps?q=");
		 boolean b2=message.contains("message sent by FeelSafe.");
		if(b && b2){
			return true;
		}else{
			return false;
		}
		 
	 }

	 public boolean is_valid_TrackMeAlert(String message){
		 boolean b=message.contains("at Location https://maps.google.com/maps?q=");
		 boolean b2=message.contains("-@Location:FeelSafe App");
		if(b && b2){
			return true;
		}else{
			return false;
		}
		 
	 }

	 
}