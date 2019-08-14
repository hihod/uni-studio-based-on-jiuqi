package com.jiuqi.rpa.action.browser.setwebattribute;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.WEBElement;

public class SetWebAttributeAction extends Action {
	private SetWebAttributeInput input;
	private SetWebAttributeOutput output;

	public SetWebAttributeAction(SetWebAttributeInput input) {
		super(input);
		this.input = input;
		this.output = new SetWebAttributeOutput();
	}

	protected IActionOutput run() throws ActionException {
		Target target = input.getTarget();
		IUIHandler uiHandler = (IUIHandler) new TargetFinder().getUIHandler(getContext(), target);
		if (uiHandler == null)
			throw new ActionException("未找到操作目标");

		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("操作目标不适用此操作");

		IUIElement uiElement = (IUIElement) uiHandler;
		if (uiElement instanceof WEBElement) {
			WEBElement webElement = (WEBElement)uiElement;
			try {
				webElement.setWebAttributeValue(input.getAttributeName(),input.getAttributeValue());
			} catch (LibraryException e) {
				e.printStackTrace();
				throw new ActionException(e);
			}
		}else{
			throw new ActionException("操作目标不是WEB元素，不支持此操作");
		}
		return output;
	}

}
