package com.feelsafe.info;

import com.feelsafe.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CreditsPage extends Activity{
	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credits_layout);
		webView = (WebView) findViewById(R.id.creditswebView);
		
		webView.loadUrl("file:///android_asset/FeelSafeCredits/index.html");
		webView.setWebViewClient(new WebViewClient() {
    	    @Override
    	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    	        view.loadUrl(url);
    	        return true;
    	    }
    	});
		
	}
	
}
