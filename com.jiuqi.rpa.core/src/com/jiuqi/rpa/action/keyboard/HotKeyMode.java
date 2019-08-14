package com.jiuqi.rpa.action.keyboard;

import java.util.HashMap;
import java.util.Map;

public enum HotKeyMode {
	/** �����ȼ� */
	NORMAL_HOTKEY(0),
	
	/** ��Ϣ�ȼ� */
	MESSAGE_HOTKEY(1);
	
	private int value;

	private static Map<Integer, HotKeyMode> finder;
	static {
		finder = new HashMap<Integer, HotKeyMode>();
		for (HotKeyMode item : HotKeyMode.values())
			finder.put(new Integer(item.value()), item);
	}

	private HotKeyMode(int value) {
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
	public static HotKeyMode valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * ����ö��ֵ����ö��
	 * @param value ��������ֵ����
	 * @return ����ö��ֵ����
	 */
	public static HotKeyMode valueOf(Integer value) {
		return finder.get(value);
	}
}
