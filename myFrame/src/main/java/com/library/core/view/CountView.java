package com.library.core.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 * 跳动数字的TextView
 */
public class CountView extends TextView {
	
	int duration = 5000;
	float number;

	public CountView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("NewApi")
	public void showNumberWithAnimation(float number) {
		// 修改number属性，会调用setNumber方法
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "number",
				0, number);
		objectAnimator.setDuration(duration);
		// 加速器，从慢到快到再到慢
		objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		objectAnimator.start();
	}

	public float getNumber() {
		return number;
	}

	public void setNumber(float number) {
		this.number = number;
		setText(String.format("%1$.2f", number));
		// setText(number+"");
	}
}
