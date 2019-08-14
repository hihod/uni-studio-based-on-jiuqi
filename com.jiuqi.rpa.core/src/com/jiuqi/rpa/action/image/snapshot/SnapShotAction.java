package com.jiuqi.rpa.action.image.snapshot;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.image.UIAImageLibary;

/**
 * �������
 * 
 * @author liangxiao01
 */
public class SnapShotAction extends Action {
	private SnapShotInput input;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public SnapShotAction(SnapShotInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		Target target = input.getTarget();
		Rect rect = null;
		IUIHandler uiHandler = null;
		try {
			uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);			
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ActionException("δ�ҵ���ѡԪ��",e);
		}
		if (uiHandler != null){
			IUIElement uiElement = (IUIElement) uiHandler;
			FindLibraryManager manager = new FindLibraryManager(getContext());
			try {
				rect = manager.getRect(uiElement);
			} catch (LibraryException e) {
				e.printStackTrace();
			}
			UIAImageLibary imageLibrary = new UIAImageLibary();
			SnapShotOutput output = new SnapShotOutput();
			try {
				output.setImage(imageLibrary.doScreenshot(rect));
			} catch (LibraryException e) {
				throw new ActionException("���ջ�쳣");
			}
			return output;
		}else {
			throw new ActionException("δ�ҵ���ѡԪ��");
		}
	}
}
