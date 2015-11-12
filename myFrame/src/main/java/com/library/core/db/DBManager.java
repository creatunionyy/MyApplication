package com.library.core.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DBManager {

	private DBHelper dbHelper;
	private static DBManager instance = null;
	private SQLiteDatabase sqliteDatabase;

	public DBManager(Context context) {
		dbHelper = new DBHelper(context);
		sqliteDatabase = dbHelper.getWritableDatabase();
	}

	public static DBManager getInstance(Context context) {
		if (instance == null)
			instance = new DBManager(context);
		return instance;
	}

	public void close() {
		if (sqliteDatabase.isOpen())
			sqliteDatabase.close();
		if (dbHelper != null)
			dbHelper.close();
		if (instance != null)
			instance = null;
	}

	public Long insertDataBySql(String sql, String[] bindArgs) throws Exception {
		long result = 0;
		if (sqliteDatabase.isOpen()) {
			SQLiteStatement statement = sqliteDatabase.compileStatement(sql);
			if (bindArgs != null) {
				int size = bindArgs.length;
				for (int i = 0; i < size; i++) {
					statement.bindString(i + 1, bindArgs[i]);
				}
				result = statement.executeInsert();
				statement.close();
			}
		} else {
		}
		return result;
	}

	public Long insertData(String table, ContentValues values) {
		long result = 0;
		if (sqliteDatabase.isOpen()) {
			result = sqliteDatabase.insert(table, null, values);
		}
		return result;
	}

	public void updateDataBySql(String sql, String[] bindArgs) throws Exception {
		if (sqliteDatabase.isOpen()) {
			SQLiteStatement statement = sqliteDatabase.compileStatement(sql);
			if (bindArgs != null) {
				int size = bindArgs.length;
				for (int i = 0; i < size; i++) {
					statement.bindString(i + 1, bindArgs[i]);
				}
				statement.execute();
				statement.close();
			}
		} else {

		}
	}

	public int updataData(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		int result = 0;
		if (sqliteDatabase.isOpen()) {
			result = sqliteDatabase.update(table, values, whereClause,
					whereArgs);
		}
		return result;
	}

	public void deleteDataBySql(String sql, String[] bindArgs) throws Exception {
		if (sqliteDatabase.isOpen()) {
			SQLiteStatement statement = sqliteDatabase.compileStatement(sql);
			if (bindArgs != null) {
				int size = bindArgs.length;
				for (int i = 0; i < size; i++) {
					statement.bindString(i + 1, bindArgs[i]);
				}
				Method[] mm = statement.getClass().getDeclaredMethods();
				for (@SuppressWarnings("unused")
				Method method : mm) {
				}
				statement.execute();
				statement.close();
			}
		} else {
		}
	}

	public int deleteData(String table, String whereClause, String[] whereArgs) {
		int result = 0;
		if (sqliteDatabase.isOpen()) {
			result = sqliteDatabase.delete(table, whereClause, whereArgs);
		}
		return result;
	}

	public Cursor queryData2Cursor(String sql, String[] selectionArgs)
			throws Exception {
		if (sqliteDatabase.isOpen()) {
			Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
			if (cursor != null) {
				cursor.moveToFirst();
			}
			return cursor;
		}
		return null;
	}

	/**
	 * 
	 * @param <T>
	 * @param <T>
	 * @param sql
	 *            执行的SQL语句 example ：select *from table
	 * @param selectionArgs
	 *            SQL语句条件 (select * from table where selecyionArgs)
	 * @param object
	 *            数据库实体对象
	 * @return 返回 数据库实体对象List集合 List<Object>
	 * */
	public <T> List<T> queryData2Object(String sql, String[] selectionArgs,
			T object) throws Exception {
		List<T> mList = new ArrayList<T>();
		if (!sqliteDatabase.isOpen())
			return null;
		Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
		Field[] f;
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				T temp = (T) object.getClass().newInstance();
				f = temp.getClass().getDeclaredFields();
				int key;
				@SuppressWarnings("unused")
				String keyname = null;
				for (int i = 0; i < f.length; i++) {
					key = cursor.getColumnIndex(f[i].getName());
					if (key == -1)
						continue;
					keyname = cursor.getString(key);

					Method method = getSetMethod(temp.getClass(),
							f[i].getName());
					method.invoke(object, new Object[] { cursor
							.getString(cursor.getColumnIndex(f[i].getName())) });

				}
				mList.add(temp);
			}
		}
		cursor.close();
		return mList;
	}

	/**
	 * @param sql
	 *            执行的SQL语句 example ：select *from table
	 * @param selectionArgs
	 *            SQL语句条件 (select * from table where selecyionArgs)
	 * @param object
	 *            数据库实体对象
	 * @return 返回以实体对象(object)类成员名为Key的Map 的 List<Map<String,Object>>集合
	 * */

	public List<Map<String, Object>> queryData2Map(String sql,
			String[] selectionArgs, Object object) throws Exception {
		List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
		if (!sqliteDatabase.isOpen())
			return null;
		Cursor cursor = sqliteDatabase.rawQuery(sql, selectionArgs);
		Field[] f;
		Map<String, Object> map;
		String fieldName = null;
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				map = new HashMap<String, Object>();
				f = object.getClass().getDeclaredFields();
				for (int i = 0; i < f.length; i++) {
					fieldName = f[i].getName();
					if (fieldName.equals("CREATOR"))
						continue;
					int index = cursor.getColumnIndex(f[i].getName());
					if (index != -1)
						map.put(f[i].getName(), cursor.getString(index));
				}
				mList.add(map);
			}
		}
		cursor.close();
		return mList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Method getSetMethod(Class objectClass, String fieldName) {
		try {
			Class[] parameterTypes = new Class[1];
			Field field = objectClass.getDeclaredField(fieldName);
			parameterTypes[0] = field.getType();
			StringBuffer sb = new StringBuffer();
			sb.append("set");
			sb.append(fieldName.substring(0, 1).toUpperCase());
			sb.append(fieldName.substring(1));
			Method method = objectClass
					.getMethod(sb.toString(), parameterTypes);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void invokeSet(Object object, String fieldName, Object value) {
		Method method = getSetMethod(object.getClass(), fieldName);
		try {
			method.invoke(object, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 单位查询 **/
	public Cursor query(String name) {

		return sqliteDatabase.rawQuery(
				"select * from unit where unitname  like '%" + name
						+ "%' or unitAddress like '%" + name + "%' limit 10",
				null);
	}

	public void query2sql(String sql) {
		sqliteDatabase.execSQL(sql);
	}

}