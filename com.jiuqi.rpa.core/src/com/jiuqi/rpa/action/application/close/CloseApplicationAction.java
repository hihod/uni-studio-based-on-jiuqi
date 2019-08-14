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
 * 活动：关闭应用
 * 
 * @author liangxiao01
 */
public class CloseApplicationAction extends Action {
	private CloseApplicationInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
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
			throw new ActionException("关闭活动异常", e);
		}
		
		return new CloseApplicationOutput();
	}

}
