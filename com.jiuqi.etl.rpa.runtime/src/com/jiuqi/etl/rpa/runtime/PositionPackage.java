package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.rpa.action.Position;

public class PositionPackage {
	private static List<Position> enumList = new ArrayList<Position>();
	private static List<Integer> valueList = new ArrayList<Integer>();
	private static List<String> titleList = new ArrayList<String>();
	
	static {
		enumList.add(Position.CENTER);
		enumList.add(Position.TOPLEFT);
		enumList.add(Position.TOPRIGHT);
		enumList.add(Position.BOTTOMLEFT);
		enumList.add(Position.BOTTOMRIGHT);

		valueList.add(Position.CENTER.value());
		valueList.add(Position.TOPLEFT.value());
		valueList.add(Position.TOPRIGHT.value());
		valueList.add(Position.BOTTOMLEFT.value());
		valueList.add(Position.BOTTOMRIGHT.value());

		titleList.add("中心");
		titleList.add("左上角");
		titleList.add("右上角");
		titleList.add("左下角");
		titleList.add("右下角");
	}

	public static int getIndex(Position p) {
		return enumList.indexOf(p);
	}

	public static int getIndex(Integer p) {
		return valueList.indexOf(p);
	}

	public static int getIndex(String p) {
		return titleList.indexOf(p);
	}

	public static Position getPosition(int i) {
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
