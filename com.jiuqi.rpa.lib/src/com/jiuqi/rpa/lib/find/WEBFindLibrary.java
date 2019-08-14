package com.jiuqi.rpa.lib.find;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.ContextProvider;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.browser.UIBrowser;
import com.jiuqi.rpa.lib.browser.WebBrowserManager;

/**
 * web查找库
 * 
 * @author wangshanyu
 *
 */
public class WEBFindLibrary extends ContextProvider implements IFindLibary {
	private UIAFindLibrary uiaFindLibrary;

	public WEBFindLibrary(Context context) {
		super(context);
		this.uiaFindLibrary = new UIAFindLibrary(context);
	}

	public boolean exists(Path path) throws LibraryException {
		UIBrowser uiBrowser = null;
		try {
			if (path.getElements().size() == 1) {
				uiBrowser = WebBrowserManager.getInstance().getBrowserByPath(path);
				if (uiBrowser != null)
					return true;
			}
			uiBrowser = WebBrowserManager.getInstance().getBrowserByPath(path);
			if (uiBrowser == null)
				return false;
			String r = (String) uiBrowser.executeScript("return JQWEB.exists(arguments[0])", path.toJson().toString());
			System.out.println("exists:" + r);
			if (StringUtils.isEmpty(r))
				return false;
			return Boolean.parseBoolean(r);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + uiBrowser.getId() + "] 下  exists 失败" + e.getMessage(), e);
		}
	}

	public IUIElement get(Point point) throws LibraryException {
		BrowserInfo browserInfo = uiaFindLibrary.getBrowserInfo(point);

		UIBrowser browser = WebBrowserManager.getInstance().getBrowserByBrowserInfo(browserInfo);
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");

		try {
			JSONObject jo = new JSONObject();
			jo.put("x", point.x);
			jo.put("y", point.y);
			String id = (String) browser.executeScript("return JQWEB.get(arguments[0])", jo.toString());
			WEBElement element = new WEBElement(browser.getId(), Long.parseLong(id));
			return (WEBElement) getContext().add(element);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  get 失败" + e.getMessage(), e);
		}
	}

	public IUIElement getTableRoot(WEBElement ele) throws LibraryException {
		try {
			UIBrowser browser = WebBrowserManager.getInstance().getBrowser(ele.getBrowserId());
			String id = (String) browser.executeScript("return JQWEB.getTableRoot(arguments[0])", ele.getElementId()+"");
			WEBElement element = new WEBElement(browser.getId(), Long.parseLong(id));
			return (WEBElement) getContext().add(element);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + ele.getBrowserId()+ "] 下  getTableRoot 失败" + e.getMessage(), e);
		}
	}

	public IUIElement get(Point point, BrowserInfo browserInfo) throws LibraryException {
		UIBrowser browser = WebBrowserManager.getInstance().getBrowserByBrowserInfo(browserInfo);
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");

		try {
			JSONObject jo = new JSONObject();
			jo.put("x", point.x);
			jo.put("y", point.y);
			String id = (String) browser.executeScript("return JQWEB.get(arguments[0])", jo.toString());
			WEBElement element = new WEBElement(browser.getId(), Long.parseLong(id));
			return (WEBElement) getContext().add(element);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  get 失败" + e.getMessage(), e);
		}
	}

	public Rect getRect(Point point) throws LibraryException {
		BrowserInfo browserInfo = uiaFindLibrary.getBrowserInfo(point);

		UIBrowser browser = WebBrowserManager.getInstance().getBrowserByBrowserInfo(browserInfo);
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");

		try {
			JSONObject jo = new JSONObject();
			jo.put("x", point.x);
			jo.put("y", point.y);
			String rectstr = (String) browser.executeScript("return JQWEB.getRect_find(arguments[0])", jo.toString());
			Rect r = new Rect();
			JSONObject rect = new JSONObject(rectstr);
			r.h = rect.getInt("height");
			r.w = rect.getInt("width");
			r.x = rect.getInt("left");
			r.y = rect.getInt("top");
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  getRect 失败" + e.getMessage(), e);
		}
	}

	public Rect getRect(Point point, BrowserInfo browserInfo) throws LibraryException {
		UIBrowser browser = WebBrowserManager.getInstance().getBrowserByBrowserInfo(browserInfo);
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");

		try {
			JSONObject jo = new JSONObject();
			jo.put("x", point.x);
			jo.put("y", point.y);

			String rectstr = (String) browser.executeScript("return JQWEB.getRect_find(arguments[0])", jo.toString());
			Rect r = new Rect();
			JSONObject rect = new JSONObject(rectstr);
			r.h = rect.getInt("height");
			r.w = rect.getInt("width");
			r.x = rect.getInt("left");
			r.y = rect.getInt("top");
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  getRect 失败" + e.getMessage(), e);
		}
	}

	public IUIElement findFirst(Path path) throws LibraryException {
		UIBrowser browser = WebBrowserManager.getInstance().getBrowserByPath(path);
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");

		try {
			String id = (String) browser.executeScript("return JQWEB.findFirst(arguments[0])",
					path.toJson().toString());
			if (id != null && !id.isEmpty()) {
				WEBElement element = new WEBElement(browser.getId(), Long.parseLong(id));
				return (WEBElement) getContext().add(element);
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  findFirst 失败" + e.getMessage(), e);
		}
	}
	
	public IUIElement findFirst(Path path, List<String> runtimeIdList) throws LibraryException {
		UIBrowser browser = WebBrowserManager.getInstance().getBrowserByPath(path);
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");

		try {
			String id = (String) browser.executeScript("return JQWEB.findFirst(arguments[0])",
					path.toJson().toString());
			
			if (id != null && !id.isEmpty()) {
				WEBElement element = new WEBElement(browser.getId(), Long.parseLong(id));
				
				Path tpath = element.getPath();
				List<PathElement> telements = tpath.getElements();
				for(int i = 0 ; i<telements.size();i++) {
					runtimeIdList.add(telements.get(i).getRuntimeId());
				}
				return (WEBElement) getContext().add(element);
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  findFirst 失败" + e.getMessage(), e);
		}
	}
	
	public IUIElement findFirstByParent(Path path,WEBElement parent) throws LibraryException{

		UIBrowser browser = WebBrowserManager.getInstance().getBrowserByPath(path);
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");

		try {
			String id = (String) browser.executeScript("return JQWEB.findFirst(arguments[0],arguments[1])",
					path.toJson().toString(),parent.getElementId());
			if (id != null && !id.isEmpty()) {
				WEBElement element = new WEBElement(browser.getId(), Long.parseLong(id));
				return (WEBElement) getContext().add(element);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  findFirst 失败" + e.getMessage(), e);
		}
	}
	
	public List<String> getRuntimeIdList(Path path) throws LibraryException{
		List<String> runtimeIdList = new ArrayList<String>();
		UIBrowser browser = WebBrowserManager.getInstance().getBrowserByPath(path);
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");
		try {
			String id = (String) browser.executeScript("return JQWEB.findFirst(arguments[0])",
					path.toJson().toString());
			
			if (id != null && !id.isEmpty()) {
				String idlist = (String) browser.executeScript("return JQWEB.getPathElementsIdList(arguments[0])",
						id);
				runtimeIdList.clear();
				JSONArray ja = new JSONArray(idlist);
				for (int i = 0; i < ja.length(); i++) {
					String tid = ja.getString(i);
					runtimeIdList.add(tid);
				}
				return runtimeIdList;
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  getRuntimeIdList 失败" + e.getMessage(), e);
		}
	}
	
	public List<String> getRuntimeIdList(WEBElement element) throws LibraryException{
		List<String> runtimeIdList = new ArrayList<String>();
		UIBrowser browser = element.getBrowser();
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");
		try {
			String id = element.getElementId()+"";
			if (id != null && !id.isEmpty()) {
				String idlist = (String) browser.executeScript("return JQWEB.getPathElementsIdList(arguments[0])",
						id);
				runtimeIdList.clear();
				JSONArray ja = new JSONArray(idlist);
				for (int i = 0; i < ja.length(); i++) {
					String tid = ja.getString(i);
					runtimeIdList.add(tid);
				}
				return runtimeIdList;
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  getRuntimeIdList 失败" + e.getMessage(), e);
		}
	}

	public IUIElement[] findAll(Path path) throws LibraryException {
		UIBrowser browser = WebBrowserManager.getInstance().getBrowserByPath(path);
		if (browser == null)
			throw new LibraryException("未找到指定浏览器");

		try {
			String idlist = (String) browser.executeScript("return JQWEB.findAll(arguments[0])",
					path.toJson().toString());
			JSONArray ja = new JSONArray(idlist);
			List<IUIElement> eles = new ArrayList<IUIElement>();
			for (int i = 0; i < ja.length(); i++) {
				String tid = ja.getString(i);
				System.out.println(tid);
				WEBElement element = new WEBElement(browser.getId(), Long.parseLong(tid));
				eles.add((WEBElement) getContext().add(element));
			}
			return eles.toArray(new IUIElement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browser.getId() + "] 下  findAll 失败" + e.getMessage(), e);
		}
	}

}
