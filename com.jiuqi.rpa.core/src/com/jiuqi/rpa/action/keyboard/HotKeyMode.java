package com.jiuqi.rpa.action.keyboard;

import java.util.HashMap;
import java.util.Map;

public enum HotKeyMode {
	/** 发送热键 */
	NORMAL_HOTKEY(0),
	
	/** 消息热键 */
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
	 * 按照枚举值查找枚举
	 * @param value 数据类型值
	 * @return 返回枚举值对象
	 */
	public static HotKeyMode valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * 按照枚举值查找枚举
	 * @param value 数据类型值对象
	 * @return 返回枚举值对象
	 */
	public static HotKeyMode valueOf(Integer value) {
		return finder.get(value);
	}
}
