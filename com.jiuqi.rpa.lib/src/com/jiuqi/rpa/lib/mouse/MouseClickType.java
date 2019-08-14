package com.jiuqi.rpa.lib.mouse;

import java.util.HashMap;
import java.util.Map;

/**
 * ���������
 *  
 * @author zhouxubo.jiuqi.com.cn
 */
public enum MouseClickType {
	/** ���� */
	SINGLE(0),
	
	/** ˫�� */
	DOUBLE(1),
	
	/** ���� */
	DOWN(2),
	
	/** ���� */
	UP(3);
	
	private int value;
	
	private static Map<Integer, MouseClickType> finder;
	static {
		finder = new HashMap<Integer, MouseClickType>();
		for (MouseClickType item : MouseClickType.values())
			finder.put(new Integer(item.value()), item);
	}

	private MouseClickType(int value) {
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
	public static MouseClickType valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * ����ö��ֵ����ö��
	 * @param value ��������ֵ����
	 * @return ����ö��ֵ����
	 */
	public static MouseClickType valueOf(Integer value) {
		return finder.get(value);
	}
}
