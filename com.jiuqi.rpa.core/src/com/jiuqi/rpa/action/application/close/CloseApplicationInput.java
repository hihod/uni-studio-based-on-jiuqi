package com.jiuqi.rpa.action.application.close;

import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.Target;

public class CloseApplicationInput implements IActionInput {
	private Target target;

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}
}
