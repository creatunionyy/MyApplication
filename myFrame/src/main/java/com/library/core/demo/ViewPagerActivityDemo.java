package com.library.core.demo;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.creatunion.fragment.ViewFragment;
import com.library.core.base.BaseFragmentVPActivity;
import com.library.viewpagerindicator.TabPageIndicator;
import com.zzc.frame.R;

public class ViewPagerActivityDemo extends BaseFragmentVPActivity implements
		OnPageChangeListener {

	private TabPageIndicator tabPageIndicator;

	@Override
	protected void onInitAttribute(BaseAttribute ba) {
		ba.mActivityLayoutId = R.layout.activity_vp;
		ba.mTitleText = "滑动测试";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String[] TITLE = new String[] { "头条", "房产","情感","婚姻","物品","娱乐"};  
		fundTypes  = TITLE;
		super.onCreate(savedInstanceState);
		ViewFragment v1 = new ViewFragment();
		ViewFragment v2 = new ViewFragment();
		ViewFragment v3 = new ViewFragment();
		ViewFragment v4 = new ViewFragment();
		ViewFragment v5 = new ViewFragment();
		addFragment(v1, true);
		addFragment(v2, true);
		addFragment(v3, true);
		addFragment(v4, true);
		addFragment(v5, true);

		initView();
	}

	private void initView() {
		tabPageIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		viewPager.setOnPageChangeListener(this);
		tabPageIndicator.setViewPager(viewPager);
		tabPageIndicator.setShowNum(5);
		tabPageIndicator.notifyDataSetChanged();
		tabPageIndicator.setCurrentItem(0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		tabPageIndicator.setCurrentItem(arg0);
		
	}
}
