package com.jiuqi.rpa.action.extract.getposition;

import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.Target;

public class GetPositionInput implements IActionInput {

	private Target target;
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	
}
