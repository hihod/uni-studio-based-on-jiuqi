package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;
import com.jiuqi.rpa.action.VirtualKey;

public class KeyBoardPackage {
	private static  List<Integer> valueList = new ArrayList<Integer>();
	private static  List<String> titleList = new ArrayList<String>();
	
	static {
		titleList.add("ENTER");             valueList.add(VirtualKey.VK_RETURN);
		titleList.add("BACKSPACE");         valueList.add(VirtualKey.VK_BACK);
		titleList.add("TAB");               valueList.add(VirtualKey.VK_TAB);
		titleList.add("CAPS LOCK");         valueList.add(VirtualKey.VK_CAPITAL);
		titleList.add("ESC");				valueList.add(VirtualKey.VK_ESCAPE);
		titleList.add("SPACE");          	valueList.add(VirtualKey.VK_SPACE);
		titleList.add("PRIOR");      	    valueList.add(VirtualKey.VK_PRIOR);
		titleList.add("NEXT");      	    valueList.add(VirtualKey.VK_NEXT);
		titleList.add("END");       		valueList.add(VirtualKey.VK_END);
		titleList.add("HOME");   	   		valueList.add(VirtualKey.VK_HOME);
		titleList.add("LEFT");      	 	valueList.add(VirtualKey.VK_LEFT);
		titleList.add("UP");          		valueList.add(VirtualKey.VK_UP);
		titleList.add("RIGHT");      		valueList.add(VirtualKey.VK_RIGHT);
		titleList.add("DOWN");       		valueList.add(VirtualKey.VK_DOWN);
		titleList.add("Print Screen"); 		valueList.add(VirtualKey.VK_SNAPSHOT);
		titleList.add("INSERT");			valueList.add(VirtualKey.VK_INSERT);
		titleList.add("DELETE");			valueList.add(VirtualKey.VK_DELETE);
		titleList.add("DECIMAL");			valueList.add(VirtualKey.VK_DECIMAL);
		titleList.add("MULTIPLY");			valueList.add(VirtualKey.VK_MULTIPLY);
		titleList.add("ADD");				valueList.add(VirtualKey.VK_ADD);
		titleList.add("SEPARATOR");			valueList.add(VirtualKey.VK_SEPARATOR);
		titleList.add("SUBTRACT");			valueList.add(VirtualKey.VK_SUBTRACT);
		titleList.add("A");                 valueList.add(0x41);
		titleList.add("B");                 valueList.add(0x42);
		titleList.add("C");                 valueList.add(0X43);
		titleList.add("D");                 valueList.add(0X44);
		titleList.add("E");                 valueList.add(0X45);
		titleList.add("F");                 valueList.add(0X46);
		titleList.add("G");                 valueList.add(0X47);
		titleList.add("H");                 valueList.add(0X48);
		titleList.add("I");                 valueList.add(0X49);
		titleList.add("J");                 valueList.add(0X4A);
		titleList.add("K");                 valueList.add(0X4B);
		titleList.add("L");                 valueList.add(0X4C);
		titleList.add("M");                 valueList.add(0X4D);
		titleList.add("N");                 valueList.add(0X4E);
		titleList.add("O");                 valueList.add(0X4F);
		titleList.add("P");                 valueList.add(0X50);
		titleList.add("Q");                 valueList.add(0X51);
		titleList.add("R");                 valueList.add(0X52);
		titleList.add("S");                 valueList.add(0X53);
		titleList.add("T");                 valueList.add(0X54);
		titleList.add("U");                 valueList.add(0X55);
		titleList.add("V");                 valueList.add(0X56);
		titleList.add("W");                 valueList.add(0X57);
		titleList.add("X");                 valueList.add(0X58);
		titleList.add("Y");                 valueList.add(0X59);
		titleList.add("Z");                 valueList.add(0X5A);
		titleList.add("NUM0");              valueList.add(VirtualKey.VK_NUMPAD0);
		titleList.add("NUM1");              valueList.add(VirtualKey.VK_NUMPAD1);
		titleList.add("NUM2");              valueList.add(VirtualKey.VK_NUMPAD2);
		titleList.add("NUM3");              valueList.add(VirtualKey.VK_NUMPAD3);
		titleList.add("NUM4");              valueList.add(VirtualKey.VK_NUMPAD4);
		titleList.add("NUM5");              valueList.add(VirtualKey.VK_NUMPAD5);
		titleList.add("NUM6");              valueList.add(VirtualKey.VK_NUMPAD6);
		titleList.add("NUM7");              valueList.add(VirtualKey.VK_NUMPAD7);
		titleList.add("NUM8");              valueList.add(VirtualKey.VK_NUMPAD8);
		titleList.add("NUM9");              valueList.add(VirtualKey.VK_NUMPAD9);
		titleList.add("F1");                valueList.add(VirtualKey.VK_F1);
		titleList.add("F2");                valueList.add(VirtualKey.VK_F2);
		titleList.add("F3");                valueList.add(VirtualKey.VK_F3);
		titleList.add("F4");                valueList.add(VirtualKey.VK_F4);
		titleList.add("F5");                valueList.add(VirtualKey.VK_F5);
		titleList.add("F6");                valueList.add(VirtualKey.VK_F6);
		titleList.add("F7");                valueList.add(VirtualKey.VK_F7);
		titleList.add("F8");                valueList.add(VirtualKey.VK_F8);
		titleList.add("F9");                valueList.add(VirtualKey.VK_F9);
		titleList.add("F10");               valueList.add(VirtualKey.VK_F10);
		titleList.add("F11");               valueList.add(VirtualKey.VK_F11);
		titleList.add("F12");               valueList.add(VirtualKey.VK_F12);
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

	public static Integer[] getValueArray() {
		return (Integer[]) valueList.toArray();
	}

	public static String[] getTitleArray() {
		return titleList.toArray(new String[0]);
	}
}
