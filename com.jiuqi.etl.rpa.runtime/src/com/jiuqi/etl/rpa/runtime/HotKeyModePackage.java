package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.rpa.action.keyboard.HotKeyMode;

public class HotKeyModePackage {
	private static List<HotKeyMode> enumList = new ArrayList<HotKeyMode>();
	private static List<Integer> valueList = new ArrayList<Integer>();
	private static List<String> titleList = new ArrayList<String>();
	
	static {
		enumList.add(HotKeyMode.NORMAL_HOTKEY);
		enumList.add(HotKeyMode.MESSAGE_HOTKEY);

		valueList.add(HotKeyMode.NORMAL_HOTKEY.value());
		valueList.add(HotKeyMode.MESSAGE_HOTKEY.value());

		titleList.add("发送热键");
		titleList.add("发送消息热键");
	}

	public static int getIndex(HotKeyMode p) {
		return enumList.indexOf(p);
	}

	public static int getIndex(Integer p) {
		return valueList.indexOf(p);
	}

	public static int getIndex(String p) {
		return titleList.indexOf(p);
	}

	public static HotKeyMode getClickMode(int i) {
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
