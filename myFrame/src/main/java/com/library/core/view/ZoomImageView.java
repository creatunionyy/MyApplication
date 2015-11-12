package com.library.core.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

/**
 * 
 * *********************************************
 *
 * File name: ZoomImageView.java 
 *
 * @Author: ZhangZhaoCheng     @version: 1.0   @Date: 2015-6-17
 *
 * @Description: 自定义多点触摸缩放view
 *
 * Others:
 *
 * Function List: 
 *
 * History:
 *
 * *********************************************
 */
public class ZoomImageView extends ImageView implements OnGlobalLayoutListener,
		OnScaleGestureListener, OnTouchListener {

	/**
	 * 保证图片加载完成只操作一次
	 */
	private boolean mOnce;

	/**
	 * 初始化时缩放值
	 */
	private float mInitScale;

	/**
	 * 双击放大的缩放值
	 */
	private float mMidScale;

	/**
	 * 放大的最大值
	 */
	private float mMaxScale;

	/**
	 * 矩阵
	 */
	private Matrix mScaleMatrix;

	/**
	 * 捕获用户多点触控时缩放的比例
	 */
	private ScaleGestureDetector mScaleGestureDetector;

	/**
	 * 上一次多点触控的数量
	 */
	private int mLastPointCount;

	/**
	 * 最后一次触摸的x，y坐标
	 */
	private float mLastX;
	private float mLastY;

	/**
	 * 判断是否move
	 */
	private int mTouchSlop;

	private boolean isCanDrag;

	/**
	 * 是否检测左右
	 */
	private boolean isCheckLeftRight;
	/**
	 * 是否检测上下
	 */
	private boolean isCheckTopBottom;

	private GestureDetector mGestureDetector;

	/**
	 * 是否正在缩放
	 */
	private boolean isAutoScale;

	public ZoomImageView(Context context) {
		this(context, null);
	}

	public ZoomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mScaleMatrix = new Matrix();
		setScaleType(ScaleType.MATRIX);
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		setOnTouchListener(this);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mGestureDetector = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onDoubleTap(MotionEvent e) {

						if (isAutoScale) {
							return true;
						}
						float x = e.getX();
						float y = e.getY();

						if (getScale() < mMidScale) {
							postDelayed(new AutoScaleRunnable(mMidScale, x, y),
									16);
							isAutoScale = true;
						} else {
							postDelayed(
									new AutoScaleRunnable(mInitScale, x, y), 16);
							isAutoScale = true;
						}
						return true;
					}
				});
	}

	private class AutoScaleRunnable implements Runnable {

		/**
		 * 缩放的目标值
		 */
		private float mTargetScale;
		/**
		 * 缩放的中心点
		 */
		private float x;
		private float y;

		/**
		 * 缩放的梯度值
		 */
		private final float BIG = 1.07f;
		private final float SMALL = 0.93f;

		private float tempScale;

		public AutoScaleRunnable(float mTargetScale, float x, float y) {
			this.mTargetScale = mTargetScale;
			this.x = x;
			this.y = y;

			if (getScale() < mTargetScale) {
				tempScale = BIG;
			}
			if (getScale() > mTargetScale) {
				tempScale = SMALL;
			}
		}

		@Override
		public void run() {
			// 进行缩放
			mScaleMatrix.postScale(tempScale, tempScale, x, y);
			checkBorderWhenScale();
			setImageMatrix(mScaleMatrix);

			float currentScale = getScale();
			if ((tempScale > 1.0f && currentScale < mTargetScale)
					|| (tempScale < 1.0f && currentScale > mTargetScale)) {
				postDelayed(this, 16);
			} else {
				// 设置目标值
				float scale = mTargetScale / currentScale;
				mScaleMatrix.postScale(scale, scale, x, y);
				checkBorderWhenScale();
				setImageMatrix(mScaleMatrix);
				// 缩放完毕
				isAutoScale = false;
			}
		}
	}

	/**
	 * 获取加载完成后的图片
	 */
	@Override
	public void onGlobalLayout() {
		if (!mOnce) {

			// 得到控件的宽和高
			int width = getWidth();
			int height = getHeight();

			// 得到图片以及宽高
			Drawable d = getDrawable();
			if (d == null) {
				return;
			}
			int dw = d.getIntrinsicWidth();
			int dh = d.getIntrinsicHeight();

			float mScale = 1.0f;
			// 图片宽度大于控件的宽度 图片高度小于控件的高度
			if (dw > width && dh < height) {
				mScale = width * 1.0f / dw;
			}

			// 图片宽度小于控件的宽度 图片高度大于控件的高度
			if (dh > height && dw < width) {
				mScale = height * 1.0f / dh;
			}

			// 图片宽高都大于或者都小于控件的宽度
			if ((dw > width && dh > height) || (dw < width && dh < height)) {
				mScale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
			}

			// 初始化缩放的比例值
			mInitScale = mScale;
			mMidScale = mScale * 2;
			mMaxScale = mScale * 4;

			// 将图片移动到控件的中心
			int dx = getWidth() / 2 - dw / 2;
			int dy = getHeight() / 2 - dh / 2;

			mScaleMatrix.postTranslate(dx, dy);
			mScaleMatrix.postScale(mInitScale, mInitScale, width / 2,
					height / 2);
			setImageMatrix(mScaleMatrix);

			mOnce = true;
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeOnGlobalLayoutListener(this);
	}

	/**
	 * 获取当前图片的缩放值
	 * 
	 * @return
	 */
	private float getScale() {
		float[] values = new float[9];
		mScaleMatrix.getValues(values);
		return values[Matrix.MSCALE_X];
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		float scale = getScale();
		float scaleFator = detector.getScaleFactor();

		if (getDrawable() == null) {
			return true;
		}

		// 缩放范围的控制
		if ((scale < mMaxScale && scaleFator > 1.0f)
				|| (scale > mInitScale && scaleFator < 1.0f)) {
			if (scale * scaleFator < mInitScale) {
				scaleFator = mInitScale / scale;
			}
			if (scale * scaleFator > mInitScale) {
				scale = mMaxScale / scale;
			}
		}
		// 执行缩放
		mScaleMatrix.postScale(scaleFator, scaleFator, detector.getFocusX(),
				detector.getFocusY());
		checkBorderWhenScale();
		setImageMatrix(mScaleMatrix);
		return true;
	}

	/**
	 * 获取图片缩放以后的宽和高以及l,t,r,b
	 * 
	 * @return
	 */
	private RectF getMatrixRectF() {
		Matrix matrix = mScaleMatrix;
		RectF rectF = new RectF();
		Drawable drawable = getDrawable();
		if (drawable != null) {
			rectF.set(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			matrix.mapRect(rectF);
		}
		return rectF;
	}

	/**
	 * 在缩放时进行边界控制和位置控制
	 */
	private void checkBorderWhenScale() {
		RectF rectF = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();

		// 缩放时进行检测防止出现白边
		if (rectF.width() >= width) {
			if (rectF.left > 0) {
				deltaX = -rectF.left;
			}
			if (rectF.right < width) {
				deltaX = width - rectF.right;
			}
		}

		if (rectF.height() >= height) {
			if (rectF.top > 0) {
				deltaY = -rectF.top;
			}
			if (rectF.bottom < height) {
				deltaY = height - rectF.bottom;
			}
		}

		// 如果宽度或者高度小于控件的宽和高 使其居中
		if (rectF.width() < width) {
			deltaX = width / 2f - rectF.right + rectF.width() / 2f;
		}

		if (rectF.height() < height) {
			deltaY = height / 2f - rectF.bottom + rectF.height() / 2f;
		}

		mScaleMatrix.postTranslate(deltaX, deltaY);

	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		// 必须return true
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}
		mScaleGestureDetector.onTouchEvent(event);

		float x = 0;
		float y = 0;
		int pointCount = event.getPointerCount();
		for (int i = 0; i < pointCount; i++) {
			x += event.getX(i);
			y += event.getY(i);
		}

		x /= pointCount;
		y /= pointCount;

		if (mLastPointCount != pointCount) {
			isCanDrag = false;
			mLastX = x;
			mLastY = y;
		}

		mLastPointCount = pointCount;

		RectF rect = getMatrixRectF();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(rect.width() > getWidth() + 0.01|| rect.height() > getHeight() + 0.01){
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			
			if(rect.width() > getWidth() + 0.01|| rect.height() > getHeight() + 0.01){
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			
			float dx = x - mLastX;
			float dy = y - mLastY;

			if (!isCanDrag) {
				isCanDrag = isMoveAction(dx, dy);
			}

			if (isCanDrag) {
				RectF rectF = getMatrixRectF();
				if (getDrawable() != null) {
					// 设置全部都可以检测
					isCheckLeftRight = true;
					isCheckTopBottom = true;

					// 如果宽度小于控件的宽度，不允许横向移动
					if (rectF.width() < getWidth()) {
						dx = 0;
						isCheckLeftRight = false;
					}

					// 如果高度小于控件的宽度，不允许纵向移动
					if (rectF.height() < getHeight()) {
						dy = 0;
						isCheckTopBottom = false;
					}
					mScaleMatrix.postTranslate(dx, dy);
					checkBorderWhenTranslate();
					setImageMatrix(mScaleMatrix);
				}
			}
			// 移动过程中不断记录上次的x，y坐标
			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mLastPointCount = 0;
			//如果松开时图片小于屏幕自动放大到屏幕
			if (getDrawable() != null) {
				RectF rectF = getMatrixRectF();
				int width = getWidth();
				int height = getHeight();
				float dw = rectF.width();
				float dh = rectF.height();
				// 如果宽高都小于控件的宽度 放大至屏幕
				if (dw < width && dh < height) {
					float mScale = Math.min(width * 1.0f / dw, height * 1.0f
							/ dh);
					mScaleMatrix.postScale(mScale, mScale, width / 2,
							height / 2);
					setImageMatrix(mScaleMatrix);
				}
			}
			break;
		}
		return true;
	}

	/**
	 * 当移动时进行边界检查
	 */
	private void checkBorderWhenTranslate() {
		RectF rectF = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();

		if (rectF.top > 0 && isCheckTopBottom) {
			deltaY = -rectF.top;
		}

		if (rectF.bottom < height && isCheckTopBottom) {
			deltaY = height - rectF.bottom;
		}

		if (rectF.left > 0 && isCheckLeftRight) {
			deltaX = -rectF.left;
		}

		if (rectF.right < width && isCheckLeftRight) {
			deltaX = width - rectF.right;
		}

		mScaleMatrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * 判断是否move
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	private boolean isMoveAction(float dx, float dy) {
		return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
	}
}
