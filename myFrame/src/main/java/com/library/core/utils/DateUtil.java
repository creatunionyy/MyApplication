package com.library.core.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @Package com.library.core.utils 
 * @ClassName: DateUtil 
 * @Description: 日期工具类
 * @author ZhangZhaoCheng
 * @version V1.0
 */
public class DateUtil {

	public static long getTimeNextDay(long lTimeMillis) {
		Date date = new Date(lTimeMillis);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		Date dateNext = cal.getTime();
		return dateNext.getTime();
	}

	public static long getTimePrevDay(long time) {
		Date date = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		Date prev = cal.getTime();
		return prev.getTime();
	}

	public static boolean isToday(long lTime) {
		return isDateDayEqual(lTime, System.currentTimeMillis());
	}

	public static boolean isTomorrow(long lTime) {
		return isDateDayEqual(lTime, getTimeNextDay(lTime));
	}

	public static boolean isDateDayEqual(long lTime1, long lTime2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(lTime1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(lTime2);

		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);
	}

	public static boolean isInLastWeek(long lTime) {
		Calendar calToday = Calendar.getInstance();
		Calendar calUnknown = Calendar.getInstance();
		calToday.setTimeInMillis(System.currentTimeMillis());
		calUnknown.setTimeInMillis(lTime);
		int nDayOfWeek = calToday.get(Calendar.DAY_OF_WEEK);
		if (nDayOfWeek == Calendar.SUNDAY) {
			nDayOfWeek = Calendar.SATURDAY + 1;
		}
		nDayOfWeek -= 1;
		calToday.add(Calendar.DAY_OF_YEAR, -(nDayOfWeek - 1));
		if (calUnknown.after(calToday)) {
			return false;
		}
		calToday.add(Calendar.DAY_OF_YEAR, -7);
		if (calUnknown.before(calToday)) {
			return false;
		}
		return true;
	}

	public static boolean isInCurrentWeek(long lTime) {
		Calendar calToday = Calendar.getInstance();
		Calendar calUnknown = Calendar.getInstance();
		calToday.setTimeInMillis(System.currentTimeMillis());
		calUnknown.setTimeInMillis(lTime);
		int nDayOfWeek = calToday.get(Calendar.DAY_OF_WEEK);
		if (nDayOfWeek == Calendar.SUNDAY) {
			nDayOfWeek = Calendar.SATURDAY + 1;
		}
		nDayOfWeek -= 1;
		calToday.add(Calendar.DAY_OF_YEAR, -(nDayOfWeek - 1));
		if (calUnknown.before(calToday)) {
			return false;
		}
		calToday.add(Calendar.DAY_OF_YEAR, 7);
		if (calUnknown.after(calToday)) {
			return false;
		}
		return true;
	}

	public static boolean isInNextWeek(long lTime) {
		Calendar calToday = Calendar.getInstance();
		Calendar calUnknown = Calendar.getInstance();
		calToday.setTimeInMillis(System.currentTimeMillis());
		calUnknown.setTimeInMillis(lTime);
		int nDayOfWeek = calToday.get(Calendar.DAY_OF_WEEK);
		if (nDayOfWeek == Calendar.SUNDAY) {
			nDayOfWeek = Calendar.SATURDAY + 1;
		}
		nDayOfWeek -= 1;
		calToday.add(Calendar.DAY_OF_YEAR, 8 - nDayOfWeek);
		if (calUnknown.before(calToday)) {
			return false;
		}
		calToday.add(Calendar.DAY_OF_YEAR, 7);
		if (calUnknown.after(calToday)) {
			return false;
		}
		return true;
	}

	public static boolean isBeyondNextWeek(long lTime) {
		Calendar calToday = Calendar.getInstance();
		Calendar calUnknown = Calendar.getInstance();
		calToday.setTimeInMillis(System.currentTimeMillis());
		calUnknown.setTimeInMillis(lTime);
		int nDayOfWeek = calToday.get(Calendar.DAY_OF_WEEK);
		if (nDayOfWeek == Calendar.SUNDAY) {
			nDayOfWeek = Calendar.SATURDAY + 1;
		}
		nDayOfWeek -= 1;
		calToday.add(Calendar.DAY_OF_YEAR, 8 - nDayOfWeek);
		if (calUnknown.before(calToday)) {
			return false;
		}
		calToday.add(Calendar.DAY_OF_YEAR, 7);
		if (calUnknown.after(calToday)) {
			return true;
		}
		return false;
	}

	public static boolean isInCurrentYear(long lTime) {
		Calendar cal = Calendar.getInstance();
		Calendar calUnknown = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		calUnknown.setTimeInMillis(lTime);
		int nDayOfYear = cal.get(Calendar.DAY_OF_YEAR);

		cal.add(Calendar.DAY_OF_YEAR, 0 - nDayOfYear);
		if (calUnknown.before(cal)) {
			return false;
		}
		cal.add(Calendar.DAY_OF_YEAR, 365);
		if (calUnknown.after(cal)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断两个时间戳之间是否超过了多长时间
	 * 
	 * @return nearTime 比较近（大）的时间，farTime比较远（小）的时间，month相隔多少个小时
	 */
	public static boolean isPassTime(long nearTime, long farTime, int month) {
		long different = (nearTime - farTime) / 3600;
		if (different > month) {
			return true;
		}
		return false;

	}
}
