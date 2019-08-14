package com.jiuqi.rpa.action.window.show;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.UIAElement;
import com.jiuqi.rpa.lib.window.WindowState;

public class WindowShowAction extends Action {
	private WindowShowInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public WindowShowAction(WindowShowInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Target target = input.getTarget();
		IUIHandler uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);
		if (uiHandler == null)
			throw new ActionException("δ�ҵ�����Ŀ��");

		if (!(uiHandler instanceof UIAElement))
			throw new ActionException("����Ŀ�겻���ô˲���");

		UIAElement uiElement = (UIAElement) uiHandler;
		try {
			uiElement.sendWindowState(WindowState.SHOW);
		} catch (LibraryException e) {
			throw new ActionException("��ʾ�����쳣");
		}
		return null;
	}

}
