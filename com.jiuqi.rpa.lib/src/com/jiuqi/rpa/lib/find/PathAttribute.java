package com.jiuqi.rpa.lib.find;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Â·¾¶ÊôÐÔ
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class PathAttribute implements IJsonable, Cloneable {
	private String name;
	private String value;
	private boolean enable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("value", value);
		json.put("enable", enable);
		return json;
	}

	public void fromJson(JSONObject json) {
		this.name = json.optString("name");
		this.value = json.optString("value");
		this.enable = json.optBoolean("enable");
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
