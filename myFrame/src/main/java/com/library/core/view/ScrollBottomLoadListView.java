package com.library.core.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.zzc.frame.R;

public class ScrollBottomLoadListView extends PullToRefreshListView implements
														AbsListView.OnScrollListener,
														View.OnClickListener{

	private OnScrollListener		mScrollListener;
	
	private OnScrollBottomListener	mListener;
	
	private View					mLoadView;
	private View					mProgressBar;
	private TextView				mTextView;
	
	private boolean					mIsLoading;
	
	private boolean					mIsAutoLoad = true;
	
	private boolean					mHasMore;
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;
//	private Context context;
	private ViewPager viewPager;
	
	public ScrollBottomLoadListView(Context context) {
		super(context);
		
		init();
	}

	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
	}
	
	public ScrollBottomLoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		super.setOnScrollListener(this);
		
		final View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer_bottomload, null);
		mProgressBar = footer.findViewById(R.id.pb);
		mTextView = (TextView)footer.findViewById(R.id.tv);
		footer.setOnClickListener(this);
		addFooterView(footer);
		
		mLoadView = footer;
		mGestureDetector = new GestureDetector(new YScrollDetector());
		setFadingEdgeLength(0);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		super.onInterceptTouchEvent(ev);
		return mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (Math.abs(distanceY) >= Math.abs(distanceX)) {
				return true;
			}
			return false;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
//			Toast.makeText(context, "ͼ" + viewPager.getCurrentItem(), 1).show();
			return super.onSingleTapUp(e);
		}

	}
	
	public void setTextColor(int color){
		mTextView.setTextColor(color);
	}
	
	public void setIsAutoLoad(boolean bAuto){
		mIsAutoLoad = bAuto;
		if(mIsAutoLoad){
			mProgressBar.setVisibility(View.VISIBLE);
			mTextView.setText(R.string.bottom_load_loading);
		}else{
			if(!mIsLoading){
				mProgressBar.setVisibility(View.GONE);
				mTextView.setText(R.string.bottom_load_loadmore);
			}
		}
	}
	
	public void setOnScrollBottomListener(OnScrollBottomListener listener){
		mListener = listener;
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener listener){
		mScrollListener = listener;
	}
	
	public void endLoad(){
		mIsLoading = false;
		if(!mIsAutoLoad){
			mProgressBar.setVisibility(View.GONE);
		}
	}
	
	public void hideBottomView(){
		mLoadView.setVisibility(View.GONE);
	}
	
	public void showBottomView(){
		mLoadView.setVisibility(View.VISIBLE);
	}
	
	public void hasMoreLoad(boolean bHasMore){
		mHasMore = bHasMore;
		mLoadView.setVisibility(View.VISIBLE);
		if(bHasMore){
			mProgressBar.setVisibility(View.GONE);
			if(mIsAutoLoad){
				mTextView.setText(null);
			}else{
				mTextView.setText(R.string.bottom_load_loadmore);
			}
		}else{
			mProgressBar.setVisibility(View.GONE);
			mTextView.setText(R.string.bottom_load_nomore);
		}
	}
	
	public void setText(String text){
		mTextView.setText(text);
	}
	
	public void setLoadFail(){
		mLoadView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		mTextView.setText(R.string.bottom_load_fail);
	}
	
	public boolean hasMore(){
		return mHasMore;
	}
	
	public void checkBottomLoad(){
		if(!mIsLoading){
			if(getLastVisiblePosition() == getCount() - 1){
				mIsLoading = true;
				mProgressBar.setVisibility(View.VISIBLE);
				mTextView.setText(R.string.bottom_load_loading);
				if(mListener != null){
					mListener.onBottomLoad(ScrollBottomLoadListView.this);
				}
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(mIsAutoLoad && mLoadView.getVisibility() == View.VISIBLE && mHasMore){
			if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
				checkBottomLoad();
			}
		}
		if(mScrollListener != null){
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(mScrollListener != null){
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
	
	@Override
	public void onClick(View v) {
		if(!mIsAutoLoad){
			if(v == mLoadView && mHasMore){
				mProgressBar.setVisibility(View.VISIBLE);
				mIsLoading = true;
				if(mListener != null){
					mListener.onBottomLoad(this);
				}
			}
		}
	}
	
	public static interface OnScrollBottomListener{
		public void onBottomLoad(ScrollBottomLoadListView listView);
	}
}
