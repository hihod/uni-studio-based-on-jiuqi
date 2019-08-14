package com.jiuqi.rpa.lib.find;

import java.util.List;

import org.json.JSONException;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.ContextProvider;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA元素查找操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAFindLibrary extends ContextProvider implements IFindLibary {
	
	/**
	 * 构造器
	 * 
	 * @param context 活动上下文
	 */
	public UIAFindLibrary(Context context) {
		super(context);
	}
	
	public IUIElement get(Point point) throws LibraryException {
		try {
			long id = JQUIA.find_get(point);
			return (UIAElement) getContext().add(new UIAElement(id));
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public IUIElement getWindow(Point point) throws LibraryException {
		try {
			long id = JQUIA.find_getWindow(point);
			return (UIAElement) getContext().add(new UIAElement(id));
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	public IUIElement getWindow(Path path) throws LibraryException {
		try {
			long id = JQUIA.find_getWindowByPath(path.toJson().toString());
			return (UIAElement) getContext().add(new UIAElement(id));
		
		} catch (JSONException e) {
			throw new LibraryException("路径转json失败", e);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	public IUIElement getWindow(IUIElement element) throws LibraryException {
		try {
			long id = JQUIA.find_getWindowByElement(element.getId());
			return (UIAElement) getContext().add(new UIAElement(id));
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	public IUIElement getWindow(BrowserInfo info) throws LibraryException {
		try {
			long id = JQUIA.find_getWindowByBrowserInfo(info);
			return (UIAElement) getContext().add(new UIAElement(id));
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public UIARect getRect(Point point) throws LibraryException {
		try {
			UIARect rect = new UIARect();
			JQUIA.find_getRect(point, rect);
			return rect;
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public UIARect getWindowRect(Point point) throws LibraryException {
		try {
			UIARect rect = new UIARect();
			JQUIA.find_getWindowRect(point, rect);
			return rect;
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public UIARect getRect(BrowserInfo browserInfo) throws LibraryException {
		try {
			UIARect rect = new UIARect();
			JQUIA.find_getBrowserCientRect(browserInfo, rect);
			return rect;
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public BrowserInfo getBrowserInfo(Point point) throws LibraryException {
		try {
			BrowserInfo browserInfo = new BrowserInfo();
			JQUIA.find_getBrowserInfo(point, browserInfo);
			return browserInfo;
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public boolean exists(Path path) throws LibraryException {
		try {
			String pathJsonStr = path.toJson().toString();
			return JQUIA.find_exists(pathJsonStr);
		} catch (JSONException e) {
			throw new LibraryException("路径转json失败", e);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public IUIElement findFirst(Path path) throws LibraryException {
		try {
			String pathJsonStr = path.toJson().toString();
			long id = JQUIA.find_findFirst(pathJsonStr);
			return (UIAElement) getContext().add(new UIAElement(id));
		} catch (JSONException e) {
			throw new LibraryException("路径转json失败", e);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public IUIElement findFirst(Path path, List<String> runtimeIdList) throws LibraryException {
		try {
			String pathJsonStr = path.toJson().toString();
			long id = JQUIA.find_findFirstEx(pathJsonStr, runtimeIdList);
			return (UIAElement) getContext().add(new UIAElement(id));
		} catch (JSONException e) {
			throw new LibraryException("路径转json失败", e);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public IUIElement[] findAll(Path path) throws LibraryException {
		try {
			String pathJsonStr = path.toJson().toString();
			long[] ids = JQUIA.find_findAll(pathJsonStr);
			
			IUIElement[] uiElements = new IUIElement[ids.length];
			for (int i = 0; i < ids.length; i++) {
				long id = ids[i];
				uiElements[i] = (UIAElement) getContext().add(new UIAElement(id));
			}
			
			return uiElements;
		} catch (JSONException e) {
			throw new LibraryException("路径转json失败", e);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

}
