package com.jiuqi.rpa.lib.drawer;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA���ʲ�����
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIADrawerLibrary {
	
	/**
	 * ��ʼ����
	 * 
	 * @param rect ָ������
	 * @param color ������ɫ
	 * @throws LibraryException
	 */
	public void startDraw(Rect rect, String color) throws LibraryException {
		if (rect == null)
			throw new LibraryException("δָ��Ҫ���ķ���");
		
		try {
			JQUIA.drawer_startDraw(rect, color);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	/**
	 * ��������
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
