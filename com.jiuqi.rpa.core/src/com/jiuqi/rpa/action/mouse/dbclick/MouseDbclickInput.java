package com.jiuqi.rpa.action.mouse.dbclick;

import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.mouse.ClickMode;

/**
 * @author liangxiao01
 */
public class MouseDbclickInput implements IActionInput, IDelayable {
	private Target target;
	private Delay delay;
	private CursorPosition cursorPosition;
	private ClickMode clickMode = ClickMode.NORMAL_CLICK;
	private int[] maskKeys = {};

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Delay getDelay() {
		return delay;
	}

	public void setDelay(Delay delay) {
		this.delay = delay;
	}

	public CursorPosition getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(CursorPosition cursorPosition) {
		this.cursorPosition = cursorPosition;
	}


	public int[] getMaskKeys() {
		return maskKeys;
	}

	public void setMaskKeys(int[] maskKeys) {
		this.maskKeys = maskKeys;
	}

	public ClickMode getClickMode() {
		return clickMode;
	}

	public void setClickMode(ClickMode clickMode) {
		this.clickMode = clickMode;
	}

}
