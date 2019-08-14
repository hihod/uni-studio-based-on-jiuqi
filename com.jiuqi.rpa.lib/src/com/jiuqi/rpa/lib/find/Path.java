package com.jiuqi.rpa.lib.find;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ·��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Path implements IJsonable, Cloneable {
	private boolean isWeb;
	private List<PathElement> elements = new ArrayList<PathElement>();
	
	/**
	 * �Ƿ���ҳ·��
	 * 
	 * @return �����Ƿ���ҳ·��
	 */
	public boolean isWeb() {
		return isWeb;
	}
	
	public void setWeb(boolean isWeb) {
		this.isWeb = isWeb;
	}
	/**
	 * ��ȡԪ���б�
	 * 
	 * @return ����Ԫ���б�
	 */
	public List<PathElement> getElements() {
		return elements;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		
		JSONArray json_elements = new JSONArray();
		for (PathElement element: elements)
			json_elements.put(element.toJson());
		json.put("elements", json_elements);
		json.put("isWeb", isWeb);
		
		return json;
	}

	public void fromJson(JSONObject json) {
		isWeb = json.optBoolean("isWeb", false);
		
		elements.clear();
		JSONArray json_elements = json.optJSONArray("elements");
		for (int i = 0; json_elements != null && i < json_elements.length(); i++) {
			PathElement element = new PathElement();
			element.fromJson(json_elements.optJSONObject(i));
			elements.add(element);
		}
	}
	
	@Override
	public Object clone() {
		try {
			Path cloned = (Path) super.clone();
			
			cloned.elements = new ArrayList<PathElement>();
			for (PathElement element: this.elements) {
				PathElement clonedElement = (PathElement) element.clone();
				cloned.elements.add(clonedElement);
			}
			
			return cloned;
		} catch (CloneNotSupportedException e) {
			return null;
		}	
	}

}
