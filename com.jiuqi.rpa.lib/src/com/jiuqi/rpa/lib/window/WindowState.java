package com.jiuqi.rpa.lib.window;

/**
 * 窗口状态
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public enum WindowState {
	/** 隐藏 */
	HIDE(0),
	
	/** 显示 */
	SHOW(5),
	
	/** 最小化 */
	MINIMIZE(2),
	
	/** 最大化 */
	MAXIMIZE(3),

	/** 还原 */
	RESOTRE(9);
	
	private int value;

	private WindowState(int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}
}
