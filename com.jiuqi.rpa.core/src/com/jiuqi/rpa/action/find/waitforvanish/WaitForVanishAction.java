package com.jiuqi.rpa.action.find.waitforvanish;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.Path;

/**
 * 活动：等待元素消失元素
 * 
 * @author liangxiao01
 */
public class WaitForVanishAction extends Action {
	private WaitForVanishInput input;

	/**
	 * 构造器
	 * 
	 * @param input
	 *            活动输入
	 */
	public WaitForVanishAction(WaitForVanishInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		try {
			IUIHandler uiHandler = input.getTarget().getElement();
			
			//通过指定元素判断
			if (uiHandler == null)
				return WaitVanishByPath(input.getTarget().getPath());
			//通过指定路径判断
			else
				return waitVanishByElement(uiHandler);
		} catch (InterruptedException e) {
			throw new ActionException("活动流程中断", e);
		} catch (LibraryException e) {
			throw new ActionException("活动异常", e);
		}
	}

	private WaitForVanishOutput waitVanishByElement(IUIHandler uiHandler) throws ActionException, LibraryException, InterruptedException {
		WaitForVanishOutput output = new WaitForVanishOutput();
		
		while (!getContext().isInterrupted()) {
			if (!(uiHandler instanceof IUIElement))
				throw new ActionException("操作目标不适用此操作");
			
			IUIElement uiElement = (IUIElement) uiHandler;
			//不可用
			if (input.isWaitNotActive() && !uiElement.enable())
				return output;
			
			//不可见
			if (input.isWaitNotVisible() && !uiElement.visible())
				return output;
			
			Thread.sleep(1000);
		}
		return output;
	}

	private WaitForVanishOutput WaitVanishByPath(Path path) throws ActionException, LibraryException, InterruptedException {
		WaitForVanishOutput output = new WaitForVanishOutput();
		FindLibraryManager manager = new FindLibraryManager(getContext());
		
		while (!getContext().isInterrupted()) {
			if (path == null)
				throw new ActionException("未提供查找路径");
			
			//不存在
			if (!manager.exists(path))
				return output;
			
			//查找目标元素
			int timeout = input.getTarget().getTimeout();
			IUIHandler uiHandler = new TargetFinder().getUIHandler(getContext(), path, timeout);
			if (uiHandler == null)
				throw new ActionException("未找到目标元素");
			
			//判断是否可用可见
			try {
				if (!(uiHandler instanceof IUIElement))
					throw new ActionException("操作目标不适用此操作");
				
				IUIElement uiElement = (IUIElement) uiHandler;
				//不可用
				if (input.isWaitNotActive() && !uiElement.enable())
					return output;
				//不可见
				if (input.isWaitNotVisible() && !uiElement.visible())
					return output;
			} finally {
				uiHandler.release();
			}
			
			Thread.sleep(1000);
		}
		return output;
	}

}
