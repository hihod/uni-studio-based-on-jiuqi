package com.jiuqi.rpa.action.control.hightlight;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.drawer.UIADrawerLibrary;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * 活动：获取勾选
 * 
 * @author liangxiao01
 */
public class HightlightAction extends Action {
	private HightlightInput input;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public HightlightAction(HightlightInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Target target = input.getTarget();
		UIADrawerLibrary drawerLibrary = new UIADrawerLibrary();
		FindLibraryManager findLibraryManager = new FindLibraryManager(getContext());
		IUIHandler uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);
		if (uiHandler == null)
			throw new ActionException("未找到操作目标");

		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("操作目标不适用此操作");

		try {
			drawerLibrary.endDraw();

			IUIElement uiElement = (IUIElement) uiHandler;
			
			//开始高亮
			drawerLibrary.startDraw(findLibraryManager.getRect(uiElement), input.getColor());
			try {
				Thread.sleep(input.getTime());
			} finally {
				//结束高亮
				drawerLibrary.endDraw();
			}
		} catch (LibraryException e) {
			throw new ActionException("设置高亮活动异常", e);
		} catch (InterruptedException e) {
			throw new ActionException("设置高亮活动中断异常", e);
		}
		return null;
		
	}

}
