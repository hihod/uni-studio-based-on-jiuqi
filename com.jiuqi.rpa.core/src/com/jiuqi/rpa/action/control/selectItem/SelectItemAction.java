package com.jiuqi.rpa.action.control.selectItem;

import java.io.UnsupportedEncodingException;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * 活动：单选条目
 * 
 * @author liangxiao01
 */
public class SelectItemAction extends Action {
	private SelectItemInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public SelectItemAction(SelectItemInput input) {
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
			uiElement.scrollIntoView();
			try {
				uiElement.selectItems(new String[]{new String(input.getItem().getBytes(),"GBK")});
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (LibraryException e) {
			throw new ActionException("单选条目活动异常", e);
		}
		return null;
		
	}

}
