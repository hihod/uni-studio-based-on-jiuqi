package com.jiuqi.rpa.action.keyboard.sendhotkey;

import com.jiuqi.rpa.action.Action;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.TargetFinder;
import com.jiuqi.rpa.action.keyboard.HotKeyMode;
import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.UIAElement;
import com.jiuqi.rpa.lib.find.WEBElement;
import com.jiuqi.rpa.lib.keyboard.UIAKeyboardLibrary;

/**
 * 活动：发送热键
 * 
 * @author liangxiao01
 */
public class SendHotKeyAction extends Action {
	private SendHotKeyInput input;
	private UIAKeyboardLibrary keyboardLibrary;

	/**
	 * 构造器
	 * 
	 * @param input 活动输入
	 */
	public SendHotKeyAction(SendHotKeyInput input) {
		super(input);
		this.input = input;
		this.keyboardLibrary = new UIAKeyboardLibrary();
	}

	@Override
	protected IActionOutput run() throws ActionException {
		try {
			if (input.getKeyMode() == HotKeyMode.NORMAL_HOTKEY)
				sendHotkey_Normal();
			else
				sendHotkey_Message();
		} catch (LibraryException e) {
			throw new ActionException("发送热键活动异常", e);
		}

		return new SendHotKeyOutput();
	}

	private void sendHotkey_Message() throws ActionException, LibraryException {
		Target target = input.getTarget();
		IUIHandler uiHandler = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);
		if (uiHandler == null)
			throw new ActionException("未找到操作目标");

		if (!(uiHandler instanceof IUIElement))
			throw new ActionException("操作目标不适用此操作");
		
		IUIElement uiElement = (IUIElement) uiHandler;
		if (uiElement instanceof WEBElement)
			throw new ActionException("WEB元素不支持消息热键");
		
		UIAElement uiaElement = (UIAElement) uiElement;
		if(input.getClearBeforeType())
			uiaElement.clearText();
		uiaElement.sendMessageHotkey(input.getKey(), input.getMaskKeys());
	}

	private void sendHotkey_Normal() throws LibraryException {
		Target target = input.getTarget();
		IUIElement uiElement = null;
		try {
			uiElement = (IUIElement) new TargetFinder().getUIHandler(getContext(), target);
		} catch (ActionException e) {
			e.printStackTrace();
		}
		if(uiElement !=null){
			if(input.getClearBeforeType())
				uiElement.clearText();
			if(uiElement != null) {
				if (input.getAttach())
					uiElement.setFocus();
				else
					;
			}
			
		}
		keyboardLibrary.sendHotkey(input.getKey(), input.getMaskKeys());
	}

}
