package com.jiuqi.rpa.lib.browser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import com.jiuqi.rpa.lib.LibraryException;

public class RemoteDriverManager {
	private final static RemoteDriverManager instance = new RemoteDriverManager();

	public static RemoteDriverManager getInstance() {
		return instance;
	}

	private Map<String, RemoteWebDriverWithType> driverMap = new LinkedHashMap<String, RemoteWebDriverWithType>();

	/**
	 * 将driver加入缓存，driver不可访问时，抛出异常
	 * @param drv
	 * @return
	 * @throws LibraryException
	 */
	public String put(RemoteWebDriver drv,WebBrowserType type) throws LibraryException {
		String sessionId = "";
		try {
			sessionId = drv.getSessionId().toString();
			driverMap.put(sessionId, new RemoteWebDriverWithType(drv, type));
		} catch (Exception e) {
			throw new LibraryException("缓存RemoteWebDriver时，获取sessionid失败！" + e.getMessage(), e);
		}
		return sessionId;
	}

	/**
	 * 根据sessionid获取driver，已处理driver已关闭等情况
	 * @param sessionid
	 * @return
	 */
	public RemoteWebDriver getDriverBySessionId(String sessionid) {
		RemoteWebDriver driver = null;
		if (driverMap.containsKey(sessionid)) {
			try {
				if(isDriverReachable(sessionid))
					driver = driverMap.get(sessionid).driver;
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		return driver;
	}

	/**
	 * 根据sessionid判断driver是否可用，不可用时将其从map中移除
	 * @param sessionid
	 * @return
	 */
	public boolean isDriverReachable(String sessionid) {
		boolean flag = false;
		try {
			RemoteWebDriver driver = driverMap.get(sessionid).driver;
			if(driver == null) return false;
			flag = isDriverReachable(driver);
		} catch (Exception e) {
		}
		if(!flag && driverMap.containsKey(sessionid)) 
			driverMap.remove(sessionid);
		return flag;
	}

	/**
	 * 判断driver是否可用，不可用时将driver退出
	 * @param driver
	 * @return
	 */
	public boolean isDriverReachable(RemoteWebDriver driver) {
		try {
			String handle = driver.getWindowHandle();
			if(handle == null || handle.isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			String[] keys = driverMap.keySet().toArray(new String[0]);
			for(String key:keys) {
				RemoteWebDriverWithType rwdwt = driverMap.get(key);
				if(rwdwt.driver == driver) {
					try {
						driverMap.remove(key);
					}catch (Exception ee) {
					}
				}
			}
			try {
				driver.close();
			} catch (Exception ee) {
			}
			try {
				driver.quit();
			} catch (Exception ee) {
			}
			return false;
		}
		return true;
	}

	/**
	 * 根据sessionid获取driver下的所有windowhandle
	 * @param sessionid
	 * @return
	 */
	public List<String> getDriverWindowHandles(String sessionid) {
		List<String> handles = new ArrayList<String>();
		try {
			RemoteWebDriver driver = driverMap.get(sessionid).driver;
			Set<String> windowHandles = driver.getWindowHandles();
			for (String hwnd : windowHandles) {
				handles.add(hwnd);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return handles;
	}

	/**
	 * 判断driver下的window是否可访问
	 * @param sessionid
	 * @param windowHandle
	 * @return
	 */
	public boolean isWindowHandleReachable(String sessionid, String windowHandle) {
		List<String> windowHandles = getDriverWindowHandles(sessionid);
		if (windowHandles.indexOf(windowHandle) != -1)
			return true;
		return false;
	}

	/**
	 * 获取缓存中可用的driver列表，不可用的移除
	 * @return
	 */
	public List<RemoteWebDriver> getDriverList() {
		List<RemoteWebDriver> list = new ArrayList<RemoteWebDriver>();
		RemoteWebDriverWithType[] drivers = driverMap.values().toArray(new RemoteWebDriverWithType[0]);

		for (RemoteWebDriverWithType driver : drivers) {
			if (isDriverReachable(driver.driver)) {
				list.add(driver.driver);
			}else {
				try {
					SessionId sessionId = driver.driver.getSessionId();
					if(driverMap.containsKey(sessionId.toString()))
						driverMap.remove(sessionId.toString());
				}catch (Exception e) {
				}
			}
		}
		return list;
	}

	/**
	 * 获取缓存中可用的driver的session列表，不可用的移除
	 * @return
	 */
	public List<String> getDriverSessionList() {
		List<String> sessionlist = new ArrayList<String>();
		for (RemoteWebDriver driver : getDriverList()) {
			try {
				sessionlist.add(driver.getSessionId().toString());
			}catch (Exception e) {
			}
		}
		return sessionlist;
	}
	/**
	 * 
	 * @return
	 */
	public List<String> getSessionWindowHandleList(){
		List<String> list = new ArrayList<String>();
		for(String session:getDriverSessionList()) {
			List<String> handles = getDriverWindowHandles(session);
			for(String handle : handles) {
				list.add(session+"_"+handle);
			}
		}
		return list;
	}
	/**
	 * get Driver {@link WebBrowserType}
	 * @param sessionid
	 * @return
	 */
	public WebBrowserType getDriverType(String sessionid) {
		if(driverMap.containsKey(sessionid)) {
			return driverMap.get(sessionid).type;
		}
		return null;
	}
	
	
	/**
	 * 释放driver并移除缓存
	 * @param sessionid
	 */
	public void release(String sessionid) {
		try {
			RemoteWebDriver driver = driverMap.get(sessionid).driver;
			driver.close();
			driver.quit();
			driverMap.remove(sessionid);
		}catch (Exception e) {
		}
	}
	
	public void releases(String[] sessions) {
		for(String sessionid:sessions) {
			release(sessionid);
		}
	}
	public void releaseAll() {
		String[] array = driverMap.keySet().toArray(new String[0]);
		releases(array);
	}
}
class RemoteWebDriverWithType{
	RemoteWebDriver driver = null;
	WebBrowserType type = null;
	public RemoteWebDriverWithType(RemoteWebDriver driver,WebBrowserType type) {
		this.driver = driver;
		this.type = type;
	}
	public RemoteWebDriverWithType() {}
}
