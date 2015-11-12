package com.library.core.base;

import com.library.core.view.ProgressDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public abstract class BaseUIFactory {

	protected Context mContext;

	public BaseUIFactory(Context context) {
		mContext = context;
	}

	public abstract ProgressDialog createProgressDialog(String title);

	public abstract Dialog createLoadingDialog();

	public abstract Dialog createYesNoDialog(String yesText, String noText,
			String title, String message,
			DialogInterface.OnClickListener listener);
	
}
