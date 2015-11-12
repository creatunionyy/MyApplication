package com.creatunion.fragment;

import com.library.core.utils.LogUtil;
import com.zzc.frame.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		LogUtil.d("实例化");
		return inflater.inflate(R.layout.view_no_search_result, null);
	}
}
