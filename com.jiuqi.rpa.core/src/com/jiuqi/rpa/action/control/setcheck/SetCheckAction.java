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
 * 活动：设置勾选
 * 
 * @author liangxiao01
 */
public class SetCheckAction extends Action {
	private SetCheckInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
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
			throw new ActionException("未找到操作目标");

		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("操作目标不适用此操作");

		IUIElement uiElement = (IUIElement) uiHandler;
		try {
			uiElement.setChecked(input.getCheck());
		} catch (LibraryException e) {
			throw new ActionException("设置勾选活动异常", e);
		}
		return null;
	}

}
