package com.library.core.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zzc.frame.R;

/**
 * File name: ProgressDialog.java
 * 
 * @Description: 该类是处理包含进度条的对话框。
 * 
 *               Function List: 1、setTitle();设置对话框标题。 2、setProgress;设置进度条进度值。
 *               History:
 */
public class ProgressDialog extends Dialog {
	
	TextView dialogTitle;
	ProgressBar pb;
	TextView percent;

	public ProgressDialog(Context context) {
		super(context, R.style.dialog);
		setContentView(R.layout.dialog_common_progressbar);
		dialogTitle = (TextView) findViewById(R.id.dialog_title);
		percent = (TextView) findViewById(R.id.percent);
		pb = (ProgressBar) findViewById(R.id.pb);
		setCanceledOnTouchOutside(false);
	}

	public void setTitle(String title) {
		dialogTitle.setText(title);
	}

	public void setProgress(int progress) {
		pb.setProgress(progress);
		percent.setText(progress + "%");
	}

}
