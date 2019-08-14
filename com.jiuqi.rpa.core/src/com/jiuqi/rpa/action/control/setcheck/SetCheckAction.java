package com.jiuqi.rpa.action.control.setcheck;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * ������ù�ѡ
 * 
 * @author liangxiao01
 */
public class SetCheckAction extends Action {
	private SetCheckInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public SetCheckAction(SetCheckInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Target target = input.getTarget();
		IUIHandler uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);
		if (uiHandler == null)
			throw new ActionException("δ�ҵ�����Ŀ��");

		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("����Ŀ�겻���ô˲���");

		IUIElement uiElement = (IUIElement) uiHandler;
		try {
			uiElement.setChecked(input.getCheck());
		} catch (LibraryException e) {
			throw new ActionException("���ù�ѡ��쳣", e);
		}
		return null;
	}

}
