package com.jiuqi.etl.rpa.runtime;

import java.util.ArrayList;
import java.util.List;

import com.jiuqi.rpa.lib.browser.WebBrowserType;

/**
 * ¿ÉÓÃä¯ÀÀÆ÷ÀàÐÍ
 * 
 * @author wangshanyu
 *
 */
public class BrowserTypePackage {
	private static List<WebBrowserType> enumList = new ArrayList<WebBrowserType>();
	private static List<Integer> keyList = new ArrayList<Integer>();
	private static List<String> titleList = new ArrayList<String>();
	
	static {
		enumList.add(WebBrowserType.CHROME);
		enumList.add(WebBrowserType.IE);
		enumList.add(WebBrowserType.FIREFOX);

		keyList.add(WebBrowserType.CHROME.key);
		keyList.add(WebBrowserType.IE.key);
		keyList.add(WebBrowserType.FIREFOX.key);

		titleList.add("¹È¸èä¯ÀÀÆ÷(chrome)");
		titleList.add("ieä¯ÀÀÆ÷(iexplore)");
		titleList.add("»ðºüä¯ÀÀÆ÷(firefox)");
	}

	public static int getIndex(WebBrowserType p) {
		return enumList.indexOf(p);
	}

	public static int getIndex(Integer p) {
		return keyList.indexOf(p);
	}

	public static int getIndex(String p) {
		return titleList.indexOf(p);
	}

	public static WebBrowserType getBrowserType(int i) {
		return enumList.get(i);
	}

	public static Integer getValue(int i) {
		return keyList.get(i);
	}

	public static String getTitle(int i) {
		return titleList.get(i);
	}

	public static String[] getTitleArray() {
		return titleList.toArray(new String[0]);
	}
}
