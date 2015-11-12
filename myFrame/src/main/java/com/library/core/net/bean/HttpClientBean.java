package com.library.core.net.bean;

import java.util.Map;

import org.apache.http.client.HttpClient;

/**
 * @ClassName HttpClientInfo
 * @Description HttpClientUtil工具类参数对象信息
 */
public class HttpClientBean {

	//请求参数设置
	private String url;//提交地址
	private String charset;//编码格式
	private Map<String,String> params;//提交参数
	private Map<String,String> filePathParams;//上传文件路径参数，注意：上传文件只能在doPost方法中使用
	private HttpClient httpClient;//http连接
	//代理设置
	private String hostname;//代理-IP
	private int port;//代理-端口
	private String scheme;//代理-协议(http,https)
	//响应返回参数设置
	private int retCode;//返回码
	private String retData;//返回数据
	private String retDetail;//结果描述

	/**
	 * 无参构造函数
	 */
	public HttpClientBean() {
		super();
	}

	/**
	 * 基本参数构造函数
	 * @param url 提交地址
	 * @param params 提交参数
	 */
	public HttpClientBean(String url, Map<String, String> params) {
		this.url = url;
		this.params = params;
	}

	/**
	 * 参数构造函数
	 * @param url 提交地址
	 * @param charset 编码格式
	 * @param params 提交参数
	 * @param httpClient http连接
	 */
	public HttpClientBean(String url, String charset, Map<String, String> params, HttpClient httpClient) {
		this.url = url;
		this.charset = charset;
		this.params = params;
		this.httpClient = httpClient;
	}

	/**
	 * 上传文件
	 * @param url 提交地址
	 * @param charset 编码格式
	 * @param params 提交参数
	 * @param filePathParams 上传文件路径
	 */
	public HttpClientBean(String url, String charset, Map<String, String> params, Map<String, String> filePathParams) {
		this.url = url;
		this.charset = charset;
		this.params = params;
		this.filePathParams = filePathParams;
	}

	/**
	 * 代理构造函数
	 * @param url 提交地址
	 * @param charset 编码格式
	 * @param params 提交参数
	 * @param httpClient http连接
	 * @param hostname 代理-IP
	 * @param port 代理-端口
	 * @param scheme 代理-协议(http,https)
	 */
	public HttpClientBean(String url, String charset, Map<String, String> params, HttpClient httpClient,
						  String hostname, int port, String scheme) {
		this.url = url;
		this.charset = charset;
		this.params = params;
		this.httpClient = httpClient;
		this.hostname = hostname;
		this.port = port;
		this.scheme = scheme;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getRetData() {
		return retData;
	}

	public void setRetData(String retData) {
		this.retData = retData;
	}

	public String getRetDetail() {
		return retDetail;
	}

	public void setRetDetail(String retDetail) {
		this.retDetail = retDetail;
	}

	public Map<String, String> getFilePathParams() {
		return filePathParams;
	}

	public void setFilePathParams(Map<String, String> filePathParams) {
		this.filePathParams = filePathParams;
	}
}
