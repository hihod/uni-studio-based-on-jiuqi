package com.jiuqi.rpa.lib.picker;

/**
 * UIA拾取器监听接口
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IUIAPickerListener {

	/**
	 * 鼠标移动监听
	 * 
	 * @param x 鼠标的x
	 * @param y 鼠标的y
	 */
	void onMouseMove(int x, int y);
	
	/**
	 * 鼠标左键弹起监听
	 * 
	 * @param x 鼠标的x
	 * @param y 鼠标的y
	 */
	void onMouseLeftUp(int x, int y);
	
	/**
	 * ESC键弹起监听
	 */
	void onKeyEscapeUp();
	
}
