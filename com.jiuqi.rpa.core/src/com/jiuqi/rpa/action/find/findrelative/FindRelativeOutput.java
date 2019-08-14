package com.jiuqi.rpa.action.find.findrelative;

import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.IUIHandler;

public class FindRelativeOutput implements IActionOutput {
	private IUIHandler element;

	public IUIHandler getElement() {
		return element;
	}

	public void setElement(IUIHandler element) {
		this.element = element;
	}
}
