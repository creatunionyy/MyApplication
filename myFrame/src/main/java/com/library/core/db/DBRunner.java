package com.library.core.db;

import android.database.sqlite.SQLiteDatabase;

import com.library.core.event.Event;

public class DBRunner extends DBBaseRunner {

	@Override
	public void onEventRunEnd(Event event) {

	}

	@Override
	protected String createTableSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onExecute(SQLiteDatabase db, Event event) {
		// TODO Auto-generated method stub

	}

}
