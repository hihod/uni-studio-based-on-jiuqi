package com.jiuqi.rpa.action.keyboard;

import java.util.HashMap;
import java.util.Map;

public enum TypeMode {
	/** 键盘录入 */
	NORMAL_TYPE(0),
	
	/** 模拟录入 */
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
	 * 按照枚举值查找枚举
	 * @param value 数据类型值
	 * @return 返回枚举值对象
	 */
	public static TypeMode valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * 按照枚举值查找枚举
	 * @param value 数据类型值对象
	 * @return 返回枚举值对象
	 */
	public static TypeMode valueOf(Integer value) {
		return finder.get(value);
	}
}
