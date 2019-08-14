package com.jiuqi.rpa.action.window.move;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.UIAElement;

public class WindowMoveAction extends Action {
	private WindowMoveInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public WindowMoveAction(WindowMoveInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Target target = input.getTarget();
		IUIHandler uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);
		if (uiHandler == null)
			throw new ActionException("未找到操作目标");


		if (!(uiHandler instanceof UIAElement))
			throw new ActionException("操作目标不适用此操作");

		UIAElement uiElement = (UIAElement) uiHandler;
		try {
			uiElement.moveWindow(input.getRect());
		} catch (LibraryException e) {
			throw new ActionException("移动窗口异常");
		}
		return null;
	}

}
