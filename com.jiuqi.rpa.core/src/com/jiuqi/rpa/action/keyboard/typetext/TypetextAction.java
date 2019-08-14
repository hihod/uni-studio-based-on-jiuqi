package com.jiuqi.rpa.action.keyboard.typetext;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.action.keyboard.TypeMode;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.UIAElement;
import com.jiuqi.rpa.lib.find.WEBElement;
import com.jiuqi.rpa.lib.keyboard.UIAKeyboardLibrary;

/**
 * ���¼���ı�
 * 
 * @author liangxiao01
 */
public class TypetextAction extends Action {
	private TypetextInput input;
	private UIAKeyboardLibrary keyboardLibrary;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public TypetextAction(TypetextInput input) {
		super(input);
		this.input = input;
		this.keyboardLibrary = new UIAKeyboardLibrary();
	}

	@Override
	protected IActionOutput run() throws ActionException {
		try {
			//��ȡ����Ŀ��
			Target target = input.getTarget();
			IUIHandler uiHandler = null;
			IUIElement uiElement = null;
			try{
				uiHandler = new TargetFinder().getUIHandler(getContext(), target);
				if (uiHandler != null && uiHandler instanceof IUIElement)
					uiElement = (IUIElement) uiHandler;
			}catch (Exception e) {
				System.out.println("δ�ܶ�λ��Ŀ��Ԫ��,���账��");
			}
			//¼��ǰ���
			if (uiElement != null && input.getClearBeforeType())
				uiElement.clearText();
			//����¼��
			//���ڵ�ǰ��꣬����uiHandlerΪ��
			if (input.getTypeMode() == TypeMode.NORMAL_TYPE)
				typeText_Normal(uiElement);
			else {
				//ģ��¼��
				//����uiHandler��������Ϊ��
				if (uiElement == null)
					throw new ActionException("δ�ҵ�����Ŀ�꣬�����Ŀ�겻���ô˲���");
				typeText_Simulate(uiElement);
			}
		} catch (LibraryException e) {
			throw new ActionException("¼���ı���쳣", e);
		}

		return new TypetextOutput();
	}

	private void typeText_Simulate(IUIElement uiElement) throws LibraryException {
		if (uiElement instanceof WEBElement) {
			WEBElement webElement = (WEBElement) uiElement;
			webElement.setText(input.getText());
		} else {
			UIAElement uiaElement = (UIAElement) uiElement;
			uiaElement.simulateTypeText(input.getText());
		}
	}

	private void typeText_Normal(IUIElement uiElement) throws LibraryException {
		if (uiElement != null) {
			if (input.getAttach()) {
				//Ԫ�����ý���
				uiElement.setFocus();
			}
			
			//Ԫ�طǼ���״̬������
			//TODO
		}
		
		String text = input.getText();
		for (int i = 0; i < text.length(); i++) {
			keyboardLibrary.typeText(text.charAt(i) + "");
			try {
				Thread.sleep(input.getDelayBetweenKeys());
			} catch (InterruptedException e) {
			}
		}
	}

}
