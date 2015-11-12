package com.creatunion.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.library.core.base.BaseFragmentTabActivity;
import com.zzc.frame.R;

public class MainActivity extends BaseFragmentTabActivity {


    private Button b;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mHasTopBar = false;
        ba.mActivityLayoutId = R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button bu = new Button(this);
       bu.setText("上传成了");

    }
}