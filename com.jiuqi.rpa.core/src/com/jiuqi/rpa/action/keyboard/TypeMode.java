package com.jiuqi.rpa.action.keyboard;

import java.util.HashMap;
import java.util.Map;

public enum TypeMode {
	/** ����¼�� */
	NORMAL_TYPE(0),
	
	/** ģ��¼�� */
	SIMULATE_TYPE(1);
	
	private int value;

	private static Map<Integer, TypeMode> finder;
	static {
		finder = new HashMap<Integer, TypeMode>();
		for (TypeMode item : TypeMode.values())
			finder.put(new Integer(item.value()), item);
	}

	private TypeMode(int value) {
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
	public static TypeMode valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * ����ö��ֵ����ö��
	 * @param value ��������ֵ����
	 * @return ����ö��ֵ����
	 */
	public static TypeMode valueOf(Integer value) {
		return finder.get(value);
	}
}
