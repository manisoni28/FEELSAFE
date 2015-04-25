package com.feelsafe.info;

import com.feelsafe.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TipsPage extends Activity{
	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tips_layout);
		webView = (WebView) findViewById(R.id.tipsLayoutwebView1);
		
		webView.loadUrl("http://athena.nitc.ac.in/~koppisetti_b120637cs/FeelSafe/tips/");
		webView.setWebViewClient(new WebViewClient() {
    	    @Override
    	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    	        view.loadUrl(url);
    	        return true;
    	    }
    	});
		
	}
	
}
