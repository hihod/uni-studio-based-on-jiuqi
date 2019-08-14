package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.rpa.lib.mouse.MouseClickType;

public class MouseClickTypePackage {
	private static List<MouseClickType> enumList = new ArrayList<MouseClickType>();
	private static List<Integer> valueList = new ArrayList<Integer>();
	private static List<String> titleList = new ArrayList<String>();
	
	static {
		enumList.add(MouseClickType.SINGLE);
		enumList.add(MouseClickType.DOUBLE);
		enumList.add(MouseClickType.DOWN);
		enumList.add(MouseClickType.UP);

		valueList.add(MouseClickType.SINGLE.value());
		valueList.add(MouseClickType.DOUBLE.value());
		valueList.add(MouseClickType.DOWN.value());
		valueList.add(MouseClickType.UP.value());

		titleList.add("单击");
		titleList.add("双击");
		titleList.add("按下");
		titleList.add("弹起");
	}

	public static int getIndex(MouseClickType p) {
		return enumList.indexOf(p);
	}

	public static int getIndex(Integer p) {
		return valueList.indexOf(p);
	}

	public static int getIndex(String p) {
		return titleList.indexOf(p);
	}

	public static MouseClickType getMouseClickType(int i) {
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
