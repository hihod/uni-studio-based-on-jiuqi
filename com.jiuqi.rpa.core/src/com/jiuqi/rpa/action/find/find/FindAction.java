package com.jiuqi.rpa.action.find.find;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * ���Ԫ�ز���
 * 
 * @author liangxiao01
 */
public class FindAction extends Action {
	private FindInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public FindAction(FindInput input) {
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
		FindOutput output = new FindOutput();
		output.setElement(uiElement);
		return output;
	}

}
