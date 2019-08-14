package com.jiuqi.rpa.lib.image;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIAͼƬ������
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAImageLibary {
	/**
	 * ִ�н���
	 * 
	 * @param rect ��Ļ�ϵ�ָ������null��ʾ��ȫ��
	 * @return ����ͼƬ����
	 * @throws LibraryException
	 */
	public byte[] doScreenshot(Rect rect) throws LibraryException {
		try {
			return JQUIA.image_doScreenShot(rect);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
}
