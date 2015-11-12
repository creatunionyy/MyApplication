package com.library.core.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class SDCardUtil {

	/**
	 * 判断SD卡是否可用
	 * 
	 * @return
	 */
	public static boolean isSdCardExists() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			Log.e(" ", "the Sdcard is not exists");
			return false;
		}
	}

	/**
	 * 获取SD卡根目录
	 * 
	 * @return
	 */
	public static String getRootPath() {
		if (isSdCardExists()) {
			File sdDir = Environment.getExternalStorageDirectory();
			return sdDir.getPath();
		} else {
			return null;
		}
	}

	/**
	 * 获取SD卡总大小
	 * 
	 * @return
	 */
	public static String getSdCardTotalSize() {
		if (!isSdCardExists())
			return "";
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getBlockCount();
		return String.valueOf((availableBlocks * blockSize) / 1024 / 1024);
	}

	/**
	 * 获取SD卡可用大小
	 * 
	 * @return
	 */
	public static String getSdcardAvailbleSize() {
		if (!isSdCardExists())
			return "";
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return String.valueOf((availableBlocks * blockSize) / 1024 / 1024);
	}

	/**
	 * 获取SD卡根目录文件列表
	 * 
	 * @return
	 */
	public static File[] getRootFiles() {
		if (getRootPath().equals(""))
			return null;
		List<File> files = new ArrayList<File>();
		File file = new File(getRootPath());
		return file.listRoots();
	}

}
