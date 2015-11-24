package com.creatunion.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

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
        final EditText textView3 = (EditText) findViewById(R.id.textView3);
        textView3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    textView3.setGravity(Gravity.LEFT);
                }else{
                    textView3.setGravity(Gravity.CENTER);
                }
            }
        });
	}
	
	@Override
	public void onClick(View v) {

	}
}
