package com.jiuqi.etl.rpa.runtime.find.findrelative;

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
import com.jiuqi.etl.rpa.runtime.CursorPosition;
import com.jiuqi.etl.rpa.runtime.TaskTarget;
import com.jiuqi.rpa.action.Position;
import com.jiuqi.rpa.lib.find.Path;

/**
 * 关联元素查找
 * 
 * @author liangxiao01
 */
public class FindRelativeTaskModel extends TaskModel {
	private CursorPosition cursorPosition = new CursorPosition();
	private TaskTarget taskTarget = new TaskTarget();
	private String outputParamName;

	private final String ATTR_OUTPUT_PARAM_NAME = "PARAM_NAME";
	private final String ATTR_TASK_TARGET = "TASK_TARGET";
	private final String ATTR_TARGET_ELEMENT = "TARGET_ELEMENT";
	private final String ATTR_TARGET_PATH = "TARGET_PATH";
	private final String ATTR_TARGET_TIMEOUT = "TARGET_TIMEOUT";
	private final String ATTR_CURSOR_POSITION = "CURSOE_POSITION";
	private final String ATTR_POSITION = "POSITION";
	private final String ATTR_X_OFFSET = "X_OFFSET";
	private final String ATTR_Y_OFFSET = "Y_OFFSET";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_OUTPUT_PARAM_NAME, outputParamName);
			JSONObject positionObj = new JSONObject();
			positionObj.putOpt(ATTR_POSITION, cursorPosition.getPosition().value());
			positionObj.putOpt(ATTR_X_OFFSET, cursorPosition.getOffsetX());
			positionObj.putOpt(ATTR_Y_OFFSET, cursorPosition.getOffsetY());
			e.put(ATTR_CURSOR_POSITION, positionObj);
			JSONObject targetObj = new JSONObject();
			if (taskTarget.getElement() != null) {
				targetObj.putOpt(ATTR_TARGET_ELEMENT, taskTarget.getElement());
			}
			if (taskTarget.getPath() != null) {
				targetObj.putOpt(ATTR_TARGET_PATH, taskTarget.getPath().toJson().toString());
			}
			targetObj.putOpt(ATTR_TARGET_TIMEOUT, taskTarget.getTimeout());
			e.put(ATTR_TASK_TARGET, targetObj);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.outputParamName = obj.optString(ATTR_OUTPUT_PARAM_NAME);
		JSONObject positionObj = obj.optJSONObject(ATTR_CURSOR_POSITION);
		this.getCursorPosition().setPosition(Position.valueOf(positionObj.optInt(ATTR_POSITION)));
		this.getCursorPosition().setOffsetX(positionObj.optString(ATTR_X_OFFSET));
		this.getCursorPosition().setOffsetY(positionObj.optString(ATTR_Y_OFFSET));
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
		return FindRelativeTaskFactory.ID;
	}

	public FindRelativeTaskModel clone() {
		FindRelativeTaskModel cloned = (FindRelativeTaskModel) super.clone();
		cloned.cursorPosition = new CursorPosition();
		cloned.cursorPosition.setPosition(cursorPosition.getPosition());
		cloned.cursorPosition.setOffsetX(cursorPosition.getOffsetX());
		cloned.cursorPosition.setOffsetY(cursorPosition.getOffsetY());
		cloned.taskTarget = new TaskTarget();
		cloned.taskTarget.setTimeout(taskTarget.getTimeout());
		cloned.taskTarget.setElement(taskTarget.getElement());
		cloned.taskTarget.setPath(taskTarget.getPath());
		return cloned;
	}

	public String getOutputParamName() {
		return outputParamName;
	}

	public void setOutputParamName(String outputParamName) {
		this.outputParamName = outputParamName;
	}

	public CursorPosition getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(CursorPosition cursorPosition) {
		this.cursorPosition = cursorPosition;
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
