package com.jiuqi.rpa.action.mouse;

import java.util.HashMap;
import java.util.Map;

/**
 * �����ʽ
 *  
 */
public enum ClickMode {
	/** ��굥�� */
	NORMAL_CLICK(0),
	
	/** ģ�ⵥ�� */
	SIMULATE_CLICK(1),
	
	/** ��Ϣ���� */
	MESSAGE_CLICK(2);
	
	private int value;
	
	private static Map<Integer, ClickMode> finder;
	static {
		finder = new HashMap<Integer, ClickMode>();
		for (ClickMode item : ClickMode.values())
			finder.put(new Integer(item.value()), item);
	}

	private ClickMode(int value) {
		this.value = value;
	}
	
	public int value() {
		return value;
	}

	/**
	 * ����ö��ֵ����ö��
	 * @param value ��������ֵ
	 * @return ����ö��ֵ����
	 */
	public static ClickMode valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * ����ö��ֵ����ö��
	 * @param value ��������ֵ����
	 * @return ����ö��ֵ����
	 */
	public static ClickMode valueOf(Integer value) {
		return finder.get(value);
	}
}
