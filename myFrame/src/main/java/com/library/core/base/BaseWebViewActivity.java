package com.library.core.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzc.frame.R;

public class BaseWebViewActivity extends BaseActivity {

	WebView webview;

	@Override
	protected void onInitAttribute(BaseAttribute ba) {
		ba.mActivityLayoutId = R.layout.webview;
		String title = getIntent().getStringExtra("title");
		ba.mTitleText = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		webview = (WebView) findViewById(R.id.webview);
		WebSettings setting = webview.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setSupportZoom(true);
		setting.setBuiltInZoomControls(true);
		setting.setUseWideViewPort(true);
		setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		setting.setLoadWithOverviewMode(true);
		
		String url = getIntent().getStringExtra("url");
		webview.loadUrl(url);
		webview.setWebViewClient(new HelloWebViewClient());
		
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); 
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

	private class HelloWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}
	}
}
