package com.jiuqi.rpa.lib.keyboard;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA��������
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAKeyboardLibrary {

	/**
	 * �����ȼ�
	 * 
	 * @param hotkey �ȼ�
	 * @param maskkeys ��ϼ�
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
	 * ¼���ı�
	 * 
	 * @param text Ҫ¼����ı�
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
