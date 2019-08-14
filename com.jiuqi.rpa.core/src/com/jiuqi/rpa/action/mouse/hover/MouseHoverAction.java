package com.jiuqi.rpa.action.mouse.hover;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.mouse.UIAMouseLibrary;

public class MouseHoverAction extends Action {
	private MouseHoverInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public MouseHoverAction(MouseHoverInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Target target = input.getTarget();
		UIAMouseLibrary mouseLibrary = new UIAMouseLibrary();
		IUIHandler uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);
		if (uiHandler == null)
			throw new ActionException("未找到操作目标");

		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("操作目标不适用此操作");

		IUIElement uiElement = (IUIElement) uiHandler;
		try {
			uiElement.scrollIntoView();

			Rect rect = new FindLibraryManager(getContext()).getRect(uiElement);
			mouseLibrary.mouseMove(CursorPosition.directPoint(rect, input.getCursorPosition()));
		} catch (LibraryException e) {
			throw new ActionException("鼠标悬浮活动异常", e);
		}
		return null;
	}

}
