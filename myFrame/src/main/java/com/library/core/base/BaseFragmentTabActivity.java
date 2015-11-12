package com.library.core.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zzc.frame.R;

/**
 * 直接进行Fragment切换的Activity
 */
public abstract class BaseFragmentTabActivity extends BaseActivity {

	private FragmentManager mFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFragmentManager = getSupportFragmentManager();
	}

	public void setFragment(Fragment fragment) {
		setFragment(fragment, false);
	}

	/**
	 * @param hasTitle
	 *            加载的Fragment是否带标题头
	 */
	public void setFragment(Fragment fragment, Boolean hasTitle) {
		Bundle arguments = fragment.getArguments();
		if (arguments == null) {
			arguments = new Bundle();
		}
		arguments.putBoolean(BaseFragment.EXTRA_HasTitle, hasTitle);
		fragment.setArguments(arguments);
		FragmentTransaction beginTransaction = mFragmentManager
				.beginTransaction();
		beginTransaction.replace(R.id.container, fragment);
		if (this != null) {
			beginTransaction.commitAllowingStateLoss();
			mFragmentManager.executePendingTransactions();
		}
	}
}
