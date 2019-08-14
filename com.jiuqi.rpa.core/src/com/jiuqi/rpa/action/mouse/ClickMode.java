package com.jiuqi.rpa.action.mouse;

import java.util.HashMap;
import java.util.Map;

/**
 * 点击方式
 *  
 */
public enum ClickMode {
	/** 鼠标单击 */
	NORMAL_CLICK(0),
	
	/** 模拟单击 */
	SIMULATE_CLICK(1),
	
	/** 消息单击 */
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
	 * 按照枚举值查找枚举
	 * @param value 数据类型值
	 * @return 返回枚举值对象
	 */
	public static ClickMode valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * 按照枚举值查找枚举
	 * @param value 数据类型值对象
	 * @return 返回枚举值对象
	 */
	public static ClickMode valueOf(Integer value) {
		return finder.get(value);
	}
}
