package com.jiuqi.rpa.lib.mouse;

import java.awt.MouseInfo;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA鼠标操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAMouseLibrary {

	/**
	 * 将光标移动到指定位置
	 * 
	 * @param point 指定位置
	 */
	public void mouseMove(Point point) throws LibraryException {
		try {
			JQUIA.mouse_mouseMove(point);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * 执行点击操作（基于当前光标）
	 * 
	 * @param clickType 点击类型
	 * @param mousekey 鼠标键
	 * @param maskkeys 组合键
	 * @throws LibraryException
	 */
	public void mouseClick(MouseClickType clickType, int mousekey, int[] maskkeys) throws LibraryException {
		try {
			java.awt.Point p = MouseInfo.getPointerInfo().getLocation();
			JQUIA.mouse_mouseClick(new Point(p.x, p.y), clickType.value(), mousekey, maskkeys);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	} 
	
}
