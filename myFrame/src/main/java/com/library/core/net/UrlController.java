package com.library.core.net;

public class UrlController {

	private String url;
	private String ip;
	private int port;

	private static class SingletonController {
		public final static UrlController instance = new UrlController();
	}

	public static UrlController getInstance(boolean isOfficial) {
		SingletonController.instance.init(isOfficial);
		return SingletonController.instance;
	}

	public void init(boolean isOfficial) {
		if (isOfficial) {
			setOfficialUrl();
		} else {
			setTestUrl();
		}
	}

	private void setOfficialUrl() {
		url = "http://www.showbook.net.cn";
		ip = "106.39.117.10";
		port = 14001;
	}

	private void setTestUrl() {
		url = "http://192.168.1.81:8056";
		ip = "192.168.2.251";
		port = 14001;
	}

	public String getUrl() {
		return url;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
}
