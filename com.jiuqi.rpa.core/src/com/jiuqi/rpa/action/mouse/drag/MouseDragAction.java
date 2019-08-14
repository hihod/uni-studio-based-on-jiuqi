package com.jiuqi.rpa.action.mouse.drag;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.action.VirtualKey;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.mouse.MouseClickType;
import com.jiuqi.rpa.lib.mouse.UIAMouseLibrary;

public class MouseDragAction extends Action {
	private MouseDragInput input;

	/**
	 * ������
	 * 
	 * @param input
	 *            �����
	 */
	public MouseDragAction(MouseDragInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Target start = input.getStartTarget();
		UIAMouseLibrary mouseLibrary = new UIAMouseLibrary();
		IUIHandler uiStartHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), start);
		if (uiStartHandler == null)
			throw new ActionException("δ�ҵ����Ŀ��");

		if (!(uiStartHandler instanceof IUIElement))
			throw new ActionException("����Ŀ�겻���ô˲���");
		Target end = input.getEndTarget();
		IUIHandler uiEndHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), end);
		if (uiEndHandler == null)
			throw new ActionException("δ�ҵ��յ�Ŀ��");

		if (!(uiEndHandler instanceof IUIElement))
			throw new ActionException("����Ŀ�겻���ô˲���");
		
		IUIElement uiStartElement = (IUIElement) uiStartHandler;
		IUIElement uiEndElement = (IUIElement) uiEndHandler;
		try {
			FindLibraryManager findLibraryManager = new FindLibraryManager(getContext());
			Rect startRect = findLibraryManager.getRect(uiStartElement);
			Rect endRect = findLibraryManager.getRect(uiEndElement);
			
			Point startPoint = CursorPosition.directPoint(startRect, input.getStartCursorPosition());
			Point endPoint = CursorPosition.directPoint(endRect, input.getEndCursorPosition());
			try {
				mouseLibrary.mouseMove(startPoint);
				mouseLibrary.mouseClick(MouseClickType.DOWN, VirtualKey.VK_LBUTTON, input.getMaskKeys());
				Thread.sleep(100);
				mouseLibrary.mouseMove(endPoint);
				Thread.sleep(100);
				mouseLibrary.mouseClick(MouseClickType.UP, VirtualKey.VK_LBUTTON, input.getMaskKeys());
			} catch (InterruptedException e) {
				throw new LibraryException(e);
			}
		} catch (LibraryException e) {
			throw new ActionException("�����ק��쳣", e);
		}

		return null;

	}

}
