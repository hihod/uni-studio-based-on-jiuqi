package com.jiuqi.rpa.action.control.settext;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * ��������ı�
 * 
 * @author liangxiao01
 */
public class SetTextAction extends Action {
	private SetTextInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public SetTextAction(SetTextInput input) {
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
			uiElement.setText(input.getText());
		} catch (LibraryException e) {
			throw new ActionException("��ȡ�ı���쳣", e);
		}
		return null;
	}

}
