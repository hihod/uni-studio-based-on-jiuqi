package com.jiuqi.rpa.action.mouse.hover;

import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IDelayable;
import com.jiuqi.rpa.action.Target;

/**
 * 活动输入：鼠标hover
 * 
 * @author liangxiao01
 */
public class MouseHoverInput implements IActionInput, IDelayable {
	private Target target;
	private Delay delay;
	private CursorPosition cursorPosition;

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

}
