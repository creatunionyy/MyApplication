package com.creatunion.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.library.core.utils.CrashHandler;

public class XApplication extends Application {

	private static XApplication mContext;
	private static int mScreenWidth;
	private static int mScreenHeight;
	private static Handler sMainThreadHandler;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;

		CrashHandler.getInstance().init(this);
		sMainThreadHandler = new Handler();
	}

	public static Context getApplicationConetext() {
		return mContext;
	}

	public static void setScreenWidth(int mScreenWidth) {
		XApplication.mScreenWidth = mScreenWidth;
	}
	public static int getScreenWidth() {
		return mScreenWidth;
	}


	public static int getScreenHeight() {
		return mScreenHeight;
	}

	public static void setScreenHeight(int mScreenHeight) {
		XApplication.mScreenHeight = mScreenHeight;
	}

	public static Handler getMainThreadHandler() {
		return sMainThreadHandler;
	}

	public static void setMainThreadHandler(Handler sMainThreadHandler) {
		XApplication.sMainThreadHandler = sMainThreadHandler;
	}

}