package com.jiuqi.rpa.lib.picker;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIAʰȡ��������
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAPickerLibrary {
	
	/**
	 * ��ʼʰȡ
	 * 
	 * @param callback �ص�����
	 * @throws LibraryException
	 */
	public void startPick(UIAPickerCallback callback) throws LibraryException {
		try {
			JQUIA.picker_startPick(callback);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * ����ʰȡ
	 * 
	 * @throws LibraryException
	 */
	public void endPick() throws LibraryException {
		try {
			JQUIA.picker_endPick();
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
}
