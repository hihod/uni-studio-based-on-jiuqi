package com.jiuqi.rpa.lib.find;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON化接口
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IJsonable {

	/**
	 * 转化为JSON对象
	 * @return 返回JSON对象
	 * @throws JSONException
	 */
	public JSONObject toJson() throws JSONException;
	
	/**
	 * 从JSON对象加载
	 * @param json 来源JSON对象
	 */
	void fromJson(JSONObject json);
	
}
