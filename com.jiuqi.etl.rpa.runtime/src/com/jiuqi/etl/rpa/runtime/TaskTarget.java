package com.jiuqi.etl.rpa.runtime;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.rpa.lib.find.IJsonable;
import com.jiuqi.rpa.lib.find.Path;

/**
 * 任务目标
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class TaskTarget implements IJsonable {
	private String element;
	private Path path;
	private String timeout = "30000";

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("element", element);
		json.put("path", path.toJson());
		json.put("timeout", timeout);
		return json;
	}

	public void fromJson(JSONObject json) {
		this.element = json.optString("element");
		this.path = new Path();
		this.path.fromJson(json.optJSONObject("path"));
		this.timeout = json.optString("timeout");
	}

}
