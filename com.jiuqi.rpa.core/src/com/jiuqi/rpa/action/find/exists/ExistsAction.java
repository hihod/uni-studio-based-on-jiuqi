package com.jiuqi.rpa.action.find.exists;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * ���Ԫ�ش���
 * 
 * @author liangxiao01
 */
public class ExistsAction extends Action {
	private ExistsInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public ExistsAction(ExistsInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() {
		Target target = input.getTarget();
		
		IUIHandler uiHandler = null;
		try {
			uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);
		} catch (ActionException e1) {
			//��ʱ ��������
			e1.printStackTrace();
		}
		if (uiHandler == null)
			return new ExistsOutput();
		IUIElement uiElement = (IUIElement) uiHandler;
		ExistsOutput output = new ExistsOutput();
		FindLibraryManager findLibraryManager  = new FindLibraryManager(getContext());		
		try {
			output.setExist(findLibraryManager.exists(uiElement.getPath()));
		} catch (LibraryException e) {
			e.printStackTrace();
			return output;
		}
		return output;
	}

}
