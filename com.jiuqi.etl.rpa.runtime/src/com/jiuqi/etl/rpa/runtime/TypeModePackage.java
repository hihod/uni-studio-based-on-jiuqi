package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;
import com.jiuqi.rpa.action.keyboard.TypeMode;

public class TypeModePackage {
	private static List<TypeMode> enumList = new ArrayList<TypeMode>();
	private static List<Integer> valueList = new ArrayList<Integer>();
	private static List<String> titleList = new ArrayList<String>();
	
	static {
		enumList.add(TypeMode.NORMAL_TYPE);
		enumList.add(TypeMode.SIMULATE_TYPE);

		valueList.add(TypeMode.NORMAL_TYPE.value());
		valueList.add(TypeMode.SIMULATE_TYPE.value());

		titleList.add("¼üÅÌ¼üÈë");
		titleList.add("Ä£Äâ¼üÈë");
	}

	public static int getIndex(TypeMode p) {
		return enumList.indexOf(p);
	}

	public static int getIndex(Integer p) {
		return valueList.indexOf(p);
	}

	public static int getIndex(String p) {
		return titleList.indexOf(p);
	}

	public static TypeMode getClickMode(int i) {
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
