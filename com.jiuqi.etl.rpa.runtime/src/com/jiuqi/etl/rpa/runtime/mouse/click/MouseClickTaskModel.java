package com.jiuqi.etl.rpa.runtime.mouse.click;

import org.json.JSONArray;
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
import com.jiuqi.etl.rpa.runtime.Delay;
import com.jiuqi.etl.rpa.runtime.TaskTarget;
import com.jiuqi.rpa.action.Position;
import com.jiuqi.rpa.action.VirtualKey;
import com.jiuqi.rpa.action.mouse.ClickMode;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.mouse.MouseClickType;

/**
 * 鼠标单击任务模型
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class MouseClickTaskModel extends TaskModel {
	private CursorPosition cursorPosition = new CursorPosition();
	private Delay delay = new Delay();
	private TaskTarget taskTarget = new TaskTarget();
	private MouseClickType clickType = MouseClickType.SINGLE;
	private ClickMode clickMode = ClickMode.NORMAL_CLICK;
	private int mouseKey = VirtualKey.VK_LBUTTON;
	private int[] maskKeys = {};
	private Boolean attach = true;
	private final String ATTR_CLICKTYPE = "CLICK_TYPE";
	private final String ATTR_MASKKEYS = "MASKKEYS";
	private final String ATTR_MOUSEKEY = "MOUSE_KEY";
	private final String ATTR_CLICKMODE = "CLICK_MODE";
	private final String ATTR_DELAY = "DELAY";
	private final String ATTR_DELAYBEFORE = "DELAY_BEFORE";
	private final String ATTR_DELAYAFTER = "DELAY_AFTER";
	private final String ATTR_CURSOR_POSITION = "CURSOE_POSITION";
	private final String ATTR_POSITION = "POSITION";
	private final String ATTR_X_OFFSET = "X_OFFSET";
	private final String ATTR_Y_OFFSET = "Y_OFFSET";
	private final String ATTR_TASK_TARGET = "TASK_TARGET";
	private final String ATTR_TARGET_ELEMENT = "TARGET_ELEMENT";
	private final String ATTR_TARGET_PATH = "TARGET_PATH";
	private final String ATTR_TARGET_TIMEOUT = "TARGET_TIMEOUT";
	private final String ATTR_ATTACH = "ATTACH_ELEMENT";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_CLICKTYPE, clickType.value());
			e.putOpt(ATTR_MASKKEYS, maskKeys);
			e.putOpt(ATTR_MOUSEKEY, mouseKey);
			e.putOpt(ATTR_CLICKMODE, clickMode.value());
			e.putOpt(ATTR_ATTACH, attach);
			JSONObject delayObj = new JSONObject();
			delayObj.putOpt(ATTR_DELAYBEFORE, delay.getBefore());
			delayObj.putOpt(ATTR_DELAYAFTER, delay.getAfter());
			e.put(ATTR_DELAY, delayObj);
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
		this.clickType = MouseClickType.valueOf(obj.optInt(ATTR_CLICKTYPE));
		this.clickMode = ClickMode.valueOf(obj.optInt(ATTR_CLICKMODE));
		this.mouseKey = obj.optInt(ATTR_MOUSEKEY);
		this.attach = obj.optBoolean(ATTR_ATTACH);
		JSONArray masksJsonArr = obj.optJSONArray(ATTR_MASKKEYS);
		int[] ids = new int[masksJsonArr.length()];
		for (int i = 0; i < masksJsonArr.length(); i++) {
			ids[i] = masksJsonArr.optInt(i);
		}
		this.maskKeys = ids;
		JSONObject delayObj = obj.optJSONObject(ATTR_DELAY);
		this.getDelay().setBefore(delayObj.optString(ATTR_DELAYBEFORE));
		this.getDelay().setAfter(delayObj.optString(ATTR_DELAYAFTER));
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

	public CursorPosition getCursorPosition() {
		return cursorPosition;
	}

	public Delay getDelay() {
		return delay;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}
	
	public void setClickType(MouseClickType clickType) {
		this.clickType = clickType;
	}

	public void setMouseKey(int mouseKey) {
		this.mouseKey = mouseKey;
	}

	public void setMaskKeys(int[] maskKeys) {
		this.maskKeys = maskKeys;
	}


	public Boolean getAttach() {
		return attach;
	}

	public void setAttach(Boolean attach) {
		this.attach = attach;
	}

	public TaskTarget getTaskTarget() {
		return taskTarget;
	}

	public MouseClickType getClickType() {
		return clickType;
	}

	
	public int getMouseKey() {
		return mouseKey;
	}

	public int[] getMaskKeys() {
		return maskKeys;
	}

	public ClickMode getClickMode() {
		return clickMode;
	}

	public void setClickMode(ClickMode clickMode) {
		this.clickMode = clickMode;
	}

	@Override
	public String getId() {
		return MouseClickTaskFactory.ID;
	}

	public MouseClickTaskModel clone() {
		MouseClickTaskModel cloned = (MouseClickTaskModel) super.clone();
		cloned.delay = new Delay();
		cloned.delay.setBefore(delay.getBefore());
		cloned.delay.setAfter(delay.getAfter());
		cloned.taskTarget = new TaskTarget();
		cloned.taskTarget.setTimeout(taskTarget.getTimeout());
		cloned.taskTarget.setElement(taskTarget.getElement());
		cloned.taskTarget.setPath(taskTarget.getPath());
		cloned.cursorPosition = new CursorPosition();
		cloned.cursorPosition.setPosition(cursorPosition.getPosition());
		cloned.cursorPosition.setOffsetX(cursorPosition.getOffsetX());
		cloned.cursorPosition.setOffsetY(cursorPosition.getOffsetY());

		return cloned;
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
