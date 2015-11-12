package com.library.core.demo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;

import com.creatunion.adapter.MainAdapter;
import com.creatunion.bean.main;
import com.creatunion.runner.TestRunner;
import com.library.core.base.BottomLoadActivity;
import com.library.core.event.Event;
import com.library.core.event.EventCode;
import com.library.core.view.PulldownableListView;
import com.library.core.view.ScrollBottomLoadListView;
import com.zzc.frame.R;

public class PullToRefreshActivityDemo extends BottomLoadActivity {

	private MainAdapter adapter;
	private boolean isLoad;
	
	@Override
	protected void onInitAttribute(BaseAttribute ba) {
		ba.mTitleText = "上下拉刷新demo";
		ba.mActivityLayoutId = R.layout.activity_pull_to_refresh_demo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}

	private void initData() {
		List<main> list = new ArrayList<main>();
		for (int i = 0; i < 40; i++) {
			list.add(new main(i + "  大爷的"));
		}
		adapter = new MainAdapter(this, list);
		mListView.setAdapter(adapter);
		mListView.hasMoreLoad(true);
	}

	/**
	 * 下拉刷新数据
	 * 当页面进入执行首次下拉加载数据
	 */
	@Override
	public void onStartRun(PulldownableListView view) {
		last = 1;
		Event event = new Event(EventCode.HTTP_Medical_ceshi,last);
		pushEventRefresh(event, new TestRunner());
	}
	
	/**
	 * 首次进入获取数据
	 */
	@Override
	protected void onOneRefreshEventEnd(Event event) {
		super.onOneRefreshEventEnd(event);
		initData();
	}
	
	/**
	 * 下拉刷新回调
	 */
	@Override
	protected void onRefreshEventEnd(Event event) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				List<main> list = new ArrayList<main>();
				for (int i = 0; i < 40; i++) {
					list.add(new main(i + "  下拉刷新"));
				}

				adapter.replaceAll(list);
				mListView.hasMoreLoad(true);
				mListView.endRun();
			}
		}, 1000);
	}

	/**
	 * 执行分页加载
	 */
	@Override
	public void onBottomLoad(ScrollBottomLoadListView listView) {
		Event event = new Event(EventCode.HTTP_Medical_ceshi,++last);
		pushEventLoad(event, new TestRunner());
	}
	
	/**
	 * 分页加载回调
	 */
	@Override
	protected void onBottomLoadEventEnd(Event event) {
		super.onBottomLoadEventEnd(event);
		
		isLoad = true;
	
	new Handler().postDelayed(new Runnable() {
		@Override
		public void run() {
			List<main> list = new ArrayList<main>();
			for (int i = 20; i < 60; i++) {
				list.add(new main(i + "  分页加载"));
			}

			adapter.addAll(list);
			mListView.hasMoreLoad(isLoad?false:true);
			mListView.endLoad();
		}
	}, 2000);
	}
}
