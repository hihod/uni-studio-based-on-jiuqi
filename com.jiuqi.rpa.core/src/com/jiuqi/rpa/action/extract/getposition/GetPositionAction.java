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
 * 活动：获取位置
 * 
 * @author liangxiao01
 */
public class GetPositionAction extends Action {
	private GetPositionInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
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
			throw new ActionException("未找到操作目标");

		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("操作目标不适用此操作");

		IUIElement uiElement = (IUIElement) uiHandler;
		GetPositionOutput output = new GetPositionOutput(); 
		try {
			output.setRect(uiElement.getRect());
		} catch (LibraryException e) {
			throw new ActionException("获取属性活动异常", e);
		}
		return output;
	}

}
