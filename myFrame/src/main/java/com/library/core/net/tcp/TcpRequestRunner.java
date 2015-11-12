package com.library.core.net.tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.library.core.event.BaseEventManager.OnEventRunner;
import com.library.core.net.URLUtils;
import com.library.core.net.Util;
import com.library.core.net.bean.TcpClientBean;
import com.library.core.utils.LogUtil;

public abstract class TcpRequestRunner implements OnEventRunner {

	public static Socket mSocket = null;
	public static OutputStream outputStream;
	public static InputStream inputStream;
	
	public static final byte START = (byte) 0x16;
	public static short MAX_SEND_LEN = 1024 + 400;
	public static short MAX_BUFFER_LEN = 1024 * 10;

	public static synchronized Socket getSocketClient() {
		try {
			if (null == mSocket)
				mSocket = new Socket(URLUtils.IP, URLUtils.PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mSocket;
	}

	public TcpClientBean doTCPCommand(TcpClientBean bean) {
		List<byte[]> buffer = getPacketData(bean.getReqType(), (byte) 0,
				toBytes(bean.getReqBuffer()));
		Socket socket = null;
		try {
			//socket = new Socket(IP, PORT);
			 socket = getSocketClient();
	
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			if (socket.isConnected() && !socket.isOutputShutdown()) {
				for (int i = 0; i < buffer.size(); i++) {
					outputStream.write(buffer.get(i));
				}
				// socket.shutdownOutput();
				LogUtil.d("client receive");
				try {
					byte[] barray = Util.readFromInputStream(inputStream);
					LogUtil.d("获取到的流长度:" + barray.length);
					byte[] result = parsePacketData(barray);
					if (result != null && result.length > 0) {
						if (result[0] != 0) {
							byte[] data = new byte[result.length - 1];
							System.arraycopy(result, 1, data, 0,
									result.length - 1);
							bean.setResType(result[0]);
							bean.setResList(getBytes(data, bean.getFieldLen()));
						}
					}
				} catch (Exception e) {
					LogUtil.d("read failure!" + e.toString());
				}
			}
		} catch (Exception e) {
			try {
				mSocket = new Socket(URLUtils.IP, URLUtils.PORT);
			} catch (Exception e1) {
				e.printStackTrace();
			}
		} finally {
			// try {
			// if (inputStream != null)
			// inputStream.close();
			// if (outputStream != null)
			// outputStream.close();
			// if (socket != null)
			// socket.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		return bean;
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

	/**
	 * TCP 网络通讯协议 组包
	 * 
	 * @param type
	 *            数据包类型
	 * @param pData
	 *            数据包数据
	 * @return
	 */
	public static List<byte[]> getPacketData(byte type, byte shipNum,
			byte[] pData) {
		List<byte[]> li = new ArrayList<byte[]>();
		// 获取数据的长度
		int nLen = pData.length;
		byte[] szBuf = new byte[nLen + 11];
		// 将业务数据进行分包
		int nCount = /* 1; */nLen / MAX_SEND_LEN + 1;

		for (short i = 0; i < nCount; i++) {
			// 数据头标示
			szBuf[0] = START;
			// 数据类型
			szBuf[1] = type;
			// 包序号
			szBuf[2] = (byte) (i + 1);
			// 总包数
			szBuf[3] = (byte) nCount;
			// 船号
			szBuf[4] = shipNum;
			// 扩展位
			szBuf[5] = 0;
			// 检验位
			szBuf[6] = (byte) 0x16;

			// 数据长度
			short nDataLen = 0;
			if (i + 1 == nCount)
				nDataLen = (short) (nLen - i * MAX_SEND_LEN);
			else
				nDataLen = MAX_SEND_LEN;

			// 数据长度数据
			Util.putShort(szBuf, nDataLen, 7);
			// 数据内容
			System.arraycopy(pData, 0, szBuf, 9, nDataLen);
			// 数据尾标识
			short usEnd = 0x146F;
			// 结束码导入
			Util.putShort(szBuf, usEnd, nDataLen + 9);
			li.add(szBuf);
		}
		return li;
	}

	/**
	 * 解包
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] parsePacketData(byte[] data) {

		if (data.length <= 11)
			return null;
		Log.e("laopai", "开始接收");
		if (data[0] != START) {
			return null;
		}
		byte type = data[1];// 数据类型
		byte cuNum = data[2];// 当前包
		byte countNum = data[3];// 总包
		byte shipNum = data[4];// 船号
		byte kzw = data[5];// 扩展位
		// 检验位
		if (data[6] != (byte) 0x16) {
			return null;
		}
		short nDataLen = Util.getShort(data, 7);
		// 类型 + 数据
		byte[] da = new byte[1 + nDataLen];
		da[0] = type;
		System.arraycopy(data, 9, da, 1, nDataLen);

		short usEnd = Util.getShort(data, nDataLen + 9);
		if (usEnd != 0x146F) {
			return null;
		}
		if (cuNum == countNum) {
			try {
				Log.e("laopai", "内容：" + new String(da, "gb2312"));
				return da;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @param pData
	 *            需要解析的字节数组
	 * @param fieldLen
	 *            需要解析的字段（个数）
	 * @return List<byte[]> 中list从0开始，按照byte数组字段一次存放
	 * @throws Exception
	 */
	public static List<List<String>> getArrayData(byte[] pData, int fieldLen)
			throws Exception {
		List<List<String>> list = new ArrayList<List<String>>();
		// 可以取出两个字节 用short表示数据一共有多少条 (从0开始取)
		short count = Util.getShort(pData, 0);
		// 从第位开始 前面用了三字节 type(1)和条数(2)
		int byteLen = 2;
		// 循环获取多少条
		for (int i = 0; i < count; i++) {
			List<String> l = new ArrayList<String>();
			// 循环获取字段
			for (int j = 0; j < fieldLen; j++) {
				int len = pData[byteLen];
				byte[] fieldBt = new byte[len];
				System.arraycopy(pData, byteLen + 1, fieldBt, 0, len);
				byteLen = byteLen + 1 + len;
				l.add(Util.byte2String(fieldBt));
			}
			list.add(l);
		}
		return list;
	}

	/**
	 * @param data
	 *            需要解析的数组
	 * @param fieldLen
	 *            数据字段个数
	 */
	public static List<String> getBytes(byte[] data, int fieldLen)
			throws Exception {
		List<String> list = new ArrayList<String>();
		int len = 0;
		for (int i = 0; i < fieldLen; i++) {
			if (len < data.length) {
				int fLen = data[len];
				byte[] b = null;
				if (fLen > 0) {
					b = new byte[fLen];
					System.arraycopy(data, len + 1, b, 0, fLen);

				}
				len = len + 1 + fLen;
				list.add(fLen == 0 ? "" : Util.byte2String(b));
			}
		}
		return list;
	}
}
