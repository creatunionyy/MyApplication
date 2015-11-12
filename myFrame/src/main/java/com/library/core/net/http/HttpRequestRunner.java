package com.library.core.net.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.os.Handler;

import com.library.core.event.BaseEventManager.OnEventRunner;
import com.library.core.net.bean.HttpClientBean;
import com.library.core.utils.FileHelper;
import com.library.core.utils.LogUtil;

/**
 * @ClassName HttpClientUtil
 * @Description HTTP请求辅助类，为整个应用程序提供唯一的一个HttpClient对象。
 */
public abstract class HttpRequestRunner  implements OnEventRunner{

	/** 字符编码 **/
	public static final String CHARSET = "UTF-8";
	/** 传输超时时间 **/
	public static final Integer SocketTimeout = 15000;
	/** 即连接超时时间 **/
	public static final Integer ConnectTimeout = 15000;
	/** 获取连接请求时间 **/
	public static final Integer ConnectionRequestTimeout = 15000;
	
	private static final String BOUNDARY = "1234567890abcd";

	// **请求返回常量定义**/
	public static int RET_CODE = 0;
	public final static int Success = ++RET_CODE;
	public final static String Success_Detail = "发送请求成功";
	public final static int ProtocolException = ++RET_CODE;
	public final static String ProtocolException_Detail = "请检查协议类型是否正确";
	public final static int NoResult = ++RET_CODE;
	public final static String NoResult_Detail = "无法获取返回信息";
	public final static int CheckURL = ++RET_CODE;
	public final static String CheckURL_Detail = "请检查提交地址是否正确";
	public final static int CheckNet = ++RET_CODE;
	public final static String CheckNet_Detail = "请检查网络连接是否正常";
	public final static int ConnectionTimeOut = ++RET_CODE;
	public final static String ConnectionTimeOut_Detail = "连接超时";
	public final static int TransferDataTimeOut = ++RET_CODE;
	public final static String TransferDataTimeOut_Detail = "传输数据超时";
	public final static int FetchConnectionTimeOut = ++RET_CODE;
	public final static String FetchConnectionTimeOut_Detail = "从连接池中获取连接超时";
	public final static int SSLException = ++RET_CODE;
	public final static String SSLException_Detail = "SSL消息识别异常，请检查协议类型";

	private static HttpClient httpClient;

	public enum Mode {
		GET, POST
	};

	public HttpRequestRunner() {
	//	SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier()); 
	}

