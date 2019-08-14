package com.jiuqi.rpa.action.extract.getposition;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * �����ȡλ��
 * 
 * @author liangxiao01
 */
public class GetPositionAction extends Action {
	private GetPositionInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public GetPositionAction(GetPositionInput input) {
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
		GetPositionOutput output = new GetPositionOutput(); 
		try {
			output.setRect(uiElement.getRect());
		} catch (LibraryException e) {
			throw new ActionException("��ȡ���Ի�쳣", e);
		}
		return output;
	}

}
