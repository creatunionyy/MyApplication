package com.creatunion.activity;

import android.os.Bundle;
import android.view.View;

import com.library.core.base.BaseActivity;
import com.zzc.frame.R;

public class LoginActivity extends BaseActivity{

	@Override
	protected void onInitAttribute(BaseAttribute ba) {
		ba.mActivityLayoutId = R.layout.activity_login;
		ba.mTitleText = "登陆";
		ba.mAddBackButton = false;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initViews() {
	}
	
	@Override
	public void onClick(View v) {

	}
}
