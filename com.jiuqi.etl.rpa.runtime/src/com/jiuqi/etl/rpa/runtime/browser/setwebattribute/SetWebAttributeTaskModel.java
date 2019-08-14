package com.jiuqi.etl.rpa.runtime.browser.setwebattribute;

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
import com.jiuqi.etl.rpa.runtime.Delay;
import com.jiuqi.etl.rpa.runtime.TaskTarget;
import com.jiuqi.rpa.lib.find.Path;

/**
 * ��������
 * 
 * @author liangxiao01
 */
public class SetWebAttributeTaskModel extends TaskModel {
	private String attributeName;
	private String attributeValue;
	private Delay delay = new Delay();
	private TaskTarget taskTarget = new TaskTarget();
	private final String ATTR_ATTRIBUTE_NAME = "ATTRIBUTE_NAME";
	private final String ATTR_ATTRIBUTE_VALUE = "ATTRIBUTE_VALUE";
	private final String ATTR_TASK_TARGET = "TASK_TARGET";
	private final String ATTR_TARGET_ELEMENT = "TARGET_ELEMENT";
	private final String ATTR_TARGET_PATH = "TARGET_PATH";
	private final String ATTR_TARGET_TIMEOUT = "TARGET_TIMEOUT";
	private final String ATTR_DELAY = "DELAY";
	private final String ATTR_DELAYBEFORE = "DELAY_BEFORE";
	private final String ATTR_DELAYAFTER = "DELAY_AFTER";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_ATTRIBUTE_NAME, attributeName);
			e.putOpt(ATTR_ATTRIBUTE_VALUE, attributeValue);
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
		this.attributeName = obj.optString(ATTR_ATTRIBUTE_NAME);
		this.attributeValue = obj.optString(ATTR_ATTRIBUTE_VALUE);
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

	public TaskTarget getTaskTarget() {
		return taskTarget;
	}

	@Override
	public String getId() {
		return SetWebAttributeTaskFactory.ID;
	}

	public SetWebAttributeTaskModel clone() {
		SetWebAttributeTaskModel cloned = (SetWebAttributeTaskModel) super.clone();
		cloned.delay = new Delay();
		cloned.delay.setBefore(delay.getBefore());
		cloned.delay.setAfter(delay.getAfter());
		cloned.taskTarget = new TaskTarget();
		cloned.taskTarget.setTimeout(taskTarget.getTimeout());
		cloned.taskTarget.setElement(taskTarget.getElement());
		cloned.taskTarget.setPath(taskTarget.getPath());
		return cloned;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Delay getDelay() {
		return delay;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}

	@Override
	public boolean validate(IProblems problems, IModel parent) {
		boolean result =  super.validate(problems, parent);
		if (!(parent instanceof ControlFlowModel)) {
			Problem problem = new Problem();
			problem.setLevel(Level.ERROR);
			problem.setParent(parent);
			problem.setSource(this);
			problem.setDescription("�޷���֤����������ȷ��");
			problems.add(problem);
			return false;
		}
		if(taskTarget.getPath()==null && StringUtils.isEmpty(taskTarget.getElement())) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("û�����ò�������");
			problems.add(p);
			result &= false;
		} 
		if(StringUtils.isEmpty(attributeName)) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("����������Ϊ��");
			problems.add(p);
			result &= false;
		} 
		return result;
	}
}
