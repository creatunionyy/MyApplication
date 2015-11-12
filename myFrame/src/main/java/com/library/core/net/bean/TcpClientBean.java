package com.library.core.net.bean;

import java.util.ArrayList;
import java.util.List;

public class TcpClientBean {

	/** 发送的内容 */
	private List<String> reqBuffer;
	
	/** 返回的内容 */
	private List<String> resList;

	/** 请求类型 */
	private byte reqType;
	
	/** 响应返回类型 */
	private byte resType;
	
	/** 返回字段的个数 */
	private int fieldLen;

	/**
	 * 无参构造方法
	 */
	public TcpClientBean() {

	}
	
	/**
	 * 有参构造方法
	 * @param reqType 请求类型
	 * @param reqBuffer 发送内容
	 */
	public TcpClientBean(byte reqType,int fieldLen) {
		this.reqType = reqType;
		this.fieldLen = fieldLen;
		if(reqBuffer == null){
			reqBuffer = new ArrayList<String>();
			reqBuffer.add("");
		}
	}

	/**
	 * 有参构造方法
	 * @param reqType 请求类型
	 * @param reqBuffer 发送内容
	 */
	public TcpClientBean(byte reqType,List<String> reqBuffer,int fieldLen) {
		this.reqType = reqType;
		this.reqBuffer = reqBuffer;
		this.fieldLen = fieldLen;
	}

	public List<String> getReqBuffer() {
		return reqBuffer;
	}

	public void setReqBuffer(List<String> reqBuffer) {
		this.reqBuffer = reqBuffer;
	}

	public List<String> getResList() {
		return resList;
	}

	public void setResList(List<String> resList) {
		this.resList = resList;
	}

	public byte getReqType() {
		return reqType;
	}

	public void setReqType(byte reqType) {
		this.reqType = reqType;
	}

	public byte getResType() {
		return resType;
	}

	public void setResType(byte resType) {
		this.resType = resType;
	}

	public int getFieldLen() {
		return fieldLen;
	}

	public void setFieldLen(int fieldLen) {
		this.fieldLen = fieldLen;
	}
}
