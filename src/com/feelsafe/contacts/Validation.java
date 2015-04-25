package com.feelsafe.contacts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageManager;

public class Validation {
	Context context;
	public Validation(Context context){
		this.context=context;
	}
	
	public boolean isPhoneNumberValid(String phoneNumber){
		phoneNumber=phoneNumber.replace("-","");
		phoneNumber=phoneNumber.replace(" ", "");
		String expression = "^(?:0091|\\+91|0|)[7-9][0-9]{9}$";
		Pattern numberPattern=Pattern.compile(expression);
		Matcher numberMatcher=numberPattern.matcher(phoneNumber);
		
		return numberMatcher.matches();
	}
	
	private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = context.getPackageManager();
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
