package com.creatunion.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.library.core.base.BaseFragment;
import com.zzc.frame.R;

/**
 * Created by ZhangZhaoCheng on 2015/9/21.
 * Description:
 */
public class Fragment2 extends BaseFragment {

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mFragmentLayoutId = R.layout.fragment_tab2;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
