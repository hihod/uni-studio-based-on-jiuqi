package com.jiuqi.rpa.action.find.find;

import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.IUIHandler;

public class FindOutput implements IActionOutput {
	private IUIHandler element;

	public IUIHandler getElement() {
		return element;
	}

	public void setElement(IUIHandler element) {
		this.element = element;
	}

}
