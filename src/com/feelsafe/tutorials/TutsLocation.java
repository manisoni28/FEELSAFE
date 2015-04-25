package com.feelsafe.tutorials;

import com.feelsafe.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class TutsLocation extends Fragment{
	WebView w;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.tutorial_layout, container, false);
		w=(WebView) rootView.findViewById(R.id.tutoriallayoutwebView1);
		w.loadUrl("file:///android_asset/Tutorial/07.html");
		return rootView;
	}
}
