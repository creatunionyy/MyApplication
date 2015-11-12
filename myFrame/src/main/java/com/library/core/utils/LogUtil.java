package com.library.core.utils;

import android.util.Log;

public class LogUtil {

	public static boolean isDebug = true;
	public static String tag = "laopai";

	public LogUtil() {
	}

	/**
	 * 错误
	 */
	public static void e(Class<?> clazz, String msg) {
		if (isDebug) {
			Log.e(clazz.getSimpleName(), msg);
		}
	}

	public static void e(String clazzName, String msg) {
		if (isDebug) {
			Log.e(clazzName, msg);
		}
	}

	/**
	 * 信息
	 */
	public static void i(Class<?> clazz, String msg) {
		if (isDebug) {
			Log.i(clazz.getSimpleName(), msg);
		}
	}

	public static void i(String clazzName, String msg) {
		if (isDebug) {
			Log.e(clazzName, msg);
		}
	}

	/**
	 * 警告
	 */
	public static void w(Class<?> clazz, String msg) {
		if (isDebug) {
			Log.w(clazz.getSimpleName(), msg);
		}
	}

	public static void w(String clazzName, String msg) {
		if (isDebug) {
			Log.e(clazzName, msg);
		}
	}

	/**
	 * 测试
	 */
	public static void d(Class<?> clazz, String msg) {
		if (isDebug) {
			Log.d(clazz.getSimpleName(), msg);
		}
	}

	public static void d(String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}
}
