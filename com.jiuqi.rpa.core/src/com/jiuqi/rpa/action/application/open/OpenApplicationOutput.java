package com.jiuqi.rpa.action.application.open;

import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.IUIHandler;

/**
 * 
 * @author liangxiao01
 */
public class OpenApplicationOutput implements IActionOutput {
	private IUIHandler element;

	public IUIHandler getElement() {
		return element;
	}

	public void setElement(IUIHandler element) {
		this.element = element;
	}

}
