package com.jiuqi.rpa.lib.find;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 路径元素
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class PathElement implements IJsonable, Cloneable {
	private String runtimeId;
	private List<PathAttribute> attributes = new ArrayList<PathAttribute>();
	private boolean enable;

	/**
	 * 元素的运行时标识
	 * 
	 * @return 运行时标识
	 */
	public String getRuntimeId() {
		return runtimeId;
	}
	
	/**
	 * 设置元素的运行时标识
	 * 
	 * @param runtimeId 运行时标识
	 */
	public void setRuntimeId(String runtimeId) {
		this.runtimeId = runtimeId;
	}
	
	/**
	 * 获取属性列表
	 * 
	 * @return 返回属性列表
	 */
	public List<PathAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * 设置元素是否启用
	 * 
	 * @param enable 元素是否启用
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * 判断元素是否启用
	 * 
	 * @return 返回元素是否启用
	 */
	public boolean isEnable() {
		return enable;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();

		JSONArray json_attributes = new JSONArray();
		for (PathAttribute attribute : attributes) {
			if (attribute == null)
				continue;
			json_attributes.put(attribute.toJson());
		}
		json.put("attributes", json_attributes);
		json.put("enable", enable);
		return json;
	}

	public void fromJson(JSONObject json) {
		attributes.clear();

		JSONArray json_attributes = json.optJSONArray("attributes");
		for (int i = 0; i < json_attributes.length(); i++) {
			PathAttribute attribute = new PathAttribute();
			attribute.fromJson(json_attributes.optJSONObject(i));
			attributes.add(attribute);
		}
		enable = json.optBoolean("enable");
		runtimeId = json.optString("runtimeId");
	}
	
	@Override
	protected Object clone() {
		try {
			PathElement cloned = (PathElement) super.clone();
			
			cloned.attributes = new ArrayList<PathAttribute>();
			for (PathAttribute attribute: this.attributes) {
				if(attribute == null) continue;
				PathAttribute clonedAttrbute = (PathAttribute) attribute.clone();
				cloned.attributes.add(clonedAttrbute);
			}
			
			return cloned;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
