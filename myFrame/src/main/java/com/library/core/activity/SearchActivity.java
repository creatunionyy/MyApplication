package com.library.core.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.library.core.base.BaseActivity;
import com.library.core.event.Event;
import com.zzc.frame.R;

/**
 * Created by ZhangZhaoCheng
 *
 * @Date: 2015/11/11
 * @version: 1.0
 * @Description:
 */
public class SearchActivity extends BaseActivity{

    private EditText et_search_ev; // 输入框
    private String et_search_str; // 输入框的值
    private ImageView iv_message_search;// 搜索
    private ImageView iv_message_clear;// 清空
    private TextView title_content; // 无数据显示的内容
    private TextView tv_message_cancel;// 取消
    private ListView lv; // listview
    private String id;// id = 1 :疾病搜索； 2：医生搜索； 3 ：医院搜索

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.activity_search;
        ba.mHasTopBar = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getString("id");
        }
    }

    @Override
    protected void initViews() {
        iv_message_clear = (ImageView) findViewById(R.id.iv_message_clear);
        iv_message_search = (ImageView) findViewById(R.id.iv_message_search);
        tv_message_cancel = (TextView) findViewById(R.id.tv_message_cancel);
        lv = (ListView) findViewById(R.id.list_lv);
        title_content = (TextView) findViewById(R.id.title_content);
        et_search_ev = (EditText) findViewById(R.id.et_search_ev);
        et_search_ev.requestFocus();
    }

    private void setAdapter() {
        if (id != null) {
            switch (Integer.parseInt(id)) {
                case 1:
                    title_content.setText("您可以搜索到各种疾病症状");
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
            // 取消
            case R.id.tv_message_cancel:
                finish();
                break;
            // 搜索
            case R.id.iv_message_search:
                et_search_str = et_search_ev.getText().toString();
                if (et_search_str != null && et_search_str.length() > 0) {
                    getData();
                } else {
                    alterText("请输入关键词！");
                }
                break;
            // 清空
            case R.id.iv_message_clear:
                et_search_str = "";
                et_search_ev.setText("");
                break;
            default:
                break;
        }

    }

    private void getData() {
        if (id != null) {
            int tag = Integer.parseInt(id);
            switch (tag) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onEventRunEnd(Event event) {
        super.onEventRunEnd(event);
    }
}
