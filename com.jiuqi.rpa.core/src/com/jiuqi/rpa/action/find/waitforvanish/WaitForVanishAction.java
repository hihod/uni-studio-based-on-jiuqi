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
 * ����ȴ�Ԫ����ʧԪ��
 * 
 * @author liangxiao01
 */
public class WaitForVanishAction extends Action {
	private WaitForVanishInput input;

	/**
	 * ������
	 * 
	 * @param input
	 *            �����
	 */
	public WaitForVanishAction(WaitForVanishInput input) {
		super(input);
		this.input = input;
	}

	@Override
	protected IActionOutput run() throws ActionException {
		try {
			IUIHandler uiHandler = input.getTarget().getElement();
			
			//ͨ��ָ��Ԫ���ж�
			if (uiHandler == null)
				return WaitVanishByPath(input.getTarget().getPath());
			//ͨ��ָ��·���ж�
			else
				return waitVanishByElement(uiHandler);
		} catch (InterruptedException e) {
			throw new ActionException("������ж�", e);
		} catch (LibraryException e) {
			throw new ActionException("��쳣", e);
		}
	}

	private WaitForVanishOutput waitVanishByElement(IUIHandler uiHandler) throws ActionException, LibraryException, InterruptedException {
		WaitForVanishOutput output = new WaitForVanishOutput();
		
		while (!getContext().isInterrupted()) {
			if (!(uiHandler instanceof IUIElement))
				throw new ActionException("����Ŀ�겻���ô˲���");
			
			IUIElement uiElement = (IUIElement) uiHandler;
			//������
			if (input.isWaitNotActive() && !uiElement.enable())
				return output;
			
			//���ɼ�
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
				throw new ActionException("δ�ṩ����·��");
			
			//������
			if (!manager.exists(path))
				return output;
			
			//����Ŀ��Ԫ��
			int timeout = input.getTarget().getTimeout();
			IUIHandler uiHandler = new TargetFinder().getUIHandler(getContext(), path, timeout);
			if (uiHandler == null)
				throw new ActionException("δ�ҵ�Ŀ��Ԫ��");
			
			//�ж��Ƿ���ÿɼ�
			try {
				if (!(uiHandler instanceof IUIElement))
					throw new ActionException("����Ŀ�겻���ô˲���");
				
				IUIElement uiElement = (IUIElement) uiHandler;
				//������
				if (input.isWaitNotActive() && !uiElement.enable())
					return output;
				//���ɼ�
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
