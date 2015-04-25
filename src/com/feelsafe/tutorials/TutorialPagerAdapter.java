package com.feelsafe.tutorials;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TutorialPagerAdapter extends FragmentPagerAdapter{

	public TutorialPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		 switch (arg0) {
	        case 0:
	            // Top Rated fragment activity
	            return new TutFeelSafe();
	        case 1:
	            // Games fragment activity
	            return new TutPrimeContacts();
	        case 2:
	            // Movies fragment activity
	            return new TutUtilities();
	        case 3:
	            // Top Rated fragment activity
	            return new TutsPanicSMS();
	        case 4:
	            // Games fragment activity
	            return new TutsPanicWhatsapp();
	        case 5:
	            // Movies fragment activity
	            return new TutsTrackMe();
	        case 6:
	            // Top Rated fragment activity
	            return new TutsLocation();
	        case 7:
	            // Games fragment activity
	            return new TutsSettings();
	        }
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
	}

}
