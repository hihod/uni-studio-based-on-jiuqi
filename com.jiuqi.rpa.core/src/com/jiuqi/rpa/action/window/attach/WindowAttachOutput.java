package com.jiuqi.rpa.action.window.attach;

import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.IUIHandler;

public class WindowAttachOutput implements IActionOutput {
	private IUIHandler element;

	public IUIHandler getElement() {
		return element;
	}

	public void setElement(IUIHandler element) {
		this.element = element;
	}
}
