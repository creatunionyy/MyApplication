package com.creatunion.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.creatunion.bean.main;
import com.library.core.adapter.SetXBaseAdapter;
import com.library.core.adapter.XBaseViewHolder;

public class MainAdapter extends SetXBaseAdapter<main>{


	public MainAdapter(Context context, List<main> mDatas) {
		super(context, mDatas, android.R.layout.simple_list_item_1);
	}

	@Override
	public void getView(XBaseViewHolder viewHolder, main bean, int position) {
		TextView tv = viewHolder.getView(android.R.id.text1);
		tv.setText(bean.getName());
	}
}
