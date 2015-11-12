package com.library.core.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

	public ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentManager mFragmentManager;
	private String[] fundTypes;

	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MyFragmentPagerAdapter(FragmentManager fm,
			FragmentManager mFragmentManager) {
		super(fm);
		this.mFragmentManager = mFragmentManager;
	}

	public MyFragmentPagerAdapter(FragmentManager fm,
			FragmentManager mFragmentManager, String[] fundTypes) {
		super(fm);
		this.mFragmentManager = mFragmentManager;
		this.fundTypes = fundTypes;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	public void addTolist(Fragment fragment) {
		fragments.add(fragment);
		notifyDataSetChanged();
	}

	public void addTolist(List<Fragment> fragments, int location) {
		this.fragments.addAll(location, fragments);
		notifyDataSetChanged();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (fundTypes != null) {
			return fundTypes[position];
		} else {
			return super.getPageTitle(position);
		}
	}

	public void clearList() {
		fragments.clear();
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = fragments.get(position);

		return fragment;
	}

	@SuppressWarnings("unused")
	private void addFragment(Fragment fragment) {
		addTolist(fragment);
	}

	@SuppressWarnings("unused")
	private void removeFragment(Fragment fragment) {
		fragments.remove(fragment);
		notifyDataSetChanged();
	}

}
