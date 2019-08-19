package com.jiuqi.rpa.lib.browser;

import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.jiuqi.bi.util.OrderGenerator;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.BrowserInfo;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.find.PathAttribute;
import com.jiuqi.rpa.lib.find.PathElement;

public class WebBrowserManager {

	private final static WebBrowserManager instance = new WebBrowserManager();

	public static WebBrowserManager getInstance() {
		return instance;
	}

	private UIBrowser lastActiveBrowser = null;
	private Set<UIBrowserCacheInfo> cacheSet = new HashSet<UIBrowserCacheInfo>();
	private boolean doRecordCache = false;
	private Map<Long, UIBrowser> browserMap = new LinkedHashMap<Long, UIBrowser>();

	public void startRecordTitleUrlCache() {
		doRecordCache = true;
	}

	public void stopRecordTitleUrlCache() {
		doRecordCache = false;
		cacheSet.clear();
	}

	public void put(Long id, UIBrowser browser) {
		browserMap.put(id, browser);
		lastActiveBrowser = browser;
	}

	public UIBrowser getBrowser(Long id) {
		if (browserMap.containsKey(id)) {
			UIBrowser browser = browserMap.get(id);
			lastActiveBrowser = browser;
			return browser;
		}
		return null;
	}

	public UIBrowser getBrowserByPath(Path path) throws LibraryException {
		try {
			PathElement browserPath = path.getElements().get(0);
			String title = null;
			String url = null;
			WebBrowserType type = null;
			for (PathAttribute pa : browserPath.getAttributes()) {
				if (pa == null)
					continue;
				if ("title".equalsIgnoreCase(pa.getName()))
					title = pa.getValue();
				else if ("url".equalsIgnoreCase(pa.getName()))
					url = pa.getValue();
				else if ("Application".equalsIgnoreCase(pa.getName())) {
					String crtTypeStr = pa.getValue();
					type = WebBrowserType.parseProcessNameToBrowserType(crtTypeStr);
				}
			}
			UIBrowser browser = getBrowserByTitleUrlAndType(title, url, type);
			lastActiveBrowser = browser;
			return browser;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("get driver by path failed" + e.getMessage(), e);
		}
	}

	public UIBrowser getBrowserByBrowserInfo(BrowserInfo info) throws LibraryException {
		WebBrowserType type = WebBrowserType.parseProcessNameToBrowserType(info.application);
		UIBrowser browser = getBrowserByTitleUrlAndType(info.title, info.url, type);
		lastActiveBrowser = browser;
		return browser;
	}


	/**
	 * @return
	 */
	private Set<String> getAllUIBrowserSessionIdAndHwndList() {
		Set<String> set = new HashSet<String>();
		for (UIBrowser b : browserMap.values()) {
			try {
				RemoteWebDriver driver = b.getDriver();
				if (driver == null) {
					continue;
				}
				set.add(b.getSessionWndHandle());
			} catch (Exception e) {
			}
		}
		return set;
	}

	protected void removeUnreachableWindowFromMap(long id) {
		if (browserMap.containsKey(id))
			browserMap.remove(id);
	}

	/**
	 * get all browser
	 * 
	 * @return {@link UIBrowser}
	 * @throws LibraryException
	 */
	public UIBrowser[] getBrowserArray() throws LibraryException {
		try {
			Collection<UIBrowser> collection = browserMap.values();
			UIBrowser[] browserarr = collection.toArray(new UIBrowser[0]);
			return browserarr;
		} catch (Exception e) {
			throw new LibraryException(e.getMessage(), e);
		}
	}

