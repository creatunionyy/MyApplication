package com.library.core.base;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.library.core.event.BaseEventManager.OnEventRunner;
import com.library.core.event.Event;
import com.library.core.view.ScrollBottomLoadListView;

public abstract class BottomLoadActivity extends PullDownloadRefreshActivity
		implements ScrollBottomLoadListView.OnScrollBottomListener {

	protected ScrollBottomLoadListView mListView;
	private List<Event> mCurrentLoadEvent = new ArrayList<Event>();
	protected int last = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListView = (ScrollBottomLoadListView) super.mListView;
		mListView.setOnScrollBottomListener(this);
		mListView.hideBottomView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void pushEventLoad(Event event, OnEventRunner runner) {
		mCurrentLoadEvent.add(event);
		pushEvent(event, runner,false);
	}

	@Override
	public void onEventRunEnd(Event event) {
		super.onEventRunEnd(event);
		if(mCurrentLoadEvent.remove(event)){
			onBottomLoadEventEnd(event);
		}
	}

	@Override
	protected void onOneRefreshEventEnd(Event event) {
		if (!event.isSuccess()) {
			mListView.setLoadFail();
		}
		super.onOneRefreshEventEnd(event);
	}

	protected void onBottomLoadEventEnd(Event event) {
		mListView.endLoad();
		if (!event.isSuccess()) {
			mListView.setLoadFail();
		}
	}

	@Override
	protected void onShowNoResultView() {
		super.onShowNoResultView();
		mListView.hideBottomView();
	}
}
