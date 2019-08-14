package com.jiuqi.rpa.action;

import java.util.HashMap;
import java.util.Map;

/**
 * ��λö�� 
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public enum Position {
	CENTER(0, "����"),
	TOPLEFT(1, "���Ͻ�"),
	TOPRIGHT(2, "���Ͻ�"),
	BOTTOMLEFT(3, "���½�"),
	BOTTOMRIGHT(4, "���½�");
	
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
	 * ����ö��ֵ����ö��
	 * @param value ��������ֵ
	 * @return ����ö��ֵ����
	 */
	public static Position valueOf(int value) {
		return valueOf(new Integer(value));
	}
	
	/**
	 * ����ö��ֵ����ö��
	 * @param value ��������ֵ����
	 * @return ����ö��ֵ����
	 */
	public static Position valueOf(Integer value) {
		return finder.get(value);
	}
}