	/**
	 * get browser by title ,url ,and browser type;
	 * 
	 * @param title
	 * @param url
	 * @param type
	 * @return {@link UIBrowser}
	 * @throws LibraryException
	 */
	private UIBrowser getBrowserByTitleUrlAndType(String title, String url, WebBrowserType type)
			throws LibraryException {

		if (lastActiveBrowser != null) {
			try {
				boolean flag = isMatchTileUrlType(lastActiveBrowser, "*", title, type);
				if (flag)
					return lastActiveBrowser;
			} catch (Exception e) {
			}
		}

		if (cacheSet.size() > 1) {
			for (UIBrowserCacheInfo info : cacheSet.toArray(new UIBrowserCacheInfo[0])) {
				if (info.isMatch(title, url, type)) {
					return info.browser;
				}
			}
		}

		try {
			Collection<UIBrowser> values = browserMap.values();
			UIBrowser[] browsers = values.toArray(new UIBrowser[0]);

			for (int i = browsers.length - 1; i >= 0; i--) {
				UIBrowser crtUIB = browsers[i];
				boolean flag = isMatchTileUrlType(crtUIB, url, title, type);
				if (flag) {
					lastActiveBrowser = crtUIB;
					return crtUIB;
				}
			}

			Set<String> sessionHandles = getAllUIBrowserSessionIdAndHwndList();
			// 手动打开的浏览器页面中不包含查询的页面（链接打开的新页面||点击新页签按钮创建的新页面）
			for (String sessionid : RemoteDriverManager.getInstance().getDriverSessionList()) {
				try {
					RemoteWebDriver rwd = RemoteDriverManager.getInstance().getDriverBySessionId(sessionid);
					WebBrowserType browserType = RemoteDriverManager.getInstance().getDriverType(sessionid);
					for (String hwnd : RemoteDriverManager.getInstance().getDriverWindowHandles(sessionid)) {
						if (sessionHandles.contains(sessionid + "_" + hwnd))
							continue;
						
						rwd.switchTo().window(hwnd);
						String crtUrl = rwd.getCurrentUrl();
						String crtTitle = rwd.getTitle();

						if (type.key != browserType.key)
							continue;
						if (StringWildcardMatchWithoutOrder(crtUrl, url)) {
							long tid = OrderGenerator.newOrderID();
							UIBrowser tbrowser = new UIBrowser(rwd, hwnd, tid, browserType);
							browserMap.put(tid, tbrowser);
							if (doRecordCache) {
								cacheSet.add(new UIBrowserCacheInfo(tbrowser, crtTitle, crtUrl));
							}
							return tbrowser;
						} else if (StringWildcardMatchWithoutOrder(title, crtTitle)) {
							long tid = OrderGenerator.newOrderID();
							UIBrowser tbrowser = new UIBrowser(rwd, hwnd, tid, browserType);
							browserMap.put(tid, tbrowser);
							if (doRecordCache) {
								cacheSet.add(new UIBrowserCacheInfo(tbrowser, crtTitle, crtUrl));
							}
							return tbrowser;
						}
					}
				} catch (Exception e) {
				}
			}
			return null;
		} catch (Exception e) {
			throw new LibraryException(
					"browser[" + title + "][" + url + "][" + type.value + "] not found！" + e.getMessage(), e);
		}
	}
	private boolean isMatchTileUrlType(UIBrowser browser, String url, String title, WebBrowserType type)
			throws Exception {

		try {
			if (browser.getDriver() == null)
				throw new Exception();
			browser.getDriver().getCurrentUrl();
		} catch (Exception e) {
			return false;
		}
		try {
			String crtUrl = browser.getCurrentUrl().trim();
			String crtTitle = browser.getPageTitle().trim();
			boolean flag = false;
			if (doRecordCache) {
				cacheSet.add(new UIBrowserCacheInfo(browser, crtTitle, crtUrl));
			}
			url = url.charAt(url.length() - 1) == '/' ? url.substring(0, url.length() - 1) : url;
			crtUrl = crtUrl.charAt(crtUrl.length() - 1) == '/' ? crtUrl.substring(0, crtUrl.length() - 1) : crtUrl;
			try {
				url = URLDecoder.decode(url, "utf-8");
				crtUrl = URLDecoder.decode(crtUrl, "utf-8");
			} catch (Exception e) {
			}

			if (type.key != browser.getType().key)
				return false;
			if (StringWildcardMatchWithoutOrder(url,crtUrl))
				flag = true;
			else
				return false;
			if (StringWildcardMatchWithoutOrder(crtTitle,title))
				flag = true;
			else
				return false;
			return flag;
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
	}
	public static boolean StringWildcardMatchWithoutOrder(String pText1,String pText2) {
		if(pText1!= null)
		pText1 = pText1.toLowerCase();
		if(pText2!= null)
			pText2 = pText2.toLowerCase();
		return StringWildcardMatch(pText1,pText2)||StringWildcardMatch(pText2,pText1);
	}
	/**
     * 通配符算法。 可以匹配"*"和"?"
     * 如a*b?d可以匹配aAAAbcd
     * @param pattern 匹配表达式
     * @param str 匹配的字符串
     * @return
     */
    public static boolean StringWildcardMatch(String pattern, String str) {
        if (pattern == null || str == null)
            return false;
 
        boolean result = false;
        char c; // 当前要匹配的字符串
        boolean beforeStar = false; // 是否遇到通配符*
        int back_i = 0;// 回溯,当遇到通配符时,匹配不成功则回溯
        int back_j = 0;
        int i, j;
        for (i = 0, j = 0; i < str.length();) {
            if (pattern.length() <= j) {
                if (back_i != 0) {// 有通配符,但是匹配未成功,回溯
                    beforeStar = true;
                    i = back_i;
                    j = back_j;
                    back_i = 0;
                    back_j = 0;
                    continue;
                }
                break;
            }
 
            if ((c = pattern.charAt(j)) == '*') {
                if (j == pattern.length() - 1) {// 通配符已经在末尾,返回true
                    result = true;
                    break;
                }
                beforeStar = true;
                j++;
                continue;
            }
 
            if (beforeStar) {
                if (str.charAt(i) == c) {
                    beforeStar = false;
                    back_i = i + 1;
                    back_j = j;
                    j++;
                }
            } else {
                if (c != '?' && c != str.charAt(i)) {
                    result = false;
                    if (back_i != 0) {// 有通配符,但是匹配未成功,回溯
                        beforeStar = true;
                        i = back_i;
                        j = back_j;
                        back_i = 0;
                        back_j = 0;
                        continue;
                    }
                    break;
                }
                j++;
            }
            i++;
        }
 
        if (i == str.length() && j == pattern.length())// 全部遍历完毕
            result = true;
        return result;
    }

	/**
	 * close all pages and quit all drivers;
	 */
	public void releaseAll() {
		Set<Long> keySet = browserMap.keySet();
		Long[] array = keySet.toArray(new Long[0]);
		release(array);
		WebBrowserLibary.cancleTimer();
	}

	public void release(Long[] ids) {
		for (Long id : ids) {
			release(id);
		}
	}

	public void release(Long id) {
		try {
			browserMap.get(id).close();
		} catch (Exception e) {
			browserMap.remove(id);
		}
	}

	/**
	 * @deprecated 暂时无用
	 */
	public static String getEncoding(String str) {
		String encode;
		encode = "UTF-16";
		try {
			if (str.equals(new String(str.getBytes(), encode))) {
				return encode;
			}
		} catch (Exception ex) {
		}
		encode = "ASCII";
		try {
			if (str.equals(new String(str.getBytes(), encode))) {
				return "字符串<< " + str + " >>中仅由数字和英文字母组成，无法识别其编码格式";
			}
		} catch (Exception ex) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(), encode))) {
				return encode;
			}
		} catch (Exception ex) {
		}
		encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(), encode))) {
				return encode;
			}
		} catch (Exception ex) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(), encode))) {
				return encode;
			}
		} catch (Exception ex) {
		}

		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(), encode))) {
				return encode;
			}
		} catch (Exception ex) {
		}
		return "未识别编码格式";
	}

}

class UIBrowserCacheInfo {
	public String title;
	public String url;
	public UIBrowser browser;

	public UIBrowserCacheInfo() {
		this.browser = null;
		this.title = "";
		this.url = "";
	}

	public UIBrowserCacheInfo(UIBrowser browser, String title, String url) {
		this.browser = browser;
		this.title = title;
		this.url = url;
	}

	public boolean isMatch(String crtTitle, String crtUrl, WebBrowserType type) {
		boolean flag = false;
		url = url.charAt(url.length() - 1) == '/' ? url.substring(0, url.length() - 1) : url;
		crtUrl = crtUrl.charAt(crtUrl.length() - 1) == '/' ? crtUrl.substring(0, crtUrl.length() - 1) : crtUrl;
		try {
			url = URLDecoder.decode(url, "utf-8");
			crtUrl = URLDecoder.decode(crtUrl, "utf-8");
		} catch (Exception e) {
		}
		if (type.key != browser.getType().key)
			return false;
		if (WebBrowserManager.StringWildcardMatchWithoutOrder(crtUrl, url))
			flag = true;
		else
			return false;
		if (WebBrowserManager.StringWildcardMatchWithoutOrder(crtTitle, title))
			flag = true;
		else
			return false;
		return flag;
	}
}
