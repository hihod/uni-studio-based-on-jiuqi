package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.rpa.action.mouse.ClickMode;

public class ClickModePackage {
	private static List<ClickMode> enumList = new ArrayList<ClickMode>();
	private static List<Integer> valueList = new ArrayList<Integer>();
	private static List<String> titleList = new ArrayList<String>();
	
	static {
		enumList.add(ClickMode.NORMAL_CLICK);
		enumList.add(ClickMode.SIMULATE_CLICK);
		enumList.add(ClickMode.MESSAGE_CLICK);

		valueList.add(ClickMode.NORMAL_CLICK.value());
		valueList.add(ClickMode.SIMULATE_CLICK.value());
		valueList.add(ClickMode.MESSAGE_CLICK.value());

		titleList.add("鼠标点击");
		titleList.add("模拟点击");
		titleList.add("消息点击");
	}

	public static int getIndex(ClickMode p) {
		return enumList.indexOf(p);
	}

	public static int getIndex(Integer p) {
		return valueList.indexOf(p);
	}

	public static int getIndex(String p) {
		return titleList.indexOf(p);
	}

	public static ClickMode getClickMode(int i) {
		return enumList.get(i);
	}

	public static Integer getValue(int i) {
		return valueList.get(i);
	}

	public static String getTitle(int i) {
		return titleList.get(i);
	}

	public static String[] getTitleArray() {
		return titleList.toArray(new String[0]);
	}
}
