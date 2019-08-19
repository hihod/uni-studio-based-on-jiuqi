package com.jiuqi.rpa.lib.browser;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.json.JSONObject;

/**
 * �������չ��ع�����
 * 
 * @author lpy
 */
public class BrowserExtensionsUtil {

	public static String request(String commands, String arguments, DataInputStream inputStream,
			DataOutputStream outputStream) throws Exception {

		try {
			// ���������������
			JSONObject request = new JSONObject();
			request.put("commands", commands);
			request.put("arguments", arguments);

			// ���������չ Native ����������
			outputStream.writeUTF(request.toString());

			// ���������չ Native �����ȡ��Ӧ
			return inputStream.readUTF();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
