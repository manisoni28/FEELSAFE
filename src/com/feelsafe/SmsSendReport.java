package com.feelsafe;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;


public class SmsSendReport {
	public BroadcastReceiver sendBroadcastReceiver;
    public BroadcastReceiver deliveryBroadcastReciever;
    Context myContext;
    
    public SmsSendReport(Context myContext) {
		// TODO Auto-generated constructor stub
    	this.myContext=myContext;
    	sendBroadcastReceiver = new sentReceiver();
        deliveryBroadcastReciever = new deliverReceiver();
	}
    
    class deliverReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(myContext, "sms_delivered",
                        Toast.LENGTH_SHORT).show();
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(myContext, "sms_not_delivered",
                        Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }

    class sentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent arg1) {
            switch (getResultCode()) {
            case Activity.RESULT_OK:
                Toast.makeText(myContext, "message sent succesfully", Toast.LENGTH_SHORT)
                        .show();
            
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(myContext, "Message Sending failed: Generic failure",
                        Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(myContext, "Message Sending Failde: No service",
                        Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                Toast.makeText(myContext, "Message Sending failed: Null PDU", Toast.LENGTH_SHORT)
                        .show();
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(myContext, "Message Sending failed: AirPlane Mode",
                        Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }
    
    
    public void sendSMS(String pn,String ms){
 	   String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(myContext, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(myContext, 0,
                new Intent(DELIVERED), 0);

        myContext.registerReceiver(sendBroadcastReceiver, new IntentFilter(SENT));

        myContext.registerReceiver(deliveryBroadcastReciever, new IntentFilter(DELIVERED));
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(pn, null, ms, sentPI, deliveredPI);
    }

}
