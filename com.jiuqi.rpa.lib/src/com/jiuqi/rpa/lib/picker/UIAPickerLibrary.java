package com.jiuqi.rpa.lib.picker;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA拾取器操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAPickerLibrary {
	
	/**
	 * 开始拾取
	 * 
	 * @param callback 回调对象
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
	 * 结束拾取
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
