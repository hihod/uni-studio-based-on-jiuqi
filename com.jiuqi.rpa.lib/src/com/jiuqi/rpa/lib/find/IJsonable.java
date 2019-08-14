package com.jiuqi.rpa.lib.find;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON���ӿ�
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IJsonable {

	/**
	 * ת��ΪJSON����
	 * @return ����JSON����
	 * @throws JSONException
	 */
	public JSONObject toJson() throws JSONException;
	
	/**
	 * ��JSON�������
	 * @param json ��ԴJSON����
	 */
	void fromJson(JSONObject json);
	
}
