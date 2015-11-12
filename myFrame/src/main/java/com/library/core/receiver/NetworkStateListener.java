package com.library.core.receiver;

import com.library.core.utils.NetworkUtil;


public interface NetworkStateListener {

	/**
	 * 网络状态回调方法
	 * 
	 * @param networkIsAvailable
	 *            网络是否可用
	 * @param networkType
	 *            网络类型
	 */
	public void onNetworkState(boolean networkIsAvailable,
			NetworkUtil.NetworkType networkType);

}
