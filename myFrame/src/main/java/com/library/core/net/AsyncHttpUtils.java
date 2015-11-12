package com.library.core.net;

import com.library.loopj.android.http.AsyncHttpClient;
import com.library.loopj.android.http.AsyncHttpResponseHandler;
import com.library.loopj.android.http.JsonHttpResponseHandler;
import com.library.loopj.android.http.RequestParams;

public class AsyncHttpUtils {
	
	public static UrlController urlController = UrlController.getInstance(false);
	public static final String BASE_URL = urlController.getUrl();
	
	public static AsyncHttpClient client = new AsyncHttpClient();

	public static String getAbsoluteUrl() {
		return BASE_URL;
	}

	public static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void get(RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(), params, responseHandler);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void get(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	public static void get(String url,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url),  responseHandler);
	}

	public static void post(RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
}