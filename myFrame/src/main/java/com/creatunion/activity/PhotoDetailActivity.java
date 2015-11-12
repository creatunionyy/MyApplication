package com.creatunion.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.library.core.base.BaseActivity;
import com.library.core.event.Event;
import com.zzc.frame.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * ********************************************* File name:
 * PhotoDetailActivity.java
 * 
 * @Author: ZhangZhaoCheng @version: 1.0 @Date: 2015-6-17
 * @Description: 照片查看器 Others: Function List: History:
 *               *********************************************
 */
public class PhotoDetailActivity extends BaseActivity implements
		OnPageChangeListener {

	private ViewPager mViewPager;// 用于滑动展示照片
	private PhotoVpAdapter mAdapter;// 照片滑动适配器
	private ArrayList<String> urls;// 图片url路径集合
	private int position;// 图片进来的位置
	private int currentPosition;// 图片当前位置

	@Override
	protected void onInitAttribute(BaseAttribute ba) {
		ba.mTitleText = "查看照片";
		ba.mActivityLayoutId = R.layout.activity_photodetail;
		ba.mHasRight = true;
			ba.mRightResId = R.drawable.ic_launcher;
	}
	
	@Override
	protected void onRightButtonClicked(View v) {
		super.onRightButtonClicked(v);
		alterText("删除");
		if (urls.size() == 1) {
		//	MainActivity.tempSelectBitmap.clear();
			finish();
		} else {
			urls.remove(currentPosition);
	//		MainActivity.tempSelectBitmap.remove(currentPosition);
			mAdapter = new PhotoVpAdapter(urls);
			mViewPager.setAdapter(mAdapter);
			mViewPager.setCurrentItem(currentPosition>0 ? currentPosition-1 : currentPosition);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		getIntentData();
		setAdapter();
		mViewPager.setOnPageChangeListener(this);
	}


	/**
	 * 初始化控件
	 */
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vp);
	}

	/**
	 * 将照片显示
	 */
	private void setAdapter() {
		mAdapter = new PhotoVpAdapter(urls);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(position);
	}

	/**
	 * 获取传递过来的图片url
	 */
	public void getIntentData() {
		Bundle bundle = getIntent().getExtras();
		urls = bundle.getStringArrayList("urls");
		position = bundle.getInt("position");
		currentPosition = position;
	}

	/**
	 * 图片滑动适配器
	 */
	public class PhotoVpAdapter extends PagerAdapter {

		private List<String> mUrls;// 图片url
		private ImageView[] mImageViews;// 显示图片的imageView

		public PhotoVpAdapter(List<String> mUrls) {
			this.mUrls = mUrls;
			mImageViews = new ImageView[mUrls.size()];
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			PhotoView imageView = new PhotoView(
					PhotoDetailActivity.this);
			imageView.setImageURI(Uri.fromFile(new File(mUrls.get(position))));
			container.addView(imageView);
			mImageViews[position] = imageView;
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mImageViews[position]);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return mUrls.size();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		currentPosition = arg0;

	}

	@Override
	public void onEventRunEnd(Event event) {
		
	}
}
