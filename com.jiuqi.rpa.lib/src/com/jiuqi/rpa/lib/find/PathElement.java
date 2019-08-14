package com.jiuqi.rpa.lib.find;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ·��Ԫ��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class PathElement implements IJsonable, Cloneable {
	private String runtimeId;
	private List<PathAttribute> attributes = new ArrayList<PathAttribute>();
	private boolean enable;

	/**
	 * Ԫ�ص�����ʱ��ʶ
	 * 
	 * @return ����ʱ��ʶ
	 */
	public String getRuntimeId() {
		return runtimeId;
	}
	
	/**
	 * ����Ԫ�ص�����ʱ��ʶ
	 * 
	 * @param runtimeId ����ʱ��ʶ
	 */
	public void setRuntimeId(String runtimeId) {
		this.runtimeId = runtimeId;
	}
	
	/**
	 * ��ȡ�����б�
	 * 
	 * @return ���������б�
	 */
	public List<PathAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * ����Ԫ���Ƿ�����
	 * 
	 * @param enable Ԫ���Ƿ�����
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * �ж�Ԫ���Ƿ�����
	 * 
	 * @return ����Ԫ���Ƿ�����
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
