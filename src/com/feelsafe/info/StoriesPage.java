package com.feelsafe.info;

import com.feelsafe.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class StoriesPage extends Activity{
	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stories_layout);
		webView = (WebView) findViewById(R.id.storieslayoutwebView1);
		
		webView.loadUrl("http://athena.nitc.ac.in/~koppisetti_b120637cs/FeelSafe/stories/");
		webView.setWebViewClient(new WebViewClient() {
    	    @Override
    	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    	        view.loadUrl(url);
    	        return true;
    	    }
    	});
		
	}
	
}
