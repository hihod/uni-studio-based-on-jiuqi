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
 * 活动：录入文本
 * 
 * @author liangxiao01
 */
public class TypetextAction extends Action {
	private TypetextInput input;
	private UIAKeyboardLibrary keyboardLibrary;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public TypetextAction(TypetextInput input) {
		super(input);
		this.input = input;
		this.keyboardLibrary = new UIAKeyboardLibrary();
	}

	@Override
	protected IActionOutput run() throws ActionException {
		try {
			//获取操作目标
			Target target = input.getTarget();
			IUIHandler uiHandler = null;
			IUIElement uiElement = null;
			try{
				uiHandler = new TargetFinder().getUIHandler(getContext(), target);
				if (uiHandler != null && uiHandler instanceof IUIElement)
					uiElement = (IUIElement) uiHandler;
			}catch (Exception e) {
				System.out.println("未能定位到目标元素,无需处理");
			}
			//录入前清空
			if (uiElement != null && input.getClearBeforeType())
				uiElement.clearText();
			//键盘录入
			//基于当前光标，允许uiHandler为空
			if (input.getTypeMode() == TypeMode.NORMAL_TYPE)
				typeText_Normal(uiElement);
			else {
				//模拟录入
				//基于uiHandler，不允许为空
				if (uiElement == null)
					throw new ActionException("未找到操作目标，或操作目标不适用此操作");
				typeText_Simulate(uiElement);
			}
		} catch (LibraryException e) {
			throw new ActionException("录入文本活动异常", e);
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
				//元素设置焦点
				uiElement.setFocus();
			}
			
			//元素非激活状态，返回
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
