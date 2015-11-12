package com.library.core.utils;

import android.widget.Toast;

import com.creatunion.utils.XApplication;

public class ToastUtil {

	public static void show(String text) {
		Toast.makeText(XApplication.getApplicationConetext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	public static void show(int id) {
		Toast.makeText(XApplication.getApplicationConetext(),
				XApplication.getApplicationConetext().getString(id), Toast.LENGTH_SHORT)
				.show();
	}
}
