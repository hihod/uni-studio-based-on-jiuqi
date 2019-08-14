package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.rpa.lib.dialog.MButtonSuite;

public class MButtonSuitePackage {
	private static List<MButtonSuite> enumList = new ArrayList<MButtonSuite>();
	private static List<Long> keyList = new ArrayList<Long>();
	private static List<String> titleList = new ArrayList<String>();

	static {
		enumList.add(MButtonSuite.OK);
		enumList.add(MButtonSuite.OKCANCEL);
		enumList.add(MButtonSuite.ABORTRETRYIGNORE);
		enumList.add(MButtonSuite.YESNOCANCEL);
		enumList.add(MButtonSuite.YESNO);
		enumList.add(MButtonSuite.RETRYCANCEL);

		keyList.add(MButtonSuite.OK.value());
		keyList.add(MButtonSuite.OKCANCEL.value());
		keyList.add(MButtonSuite.ABORTRETRYIGNORE.value());
		keyList.add(MButtonSuite.YESNOCANCEL.value());
		keyList.add(MButtonSuite.YESNO.value());
		keyList.add(MButtonSuite.RETRYCANCEL.value());

		titleList.add(MButtonSuite.OK.title());
		titleList.add(MButtonSuite.OKCANCEL.title());
		titleList.add(MButtonSuite.ABORTRETRYIGNORE.title());
		titleList.add(MButtonSuite.YESNOCANCEL.title());
		titleList.add(MButtonSuite.YESNO.title());
		titleList.add(MButtonSuite.RETRYCANCEL.title());
	}

	public static int getIndex(MButtonSuite p) {
		return enumList.indexOf(p);
	}

	public static int getIndex(Long p) {
		return keyList.indexOf(p);
	}

	public static int getIndex(String p) {
		return titleList.indexOf(p);
	}

	public static MButtonSuite getMButtonSuite(int i) {
		return enumList.get(i);
	}

	public static Long getValue(int i) {
		return keyList.get(i);
	}

	public static String getTitle(int i) {
		return titleList.get(i);
	}

	public static String[] getTitleArray() {
		return titleList.toArray(new String[0]);
	}
}
