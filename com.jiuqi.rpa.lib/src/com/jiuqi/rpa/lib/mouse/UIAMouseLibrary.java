package com.jiuqi.rpa.lib.mouse;

import java.awt.MouseInfo;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA��������
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAMouseLibrary {

	/**
	 * ������ƶ���ָ��λ��
	 * 
	 * @param point ָ��λ��
	 */
	public void mouseMove(Point point) throws LibraryException {
		try {
			JQUIA.mouse_mouseMove(point);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * ִ�е�����������ڵ�ǰ��꣩
	 * 
	 * @param clickType �������
	 * @param mousekey ����
	 * @param maskkeys ��ϼ�
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
