package com.jiuqi.rpa.lib.browser;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.script.ScriptInitor;

/**
 * Selenium web Object <br>
 * <em>Attention , one browser here ,is one windowHandle about
 * remoteWebDriver</em> <br>
 * <em>That means, it is only one page in which browser you
 * {@link WebBrowserLibary}.open</em>
 * 
 * @author zhouxubo.jiuqi.com.cn,wangshanyu
 */
public class UIBrowser implements IUIHandler {
	private long id = -1;
	private String sessionid = "";
	private String tagWindowHandle;
	private WebBrowserType type = null;
	private String url = "";
	private String title = "";
	

	UIBrowser(RemoteWebDriver driver, String tagWindowHandle, long id, WebBrowserType type) {
		
		this.tagWindowHandle = tagWindowHandle;
		this.id = id;
		this.type = type;
		try {
			this.sessionid= RemoteDriverManager.getInstance().put(driver,type);
			this.url = getCurrentUrl();
			this.title = getPageTitle();
		} catch (LibraryException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isReachable() {
		return RemoteDriverManager.getInstance().isWindowHandleReachable(sessionid, tagWindowHandle);
	}

	/**
	 * get page's RemoteWebDriver
	 * 
	 * @return {@link RemoteWebDriver}
	 * @throws LibraryException
	 */
	public RemoteWebDriver getDriver() throws LibraryException {
			if (sessionid == null || tagWindowHandle == null)
				throw new LibraryException("cannot get driver with"
						+ (sessionid == null ? (tagWindowHandle == null ? "handle" : "") + "sessionid is"
								: "handle is")
						+ " null");
			RemoteWebDriver browserDriver = null;
			try {
				browserDriver = RemoteDriverManager.getInstance().getDriverBySessionId(sessionid);
				if (!browserDriver.getWindowHandle().equalsIgnoreCase(tagWindowHandle))
					browserDriver.switchTo().window(tagWindowHandle);
			} catch (Exception e) {
				System.err.println("window is already closed! switchTo err;");
				WebBrowserManager.getInstance().removeUnreachableWindowFromMap(getId());
			}
			return browserDriver;
	}

	public RemoteWebDriver getDriverWithoutSwitchTo() {
		return RemoteDriverManager.getInstance().getDriverBySessionId(sessionid);
	}
	
	/**
	 * 
	 * @return {@link WebBrowserType}
	 */
	public WebBrowserType getType() {
		return this.type;
	}

	/**
	 * 
	 * @return current page's url
	 * @throws LibraryException
	 */
	public String getCurrentUrl() throws LibraryException {
			this.url = getDriver().getCurrentUrl();
			if (url.charAt(url.length() - 1) == '/')
				return url.substring(0, url.length() - 1);
			return url;
	}

	/**
	 * 
	 * @return current page's Title
	 * @throws LibraryException
	 */
	public String getPageTitle() throws LibraryException {
		this.title = getDriver().getTitle();
		return title;
	}

	/**
	 * @return current page's windowHandle
	 */
	public String getWindowHandle() {
			return tagWindowHandle;
	}

	/**
	 * @return current page's sessionid_windowHandle
	 */
	public String getSessionWndHandle() {
			return sessionid+ "_" +tagWindowHandle;
	}

	public long getId() {
		return id;
	}

	/**
	 * close tab page <em>if no page in RemoteWebDriver , close and quit the driver
	 * </em>
	 */
	public void close() throws LibraryException {
		if (id == -1)
			throw new LibraryException("找不到ID[" + id + "]对应的driver！");
		try {
			if(WebBrowserManager.getInstance().getBrowser(id) == null) return;
			RemoteWebDriver driver = getDriver();
			try {
				driver.close();
			} catch (Exception e) {
			}
			WebBrowserManager.getInstance().removeUnreachableWindowFromMap(id);
		} catch (Exception e) {
			throw new LibraryException("关闭页签失败！" + e.getMessage(), e);
		}
	}
	
	public void release() throws LibraryException {
		
	}

	/**
	 * 
	 * @throws LibraryException
	 */
	public void attach() throws LibraryException {
		if (id == -1)
			throw new LibraryException("找不到ID[" + id + "]对应的driver！");
		getDriver();
	}

	/**
	 * do navigate on current page
	 * 
	 * @param url
	 * @throws LibraryException
	 */
	public void navigateTo(String url) throws LibraryException {
		if (id == -1)
			throw new LibraryException("找不到ID[" + id + "]对应的driver！");
		try {
			RemoteWebDriver driver = getDriver();
			if(url.indexOf("http") == -1) url="http://"+url;
			driver.get(url);
		} catch (Exception e) {
			throw new LibraryException("跳转到URL[" + url + "]失败！" + e.getMessage(),e);
		}
	}

	/**
	 * @throws LibraryException
	 */
	public void doInit() throws LibraryException {
		if (id == -1)
			throw new LibraryException("找不到ID[" + id + "]对应的driver！");
		try {
			RemoteWebDriver driver = getDriver();
			String isInj = (String) driver.executeScript("return typeof(window.JQWEB);");
			if ("undefined".equals(isInj)) {
				ScriptInitor.initScript(driver, "JQWEB.js");
				ScriptInitor.initScript(driver, "TraceMessage.js");
				ScriptInitor.initScript(driver, "IDManager.js");
				ScriptInitor.initScript(driver, "ElementLibrary.js");
				ScriptInitor.initScript(driver, "TreeLibrary.js");
				ScriptInitor.initScript(driver, "FindLibrary.js");
				ScriptInitor.initScript(driver, "SafePage.js");
				ScriptInitor.initScript(driver, "HTMLElementFunction.js");
				ScriptInitor.initScript(driver, "Utils.js");
				ScriptInitor.initScript(driver, "PageRectUtil.js");
			}
		} catch (Exception e) {
			throw new LibraryException("初始化浏览器自动化脚本失败！browserId[" + id + "] " + e.getMessage(), e);
		}
	}

	/**
	 * use before each browser operation.
	 * 
	 * @param id
	 * @throws LibraryException
	 */
	public static void doInit(long id) throws LibraryException {
		if (id == -1)
			throw new LibraryException("找不到ID[" + id + "]对应的driver！");
		try {
			RemoteWebDriver driver = WebBrowserManager.getInstance().getBrowser(id).getDriver();
			String isInj = (String) driver.executeScript("return typeof(window.JQWEB);");
			if ("undefined".equals(isInj)) {
				ScriptInitor.initScript(driver, "JQWEB.js");
				ScriptInitor.initScript(driver, "TraceMessage.js");
				ScriptInitor.initScript(driver, "IDManager.js");
				ScriptInitor.initScript(driver, "ElementLibrary.js");
				ScriptInitor.initScript(driver, "TreeLibrary.js");
				ScriptInitor.initScript(driver, "FindLibrary.js");
				ScriptInitor.initScript(driver, "SafePage.js");
				ScriptInitor.initScript(driver, "HTMLElementFunction.js");
				ScriptInitor.initScript(driver, "Utils.js");
			}
		} catch (Exception e) {
			throw new LibraryException("初始化浏览器自动化脚本失败！browserId[" + id + "] " + e.getMessage(), e);
		}
	}

	/**
	 * execute Script in browser and return some like String,int,double,boolean and
	 * {@link WebElement}
	 * 
	 * @param id
	 * @param script
	 * @param args
	 * @return
	 * @throws LibraryException
	 */
	public Object executeScript(String script, Object... args) throws LibraryException {
		try {
			doInit();
			Thread.sleep(100);
			RemoteWebDriver driver = getDriver();
			return driver.executeScript(script, args);
		} catch (Exception e) {
			throw new LibraryException("driver[" + id + "] execute script failed！" + e.getMessage(), e);
		}

	}
}
