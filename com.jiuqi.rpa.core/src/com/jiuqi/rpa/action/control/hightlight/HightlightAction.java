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
 * �����ȡ��ѡ
 * 
 * @author liangxiao01
 */
public class HightlightAction extends Action {
	private HightlightInput input;

	/**
	 * ������
	 * 
	 * @param input �����
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
			throw new ActionException("δ�ҵ�����Ŀ��");

		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("����Ŀ�겻���ô˲���");

		try {
			drawerLibrary.endDraw();

			IUIElement uiElement = (IUIElement) uiHandler;
			
			//��ʼ����
			drawerLibrary.startDraw(findLibraryManager.getRect(uiElement), input.getColor());
			try {
				Thread.sleep(input.getTime());
			} finally {
				//��������
				drawerLibrary.endDraw();
			}
		} catch (LibraryException e) {
			throw new ActionException("���ø�����쳣", e);
		} catch (InterruptedException e) {
			throw new ActionException("���ø�����ж��쳣", e);
		}
		return null;
		
	}

}
