package com.jiuqi.rpa.lib.window;

/**
 * ����״̬
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public enum WindowState {
	/** ���� */
	HIDE(0),
	
	/** ��ʾ */
	SHOW(5),
	
	/** ��С�� */
	MINIMIZE(2),
	
	/** ��� */
	MAXIMIZE(3),

	/** ��ԭ */
	RESOTRE(9);
	
	private int value;

	private WindowState(int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}
}
