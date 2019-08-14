package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;
import com.jiuqi.rpa.action.VirtualKey;

public class MouseKeyPackage {
	private static List<Integer> valueList = new ArrayList<Integer>();
	private static List<String> titleList = new ArrayList<String>();

	static {
		valueList.add(VirtualKey.VK_LBUTTON);
		valueList.add(VirtualKey.VK_RBUTTON);
		valueList.add(VirtualKey.VK_MBUTTON);

		titleList.add("×ó¼ü");
		titleList.add("ÓÒ¼ü");
		titleList.add("ÖÐ¼ü");
	}

	public static int getIndex(Integer p) {
		return valueList.indexOf(p);
	}

	public static int getIndex(String p) {
		return titleList.indexOf(p);
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
