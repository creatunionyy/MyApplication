package com.library.core.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzc.frame.R;

public class MenuItemAdapter extends XBaseAdapter<MenuItemAdapter.MenuItem>{

	public MenuItemAdapter(Context context, List<MenuItem> mDatas) {
		super(context, mDatas);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		XBaseViewHolder holder = XBaseViewHolder.getInstance(context, position,
				convertView, parent,  R.layout.item__menu_footer);
		TextView tv = holder.getView(R.id.item_menu_title);
		tv.setText(mDatas.get(position).mTextStringId);
		return holder.getConvertView();
	}

	public static class MenuItem{
		
		private int mId;
		private String mTextStringId;
		
		public MenuItem(int nId,String nStringId){
			mId = nId;
			mTextStringId = nStringId;
		}
		
		public int getId(){
			return mId;
		}
	}

//	@Override
//	public void getView(XBaseViewHolder viewHolder, MenuItem bean) {
//		TextView tv = viewHolder.getView(R.id.item_menu_title);
//		tv.setText(bean.mTextStringId);
//	}
//	
	public void addItem(int nPos,MenuItem item){
		mDatas.add(nPos, item);
	}
	
	public void removeItem(int nId){
		for(MenuItem item : mDatas){
			if(item.getId() == nId){
				mDatas.remove(item);
				break;
			}
		}
	}
	
	public MenuItem getMenuItem(int nId){
		for(MenuItem item : mDatas){
			if(item.getId() == nId){
				return item;
			}
		}
		return null;
	}

}
