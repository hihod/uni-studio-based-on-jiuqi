package com.jiuqi.etl.rpa.runtime.dialog.input;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.model.ETLModelException;
import com.jiuqi.etl.model.IModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.model.graph.Context;
import com.jiuqi.etl.model.problem.IProblems;
import com.jiuqi.etl.model.problem.Problem;
import com.jiuqi.etl.model.problem.Problem.Level;

public class DialogInputTaskModel extends TaskModel {
	private String inputTitle;
	private String inputLabel;
	private Boolean password = false;
	private String[] options = new String[] {};

	private String outputParamName;

	private final String ATTR_INPUT_LABEL = "ATTR_INPUT_LABEL";
	private final String ATTR_INPUT_TITLE = "ATTR_INPUT_TITLE";
	private final String ATTR_PASSWORD = "ATTR_PASSWORD";
	private final String ATTR_OPTIONS = "ATTR_OPTIONS";
	private final String ATTR_OUTPUT_PARAM_NAME = "PARAM_NAME";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_OUTPUT_PARAM_NAME, outputParamName);
			e.putOpt(ATTR_OPTIONS, options);
			e.putOpt(ATTR_INPUT_TITLE, inputTitle);
			e.putOpt(ATTR_INPUT_LABEL, inputLabel);
			e.putOpt(ATTR_PASSWORD, password);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.outputParamName = obj.optString(ATTR_OUTPUT_PARAM_NAME);
		this.password = obj.optBoolean(ATTR_PASSWORD);
		this.inputLabel = obj.optString(ATTR_INPUT_LABEL);
		this.inputTitle = obj.optString(ATTR_INPUT_TITLE);
		JSONArray itemsJsonArr = obj.optJSONArray(ATTR_OPTIONS);
		String[] ids = new String[itemsJsonArr.length()];
		for (int i = 0; i < itemsJsonArr.length(); i++) {
			ids[i] = itemsJsonArr.optString(i);
		}
		this.options = ids;
	}

	@Override
	public String getId() {
		return DialogInputTaskFactory.ID;
	}

	public DialogInputTaskModel clone() {
		DialogInputTaskModel cloned = (DialogInputTaskModel) super.clone();
		return cloned;
	}

	public String getOutputParamName() {
		return outputParamName;
	}

	public void setOutputParamName(String outputParamName) {
		this.outputParamName = outputParamName;
	}

	public String getInputTitle() {
		return inputTitle;
	}

	public void setInputTitle(String inputTitle) {
		this.inputTitle = inputTitle;
	}

	public String getInputLabel() {
		return inputLabel;
	}

	public void setInputLabel(String inputLabel) {
		this.inputLabel = inputLabel;
	}

	public Boolean getPassword() {
		return password;
	}

	public void setPassword(Boolean password) {
		this.password = password;
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}
	@Override
	public boolean validate(IProblems problems, IModel parent) {
		boolean result =  super.validate(problems, parent);
		if(StringUtils.isEmpty(inputLabel)) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置标签");
			problems.add(p);
			result &= false;
		} 
		return result;
	}
}
