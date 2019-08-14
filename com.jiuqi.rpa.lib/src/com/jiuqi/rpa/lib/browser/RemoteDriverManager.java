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
	 * ��driver���뻺�棬driver���ɷ���ʱ���׳��쳣
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
			throw new LibraryException("����RemoteWebDriverʱ����ȡsessionidʧ�ܣ�" + e.getMessage(), e);
		}
		return sessionId;
	}

	/**
	 * ����sessionid��ȡdriver���Ѵ���driver�ѹرյ����
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
	 * ����sessionid�ж�driver�Ƿ���ã�������ʱ�����map���Ƴ�
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
	 * �ж�driver�Ƿ���ã�������ʱ��driver�˳�
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
	 * ����sessionid��ȡdriver�µ�����windowhandle
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
	 * �ж�driver�µ�window�Ƿ�ɷ���
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
	 * ��ȡ�����п��õ�driver�б������õ��Ƴ�
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
	 * ��ȡ�����п��õ�driver��session�б������õ��Ƴ�
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
	 * �ͷ�driver���Ƴ�����
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
