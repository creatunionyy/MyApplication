package com.library.core.base;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.creatunion.utils.XApplication;
import com.library.core.event.BaseEventManager.OnEventRunner;
import com.library.core.event.Event;
import com.library.core.utils.DensityUtils;
import com.library.core.view.PulldownableListView;
import com.zzc.frame.R;

public abstract class PullDownloadRefreshActivity extends BaseActivity
		implements PulldownableListView.OnPullDownListener,
		AdapterView.OnItemClickListener, OnScrollListener {

	protected PulldownableListView mListView;

	protected boolean mIsCreateRefresh = true;

	private boolean mIsShowNoResultView = false;
	private int mNoResultTextId = R.string.no_search_result;
	protected View mViewNoResult;
	protected TextView mTextViewNoResult;
	private List<Event> mCurrentRefreshEvents = new ArrayList<Event>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListView = (PulldownableListView) findViewById(R.id.lv);
		mListView.setDivider(null);
		mListView.setOnPullDownListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setVerticalFadingEdgeEnabled(false);

		if (mIsCreateRefresh) {
			mListView.post(new Runnable() {
				@Override
				public void run() {
					mListView
							.setOnScrollListener(PullDownloadRefreshActivity.this);
					mListView.startRun();
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		mListView.endRun();
		super.onDestroy();
	}

	protected void pushEventRefresh(Event event, OnEventRunner runner) {
		mCurrentRefreshEvents.add(event);
		pushEvent(event, runner,false);
	}

	@Override
	public void onEventRunEnd(Event event) {
		super.onEventRunEnd(event);
		
		if (mCurrentRefreshEvents.remove(event)) {
			if(mIsCreateRefresh){
				onOneRefreshEventEnd(event);
				mIsCreateRefresh = false;
				return;
			}
			if (mCurrentRefreshEvents.size() == 0) {
				onRefreshEventEnd(event);
			}
		}
	}

	protected void setNoResultTextId() {
		setNoResultTextId(R.string.no_search_result);
	}

	protected void setNoResultTextId(int textId) {
		mNoResultTextId = textId;
		mIsShowNoResultView = true;
	}

	protected void onOneRefreshEventEnd(Event event) {
		mListView.endRun();
		if (mIsShowNoResultView) {
			onCheckNoResult(event);
		}
	}

	protected void onCheckNoResult(Event e) {
		if (e.isSuccess()) {
			final List<?> items = e.findReturnParam(List.class);
			if (items != null) {
				if (items.size() == 0) {
					onShowNoResultView();
				} else {
					hideNoResultView();
				}
			}
		}
	}

	protected void onShowNoResultView() {
		if (mViewNoResult == null) {
			mViewNoResult = LayoutInflater.from(this).inflate(
					R.layout.view_no_search_result, null);
			mTextViewNoResult = (TextView) mViewNoResult.findViewById(R.id.tv);
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					XApplication.getScreenHeight()
							- DensityUtils.dp2px(this, 50));
			lp.gravity = Gravity.CENTER_HORIZONTAL;
			lp.topMargin = 50;
			mTextViewNoResult.setText(mNoResultTextId);
			addContentView(mViewNoResult, lp);
		} else {
			mViewNoResult.setVisibility(View.VISIBLE);
		}
	}

	protected void hideNoResultView() {
		if (mViewNoResult != null) {
			mViewNoResult.setVisibility(View.GONE);
		}
	}

	protected boolean isNoResultViewVisible() {
		return mViewNoResult != null
				&& mViewNoResult.getVisibility() == View.VISIBLE;
	}

	protected void onRefreshEventEnd(Event event) {
		mListView.endRun();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
}
