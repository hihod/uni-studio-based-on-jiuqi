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
	 * ������
	 * 
	 * @param input �����
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
			throw new ActionException("δ�ҵ�����Ŀ��");

		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("����Ŀ�겻���ô˲���");

		IUIElement uiElement = (IUIElement) uiHandler;
		try {
			uiElement.scrollIntoView();

			Rect rect = new FindLibraryManager(getContext()).getRect(uiElement);
			mouseLibrary.mouseMove(CursorPosition.directPoint(rect, input.getCursorPosition()));
		} catch (LibraryException e) {
			throw new ActionException("���������쳣", e);
		}
		return null;
	}

}
