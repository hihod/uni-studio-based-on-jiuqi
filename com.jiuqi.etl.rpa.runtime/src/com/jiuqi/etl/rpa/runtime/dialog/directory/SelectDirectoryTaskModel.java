package com.jiuqi.etl.rpa.runtime.dialog.directory;

import org.json.JSONException;
import org.json.JSONObject;
import com.jiuqi.etl.model.ETLModelException;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.model.graph.Context;

public class SelectDirectoryTaskModel extends TaskModel {

	private String initPath;

	public String getInitPath() {
		return initPath;
	}

	public void setInitPath(String initPath) {
		this.initPath = initPath;
	}

	private String outputParamName;

	private final String ATTR_INITPATH = "ATTR_INITPATH";
	private final String ATTR_OUTPUT_PARAM_NAME = "PARAM_NAME";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_OUTPUT_PARAM_NAME, outputParamName);
			e.putOpt(ATTR_INITPATH, initPath);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.outputParamName = obj.optString(ATTR_OUTPUT_PARAM_NAME);
		this.initPath = obj.optString(ATTR_INITPATH);

	}

	@Override
	public String getId() {
		return SelectDirectoryTaskFactory.ID;
	}

	public SelectDirectoryTaskModel clone() {
		SelectDirectoryTaskModel cloned = (SelectDirectoryTaskModel) super.clone();
		return cloned;
	}

	public String getOutputParamName() {
		return outputParamName;
	}

	public void setOutputParamName(String outputParamName) {
		this.outputParamName = outputParamName;
	}

}
