package com.library.core.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zzc.frame.R;

/**
 * 上下拉刷新
 * 
 * @author ZhangZhaoCheng
 */
public class RefreshExpandableListView extends ExpandableListView implements
		OnScrollListener {

	private static final String TAG = "listView";

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;// 下拉刷新布局
	private int headContentWidth;// 下拉刷新布局宽度
	private int headContentHeight;// 下拉刷新布局高度

	private TextView tipsTextview;// 下拉刷新 释放刷新 切换显示
	private TextView lastUpdatedTextView;// 最后更新时间
	private ImageView arrowImageView;// 上下箭头
	private ProgressBar progressBar;// 刷新时进度条显示

	private RotateAnimation animation;// 箭头向上动画
	private RotateAnimation reverseAnimation;// 箭头向下动画

	private boolean isBack;// 判断是否由RELEASE_To_REFRESH状态转变来的
	private boolean isRecored;// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRefreshable;// 是否设置了下拉刷新

	private int state;// 当前状态
	private int startY;// 记录按下时的Y值
	private int firstItemIndex;// 第一个可见item的位置
	private int scrollState;// 记录滚动状态
	private OnPullToRefreshListener refreshListener;// 下拉刷新数据接口

	private final static int DONE = 0;// 正常状态
	private final static int PULL_To_REFRESH = 1;// 下拉刷新状态
	private final static int RELEASE_To_REFRESH = 2;// 释放刷新状态
	private final static int REFRESHING = 3;// 刷新中状态

	public RefreshExpandableListView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 两个参数的构造方法
	 * 
	 * @param context
	 * @param attrs
	 */
	public RefreshExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		// 获取下拉刷新头布局
		headView = (LinearLayout) inflater.inflate(R.layout.refreshview, null);
		arrowImageView = (ImageView) headView.findViewById(R.id.ivPullArrow);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView.findViewById(R.id.pbRefresh);
		tipsTextview = (TextView) headView.findViewById(R.id.tvRefresh);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.tvTime);
		measureView(headView);
		// 直接获取header布局的高度为0 因为没有告诉父布局自己占多大地方
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();
		// 直接设置头布局 下拉时不能一点一点出现 首先设置头布局的上边距为高度的负值
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		Log.v("size", "width:" + headContentWidth + " height:"
				+ headContentHeight);
		// 为listView添加头布局
		addHeaderView(headView, null, false);
		// 设置滚动监听器
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;// 默认正常状态
		isRefreshable = false;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstItemIndex = firstVisibleItem;
	}

	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
					Log.v(TAG, "在down时候记录当前位置");
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING) {
					if (state == DONE) {
						// 什么都不做
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
						Log.v(TAG, "由下拉刷新状态，到done状态");
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
						Log.v(TAG, "由松开刷新状态，到done状态");
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				int space = tempY - startY;
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
					Log.v(TAG, "在move时候记录下位置");
				}
				if (state != REFRESHING && isRecored) {
					move(space);
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	private void move(int space) {
		/*
		 * 保证在设置padding的过程中，当前的位置一直是在head 否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
		 */
		switch (state) {
		// done状态下
		case DONE:
			// 移动的距离大于0 证明在执行下拉操作
			if (space > 0) {
				state = PULL_To_REFRESH;
				changeHeaderViewByState();
			}
			break;
		// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
		case PULL_To_REFRESH:
			// 下拉到可以进入RELEASE_TO_REFRESH的状态
			if (space / RATIO >= headContentHeight
					&& SCROLL_STATE_TOUCH_SCROLL == scrollState) {
				state = RELEASE_To_REFRESH;
				isBack = true;
				changeHeaderViewByState();
				Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
			}
			// 上推到顶了
			else if (space <= 0) {
				state = DONE;
				changeHeaderViewByState();
				Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
			}
			// 更新headView的size
			headView.setPadding(0, -1 * headContentHeight + space / RATIO, 0, 0);
			setSelection(0);
			break;
		// 可以松手去刷新了
		case RELEASE_To_REFRESH:
			// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
			if ((space / RATIO < headContentHeight) && space > 0) {
				state = PULL_To_REFRESH;
				changeHeaderViewByState();
				Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
			}
			// 一下子推到顶了
			else if (space <= 0) {
				state = DONE;
				changeHeaderViewByState();
				Log.v(TAG, "由松开刷新状态转变到done状态");
			}
			// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
			else {
				// 不用进行特别的操作，只用更新paddingTop的值就行了
			}
			// 更新headView的paddingTop
			headView.setPadding(0, space / RATIO - headContentHeight, 0, 0);
			setSelection(0);
			break;
		}
	}

	/**
	 * 根据当前的状态改变View的显示状态
	 * 
	 * @param state
	 */
	private void changeHeaderViewByState() {
		switch (state) {
		// 正常状态
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.pull_to_refresh_arrow);
			tipsTextview.setText("下拉刷新");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			Log.v(TAG, "当前状态，done");
			break;
		// 下拉刷新状态
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
				tipsTextview.setText("下拉刷新");
			} else {
				tipsTextview.setText("下拉刷新");
			}
			Log.v(TAG, "当前状态，下拉刷新");
			break;
		// 释放刷新状态
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			tipsTextview.setText("松开刷新");
			Log.v(TAG, "当前状态，松开刷新");
			break;
		// 下拉刷新中
		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			Log.v(TAG, "当前状态,正在刷新...");
			break;
		}
	}

	/**
	 * 设置传入数据接口
	 * 
	 * @param refreshListener
	 */
	public void setonRefreshListener(OnPullToRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	/**
	 * 下拉刷新数据接口
	 * 
	 * @author ZhangZhaoCheng
	 */
	public interface OnPullToRefreshListener {
		public void onRefresh();
	}

	/**
	 * 下拉刷新完毕
	 */
	@SuppressLint("SimpleDateFormat")
	public void onRefreshComplete() {
		state = DONE;
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		lastUpdatedTextView.setText("最近更新:" + date);
		changeHeaderViewByState();
	}

	/**
	 * 下拉刷新数据
	 */
	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	/**
	 * 通知父布局 占用的宽高
	 * 
	 * @param view
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// 获取子布局的宽度
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			// 高度不为0就填充布局
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		// 通知父布局 宽高
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 设置上次更新时间
	 * 
	 * @param adapter
	 */
	public void setAdapter(BaseAdapter adapter) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		lastUpdatedTextView.setText("最近更新:" + date);
		super.setAdapter(adapter);
	}
}
