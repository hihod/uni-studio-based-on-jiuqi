package com.jiuqi.rpa.lib.picker;

/**
 * JQUIA回调对象
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAPickerCallback {
	private static IUIAPickerListener listener;

	public static void setListener(IUIAPickerListener uiaPickerListener) {
		listener = uiaPickerListener;
	}
	
	public static void onMouseMove(int x, int y) {
		System.out.println("----------------------------------");
		listener.onMouseMove(x, y);
	}
	
	public static void onMouseLeftUp(int x, int y) {
		listener.onMouseLeftUp(x, y);
	}
	
	public static void onKeyEscapeUp() {
		listener.onKeyEscapeUp();
	}
	
}