package com.library.core.utils;

import android.content.SharedPreferences;

import com.creatunion.utils.XApplication;

public class SharedUtil {

	private static SharedUtil shareData;
	private SharedPreferences sharedPreferences;

	public SharedUtil() {
		initPreferences();
	}

	public static SharedUtil getInstance() {
		if (shareData == null) {
			shareData = new SharedUtil();
		}
		return shareData;
	}

	public void initPreferences() {
		if (this.sharedPreferences == null)
			this.sharedPreferences = XApplication.getApplicationConetext().getSharedPreferences(
					"zzc", 0);
	}

	public void putPreferences(String paramString, float paramFloat) {
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.putFloat(paramString, paramFloat);
		localEditor.commit();
	}

	public void putPreferences(String paramString, int paramInt) {
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.putInt(paramString, paramInt);
		localEditor.commit();
	}

	public void putPreferences(String paramString, long paramLong) {
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.putLong(paramString, paramLong);
		localEditor.commit();
	}

	public void putPreferences(String paramString1, String paramString2) {
		if ((paramString1 == null) || (paramString2 == null))
			return;
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.putString(paramString1, paramString2);
		localEditor.commit();
	}

	public void putPreferences(String paramString, boolean paramBoolean) {
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.putBoolean(paramString, paramBoolean);
		localEditor.commit();
	}

	public float getPreferences(String paramString, float paramFloat) {
		return this.sharedPreferences.getFloat(paramString, paramFloat);
	}

	public int getPreferences(String paramString, int paramInt) {
		return this.sharedPreferences.getInt(paramString, paramInt);
	}

	public long getPreferences(String paramString, long paramLong) {
		return this.sharedPreferences.getLong(paramString, paramLong);
	}

	public String getPreferences(String paramString1, String paramString2) {
		return this.sharedPreferences.getString(paramString1, paramString2);
	}

	public boolean getPreferences(String paramString, boolean paramBoolean) {
		return this.sharedPreferences.getBoolean(paramString, paramBoolean);
	}
	
	public void removeAllData(){
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.clear();
		localEditor.commit();
	}
	
	public void clearData(String key){
		SharedPreferences.Editor localEditor = this.sharedPreferences.edit();
		localEditor.remove(key);
		localEditor.commit();
	}
}
