package com.creatunion.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.library.core.adapter.CommonPagerAdapter;
import com.library.core.bean.PicUrl;
import com.library.polites.android.GestureImageView;
import com.zzc.frame.R;

public class ViewPagerAdater extends CommonPagerAdapter implements OnClickListener{
	
	private Context context;
	private List<PicUrl> mUrls;
	
	public ViewPagerAdater(Context context,List<PicUrl> mUrls){
		this.context = context;
		this.mUrls=mUrls;
	}
	
    protected View getView(View v, int nPos) {
        View rv = LayoutInflater.from(context).inflate(R.layout.slidingmenumain, null);
//        final String url = mUrls.get(nPos).getPicUrl();
//        final GestureImageView mImageTouchView = (GestureImageView) rv.findViewById(R.id.ivPhoto);
//        mImageTouchView.setOnClickListener(this);
//        if ("tag" != null) {
//   //         mImageTouchView.setOnLongClickListener(context);
//        }
//        mImageTouchView.setTag(mUrls.get(nPos));
      //  XHApplication.setBitmapEx(mImageTouchView, url, R.drawable.default_pic_126);
        return rv;
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }
    
	@Override
	public void onClick(View v) {
		
	}

}
