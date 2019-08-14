package com.jiuqi.etl.rpa.runtime.dialog.file;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.jiuqi.etl.model.ETLModelException;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.model.graph.Context;

public class SelectFileTaskModel extends TaskModel {
	private String[] filter = new String[] { "*.*" };
	private String initialPath;
	private String outputParamName;

	private final String ATTR_FILTER = "ATTR_FILTER";
	private final String ATTR_INIT_PATH = "ATTR_INIT_PATH";
	private final String ATTR_OUTPUT_PARAM_NAME = "PARAM_NAME";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_OUTPUT_PARAM_NAME, outputParamName);
			e.putOpt(ATTR_FILTER, filter);
			e.putOpt(ATTR_INIT_PATH, initialPath);

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.outputParamName = obj.optString(ATTR_OUTPUT_PARAM_NAME);
		JSONArray itemsJsonArr = obj.optJSONArray(ATTR_FILTER);
		if (itemsJsonArr != null) {
			String[] ids = new String[itemsJsonArr.length()];
			for (int i = 0; i < itemsJsonArr.length(); i++)
				ids[i] = itemsJsonArr.optString(i);
			this.filter = ids;
		}
		this.initialPath = obj.optString(ATTR_INIT_PATH);
	}

	@Override
	public String getId() {
		return SelectFileTaskFactory.ID;
	}

	public SelectFileTaskModel clone() {
		SelectFileTaskModel cloned = (SelectFileTaskModel) super.clone();

		return cloned;
	}

	public String getOutputParamName() {
		return outputParamName;
	}

	public void setOutputParamName(String outputParamName) {
		this.outputParamName = outputParamName;
	}

	public String[] getFilter() {
		return filter;
	}

	public void setFilter(String[] filter) {
		this.filter = filter;
	}

	public String getInitialPath() {
		return initialPath;
	}

	public void setInitialPath(String initialPath) {
		this.initialPath = initialPath;
	}

}
