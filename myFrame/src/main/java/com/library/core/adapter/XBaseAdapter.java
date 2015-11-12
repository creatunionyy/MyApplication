package com.library.core.adapter;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class XBaseAdapter<T> extends BaseAdapter {

	public Context context;
	public List<T> mDatas;

	public XBaseAdapter(Context context, List<T> mDatas) {
		this.context = context;
		this.mDatas = mDatas;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
//		XBaseViewHolder holder = XBaseViewHolder.getInstance(context, position,
//				convertView, parent, layoutId);
//		getView(holder, mDatas.get(position));
//		return holder.getConvertView();



	public void replaceAll(Collection<T> collection) {
		mDatas.clear();
		if (collection != null) {
			mDatas.addAll(collection);
		}
		notifyDataSetChanged();
	}

	public void addAll(Collection<T> collection) {
		if (collection != null) {
			mDatas.addAll(collection);
		}
		notifyDataSetChanged();
	}

	public void addItem(T e) {
		mDatas.add(e);
		notifyDataSetChanged();
	}

	public void addAllItem(List<T> list) {
		mDatas.addAll(list);
		notifyDataSetChanged();
	}

	public void removeItem(T e) {
		mDatas.remove(e);
		notifyDataSetChanged();
	}

	public void removeAllItem(List<T> list) {
		mDatas.removeAll(list);
		notifyDataSetChanged();
	}

	public List<T> getAllItem() {
		return mDatas;
	}

	public void clear() {
		mDatas.clear();
		notifyDataSetChanged();
	}

}
