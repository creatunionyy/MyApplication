package com.library.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.library.core.utils.LogUtil;

public class DBHelper extends SQLiteOpenHelper {

	public String table = "CREATE TABLE code(id integer primary key autoincrement,"
			+ "codeid  varchar(30)," + "codename varchar(100))";
	
	public DBHelper(Context context) {
		super(context, "core", null, 1);
	}

	/**
	 * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行. 重写onCreate方法，调用execSQL方法创建表
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		LogUtil.d("初始化数据库");
		db.execSQL(table);
	}

	// 每次打开数据库时调用该方法
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		LogUtil.d("打开数据库");
	}

	// 当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LogUtil.d("数据库版本需要升级");
	}

}
