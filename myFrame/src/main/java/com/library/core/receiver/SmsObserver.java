package com.library.core.receiver;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.library.core.utils.LogUtil;

/**
 * Created by zzc on 2015/9/21.
 */
public class SmsObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
    }


    @Override
    public void onChange(boolean selfChange, Uri uri) {
        LogUtil.d("uri:"+ uri.toString());
        LogUtil.d("uri1:"+ uri.toString());

        if(uri.equals("")){
            return;
        }

        if (uri.equals("")){

        }
    }
}
