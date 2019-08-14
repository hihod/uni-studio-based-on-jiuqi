package com.jiuqi.rpa.action.find.findrelative;

import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.Target;

/**
 * 
 * @author liangxiao01
 */
public class FindRelativeInput implements IActionInput {
	private Target target;
	private CursorPosition cursorPosition;

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public CursorPosition getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(CursorPosition cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

}
