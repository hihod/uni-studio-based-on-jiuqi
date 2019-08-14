package com.jiuqi.etl.rpa.runtime.keyboard.typetext;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.etl.model.ETLModelException;
import com.jiuqi.etl.model.IModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.model.graph.Context;
import com.jiuqi.etl.model.problem.IProblems;
import com.jiuqi.etl.rpa.runtime.Delay;
import com.jiuqi.etl.rpa.runtime.TaskTarget;
import com.jiuqi.rpa.action.keyboard.TypeMode;
import com.jiuqi.rpa.lib.find.Path;

/**
 * 文本录入任务模型
 * 
 * @author liangxiao01
 */
public class TypeTextTaskModel extends TaskModel {

	private Delay delay = new Delay();
	private TaskTarget taskTarget = new TaskTarget();
	private String expression = "";
	private Boolean clearBeforeType = false;
	private TypeMode typeMode = TypeMode.NORMAL_TYPE;
	private String delayBetweenKeys = "10";
	private Boolean attach = true;
	private final String ATTR_DELAY = "DELAY";
	private final String ATTR_DELAYBEFORE = "DELAY_BEFORE";
	private final String ATTR_TYPEMODE = "TYPE_MODE";
	private final String ATTR_DELAYAFTER = "DELAY_AFTER";
	private final String ATTR_TASK_TARGET = "TASK_TARGET";
	private final String ATTR_TARGET_ELEMENT = "TARGET_ELEMENT";
	private final String ATTR_TARGET_PATH = "TARGET_PATH";
	private final String ATTR_TARGET_TIMEOUT = "TARGET_TIMEOUT";
	private final String ATTR_EXPRESSION = "EXPRESSION";
	private final String ATTR_CLEAR_BEFORE_TYPE = "CLEAR_BEFORE_TYPE";
	private final String ATTR_DELAY_BETWEEN_KEYS = "DELAY_BETWEEN_KEYS";
	private final String ATTR_ATTACH = "ATTACH_ELEMENT";
	
	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_DELAY_BETWEEN_KEYS, delayBetweenKeys);
			e.putOpt(ATTR_CLEAR_BEFORE_TYPE, clearBeforeType);
			e.putOpt(ATTR_ATTACH, attach);
			e.putOpt(ATTR_TYPEMODE, typeMode.value());
			e.putOpt(ATTR_EXPRESSION, expression);
			JSONObject delayObj = new JSONObject();
			delayObj.putOpt(ATTR_DELAYBEFORE, delay.getBefore());
			delayObj.putOpt(ATTR_DELAYAFTER, delay.getAfter());
			e.put(ATTR_DELAY, delayObj);
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
		this.expression = obj.optString(ATTR_EXPRESSION);
		this.attach = obj.optBoolean(ATTR_ATTACH);
		this.clearBeforeType = obj.optBoolean(ATTR_CLEAR_BEFORE_TYPE);
		this.delayBetweenKeys = obj.optString(ATTR_DELAY_BETWEEN_KEYS);
		this.typeMode = TypeMode.valueOf(obj.optInt(ATTR_TYPEMODE));
		JSONObject delayObj = obj.optJSONObject(ATTR_DELAY);
		this.getDelay().setBefore(delayObj.optString(ATTR_DELAYBEFORE));
		this.getDelay().setAfter(delayObj.optString(ATTR_DELAYAFTER));
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

	public Delay getDelay() {
		return delay;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}

	public TaskTarget getTaskTarget() {
		return taskTarget;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Boolean getClearBeforeType() {
		return clearBeforeType;
	}

	public void setClearBeforeType(Boolean clearBeforeType) {
		this.clearBeforeType = clearBeforeType;
	}

	public String getDelayBetweenKeys() {
		return delayBetweenKeys;
	}

	public void setDelayBetweenKeys(String delayBetweenKeys) {
		this.delayBetweenKeys = delayBetweenKeys;
	}

	public TypeMode getTypeMode() {
		return typeMode;
	}

	public void setTypeMode(TypeMode typeMode) {
		this.typeMode = typeMode;
	}

	@Override
	public String getId() {
		return TypeTextTaskFactory.ID;
	}

	public TypeTextTaskModel clone() {
		TypeTextTaskModel cloned = (TypeTextTaskModel) super.clone();
		cloned.delay = new Delay();
		cloned.delay.setBefore(delay.getBefore());
		cloned.delay.setAfter(delay.getAfter());
		cloned.taskTarget = new TaskTarget();
		cloned.taskTarget.setTimeout(taskTarget.getTimeout());
		cloned.taskTarget.setElement(taskTarget.getElement());
		cloned.taskTarget.setPath(taskTarget.getPath());
		return cloned;
	}
	

	@Override
	public boolean validate(IProblems problems, IModel parent) {
		boolean result =  super.validate(problems, parent);

		return result;
	}

	public Boolean getAttach() {
		return attach;
	}

	public void setAttach(Boolean attach) {
		this.attach = attach;
	}
}
