package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;

public class ColorPackage {
	private static List<String> valueList = new ArrayList<String>();
	private static List<String> titleList = new ArrayList<String>();
	
	static {
		valueList.add("#FF0000");
		valueList.add("#0000FF");
		valueList.add("#00FF00");
		valueList.add("#FFFF00");

		titleList.add("红色");
		titleList.add("蓝色");
		titleList.add("绿色");
		titleList.add("黄色");
	}

	public static int getIndex(String p) {
		return valueList.indexOf(p);
	}

	public static String getValue(int i) {
		return valueList.get(i);
	}

	public static String getTitle(int i) {
		return titleList.get(i);
	}

	public static String[] getTitleArray() {
		return titleList.toArray(new String[0]);
	}

	public static String[] getValueArray() {
		return valueList.toArray(new String[0]);
	}
}
