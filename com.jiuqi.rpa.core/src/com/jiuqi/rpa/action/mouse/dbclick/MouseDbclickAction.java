package com.jiuqi.rpa.action.mouse.dbclick;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.action.mouse.ClickMode;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.UIAElement;
import com.jiuqi.rpa.lib.find.WEBElement;
import com.jiuqi.rpa.lib.mouse.MouseClickType;
import com.jiuqi.rpa.lib.mouse.UIAMouseLibrary;

public class MouseDbclickAction extends Action {
	public MouseDbclickAction(IActionInput input) {
		super(input);
	}

	private MouseDbclickInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public MouseDbclickAction(MouseDbclickInput input) {
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
			if (uiElement instanceof WEBElement) {
				WEBElement webElement = (WEBElement) uiElement;
				
				if (input.getClickMode() == ClickMode.NORMAL_CLICK) {
					uiElement.scrollIntoView();
					Rect rect = new FindLibraryManager(getContext()).getRect(uiElement);
					Point point = CursorPosition.directPoint(rect, input.getCursorPosition());
					
					UIAMouseLibrary uiaMouseLibrary = new UIAMouseLibrary();
					uiaMouseLibrary.mouseMove(point);
					uiaMouseLibrary.mouseClick(MouseClickType.DOUBLE, 0x01, input.getMaskKeys());
				} else if (input.getClickMode() == ClickMode.SIMULATE_CLICK) {
					webElement.simulateClick(MouseClickType.DOUBLE, 0x01);
				}
			} else {
				UIAElement uiaElement = (UIAElement) uiElement;
				if (input.getClickMode() == ClickMode.NORMAL_CLICK) {
					Point screenPoint = CursorPosition.directPoint(uiaElement.getRect(), input.getCursorPosition());
					mouseLibrary.mouseMove(screenPoint);
					mouseLibrary.mouseClick(MouseClickType.DOUBLE, 0x01, input.getMaskKeys());
				} else if (input.getClickMode() == ClickMode.SIMULATE_CLICK) {
					uiaElement.simulateClick();
				} else if (input.getClickMode() == ClickMode.MESSAGE_CLICK) {
					Rect rect = uiaElement.getRect();
					Point controlPoint = new Point(rect.w/2, rect.h/2);
					uiaElement.messageClick(controlPoint, MouseClickType.DOUBLE, 0x01, input.getMaskKeys());
				}
			}
		} catch (LibraryException e) {
			throw new ActionException("元素双击活动异常", e);
		}

		System.out.println(input);
		return null;
	}
	

}
