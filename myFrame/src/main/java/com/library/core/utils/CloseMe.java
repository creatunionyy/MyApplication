package com.library.core.utils;

import java.util.LinkedList;

import android.app.Activity;

/**
 * 
 * @Package com.library.core.utils 
 * @ClassName: CloseMe 
 * @Description: 关闭程序
 * @author ZhangZhaoCheng
 * @version V1.0
 */
public class CloseMe {
	
	private static LinkedList<Activity> acys = new LinkedList<Activity>();

	public static void add(Activity acy) {
		acys.add(acy);
	}

	public static void remove(Activity acy) {
		acys.remove(acy);
	}

	public static void close() {
		Activity acy;
		while (acys.size() != 0) {
			acy = acys.poll();
			if (!acy.isFinishing()) {
				acy.finish();
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
