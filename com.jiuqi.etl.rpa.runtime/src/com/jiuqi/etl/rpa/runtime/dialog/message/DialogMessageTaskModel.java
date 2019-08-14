package com.jiuqi.etl.rpa.runtime.dialog.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.ETLModelException;
import com.jiuqi.etl.model.IModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.model.graph.Context;
import com.jiuqi.etl.model.problem.IProblems;
import com.jiuqi.etl.model.problem.Problem;
import com.jiuqi.etl.model.problem.Problem.Level;

public class DialogMessageTaskModel extends TaskModel {
	private String inputTitle;
	private String inputMsg;
	private Long buttonGroup = 0x00000000L;

	private String outputParamName;

	private final String ATTR_INPUT_MSG = "ATTR_INPUT_MSG";
	private final String ATTR_INPUT_TITLE = "ATTR_INPUT_TITLE";
	private final String ATTR_BUTTON_GROUP = "ATTR_BUTTON_GROUP";
	private final String ATTR_OUTPUT_PARAM_NAME = "PARAM_NAME";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_OUTPUT_PARAM_NAME, outputParamName);
			e.putOpt(ATTR_INPUT_TITLE, inputTitle);
			e.putOpt(ATTR_INPUT_MSG, inputMsg);
			e.putOpt(ATTR_BUTTON_GROUP, buttonGroup);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.outputParamName = obj.optString(ATTR_OUTPUT_PARAM_NAME);
		this.buttonGroup = obj.optLong(ATTR_BUTTON_GROUP);
		this.inputMsg = obj.optString(ATTR_INPUT_MSG);
		this.inputTitle = obj.optString(ATTR_INPUT_TITLE);
	}

	public String getInputMsg() {
		return inputMsg;
	}

	public void setInputMsg(String inputMsg) {
		this.inputMsg = inputMsg;
	}

	public Long getButtonGroup() {
		return buttonGroup;
	}

	public void setButtonGroup(Long buttonGroup) {
		this.buttonGroup = buttonGroup;
	}


	@Override
	public String getId() {
		return DialogMessageTaskFactory.ID;
	}

	public DialogMessageTaskModel clone() {
		DialogMessageTaskModel cloned = (DialogMessageTaskModel) super.clone();
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
	@Override
	public boolean validate(IProblems problems, IModel parent) {
		boolean result =  super.validate(problems, parent);
		if (!(parent instanceof ControlFlowModel)) {
			Problem problem = new Problem();
			problem.setLevel(Level.ERROR);
			problem.setParent(parent);
			problem.setSource(this);
			problem.setDescription("无法验证适配器的正确性");
			problems.add(problem);
			return false;
		}
		
		if(StringUtils.isEmpty(inputMsg)) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置消息");
			problems.add(p);
			result &= false;
		} 
		return result;
	}
}
