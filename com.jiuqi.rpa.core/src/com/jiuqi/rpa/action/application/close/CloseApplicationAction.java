package com.jiuqi.rpa.action.application.close;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.application.UIAApplicationLibary;

/**
 * ����ر�Ӧ��
 * 
 * @author liangxiao01
 */
public class CloseApplicationAction extends Action {
	private CloseApplicationInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public CloseApplicationAction(CloseApplicationInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		try {
			UIAApplicationLibary library = new UIAApplicationLibary(getContext());
			
			Target target = input.getTarget();
			IUIHandler element = new TargetFinder().getUIHandler(getContext(), target);
			library.closeApplication(element.getId());
		} catch (LibraryException e) {
			throw new ActionException("�رջ�쳣", e);
		}
		
		return new CloseApplicationOutput();
	}

}
