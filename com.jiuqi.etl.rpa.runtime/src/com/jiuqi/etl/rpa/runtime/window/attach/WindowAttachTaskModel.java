package com.jiuqi.etl.rpa.runtime.window.attach;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.etl.model.ETLModelException;
import com.jiuqi.etl.model.IModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.model.graph.Context;
import com.jiuqi.etl.model.problem.IProblems;
import com.jiuqi.etl.model.problem.Problem;
import com.jiuqi.etl.model.problem.Problem.Level;
import com.jiuqi.rpa.lib.find.Path;

public class WindowAttachTaskModel extends TaskModel {
	private final String ATTR_TARGET_PATH = "TARGET_PATH";
	private final String ATTR_TARGET_TIMEOUT = "TARGET_TIMEOUT";
	private final String ATTR_PARAMNAME = "OUTPUT_PARAMNAME";
	
	private Path path;
	private String timeout = "30000";
	private String outputParamName;

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			if (getPath() != null) {
				e.putOpt(ATTR_TARGET_PATH, getPath().toJson().toString());
			}
			e.putOpt(ATTR_TARGET_TIMEOUT, getTimeout());
			e.putOpt(ATTR_PARAMNAME, outputParamName);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		this.outputParamName = e.optString(ATTR_PARAMNAME);
		
		this.setTimeout(e.optString(ATTR_TARGET_TIMEOUT));
		if (e.has(ATTR_TARGET_PATH)) {
			Path path = new Path();
			try {
				path.fromJson(new JSONObject(e.optString(ATTR_TARGET_PATH)));
				this.setPath(path);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
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

	@Override
	public String getId() {
		return WindowAttachTaskFactory.ID;
	}

	public String getOutputParamName() {
		return outputParamName;
	}

	public void setOutputParamName(String outputParamName) {
		this.outputParamName = outputParamName;
	}

	public WindowAttachTaskModel clone() {
		WindowAttachTaskModel cloned = (WindowAttachTaskModel) super.clone();
		cloned.setTimeout(getTimeout());
		cloned.setOutputParamName(getOutputParamName());
		cloned.setPath(getPath());
		return cloned;
	}
	@Override
	public boolean validate(IProblems problems, IModel parent) {
		boolean result =  super.validate(problems, parent);
		if(getPath()==null) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置操作对象");
			problems.add(p);
			result &= false;
		} 
		return result;
	}
}
