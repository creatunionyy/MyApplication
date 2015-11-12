package com.library.core.utils;

import java.lang.reflect.Field;


public class ConstantsUtil {
	public static String get(String key) {
		Constants c = new Constants();
		Field[] fields = c.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String varName = fields[i].getName();
			if (varName.equals(key)) {
				try {
					Object o = fields[i].get(c);
					return o.toString();
				} catch (Exception e) {
				}
			}
		}
		return null;
	}
}
