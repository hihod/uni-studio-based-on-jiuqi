package com.jiuqi.rpa.action.window.maximize;

import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.Target;

/**
 * @author liangxiao01
 */
public class WindowMaximizeInput implements IActionInput {
	private Target target;

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}
}
