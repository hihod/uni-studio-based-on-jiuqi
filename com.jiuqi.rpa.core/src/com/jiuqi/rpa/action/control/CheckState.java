package com.jiuqi.rpa.action.control;

import java.util.HashMap;
import java.util.Map;


public enum CheckState {
	UNCHECKED(0), // δ��ѡ
	CHECKED(1), // ��ѡ
	INDETERMINATE(2);// ��ȷ��
	private int value;

	private static Map<Integer, CheckState> finder;
	static {
		finder = new HashMap<Integer, CheckState>();
		for (CheckState item : CheckState.values())
			finder.put(new Integer(item.value()), item);
	}

	private CheckState(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

	/**
	 * ����ö��ֵ����ö��
	 * 
	 * @param value
	 *            ��������ֵ
	 * @return ����ö��ֵ����
	 */
	public static CheckState valueOf(int value) {
		return valueOf(new Integer(value));
	}

	/**
	 * ����ö��ֵ����ö��
	 * 
	 * @param value
	 *            ��������ֵ����
	 * @return ����ö��ֵ����
	 */
	public static CheckState valueOf(Integer value) {
		return finder.get(value);
	}
}
