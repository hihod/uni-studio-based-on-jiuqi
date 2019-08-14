package com.jiuqi.rpa.action.extract.getattribute;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * �����ȡ����
 * 
 * @author liangxiao01
 */
public class GetAttributeAction extends Action {
	private GetAttributeInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public GetAttributeAction(GetAttributeInput input) {
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
		GetAttributeOutput output = new GetAttributeOutput(); 
		try {
			output.setValue(uiElement.getAttributeValue(input.getAttributeName()));
		} catch (LibraryException e) {
			throw new ActionException("��ȡ���Ի�쳣", e);
		}
		return output;
	}

}
