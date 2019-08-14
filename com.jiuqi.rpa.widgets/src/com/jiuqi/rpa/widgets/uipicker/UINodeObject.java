package com.jiuqi.rpa.widgets.uipicker;

import com.jiuqi.rpa.lib.find.IUIElement;

public 	class UINodeObject {
	private Object parent;
	private IUIElement uiElement;
	private Object[] childern = null;

	public UINodeObject(Object parent, IUIElement uiElement) {
		this.parent = parent;
		this.uiElement = uiElement;
	}

	public Object getParent() {
		return parent;
	}

	public IUIElement getUiElement() {
		return uiElement;
	}

	public Object[] getChildern() {
		return childern;
	}

	public void setChildern(Object[] childern) {
		this.childern = childern;
	}
}