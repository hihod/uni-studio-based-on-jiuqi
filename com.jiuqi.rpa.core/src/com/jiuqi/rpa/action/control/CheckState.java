package com.jiuqi.rpa.action.control;

import java.util.HashMap;
import java.util.Map;


public enum CheckState {
	UNCHECKED(0), // 未勾选
	CHECKED(1), // 勾选
	INDETERMINATE(2);// 不确定
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
	 * 按照枚举值查找枚举
	 * 
	 * @param value
	 *            数据类型值
	 * @return 返回枚举值对象
	 */
	public static CheckState valueOf(int value) {
		return valueOf(new Integer(value));
	}

	/**
	 * 按照枚举值查找枚举
	 * 
	 * @param value
	 *            数据类型值对象
	 * @return 返回枚举值对象
	 */
	public static CheckState valueOf(Integer value) {
		return finder.get(value);
	}
}
