package com.library.core.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.library.core.utils.NetworkUtil;

/**
 * 检测当前环境的网络状态
 * 
 * 配置要求（注册、权限）： <receiver android:name="core.networkstate.NetworkStateReceiver"
 * > <intent-filter> <action android:name="android.net.conn.CONNECTIVITY_CHANGE"
 * /> <action android:name="android.gzcpc.conn.CONNECTIVITY_CHANGE" />
 * </intent-filter> </receiver> <uses-permission
 * android:name="android.permission.ACCESS_NETWORK_STATE" /> <uses-permission
 * android:name="android.permission.ACCESS_WIFI_STATE" /> <uses-permission
 * android:name="android.permission.CHANGE_NETWORK_STATE" /> <uses-permission
 * android:name="android.permission.CHANGE_WIFI_STATE" />
 */
public class NetworkStateReceiver extends BroadcastReceiver {

	private static BroadcastReceiver broadcastReceiver;
	private static ArrayList<NetworkStateListener> networkStateListenerList = new ArrayList<NetworkStateListener>();
	private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	private static boolean lastNetworkState = false;// 上一次网络状态:true 可用 false 不可用
	private static boolean isFirst = true;// 防止多次接收重复广播（由于网络状态改变时系统广播多次）
	private static boolean needNotify = false;// 是否需要通知

	public static BroadcastReceiver getBroadcastReceiver() {
		return broadcastReceiver == null ? new NetworkStateReceiver()
				: broadcastReceiver;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		broadcastReceiver = NetworkStateReceiver.this;
		if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
			if (!NetworkUtil.isNetworkAvailable(context)) {
				notifyListener(false, NetworkUtil.NetworkType.NONENET);
			} else {
				notifyListener(true, NetworkUtil.getNetworkType(context));
			}
		}
	}

	/**
	 * 通知网络状态监听器
	 */
	private void notifyListener(boolean networkIsAvailable,
			NetworkUtil.NetworkType networkType) {
		if (isFirst) {
			isFirst = false;
			lastNetworkState = networkIsAvailable;
			needNotify = true;
		} else {
			if (lastNetworkState != networkIsAvailable) {
				lastNetworkState = networkIsAvailable;
				needNotify = true;
			} else {
				needNotify = false;
			}
		}
		if (needNotify) {
			for (int i = 0; i < networkStateListenerList.size(); i++) {
				final NetworkStateListener listener = networkStateListenerList
						.get(i);
				if (null != listener) {
					listener.onNetworkState(networkIsAvailable, networkType);
				}
			}
		}
	}

	public static void addNetworkStateListener(NetworkStateListener observer) {
		if (null == networkStateListenerList) {
			networkStateListenerList = new ArrayList<NetworkStateListener>();
		}
		networkStateListenerList.add(observer);
	}

	public static void removeNetworkStateListener(NetworkStateListener observer) {
		if (null != networkStateListenerList) {
			networkStateListenerList.remove(observer);
		}
	}

	public static void registerNetworkStateReceiver(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ANDROID_NET_CHANGE_ACTION);
		context.getApplicationContext().registerReceiver(
				getBroadcastReceiver(), filter);
	}

	public static void unRegisterNetworkStateReceiver(Context context) {
		if (null != broadcastReceiver) {
			try {
				context.getApplicationContext().unregisterReceiver(
						broadcastReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
