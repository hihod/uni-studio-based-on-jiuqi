package com.jiuqi.rpa.lib.mouse;

import java.util.HashMap;
import java.util.Map;

/**
 * 鼠标点击类型
 *  
 * @author zhouxubo.jiuqi.com.cn
 */
public enum MouseClickType {
	/** 单击 */
	SINGLE(0),
	
	/** 双击 */
	DOUBLE(1),
	
	/** 按下 */
	DOWN(2),
	
	/** 弹起 */
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
	 * 按照枚举值查找枚举
	 * @param value 数据类型值
	 * @return 返回枚举值对象
	 */
	public static MouseClickType valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * 按照枚举值查找枚举
	 * @param value 数据类型值对象
	 * @return 返回枚举值对象
	 */
	public static MouseClickType valueOf(Integer value) {
		return finder.get(value);
	}
}
