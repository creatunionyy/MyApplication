package com.library.core.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.core.view.ProgressDialog;
import com.zzc.frame.R;

public class SimpleBaseUIFactory extends BaseUIFactory {

	public SimpleBaseUIFactory(Context context) {
		super(context);
	}

	@Override
	public ProgressDialog createProgressDialog(String title) {
		ProgressDialog mProgressBar = new ProgressDialog(mContext);
		mProgressBar.setTitle(title);
		mProgressBar.show();
		return mProgressBar;
	}

	@Override
	public Dialog createLoadingDialog() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.layout_loading_dialog, null);
		TextView loadingText = (TextView) view
				.findViewById(R.id.id_tv_loading_dialog_text);
		loadingText.setText(mContext.getResources().getString(
				R.string.isloading));
		Dialog mLoadingDialog = new Dialog(mContext,
				R.style.loading_dialog_style);
		mLoadingDialog.setCancelable(false);
		mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		return mLoadingDialog;
	}

	@Override
	public Dialog createYesNoDialog(String yesText, String noText,
			String title, String message, OnClickListener listener) {
		BaseDialog d = new BaseDialog(mContext);
		d.setTitle(title);
		d.setMessage(message).setPositiveButton(yesText, listener);
		if (noText != null) {
			d.setNegativeButton(noText, listener);
		}
		return d;
	}

}
