package com.jiuqi.rpa.lib.find;

import java.util.List;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.browser.UIBrowser;
import com.jiuqi.rpa.lib.browser.WebBrowserType;

/**
 * 查找库管理器
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class FindLibraryManager {
	private UIAFindLibrary uialib;
	private WEBFindLibrary weblib;
	
	/**
	 * 构造器
	 * 
	 * @param context 活动上下文
	 */
	public FindLibraryManager(Context context) {
		this.uialib = new UIAFindLibrary(context);
		this.weblib = new WEBFindLibrary(context);
	}
	
	/**
	 * 根据指定位置获取UI元素
	 * 
	 * @param uiaPoint 指定的位置 
	 * @return 返回UI元素对象
	 * @throws LibraryException
	 */
	public IUIElement get(Point uiaPoint) throws LibraryException {
		UIARect uiaRect = uialib.getRect(uiaPoint);
		if (uiaRect.isBrowserClient) {
			Point webPoint = new Point(uiaPoint.x - uiaRect.x, uiaPoint.y - uiaRect.y);
			BrowserInfo browserInfo = uialib.getBrowserInfo(uiaPoint);
			return weblib.get(webPoint, browserInfo);
		} else
			return uialib.get(uiaPoint);
	}
	
	/**
	 * 根据指定位置获取窗体
	 * 
	 * @param uiaPoint 指定的位置 
	 * @return 返回窗体对象
	 * @throws LibraryException
	 */
	public IUIElement getWindow(Point uiaPoint) throws LibraryException {
		return uialib.getWindow(uiaPoint);
	}

	
	public IUIElement getWindow(IUIElement element) throws LibraryException{
		if(element instanceof WEBElement) {
			UIBrowser uiBrowser = ((WEBElement) element).getBrowser();
			
			BrowserInfo info = new BrowserInfo();
			WebBrowserType type = uiBrowser.getType();
			info.appication = WebBrowserType.parseTypeToProcessName(type);
			info.title =  uiBrowser.getPageTitle();
			info.url = uiBrowser.getCurrentUrl();
			return uialib.getWindow(info);
		}else {
			return uialib.getWindow(element);
		}
	}
	
	public IUIElement getWindow(Path path) throws LibraryException{
		return uialib.getWindow(path);
	}
	
	
	/**
	 * 根据指定位置获取对应元素的方框
	 * 
	 * @param uiaPoint 指定的位置
	 * @return 返回UI元素的方框
	 * @throws LibraryException
	 */
	public Rect getRect(Point uiaPoint) throws LibraryException {
		UIARect uiaRect = uialib.getRect(uiaPoint);
		if (uiaRect.isBrowserClient) {
			Point webPoint = new Point(uiaPoint.x - uiaRect.x, uiaPoint.y - uiaRect.y);
			BrowserInfo wInfo = uialib.getBrowserInfo(uiaPoint);
			System.out.println("BrowserInfo：" + wInfo);
			
			Rect webRect = weblib.getRect(webPoint, wInfo);
			if (webRect != null)
				return new Rect(uiaRect.x + webRect.x, uiaRect.y + webRect.y, webRect.w, webRect.h);
		}
		
		return uiaRect;
	}
	
	/**
	 * 根据指定位置获取窗体的方框
	 * 
	 * @param uiaPoint 指定的位置
	 * @return 返回窗体的方框
	 * @throws LibraryException
	 */
	public Rect getWinodwRect(Point uiaPoint) throws LibraryException {
		return uialib.getWindowRect(uiaPoint);
	}
	
	public Rect getRect(IUIElement uiElement) throws LibraryException {
		if (uiElement == null)
			return null;
		
		if (uiElement instanceof WEBElement) {
			WEBElement webElement = (WEBElement) uiElement;
			
			UIBrowser uiBrowser = webElement.getBrowser();
			BrowserInfo browserInfo = new BrowserInfo();
			browserInfo.appication = WebBrowserType.parseTypeToProcessName(uiBrowser.getType());
			browserInfo.title = uiBrowser.getPageTitle();
			browserInfo.url = uiBrowser.getCurrentUrl();
			UIARect uiaRect = uialib.getRect(browserInfo);
			
			Rect webRect = uiElement.getRect();
			return new Rect(uiaRect.x + webRect.x, uiaRect.y + webRect.y, webRect.w, webRect.h);
		} else
			return uiElement.getRect();
	}
	
	/**
	 * 判断指定路径的UI元素是否存在
	 * 
	 * @param path 路径
	 * @return 存在返回true，否则返回false
	 * @throws LibraryException
	 */
	public boolean exists(Path path) {
		try {
			if (path.isWeb())
				return weblib.exists(path);
			else
				return uialib.exists(path);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 获取指定路径的元素，如果存在多个，返回第一个
	 * 
	 * @param path 路径
	 * @return 返回UI元素对象
	 * @throws LibraryException
	 */
	public IUIElement findFirst(Path path) throws LibraryException {
		if (path.isWeb())
			return weblib.findFirst(path);
		else
			return uialib.findFirst(path);
	}
	
	/**
	 * 获取指定路径的元素，如果存在多个，返回第一个
	 * 
	 * @param path 路径
	 * @param runtimeIdList 运行时标识列表
	 * @return 返回UI元素对象
	 * @throws LibraryException
	 */
	public IUIElement findFirst(Path path, List<String> runtimeIdList) throws LibraryException {
		if (path.isWeb())
			return weblib.findFirst(path, runtimeIdList);
		else
			return uialib.findFirst(path, runtimeIdList);
	}
	
	/**
	 * 获取指定路径的元素集合
	 * 
	 * @param path 路径
	 * @return 返回UI元素对象集合
	 * @throws LibraryException
	 */
	public IUIElement[] findAll(Path path) throws LibraryException {
		if (path.isWeb())
			return weblib.findAll(path);
		else
			return uialib.findAll(path);
	}
	
}
