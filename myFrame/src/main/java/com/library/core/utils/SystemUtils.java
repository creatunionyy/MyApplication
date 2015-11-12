package com.library.core.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.creatunion.utils.XApplication;

public class SystemUtils {

	private static int sArmArchitecture = -1;

	public static String getIp(Context context) {
		WifiManager m = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		int i = m.getConnectionInfo().getIpAddress();

		String ip = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
				+ ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
		return ip;
	}

	public static int getArmArchitecture() {
		if (sArmArchitecture != -1)
			return sArmArchitecture;
		try {
			InputStream is = new FileInputStream("/proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			try {
				String name = "CPU architecture";
				while (true) {
					String line = br.readLine();
					String[] pair = line.split(":");
					if (pair.length != 2)
						continue;
					String key = pair[0].trim();
					String val = pair[1].trim();
					if (key.compareToIgnoreCase(name) == 0) {
						String n = val.substring(0, 1);
						sArmArchitecture = Integer.parseInt(n);
						break;
					}
				}
			} finally {
				br.close();
				ir.close();
				is.close();
				if (sArmArchitecture == -1)
					sArmArchitecture = 6;
			}
		} catch (Exception e) {
			sArmArchitecture = 6;
		}
		return sArmArchitecture;
	}

	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	public static boolean isInBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> listAppProcessInfo = am
				.getRunningAppProcesses();
		if (listAppProcessInfo != null) {
			final String strPackageName = context.getPackageName();
			for (RunningAppProcessInfo pi : listAppProcessInfo) {

				if (pi.processName.equals(strPackageName)) {
					if (pi.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
							&& pi.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static void launchHome(Context context) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(intent);
	}

	public static int randomRange(int nStart, int nEnd) {
		if (nStart >= nEnd) {
			return nEnd;
		}
		return nStart + (int) (Math.random() * (nEnd - nStart));
	}

	public static boolean isExternalStorageMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	public static String getExternalCachePath(Context context) {
		return Environment.getExternalStorageDirectory().getPath()
				+ "/Android/data/" + context.getPackageName() + "/cache";
	}

	public static String getDeviceUUID(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String strIMEI = tm.getDeviceId();
		if (TextUtils.isEmpty(strIMEI)) {
			strIMEI = "1";
		}

		String strMacAddress = getMacAddress(context);
		if (TextUtils.isEmpty(strMacAddress)) {
			strMacAddress = "1";
		}

		return strIMEI + strMacAddress;
	}

	public static String getMacAddress(Context context) {
		final WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wm.getConnectionInfo().getMacAddress();
	}

	public static void printCallStack() {
		for (StackTraceElement e : new Throwable().getStackTrace()) {
			System.out.println(e.toString());
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void copyToClipBoard(Context context, String strText) {
		final ClipboardManager manager = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		manager.setText(strText);
	}

	public static boolean isEmail(String strEmail) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(strEmail);
		return matcher.matches();
	}

	public static String getVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static Location getLocation(Context context) {
		final LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		final String strProvider = locationManager.getBestProvider(criteria,
				false);
		Location location = locationManager.getLastKnownLocation(strProvider);
		try {
			if (location == null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (location == null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		} catch (Exception e) {

		}
		return location;
	}

	public static void addEditTextLengthFilter(EditText editText,
			int nLengthLimit) {
		InputFilter filters[] = editText.getFilters();
		if (filters == null) {
			editText.getEditableText().setFilters(
					new InputFilter[] { new InputFilter.LengthFilter(
							nLengthLimit) });
		} else {
			final int nSize = filters.length + 1;
			InputFilter newFilters[] = new InputFilter[nSize];
			int nIndex = 0;
			for (InputFilter filter : filters) {
				newFilters[nIndex++] = filter;
			}
			newFilters[nIndex] = new InputFilter.LengthFilter(nLengthLimit);
			editText.getEditableText().setFilters(newFilters);
		}
	}

	public static void addEditTextInputFilter(EditText et, InputFilter filter) {
		if (filter == null) {
			return;
		}
		InputFilter filters[] = et.getFilters();
		if (filters == null) {
			et.getEditableText().setFilters(new InputFilter[] { filter });
		} else {
			final int nSize = filters.length + 1;
			InputFilter newFilters[] = new InputFilter[nSize];
			int nIndex = 0;
			for (InputFilter f : filters) {
				newFilters[nIndex++] = f;
			}
			newFilters[nIndex] = filter;
			et.getEditableText().setFilters(newFilters);
		}
	}

	public static boolean getCursorBoolean(Cursor cursor, int nColumnIndex) {
		return cursor.getInt(nColumnIndex) == 1 ? true : false;
	}

	public static void safeSetImageBitmap(ImageView iv, String path) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		computeSampleSize(op, path, 512, 512 * 512);
		try {
			iv.setImageBitmap(BitmapFactory.decodeFile(path, op));
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getVideoThumbnail(String filePath) {
		return getVideoThumbnail(filePath,
				DensityUtils.dp2px(XApplication.getApplicationConetext(), 100));
	}

	public static Bitmap getVideoThumbnail(String filePath, int maxSize) {
		Bitmap bmp = ThumbnailUtils.createVideoThumbnail(filePath,
				Images.Thumbnails.MINI_KIND);
		if (bmp != null) {
			final int width = bmp.getWidth();
			final int height = bmp.getHeight();
			if (width > maxSize || height > maxSize) {
				int fixWidth = 0;
				int fixHeight = 0;
				if (height > width) {
					fixHeight = maxSize;
					fixWidth = width * fixHeight / height;
				} else {
					fixWidth = maxSize;
					fixHeight = height * fixWidth / width;
				}
				bmp = Bitmap
						.createScaledBitmap(bmp, fixWidth, fixHeight, false);
			}
		}
		return bmp;
	}

	public static int nextPowerOf2(int n) {
		n -= 1;
		n |= n >>> 16;
		n |= n >>> 8;
		n |= n >>> 4;
		n |= n >>> 2;
		n |= n >>> 1;
		return n + 1;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			String path, int minSideLength, int maxNumOfPixels) {
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		if (options.outWidth != -1) {
			options.inJustDecodeBounds = false;

			int initialSize = computeInitialSampleSize(options, minSideLength,
					maxNumOfPixels);

			int roundedSize;
			if (initialSize <= 8) {
				roundedSize = 1;
				while (roundedSize < initialSize) {
					roundedSize <<= 1;
				}
			} else {
				roundedSize = (initialSize + 7) / 8 * 8;
			}

			options.inSampleSize = roundedSize;

			return roundedSize;
		} else {
			return -1;
		}
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels < 0) ? 1 : (int) Math.ceil(Math.sqrt(w
				* h / maxNumOfPixels));
		int upperBound = (minSideLength < 0) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if (maxNumOfPixels < 0 && minSideLength < 0) {
			return 1;
		} else if (minSideLength < 0) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static boolean compressBitmapFile(String dstPath, String srcPath,
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		BitmapFactory.decodeFile(srcPath, options);
		if (options.outWidth > 0) {
			if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
				options.inSampleSize = calculateInSampleSize(options, reqWidth,
						reqHeight);
				options.inJustDecodeBounds = false;
				try {
					Bitmap bmp = BitmapFactory.decodeFile(srcPath, options);
					FileHelper.saveBitmapToFile(dstPath, bmp, 90);
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					return false;
				}
			} else {
				FileHelper.copyFile(dstPath, srcPath);
			}
		} else {
			return false;
		}
		return true;
	}

	public static Bitmap decodeSampledBitmapFromFilePath(String path,
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		BitmapFactory.decodeFile(path, options);
		if (options.outWidth > 0) {
			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inJustDecodeBounds = false;
			try {
				return BitmapFactory.decodeFile(path, options);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}

			final float totalPixels = width * height;

			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	public static boolean isArrayContain(Object[] objs, Object item) {
		for (Object obj : objs) {
			if (obj.equals(item)) {
				return true;
			}
		}
		return false;
	}

	public static String throwableToString(Throwable e) {
		StringBuffer sb = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		e.printStackTrace(pw);
		Throwable cause = e.getCause();

		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();
		String result = writer.toString();
		result = result.replaceAll("\n", "\r\n");
		sb.append(result);
		final String ret = sb.toString();
		if (TextUtils.isEmpty(ret)) {
			return e.getMessage();
		}
		return ret;
	}

	public static byte[] objectToByteArray(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream bos = new ObjectOutputStream(baos);
		bos.writeObject(obj);
		try {
			return baos.toByteArray();
		} finally {
			bos.close();
		}
	}

	public static Object byteArrayToObject(byte[] data)
			throws StreamCorruptedException, IOException,
			ClassNotFoundException {
		if (data == null) {
			return null;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		try {
			return ois.readObject();
		} finally {
			ois.close();
		}
	}

	public static int getPictureExifRotateAngle(String path) {
		int rotate = 0;
		try {
			ExifInterface ei = new ExifInterface(path);
			int ori = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			if (ori == ExifInterface.ORIENTATION_ROTATE_180) {
				rotate = 180;
			} else if (ori == ExifInterface.ORIENTATION_ROTATE_270) {
				rotate = 270;
			} else if (ori == ExifInterface.ORIENTATION_ROTATE_90) {
				rotate = 90;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}

	public static int getWordCount(String s) {
		if (s == null) {
			return 0;
		}
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255)
				length++;
			else
				length += 2;

		}
		return length;

	}
}
