package com.library.core.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.core.utils.DensityUtils;
import com.zzc.frame.R;

public class BaseDialog extends Dialog {

	private TextView mTextViewMessage;
	private Button mBtnPositive;
	private Button mBtnNegative;

	public BaseDialog(Context context) {
		super(context, R.style.dialog);
		setContentView(R.layout.base_dialog);
		mTextViewMessage = (TextView) findViewById(R.id.tvMessage);
		mBtnPositive = (Button) findViewById(R.id.btnOK);
		mBtnNegative = (Button) findViewById(R.id.btnCancel);
	}

	public void setTitle(CharSequence title) {
		mTextViewMessage.setText(title);
	}

	public void setTitle(int titleId) {
		mTextViewMessage.setText(titleId);
	}

	public BaseDialog setMessage(CharSequence message) {
		mTextViewMessage.setText(message);
		return this;
	}

	public BaseDialog setMessage(int messageId) {
		mTextViewMessage.setText(messageId);
		return this;
	}

	public BaseDialog setPositiveButton(String text,
			final OnClickListener listener) {
		mBtnPositive.setText(text);
		mBtnPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onClick(BaseDialog.this,
							DialogInterface.BUTTON_POSITIVE);
				}
			}
		});
		return this;
	}

	public BaseDialog setNegativeButton(String text,
			final OnClickListener listener) {
		mBtnNegative.setText(text);
		mBtnNegative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onClick(BaseDialog.this,
							DialogInterface.BUTTON_NEGATIVE);
				}
			}
		});
		return this;
	}

	@Override
	public void show() {
		try {
			if (TextUtils.isEmpty(mBtnNegative.getText())) {
				mBtnNegative.setVisibility(View.GONE);
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBtnPositive
						.getLayoutParams();
				lp.weight = 0;
				lp.width = DensityUtils.dp2px(getContext(), 120);
				mBtnPositive.setLayoutParams(lp);
			}
			super.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
