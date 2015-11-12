package com.library.core.net;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Util {

	/**
	 * 转换short为byte
	 * 
	 * @param b
	 * @param s
	 *            需要转换的short
	 * @param index
	 */
	public static void putShort(byte b[], short s, int index) {
		b[index + 1] = (byte) (s >> 8);
		b[index + 0] = (byte) (s >> 0);
	}

	/**
	 * 通过byte数组取到short
	 * 
	 * @param b
	 * @param index
	 *            第几位开始取
	 * @return
	 */
	public static short getShort(byte[] b, int index) {
		return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
	}

	/**
	 * 公用字符串拼接byte数组 每个串前增加一个字节长度
	 * 
	 * @param list
	 * @return
	 */
	public static byte[] toBytes(List<String> list) {
		int len = list.size();
		for (int i = 0; i < list.size(); i++) {
			len += list.get(i).getBytes().length;
		}
		byte[] b = new byte[len];
		int byteLen = 0;
		for (int i = 0; i < list.size(); i++) {
			try {
				byte[] bt = list.get(i).getBytes("gb2312");
				b[byteLen] = (byte) bt.length;
				System.arraycopy(bt, 0, b, byteLen + 1, bt.length);
				byteLen += bt.length + 1;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	public static List<byte[]> getBytes(byte[] pData, int fieldLen) {

		// 从第位开始 前面用了三字节 和条数(2)
		int byteLen = 0;
		// 循环获取多少条
		List<byte[]> list = new ArrayList<byte[]>();
		// 循环获取字段
		for (int j = 0; j < fieldLen; j++) {
			int len = pData[byteLen];
			byte[] fieldBt = new byte[len];
			System.arraycopy(pData, byteLen + 1, fieldBt, 0, len);
			byteLen = byteLen + 1 + len;
			list.add(fieldBt);
		}
		return list;
	}

	/**
	 * 通过输入流获取返回的数据
	 * 
	 * @param in
	 * @return byte[]
	 */
	public static byte[] readFromInputStream(InputStream in) {
		int count = 0;
		byte[] inDatas = null;
		try {
			while (count == 0) {
				count = in.available();
			}
			inDatas = new byte[count];
			in.read(inDatas, 0, count);
			return inDatas;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 过滤数据后缀单位
	 * 
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String byte2String(byte[] data)
			throws UnsupportedEncodingException {
		return new String(data, "gb2312");
	}
}
