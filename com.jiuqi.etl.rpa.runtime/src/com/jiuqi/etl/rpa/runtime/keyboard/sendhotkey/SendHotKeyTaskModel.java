package com.jiuqi.etl.rpa.runtime.keyboard.sendhotkey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.jiuqi.rpa.action.keyboard.HotKeyMode;
import com.jiuqi.rpa.lib.find.Path;

/**
 * 发送热键
 * 
 * @author liangxiao01
 */
public class SendHotKeyTaskModel extends TaskModel {

	private Delay delay = new Delay();
	private TaskTarget taskTarget = new TaskTarget();
	private HotKeyMode keyMode = HotKeyMode.NORMAL_HOTKEY;
	private int key = 0;
	private Boolean clearBeforeType = false;
	private int[] maskKeys = {};
	private Boolean attach = true;
	private final String ATTR_DELAY = "DELAY";
	private final String ATTR_KEY = "HOTKEY";
	private final String ATTR_MASKKEYS = "MASKKEYS";
	private final String ATTR_TYPEMODE = "TYPE_MODE";
	private final String ATTR_DELAYBEFORE = "DELAY_BEFORE";
	private final String ATTR_DELAYAFTER = "DELAY_AFTER";
	private final String ATTR_TASK_TARGET = "TASK_TARGET";
	private final String ATTR_TARGET_ELEMENT = "TARGET_ELEMENT";
	private final String ATTR_TARGET_PATH = "TARGET_PATH";
	private final String ATTR_TARGET_TIMEOUT = "TARGET_TIMEOUT";
	private final String ATTR_CLEAR_BEFORE_TYPE = "CLEAR_BEFORE_TYPE";
	private final String ATTR_ATTACH = "ATTACH_ELEMENT";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_CLEAR_BEFORE_TYPE, clearBeforeType);
			e.putOpt(ATTR_ATTACH, attach);
			e.putOpt(ATTR_TYPEMODE, keyMode.value());
			e.putOpt(ATTR_KEY, key);
			e.putOpt(ATTR_MASKKEYS, maskKeys);
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
		this.key = obj.optInt(ATTR_KEY);
		this.clearBeforeType = obj.optBoolean(ATTR_CLEAR_BEFORE_TYPE);
		this.attach = obj.optBoolean(ATTR_ATTACH);
		this.keyMode = HotKeyMode.valueOf(obj.optInt(ATTR_TYPEMODE));
		JSONArray masksJsonArr = obj.optJSONArray(ATTR_MASKKEYS);
		int[] ids = new int[masksJsonArr.length()];
		for (int i = 0; i < masksJsonArr.length(); i++) {
			ids[i] = masksJsonArr.optInt(i);
		}
		this.maskKeys = ids;
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

	public Boolean getClearBeforeType() {
		return clearBeforeType;
	}

	public void setClearBeforeType(Boolean clearBeforeType) {
		this.clearBeforeType = clearBeforeType;
	}

	@Override
	public String getId() {
		return SendHotKeyTaskFactory.ID;
	}

	public SendHotKeyTaskModel clone() {
		SendHotKeyTaskModel cloned = (SendHotKeyTaskModel) super.clone();
		cloned.delay = new Delay();
		cloned.delay.setBefore(delay.getBefore());
		cloned.delay.setAfter(delay.getAfter());
		cloned.taskTarget = new TaskTarget();
		cloned.taskTarget.setTimeout(taskTarget.getTimeout());
		cloned.taskTarget.setElement(taskTarget.getElement());
		cloned.taskTarget.setPath(taskTarget.getPath());
		return cloned;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int[] getMaskKeys() {
		return maskKeys;
	}

	public void setMaskKeys(int[] maskKeys) {
		this.maskKeys = maskKeys;
	}

	public HotKeyMode getTypeMode() {
		return keyMode;
	}

	public void setTypeMode(HotKeyMode typeMode) {
		this.keyMode = typeMode;
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

		return result;
	}

	public Boolean getAttach() {
		return attach;
	}

	public void setAttach(Boolean attach) {
		this.attach = attach;
	}
}
