package com.library.core.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.zzc.frame.R;

/************************************************
 * 
 * 
 * @Description: // 用于详细说明此程序文件完成的主要功能，与其他模块 // 或函数的接口，输出值、取值范围、含义及参数间的控 //
 *               制、顺序、独立或依赖等关系
 *               1.该类是使用ViewPager进行Fragment切换的Activity,需要选项卡展示类且需要滑动切换效果时可继承该类
 * 
 *               Others: // 其它内容的说明
 * 
 *               Function List: // 主要函数列表，每条记录应包括函数名及功能简要说明 1. addFragment()
 *               添加子视图
 * 
 *               History: // 修改历史记录列表，每条修改记录应包括修改日期、修改者及修改内容简述
 * 
 ***********************************************/

public abstract class BaseFragmentVPActivity extends BaseActivity {

	private FragmentManager mFragmentManager;
	protected MyFragmentPagerAdapter vpAdapter;
	protected ViewPager viewPager;
	protected String[] fundTypes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getSupportFragmentManager();
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		vpAdapter = new MyFragmentPagerAdapter(mFragmentManager,
				mFragmentManager, fundTypes);
		viewPager.setAdapter(vpAdapter);
	}

	/**
	 * @param hasTitle
	 *            加载的Fragment是否带标题头
	 */
	public void addFragment(Fragment fragment, Boolean hasTitle) {
		Bundle arguments = fragment.getArguments();
		if (arguments == null) {
			arguments = new Bundle();
		}
		arguments.putBoolean(BaseFragment.EXTRA_HasTitle, hasTitle);
		fragment.setArguments(arguments);
		vpAdapter.addTolist(fragment);
	}


	public void setCurrentItem(int position) {
		viewPager.setCurrentItem(position);
	}

	@Override
	protected void onDestroy() {
		vpAdapter.clearList();
		vpAdapter = null;
		super.onDestroy();
	}
}
