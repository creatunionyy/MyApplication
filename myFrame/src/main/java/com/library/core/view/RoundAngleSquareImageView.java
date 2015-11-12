package com.library.core.view;

import android.content.Context;
import android.util.AttributeSet;

public class RoundAngleSquareImageView extends RoundAngleImageView {
	
	
	public RoundAngleSquareImageView(Context context) {
		super(context);
	}

	
	public RoundAngleSquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public RoundAngleSquareImageView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs,defStyle);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
	}
}
