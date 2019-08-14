package com.jiuqi.etl.rpa.runtime.mouse.drag;

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
import com.jiuqi.etl.rpa.runtime.CursorPosition;
import com.jiuqi.etl.rpa.runtime.Delay;
import com.jiuqi.etl.rpa.runtime.TaskTarget;
import com.jiuqi.rpa.action.Position;
import com.jiuqi.rpa.lib.find.Path;

/**
 * 鼠标拖拽任务模型
 * 
 * @author liangxiao01
 */
public class MouseDragTaskModel extends TaskModel {
	private CursorPosition startCursorPosition = new CursorPosition();
	private CursorPosition endCursorPosition = new CursorPosition();
	private Delay delay = new Delay();
	private TaskTarget startTaskTarget = new TaskTarget();
	private TaskTarget endTaskTarget = new TaskTarget();
	private int[] maskKeys = {};
	private Boolean attach = true;
	public int[] getMaskKeys() {
		return maskKeys;
	}

	public void setMaskKeys(int[] maskKeys) {
		this.maskKeys = maskKeys;
	}
	private final String ATTR_ATTACH = "ATTACH_ELEMENT";
	private final String ATTR_DELAY = "DELAY";
	private final String ATTR_DELAYBEFORE = "DELAY_BEFORE";
	private final String ATTR_DELAYAFTER = "DELAY_AFTER";
	private final String ATTR_MASKKEYS = "MASKKEYS";
	private final String ATTR_START_CURSOR_POSITION = "START_CURSOE_POSITION";
	private final String ATTR_START_POSITION = "START_POSITION";
	private final String ATTR_START_X_OFFSET = "START_X_OFFSET";
	private final String ATTR_START_Y_OFFSET = "START_Y_OFFSET";

	private final String ATTR_START_TASK_TARGET = "START_TASK_TARGET";
	private final String ATTR_START_TARGET_ELEMENT = "START_TARGET_ELEMENT";
	private final String ATTR_START_TARGET_PATH = "START_TARGET_PATH";
	private final String ATTR_START_TARGET_TIMEOUT = "START_TARGET_TIMEOUT";

	private final String ATTR_END_CURSOR_POSITION = "END_CURSOE_POSITION";
	private final String ATTR_END_POSITION = "END_POSITION";
	private final String ATTR_END_X_OFFSET = "END_X_OFFSET";
	private final String ATTR_END_Y_OFFSET = "END_Y_OFFSET";