	public static synchronized HttpClient getHttpClient() {
		if (null == httpClient) {
			// 初始化工作
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params,
					HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			// 设置连接管理器的超时
			ConnManagerParams.setTimeout(params, ConnectionRequestTimeout);
			// 设置连接超时
			HttpConnectionParams.setConnectionTimeout(params, ConnectTimeout);
			// 设置Socket超时
			HttpConnectionParams.setSoTimeout(params, SocketTimeout);
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 80));
			ClientConnectionManager conManager = new ThreadSafeClientConnManager(
					params, schReg);
			httpClient = new DefaultHttpClient(conManager, params);
		}
		return httpClient;
	}

	/**
	 * HTTP Get 获取内容
	 * 
	 * @param httpClientInfo
	 *            入参
	 * @return 页面内容
	 */
	public static HttpClientBean doGet(HttpClientBean httpClientInfo) {
		return doHttp(httpClientInfo, Mode.GET);
	}

	/**
	 * HTTP Post 获取内容
	 * 
	 * @param httpClientInfo
	 *            入参
	 * @return 页面内容
	 */
	public static HttpClientBean doPost(HttpClientBean httpClientInfo) {
		return doHttp(httpClientInfo, Mode.POST);
	}

	/**
	 * doHttp 获取内容
	 * 
	 * @param httpClientInfo
	 *            入参
	 * @param mode
	 *            请求模式
	 * @return 页面内容
	 */
	public static HttpClientBean doHttp(HttpClientBean httpClientInfo, Mode mode) {
		String url = httpClientInfo.getUrl();
		String url_log = url + "?";
		// 检测提交地址
		if (url == null || "".equals(url)) {
			httpClientInfo.setRetCode(CheckURL);
			httpClientInfo.setRetDetail(CheckURL_Detail);
			return httpClientInfo;
		}
		HttpGet httpGet = null;
		HttpPost httpPost = null;
		HttpResponse httpResponse = null;
		try {
			String charset = httpClientInfo.getCharset();
			// 封装参数
			if (httpClientInfo.getParams() != null
					&& !httpClientInfo.getParams().isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(
						httpClientInfo.getParams().size());
				for (Map.Entry<String, String> entry : httpClientInfo
						.getParams().entrySet()) {
					if (entry.getValue() != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), entry
								.getValue()));
						url_log += "&" + entry.getKey() + "="
								+ entry.getValue();
					}
				}
				// 参数编码设置
				charset = (charset == null) ? CHARSET : charset;
				url += "?"
						+ EntityUtils.toString(new UrlEncodedFormEntity(pairs,
								charset));
			}
			if (mode == Mode.GET) {
				httpGet = new HttpGet(url);
				// 关闭异常再次发送请求
				httpGet.getParams().setBooleanParameter(
						CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
				httpResponse = HttpRequestRunner.getHttpClient().execute(httpGet);
			} else {
				httpPost = new HttpPost(url);
				MultipartEntityEx entityEx = null;
				if (httpClientInfo.getFilePathParams() != null) {
					entityEx = new MultipartEntityEx(
							HttpMultipartMode.BROWSER_COMPATIBLE, null, null,
							null);
					for (String filPath : httpClientInfo.getFilePathParams()
							.keySet()) {
						entityEx.addPart(filPath, new FileBody(
								new File(httpClientInfo.getFilePathParams()
										.get(filPath))));
					}
				}
				if (entityEx != null) {
					httpPost.setEntity(entityEx);
				}
				httpResponse = HttpRequestRunner.getHttpClient().execute(httpPost);
			}
			HttpEntity entity = httpResponse.getEntity();
			int httpStatus = httpResponse.getStatusLine().getStatusCode();
			if (httpStatus == HttpStatus.SC_OK) {
				if (entity != null) {
					httpClientInfo.setRetCode(Success);
					httpClientInfo.setRetDetail(Success_Detail);
					httpClientInfo.setRetData(EntityUtils.toString(entity,
							charset));
				} else {
					httpClientInfo.setRetCode(NoResult);
					httpClientInfo.setRetDetail(NoResult_Detail);
				}
			} else {
				httpClientInfo.setRetCode(CheckNet);
				httpClientInfo.setRetDetail(CheckNet_Detail + "[" + httpStatus
						+ "]");
			}
		} catch (ConnectionPoolTimeoutException e) {
			// 从连接池中获取连接超时
			e.printStackTrace();
			httpClientInfo.setRetCode(FetchConnectionTimeOut);
			httpClientInfo.setRetDetail(FetchConnectionTimeOut_Detail);
		} catch (ClientProtocolException e) {
			// 协议类型错误
			e.printStackTrace();
			httpClientInfo.setRetCode(ProtocolException);
			httpClientInfo.setRetDetail(ProtocolException_Detail);
		} catch (SocketTimeoutException e) {
			// 数据传输超时
			e.printStackTrace();
			httpClientInfo.setRetCode(TransferDataTimeOut);
			httpClientInfo.setRetDetail(TransferDataTimeOut_Detail);
		} catch (ConnectException e) {
			// 连接超时
			e.printStackTrace();
			httpClientInfo.setRetCode(ConnectionTimeOut);
			httpClientInfo.setRetDetail(ConnectionTimeOut_Detail);
		} catch (SSLException e) {
			// SSL消息识别异常
			e.printStackTrace();
			httpClientInfo.setRetCode(SSLException);
			httpClientInfo.setRetDetail(SSLException_Detail);
		} catch (Exception e) {
			e.printStackTrace();
			httpClientInfo.setRetCode(CheckNet);
			httpClientInfo.setRetDetail(CheckNet_Detail);
		} finally {
			// 释放连接
			if (httpGet != null) {
				httpGet.abort();
			}
			if (httpPost != null) {
				httpPost.abort();
			}
			LogUtil.d(HttpRequestRunner.class, httpClientInfo.getRetDetail());
			LogUtil.d(HttpRequestRunner.class, (mode == Mode.GET ? "GetURL: "
					: "PostURL: ") + url_log);
		}
		return httpClientInfo;
	}

	private static HttpResponse doConnection(String strUrl) {
		HttpResponse response = null;
		try {
			final URI uri = new URI(strUrl);
			HttpGet httpGet = new HttpGet(uri);
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params,
					ConnectTimeout);
			HttpConnectionParams.setSoTimeout(params, ConnectTimeout);

			response = httpClient.execute(httpGet);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}
	
	private static boolean isResponseAvailable(HttpResponse response) {
		if (response != null
				&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return true;
		}
		return false;
	}
	
	
	public static String doUpload(String strUrl, String strFilePath,
			ProgressRunnable progress, Handler handler) {
		InputStream is = null;
		OutputStream os = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();

			File file = new File(strFilePath);
			String strFilename = file.getName();

			StringBuffer sbPrefix = new StringBuffer();
			sbPrefix.append("\r\n")
					.append("--" + BOUNDARY + "\r\n")
					.append("Content-Disposition: form-data; name=\"pic_file\"; filename=\""
							+ strFilename + "\"\r\n")
					.append("Content-Type: " + "application/octet-stream"
							+ "\r\n").append("\r\n");

			StringBuffer sbSuffix = new StringBuffer();
			sbSuffix.append("\r\n--" + BOUNDARY + "--\r\n");

			byte bytePrefix[] = sbPrefix.toString().getBytes("UTF-8");
			byte byteSuffix[] = sbSuffix.toString().getBytes("UTF-8");

			final long lContentLength = bytePrefix.length + file.length()
					+ byteSuffix.length;

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			conn.setRequestProperty("Content-Length",
					String.valueOf(lContentLength));
			// conn.setConnectTimeout(TIMEOUT_CONNECTION);
			// conn.setReadTimeout(TIMEOUT_SO);
			// conn.setRequestProperty("HOST", "192.168.1.16:8080");
			conn.setDoOutput(true);

			os = conn.getOutputStream();
			is = new FileInputStream(file);

			os.write(bytePrefix);

			byte[] buf = new byte[1024];
			int nReadBytes = 0;

			if (progress == null) {
				while ((nReadBytes = is.read(buf)) != -1) {
					os.write(buf, 0, nReadBytes);
				}

				os.write(byteSuffix);
			} else {
				long lUploadBytes = bytePrefix.length;
				while ((nReadBytes = is.read(buf)) != -1) {
					os.write(buf, 0, nReadBytes);
					lUploadBytes += nReadBytes;

					progress.mPercentage = (int) (lUploadBytes * 100 / lContentLength);
					handler.post(progress);
				}

				os.write(byteSuffix);

				progress.mPercentage = 100;
				handler.post(progress);
			}

		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					conn = null;
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					conn = null;
				}
			}
		}

		String strRet = null;

		if (conn != null) {
			try {
				InputStream isResponse = (InputStream) conn.getContent();
				if (isResponse != null) {
					int nRead = 0;
					byte buf[] = new byte[128];
					CharArrayBuffer bab = new CharArrayBuffer(128);
					while ((nRead = isResponse.read(buf)) != -1) {
						bab.append(buf, 0, nRead);
					}
					strRet = bab.substring(0, bab.length());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return strRet;
	}
	
	public static String doUpload(String strUrl, String strFilePath) {
		return doUpload(strUrl, strFilePath, null, null);
	}
	
	public static boolean doDownload(String strUrl, String strSavePath,
			boolean bUseTemp, ProgressRunnable progress, Handler handler,
			AtomicBoolean bCancel) {
		HttpResponse response = doConnection(strUrl);
		if (isResponseAvailable(response)) {
			InputStream is = null;
			boolean bSuccess = false;
			FileOutputStream fos = null;
			final String path = bUseTemp ? strSavePath + ".temp" : strSavePath;
			try {
				is = response.getEntity().getContent();
				fos = FileHelper.createFileOutputStream(path);
				if (fos != null) {
					final byte buf[] = new byte[1024];
					if (progress == null) {
						int lReadLength = 0;
						while ((lReadLength = is.read(buf)) != -1) {
							fos.write(buf, 0, lReadLength);
						}
					} else {
						long lDownloadLength = 0;
						int lReadLength = 0;
						final long lTotalLength = response.getEntity()
								.getContentLength();
						while (true) {
							if (bCancel != null && bCancel.get()) {
								File file = new File(path);
								file.delete();
								return false;
							} else if ((lReadLength = is.read(buf)) != -1) {
								fos.write(buf, 0, lReadLength);
								lDownloadLength += lReadLength;
								progress.mPercentage = (int) (lDownloadLength * 100 / lTotalLength);
								handler.post(progress);
							} else {
								break;
							}
						}
					}
					bSuccess = true;
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				FileHelper.deleteFile(path);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
					if (fos != null) {
						fos.close();
					}
					if (bSuccess) {
						if (bUseTemp) {
							File file = new File(path);
							File fileDst = new File(strSavePath);
							file.renameTo(fileDst);
						}
					}
				} catch (Exception e) {
				}
			}
		}
		return false;
	}
	
	private static class MultipartEntityEx extends MultipartEntity {
		public long mTransferredSize;
		public long mTotalSize;
		public ProgressRunnable mRunnable;
		public Handler mHandler;
		public AtomicBoolean mCancel;

		public MultipartEntityEx(HttpMultipartMode mode, ProgressRunnable run,
				Handler handler, AtomicBoolean cancel) {
			super(mode);
			mRunnable = run;
			mHandler = handler;
			mCancel = cancel;
		}

		@Override
		public void writeTo(OutputStream os) throws IOException {
			super.writeTo(new CustomOutputStream(os));
		}

		private class CustomOutputStream extends FilterOutputStream {

			public CustomOutputStream(OutputStream os) {
				super(os);
			}

			@Override
			public void write(byte[] buffer, int offset, int length)
					throws IOException {
				if (mCancel != null && mCancel.get()) {
					throw new IOException();
				}
				out.write(buffer, offset, length);
				// super.write(buffer, offset, length);
				mTransferredSize += length;
				notifyProgress();
			}

			@Override
			public void write(int oneByte) throws IOException {
				if (mCancel != null && mCancel.get()) {
					throw new IOException();
				}
				super.write(oneByte);
				++mTransferredSize;
				notifyProgress();
			}

			protected void notifyProgress() {
				if (mHandler != null && mRunnable != null) {
					final int nPer = (int) (mTransferredSize * 100 / mTotalSize);
					if (mRunnable.mPercentage != nPer) {
						mRunnable.mPercentage = nPer;
						mHandler.post(mRunnable);
					}
				}
			}
		}
	}

	public static abstract class ProgressRunnable implements Runnable {
		private int mPercentage = -1;

		public int getPercentage() {
			return mPercentage;
		}
	}

}