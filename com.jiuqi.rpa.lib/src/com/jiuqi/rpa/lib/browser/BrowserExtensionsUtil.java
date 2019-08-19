package com.jiuqi.rpa.lib.browser;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.json.JSONObject;

/**
 * 浏览器扩展相关工具类
 * 
 * @author lpy
 */
public class BrowserExtensionsUtil {

	public static String request(String commands, String arguments, DataInputStream inputStream,
			DataOutputStream outputStream) throws Exception {

		try {
			// 构建请求命令及参数
			JSONObject request = new JSONObject();
			request.put("commands", commands);
			request.put("arguments", arguments);

			// 向浏览器扩展 Native 程序发送请求
			outputStream.writeUTF(request.toString());

			// 从浏览器扩展 Native 程序获取响应
			return inputStream.readUTF();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
