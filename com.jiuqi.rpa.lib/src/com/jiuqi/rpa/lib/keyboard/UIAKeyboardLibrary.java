package com.jiuqi.rpa.lib.keyboard;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA鼠标操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAKeyboardLibrary {

	/**
	 * 发送热键
	 * 
	 * @param hotkey 热键
	 * @param maskkeys 组合键
	 * @throws LibraryException
	 */
	public void sendHotkey(int hotkey, int[] maskkeys) throws LibraryException {
		try {
			JQUIA.keyboard_sendHotKey(hotkey, maskkeys);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * 录入文本
	 * 
	 * @param text 要录入的文本
	 * @throws LibraryException
	 */
	public void typeText(String text) throws LibraryException {
		try {
			JQUIA.keyboard_typeText(text);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
}
