package com.jiuqi.etl.rpa.runtime.window.move;

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
import com.jiuqi.etl.rpa.runtime.TaskTarget;
import com.jiuqi.rpa.lib.find.Path;

public class WindowMoveTaskModel extends TaskModel {
	private TaskTarget taskTarget = new TaskTarget();
	private String inputX = "0";
	private String inputY = "0";
	private String inputW = "0";
	private String inputH = "0";
	private final String ATTR_INPUT_X = "INPUT_X";
	private final String ATTR_INPUT_Y = "INPUT_Y";
	private final String ATTR_INPUT_W = "INPUT_W";
	private final String ATTR_INPUT_H = "INPUT_H";
	private final String ATTR_TASK_TARGET = "TASK_TARGET";
	private final String ATTR_TARGET_ELEMENT = "TARGET_ELEMENT";
	private final String ATTR_TARGET_PATH = "TARGET_PATH";
	private final String ATTR_TARGET_TIMEOUT = "TARGET_TIMEOUT";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			JSONObject targetObj = new JSONObject();
			if (taskTarget.getElement() != null) {
				targetObj.putOpt(ATTR_TARGET_ELEMENT, taskTarget.getElement());
			}
			if (taskTarget.getPath() != null) {
				targetObj.putOpt(ATTR_TARGET_PATH, taskTarget.getPath().toJson().toString());
			}
			targetObj.putOpt(ATTR_TARGET_TIMEOUT, taskTarget.getTimeout());
			e.put(ATTR_INPUT_X, inputX);
			e.put(ATTR_INPUT_Y, inputY);
			e.put(ATTR_INPUT_W, inputW);
			e.put(ATTR_INPUT_H, inputH);
			e.put(ATTR_TASK_TARGET, targetObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.inputX = obj.optString(ATTR_INPUT_X);
		this.inputY = obj.optString(ATTR_INPUT_Y);
		this.inputW = obj.optString(ATTR_INPUT_W);
		this.inputH = obj.optString(ATTR_INPUT_H);
		JSONObject targetObj = obj.optJSONObject(ATTR_TASK_TARGET);
		this.getTaskTarget().setTimeout(targetObj.optString(ATTR_TARGET_TIMEOUT));
		if (targetObj.has(ATTR_TARGET_ELEMENT)) {
			this.getTaskTarget().setElement(targetObj.optString(ATTR_TARGET_ELEMENT));
		}
		if (targetObj.has(ATTR_TARGET_PATH)) {
			Path path = new Path();
			try {
				path.fromJson(new JSONObject(targetObj.optString(ATTR_TARGET_PATH)));
				this.getTaskTarget().setPath(path);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}

	public TaskTarget getTaskTarget() {
		return taskTarget;
	}

	@Override
	public String getId() {
		return WindowMoveTaskFactory.ID;
	}

	public WindowMoveTaskModel clone() {
		WindowMoveTaskModel cloned = (WindowMoveTaskModel) super.clone();
		cloned.taskTarget = new TaskTarget();
		cloned.taskTarget.setTimeout(taskTarget.getTimeout());
		cloned.taskTarget.setElement(taskTarget.getElement());
		cloned.taskTarget.setPath(taskTarget.getPath());
		return cloned;
	}

	public String getInputX() {
		return inputX;
	}

	public void setInputX(String inputX) {
		this.inputX = inputX;
	}

	public String getInputY() {
		return inputY;
	}

	public void setInputY(String inputY) {
		this.inputY = inputY;
	}

	public String getInputW() {
		return inputW;
	}

	public void setInputW(String inputW) {
		this.inputW = inputW;
	}

	public String getInputH() {
		return inputH;
	}

	public void setInputH(String inputH) {
		this.inputH = inputH;
	}

	@Override
	public boolean validate(IProblems problems, IModel parent) {
		boolean result =  super.validate(problems, parent);
		if(taskTarget.getPath()==null && StringUtils.isEmpty(taskTarget.getElement())) {
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
