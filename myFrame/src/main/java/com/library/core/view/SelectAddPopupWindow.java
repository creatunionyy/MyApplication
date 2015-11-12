package com.library.core.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.zzc.frame.R;

public class SelectAddPopupWindow extends PopupWindow {

	@SuppressWarnings("deprecation")
	public SelectAddPopupWindow(final Activity context, View v) {
		super(context);

		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(v);
		this.setWidth(w / 2 + 50);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.popwindow_style);
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		v.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				// int height = v.findViewById(R.id.pop_layout2).getTop();
				// int y = (int) event.getY();
				// if (event.getAction() == MotionEvent.ACTION_UP) {
				// if (y < height) {
				// dismiss();
				// }
				// }
				return true;
			}
		});

	}

	public SelectAddPopupWindow(final Activity context, View v, int width) {
		super(context);

		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(v);
		if (width == -1) {

			this.setWidth(w);
			this.setHeight(LayoutParams.WRAP_CONTENT);
		} else {
			this.setWidth(w / 2 + 50);
			this.setHeight(width);
		}
		this.setFocusable(true);
		this.setAnimationStyle(R.style.popwindow_style);
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		v.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

//				int height = v.findViewById(R.id.pop_layout2).getTop();
//				int y = (int) event.getY();
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (y < height) {
//						dismiss();
//					}
//				}
				return true;
			}
		});

	}

}
