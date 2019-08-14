package com.jiuqi.rpa.action.find.findrelative;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * 活动：查找关联元素
 * 
 * @author liangxiao01
 */
public class FindRelativeAction extends Action {
	private FindRelativeInput input;

	/**
	 * 构造器
	 * 
	 * @param input
	 *            活动输入
	 */
	public FindRelativeAction(FindRelativeInput input) {
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
			FindLibraryManager manager = new FindLibraryManager(getContext());
			
			Point p = getRelationPoint(manager.getRect(uiElement), input.getCursorPosition());
			IUIElement relativeElement = manager.get(p);
			FindRelativeOutput output = new FindRelativeOutput();
			output.setElement(relativeElement);
			return output;
		} catch (LibraryException e) {
			throw new ActionException("查找关联元素活动异常", e);
		}
	}

	private Point getRelationPoint(Rect rect, CursorPosition position) {
		switch (position.getPosition()) {
		case CENTER:
			return new Point(rect.x + rect.w / 2 + position.getOffsetX(), rect.y + rect.h / 2 + position.getOffsetY());
		case TOPLEFT:
			return new Point(rect.x + position.getOffsetX(), rect.y + position.getOffsetY());
		case TOPRIGHT:
			return new Point(rect.x + rect.w + position.getOffsetX(), rect.y + position.getOffsetY());
		case BOTTOMLEFT:
			return new Point(rect.x + position.getOffsetX(), rect.y + rect.h + position.getOffsetY());
		case BOTTOMRIGHT:
			return new Point(rect.x + rect.w + position.getOffsetX(), rect.y + rect.h + position.getOffsetY());
		default:
			return new Point(rect.x + rect.w / 2 + position.getOffsetX(), rect.y + rect.h / 2 + position.getOffsetY());
		}

	}
}
