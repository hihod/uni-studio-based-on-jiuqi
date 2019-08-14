package com.jiuqi.rpa.action.window.move;

import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.lib.Rect;

/**
 * @author liangxiao01
 */
public class WindowMoveInput implements IActionInput {
	private Target target;
	private Rect rect = new Rect();
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}
}
