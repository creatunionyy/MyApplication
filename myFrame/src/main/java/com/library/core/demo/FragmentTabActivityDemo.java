package com.library.core.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.creatunion.fragment.Fragment1;
import com.creatunion.fragment.Fragment2;
import com.library.core.base.BaseActivity;
import com.library.core.base.BaseFragmentTabActivity;
import com.zzc.frame.R;

/**
 * Created by ZhangZhaoCheng on 2015/9/21.
 *
 * Description: fragment在activity切换demo
 */
public class FragmentTabActivityDemo extends BaseFragmentTabActivity {

    private Fragment1 fragment1;
    private Fragment2 fragment2;


    @Override
    protected void onInitAttribute(BaseActivity.BaseAttribute ba) {
        ba.mHasTopBar = false;
        ba.mActivityLayoutId = R.layout.activity_fragment_tab_demo;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        fragment1 = new Fragment1();
        Bundle b = new Bundle();
        b.putString("key", "Num one");
        fragment1.setArguments(b);
        setFragment(fragment1, true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.button:
                fragment1 = new Fragment1();
                Bundle b = new Bundle();
                b.putString("key", "Num one");
                fragment1.setArguments(b);
                setFragment(fragment1, true);
                break;
            case R.id.button2:
                fragment2 = new Fragment2();
                Bundle b2 = new Bundle();
                b2.putString("key", "Num two");
                fragment2.setArguments(b2);
                setFragment(fragment2, true);
                break;
        }
    }
}