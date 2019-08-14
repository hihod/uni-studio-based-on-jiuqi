package com.jiuqi.rpa.action.keyboard.typesecuretext;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.action.keyboard.TypeMode;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.UIAElement;
import com.jiuqi.rpa.lib.find.WEBElement;
import com.jiuqi.rpa.lib.keyboard.UIAKeyboardLibrary;

/**
 * ���¼������
 * 
 * @author liangxiao01
 */
public class TypeSecuretextAction extends Action {
	private TypeSecuretextInput input;
	private UIAKeyboardLibrary keyboardLibrary;

	/**
	 * ������
	 * 
	 * @param input �����
	 */
	public TypeSecuretextAction(TypeSecuretextInput input) {
		super(input);
		this.input = input;
		this.keyboardLibrary = new UIAKeyboardLibrary();
	}

	@Override
	protected IActionOutput run() throws ActionException {
		try {
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
			
			if(uiElement!=null && input.getClearBeforeType())
				uiElement.clearText();
			
			if (input.getTypeMode() == TypeMode.NORMAL_TYPE) {
				typeSecureText_Normal(uiElement);
			} else {
				if (input.getTypeMode() == TypeMode.SIMULATE_TYPE){
					if (uiHandler == null)
						throw new ActionException("δ�ҵ�����Ŀ��");
					if (!(uiHandler instanceof IUIElement))
						throw new ActionException("����Ŀ�겻���ô˲���");
					typeSecureText_Simulate(uiElement);
				}else
					typeSecureText_Message(uiElement);
			}
		} catch (LibraryException e) {
			throw new ActionException("¼�����Ļ�쳣", e);
		}
		
		return new TypeSecuretextOutput();
	}

	private void typeSecureText_Message(IUIElement uiElement) throws LibraryException {
		// uiaElement.setText(input.getExpression());
	}
	
	private void typeSecureText_Simulate(IUIElement uiElement) throws LibraryException {
		if (uiElement instanceof WEBElement) {
			WEBElement webElement = (WEBElement) uiElement;
			webElement.setText(input.getExpression());
		} else {
			UIAElement uiaElement = (UIAElement) uiElement;
			uiaElement.simulateTypeText(input.getExpression());
		}
	}

	private void typeSecureText_Normal(IUIElement uiElement) throws LibraryException {
		
		if(uiElement !=null && input.getAttach()){
			if(uiElement instanceof WEBElement) {
				IUIElement window = new FindLibraryManager(getContext()).getWindow(uiElement);
				window.setFocus();
			}
			uiElement.setFocus();
		}
		String expression = input.getExpression();
		for (int i = 0; i < expression.length(); i++) {
			keyboardLibrary.typeText(expression.charAt(i) + "");
			try {
				Thread.sleep(input.getDelayBetweenKeys());
			} catch (InterruptedException e) {
			}
		}
	}

}