	private final String ATTR_END_TASK_TARGET = "END_TASK_TARGET";
	private final String ATTR_END_TARGET_ELEMENT = "END_TARGET_ELEMENT";
	private final String ATTR_END_TARGET_PATH = "END_TARGET_PATH";
	private final String ATTR_END_TARGET_TIMEOUT = "END_TARGET_TIMEOUT";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_MASKKEYS, maskKeys);
			JSONObject delayObj = new JSONObject();
			delayObj.putOpt(ATTR_DELAYBEFORE, delay.getBefore());
			delayObj.putOpt(ATTR_DELAYAFTER, delay.getAfter());
			e.put(ATTR_DELAY, delayObj);
			e.put(ATTR_ATTACH,attach);
			JSONObject positionObj1 = new JSONObject();
			positionObj1.putOpt(ATTR_START_POSITION, startCursorPosition.getPosition().value());
			positionObj1.putOpt(ATTR_START_X_OFFSET, startCursorPosition.getOffsetX());
			positionObj1.putOpt(ATTR_START_Y_OFFSET, startCursorPosition.getOffsetY());
			e.put(ATTR_START_CURSOR_POSITION, positionObj1);
			JSONObject positionObj2 = new JSONObject();
			positionObj2.putOpt(ATTR_END_POSITION, endCursorPosition.getPosition().value());
			positionObj2.putOpt(ATTR_END_X_OFFSET, endCursorPosition.getOffsetX());
			positionObj2.putOpt(ATTR_END_Y_OFFSET, endCursorPosition.getOffsetY());
			e.put(ATTR_END_CURSOR_POSITION, positionObj2);
			JSONObject targetObj1 = new JSONObject();
			if (startTaskTarget.getElement() != null) {
				targetObj1.putOpt(ATTR_START_TARGET_ELEMENT, startTaskTarget.getElement());
			}
			if (startTaskTarget.getPath() != null) {
				targetObj1.putOpt(ATTR_START_TARGET_PATH, startTaskTarget.getPath().toJson().toString());
			}
			targetObj1.putOpt(ATTR_START_TARGET_TIMEOUT, startTaskTarget.getTimeout());
			e.put(ATTR_START_TASK_TARGET, targetObj1);

			JSONObject targetObj2 = new JSONObject();
			if (endTaskTarget.getElement() != null) {
				targetObj2.putOpt(ATTR_END_TARGET_ELEMENT, endTaskTarget.getElement());
			}
			if (endTaskTarget.getPath() != null) {
				targetObj2.putOpt(ATTR_END_TARGET_PATH, endTaskTarget.getPath().toJson().toString());
			}
			targetObj2.putOpt(ATTR_END_TARGET_TIMEOUT, endTaskTarget.getTimeout());
			e.put(ATTR_END_TASK_TARGET, targetObj2);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		JSONArray masksJsonArr = obj.optJSONArray(ATTR_MASKKEYS);
		int[] ids = new int[masksJsonArr.length()];
		for (int i = 0; i < masksJsonArr.length(); i++) {
			ids[i] = masksJsonArr.optInt(i);
		}
		this.maskKeys = ids;
		JSONObject delayObj = obj.optJSONObject(ATTR_DELAY);
		this.getDelay().setBefore(delayObj.optString(ATTR_DELAYBEFORE));
		this.attach = obj.optBoolean(ATTR_ATTACH);
		this.getDelay().setAfter(delayObj.optString(ATTR_DELAYAFTER));
		JSONObject positionObj1 = obj.optJSONObject(ATTR_START_CURSOR_POSITION);
		this.getStartCursorPosition().setPosition(Position.valueOf(positionObj1.optInt(ATTR_START_POSITION)));
		this.getStartCursorPosition().setOffsetX(positionObj1.optString(ATTR_START_X_OFFSET));
		this.getStartCursorPosition().setOffsetY(positionObj1.optString(ATTR_START_Y_OFFSET));
		JSONObject positionObj2 = obj.optJSONObject(ATTR_END_CURSOR_POSITION);
		this.getEndCursorPosition().setPosition(Position.valueOf(positionObj2.optInt(ATTR_END_POSITION)));
		this.getEndCursorPosition().setOffsetX(positionObj2.optString(ATTR_END_X_OFFSET));
		this.getEndCursorPosition().setOffsetY(positionObj2.optString(ATTR_END_Y_OFFSET));

		JSONObject targetObj1 = obj.optJSONObject(ATTR_START_TASK_TARGET);
		this.getStartTaskTarget().setTimeout(targetObj1.optString(ATTR_START_TARGET_TIMEOUT));
		if (targetObj1.has(ATTR_START_TARGET_ELEMENT)) {
			this.getStartTaskTarget().setElement(targetObj1.optString(ATTR_START_TARGET_ELEMENT));
		}
		if (targetObj1.has(ATTR_START_TARGET_PATH)) {
			Path path = new Path();
			try {
				path.fromJson(new JSONObject(targetObj1.optString(ATTR_START_TARGET_PATH)));
				this.getStartTaskTarget().setPath(path);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		JSONObject targetObj2 = obj.optJSONObject(ATTR_END_TASK_TARGET);
		this.getEndTaskTarget().setTimeout(targetObj2.optString(ATTR_END_TARGET_TIMEOUT));
		if (targetObj2.has(ATTR_END_TARGET_ELEMENT)) {
			this.getEndTaskTarget().setElement(targetObj2.optString(ATTR_END_TARGET_ELEMENT));
		}
		if (targetObj2.has(ATTR_END_TARGET_PATH)) {
			Path path = new Path();
			try {
				path.fromJson(new JSONObject(targetObj2.optString(ATTR_END_TARGET_PATH)));
				this.getEndTaskTarget().setPath(path);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	

	public Boolean getAttach() {
		return attach;
	}

	public void setAttach(Boolean attach) {
		this.attach = attach;
	}

	public CursorPosition getStartCursorPosition() {
		return startCursorPosition;
	}

	public CursorPosition getEndCursorPosition() {
		return endCursorPosition;
	}

	public TaskTarget getStartTaskTarget() {
		return startTaskTarget;
	}

	public TaskTarget getEndTaskTarget() {
		return endTaskTarget;
	}

	public Delay getDelay() {
		return delay;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}

	@Override
	public String getId() {
		return MouseDragTaskFactory.ID;
	}

	public MouseDragTaskModel clone() {
		MouseDragTaskModel cloned = (MouseDragTaskModel) super.clone();
		cloned.delay = new Delay();
		cloned.delay.setBefore(delay.getBefore());
		cloned.delay.setAfter(delay.getAfter());
		cloned.startTaskTarget = new TaskTarget();
		cloned.startTaskTarget.setTimeout(startTaskTarget.getTimeout());
		cloned.startTaskTarget.setElement(startTaskTarget.getElement());
		cloned.startTaskTarget.setPath(startTaskTarget.getPath());
		cloned.endTaskTarget = new TaskTarget();
		cloned.endTaskTarget.setTimeout(endTaskTarget.getTimeout());
		cloned.endTaskTarget.setElement(endTaskTarget.getElement());
		cloned.endTaskTarget.setPath(endTaskTarget.getPath());
		cloned.startCursorPosition = new CursorPosition();
		cloned.startCursorPosition.setPosition(startCursorPosition.getPosition());
		cloned.startCursorPosition.setOffsetX(startCursorPosition.getOffsetX());
		cloned.startCursorPosition.setOffsetY(startCursorPosition.getOffsetY());
		cloned.endCursorPosition = new CursorPosition();
		cloned.endCursorPosition.setPosition(endCursorPosition.getPosition());
		cloned.endCursorPosition.setOffsetX(endCursorPosition.getOffsetX());
		cloned.endCursorPosition.setOffsetY(endCursorPosition.getOffsetY());
		return cloned;
	}
	
	@Override
	public boolean validate(IProblems problems, IModel parent) {
		boolean result =  super.validate(problems, parent);
		if(startTaskTarget.getPath()==null && StringUtils.isEmpty(startTaskTarget.getElement())) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置开始操作对象");
			problems.add(p);
			result &= false;
		} 
		if(endTaskTarget.getPath()==null && StringUtils.isEmpty(endTaskTarget.getElement())) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置结束操作对象");
			problems.add(p);
			result &= false;
		} 
		return result;
	}
}
