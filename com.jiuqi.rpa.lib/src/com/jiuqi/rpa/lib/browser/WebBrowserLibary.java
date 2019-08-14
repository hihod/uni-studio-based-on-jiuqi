package com.jiuqi.rpa.lib.browser;

import java.util.Timer;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.jiuqi.bi.util.OrderGenerator;
import com.jiuqi.rpa.lib.LibraryException;

/**
 * Webä¯ÀÀÆ÷²Ù×÷Àà
 * 
 * @author zhouxubo.jiuqi.com.cn,wangshanyu
 */
public class WebBrowserLibary {
	private static String SeleniumPath = (String) System.getenv().get("APPDATA") + System.getProperty("file.separator") + "RPA"+ System.getProperty("file.separator") +"Selenium";
	private static String FIREFOX_DRIVER = SeleniumPath + System.getProperty("file.separator") + "geckodriver.exe";
	private static String FIREFOX_DIR = "";
	private static String CHROME_DRIVER = SeleniumPath + System.getProperty("file.separator") + "chromedriver.exe";
	private static String IE_DRIVER = SeleniumPath + System.getProperty("file.separator") + "IEDriverServer.exe";
	private static Timer timer = new Timer();
	private static boolean isTimerRunning = false;
	public static UIBrowser open(WebBrowserType type) throws LibraryException{
		return open(type, "");
	}
	
	/**
	 * open browser with type and initial url<br>
	 * <br>
	 * <em>Normally , each {@link WebBrowserLibary}.open create a UIBroser
	 * Object,</em> <br>
	 * <em>but , tab pages created manually or by hyperlinks are not mapped .</em>
	 * <br>
	 * <em>use title,url and type to find it ,then mapped automatically</em> <br>
	 * <em>see
	 * {@link #getBrowserByTitleUrlAndType(String, String, WebBrowserType)}</em>
	 * 
	 * @param type ,url
	 * @return {@link UIBrowser}
	 * @throws LibraryException
	 */
	public static UIBrowser open(WebBrowserType type,String initUrl) throws LibraryException {
		RemoteWebDriver driver;
		UIBrowser result = null;
		if(initUrl == null || initUrl.isEmpty())
			initUrl = "localhost";
		if(initUrl.indexOf("http") == -1) initUrl="http://"+initUrl;
		switch (type) {
		case CHROME:
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,CHROME_DRIVER);
			ChromeOptions coptions = new ChromeOptions();
			coptions.addArguments("--start-maximized");
			driver = new ChromeDriver(coptions);
			driver.get(initUrl);
			break;
		case FIREFOX:
			System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY,FIREFOX_DRIVER);
			//System.setProperty(FirefoxDriver.SystemProperty.BROWSER_BINARY,FIREFOX_DIR);
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.get(initUrl);
			break;
		case IE:
			System.setProperty(InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY,IE_DRIVER);
			InternetExplorerOptions ieo = new InternetExplorerOptions();
			ieo.withInitialBrowserUrl(initUrl);
			driver = new InternetExplorerDriver(ieo);
			driver.manage().window().maximize();
			break;
		case EDGE:
			System.setProperty(EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY, "");
			driver = new EdgeDriver();
			break;
		default:
			throw new LibraryException("Unknown browser type [" + type.value + "]");
		}
		long tid = OrderGenerator.newOrderID();
		result = new UIBrowser(driver, driver.getWindowHandle(), tid, type);
		WebBrowserManager.getInstance().put(tid, result);
		if(!isTimerRunning) {
			timer.schedule(new RemoteDriverTimerTask(), 1000,5000);
			isTimerRunning = true;
		}
		return result;
	}

	
	/**
	 * close all pages and quit all drivers;
	 */
	public static void releaseAll() {
		WebBrowserManager.getInstance().releaseAll();
	}
	
	public static void release(Long[] ids) {
		for(Long id :ids) {
			WebBrowserManager.getInstance().release(id);
		}
	}
	
	public static void cancleTimer() {
		try {
		if(isTimerRunning)
			timer.cancel();
		}catch (Exception e) {
		}
	}

}
