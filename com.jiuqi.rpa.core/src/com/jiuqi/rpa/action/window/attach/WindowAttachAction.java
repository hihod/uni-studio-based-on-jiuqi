package com.jiuqi.rpa.action.window.attach;
import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.find.UIAElement;
import com.jiuqi.rpa.lib.find.WEBElement;

public class WindowAttachAction extends Action {
	private WindowAttachInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public WindowAttachAction(WindowAttachInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Path path = input.getPath();
		int timeout = input.getTimeout();
		
		IUIHandler uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), path, timeout);
		if (uiHandler == null)
			throw new ActionException("未找到操作目标");

		if (!(uiHandler instanceof UIAElement)) {
			if(uiHandler instanceof WEBElement) {
				FindLibraryManager flm = new FindLibraryManager(getContext());
				try {
					flm.getWindow((WEBElement)uiHandler);
				} catch (LibraryException e) {
					throw new ActionException("根据WEB元素获取浏览器窗口失败",e);
				}
			}
			throw new ActionException("操作目标不适用此操作");
		}

		IUIElement uiElement = (IUIElement) uiHandler;
		try {
			uiElement.setFocus();
		} catch (LibraryException e) {
			e.printStackTrace();
		}
		WindowAttachOutput output = new WindowAttachOutput();
		output.setElement(uiElement);
		return output;
	}

}
