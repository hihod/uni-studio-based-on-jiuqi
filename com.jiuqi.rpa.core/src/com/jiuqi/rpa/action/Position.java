package com.jiuqi.rpa.action;

import java.util.HashMap;
import java.util.Map;

/**
 * 方位枚举 
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public enum Position {
	CENTER(0, "中心"),
	TOPLEFT(1, "左上角"),
	TOPRIGHT(2, "右上角"),
	BOTTOMLEFT(3, "左下角"),
	BOTTOMRIGHT(4, "右下角");
	
	private int value;
	private String title;
	
	Position(int value, String title) {
		this.value = value;
		this.title = title;
	}
	
	public int value() {
		return value;
	}
	
	public String title() {
		return title;
	}
	private static Map<Integer, Position> finder;
	static {
		finder = new HashMap<Integer, Position>();
		for (Position item : Position.values())
			finder.put(new Integer(item.value()), item);
	}
	/**
	 * 按照枚举值查找枚举
	 * @param value 数据类型值
	 * @return 返回枚举值对象
	 */
	public static Position valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * 按照枚举值查找枚举
	 * @param value 数据类型值对象
	 * @return 返回枚举值对象
	 */
	public static Position valueOf(Integer value) {
		return finder.get(value);
	}
}
