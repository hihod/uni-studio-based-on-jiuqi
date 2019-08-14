package com.jiuqi.rpa.action.extract.getattribute;

import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.Target;

public class GetAttributeInput implements IActionInput {

	private Target target;
	private String attributeName;
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
}
