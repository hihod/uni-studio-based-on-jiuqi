package com.jiuqi.rpa.lib.picker;

/**
 * UIAʰȡ�������ӿ�
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IUIAPickerListener {

	/**
	 * ����ƶ�����
	 * 
	 * @param x ����x
	 * @param y ����y
	 */
	void onMouseMove(int x, int y);
	
	/**
	 * �������������
	 * 
	 * @param x ����x
	 * @param y ����y
	 */
	void onMouseLeftUp(int x, int y);
	
	/**
	 * ESC���������
	 */
	void onKeyEscapeUp();
	
}
