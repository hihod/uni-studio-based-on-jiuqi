package com.jiuqi.rpa.action.control.getcheck;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.action.control.CheckState;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * �����ȡ��ѡ
 * 
 * @author liangxiao01
 */
public class GetCheckAction extends Action {
	private GetCheckInput input;

	/**
	 * ������
	 * 
	 * @param input
	 *            �����
	 */
	public GetCheckAction(GetCheckInput input) {
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
		GetCheckOutput output = new GetCheckOutput();
		try {

			output.setCheck(uiElement.isChecked());
			output.setCheckState(CheckState.valueOf(uiElement.getCheckState()));
		} catch (LibraryException e) {
			throw new ActionException("��ȡ��ѡ��쳣", e);
		}
		return output;
	}

}
