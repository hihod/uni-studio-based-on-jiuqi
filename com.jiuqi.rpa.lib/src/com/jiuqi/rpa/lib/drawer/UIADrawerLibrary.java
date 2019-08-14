package com.jiuqi.rpa.lib.drawer;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA画笔操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIADrawerLibrary {
	
	/**
	 * 开始画框
	 * 
	 * @param rect 指定方框
	 * @param color 画笔颜色
	 * @throws LibraryException
	 */
	public void startDraw(Rect rect, String color) throws LibraryException {
		if (rect == null)
			throw new LibraryException("未指定要画的方框");
		
		try {
			JQUIA.drawer_startDraw(rect, color);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	/**
	 * 结束画框
	 * 
	 * @throws LibraryException
	 */
	public void endDraw() throws LibraryException {
		try {
			JQUIA.drawer_endDraw();
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
}
