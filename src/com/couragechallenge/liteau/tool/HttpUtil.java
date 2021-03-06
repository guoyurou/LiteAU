package com.couragechallenge.liteau.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http util 
 * @author weisir
 * 2015-4-21
 */
public class HttpUtil {
	private static final int READ_TIMEOUT = 30 * 1000;

	public static final int BUFFER_SIZE = 8 * 1024;

	public static final String ENCODING = "utf-8";

	public static byte[] doPost(String serverURL, String params, InputStream inStream) {
		return doPost(serverURL, params, inStream, READ_TIMEOUT);
	}

	public static byte[] doPost(String serverURL, String params, InputStream inStream, int connectionTimeOut) {
		URL objURL = null;
		byte[] dataReturn = null;
		HttpURLConnection objConn = null;
		BufferedOutputStream outputStream = null;
		BufferedInputStream inputStream = null;
		
		try {

			objURL = new URL(serverURL + "?" + params);
			
			// 打开连接;
			objConn = (HttpURLConnection) objURL.openConnection();
			objConn.setRequestMethod("POST");
//			objConn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

			// 设置连接和读取超时时间
			objConn.setConnectTimeout(connectionTimeOut);
			objConn.setReadTimeout(READ_TIMEOUT);
			objConn.setDoInput(true);

			byte[] data = new byte[BUFFER_SIZE];
			int c = 0;

			// 如果有输入数据时则写入输出流
			if (inStream != null) {
				// 设置传输长度
				objConn.setChunkedStreamingMode(inStream.available());
				objConn.setUseCaches(false);

				// 设置URLConnection允许输出
				objConn.setDoOutput(true);
				outputStream = new BufferedOutputStream(objConn.getOutputStream());

				while ((c = inStream.read(data)) != -1) {
					outputStream.write(data, 0, c);
				}
				outputStream.flush();
				outputStream.close();
				outputStream = null;

			} else {
				objConn.setDoOutput(false);
			}

			// 读取返回字节数组
			inputStream = new BufferedInputStream(objConn.getInputStream());
			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			byte[] bufferByte = new byte[BUFFER_SIZE];
			for (int len = -1; (len = inputStream.read(bufferByte)) > -1;) {
				bout.write(bufferByte, 0, len);
				bout.flush();
			}
			inputStream.close();
			inputStream = null;

			dataReturn = bout.toByteArray();

		} catch (Exception ex) {
			// 记录错误信息
			Logger.e("[doPost]--http请求异常;", ex);

		} finally {
			// 最后关闭连接和数据流
			if (objConn != null) {
				objConn.disconnect();
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					Logger.e("[doPost]--输出流关闭异常;", e);
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					Logger.e("[doPost]--输入流关闭异常;", e);
				}
			}
		}

		Logger.d("[doPost]--http请求成功;URL:"+objURL+",获得数据长度:" + (null == dataReturn ? "null" : "" + dataReturn.length));
		return dataReturn;
	}
	
	public static byte[] doGet(String url) {
		return doGet(url,ENCODING);
	}
	
	public static byte[] doGet(String url,String encoding) {
		URL objURL = null;
		byte[] dataReturn = null;
		HttpURLConnection objConn = null;
		BufferedInputStream inputStream = null;
		try {
	    	objURL = new URL(url);
	    	// 打开连接
	    	objConn = (HttpURLConnection) objURL.openConnection();
	        objConn.setRequestMethod("GET");
	        objConn.setRequestProperty("Content-Type", "text/plain;charset="+encoding);

			// 设置连接和读取超时时间
	        objConn.setConnectTimeout(READ_TIMEOUT);
	        objConn.setReadTimeout(READ_TIMEOUT);
	        
	        // 读取返回字节数组
	        inputStream = new BufferedInputStream(objConn.getInputStream());
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();
	        byte[] bufferByte = new byte[BUFFER_SIZE];
    		for (int l = -1; (l = inputStream.read(bufferByte)) > -1;) {
    			bout.write(bufferByte, 0, l);
    			bout.flush();
    		}
    		
    		dataReturn = bout.toByteArray();
		} catch (Exception ex) {
			// 记录错误信息
			Logger.e("[doGet]--http请求异常;", ex);
		} finally {
			// 最后关闭连接和数据流
			if (objConn != null) {
				objConn.disconnect();
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				Logger.e("[doGet]--输入流关闭异常;", e);
			}
		}
		
		Logger.d("[doGet]--http请求成功;URL:"+objURL+",获得数据长度:" + (null == dataReturn ? "null" : "" + dataReturn.length));
		return dataReturn;
	}

}
