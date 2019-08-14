package com.jiuqi.rpa.lib.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.ContextProvider;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.browser.UIBrowser;
import com.jiuqi.rpa.lib.browser.WebBrowserManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.WEBElement;

/**
 * web树形操作库
 * 
 * @author wangshanyu
 *
 */
public class WEBTreeWalker extends ContextProvider implements ITreeWalker {

	private long browserId = -1;

	public WEBTreeWalker(long browserId, Context context) {
		super(context);
		this.browserId = browserId;
	}

	public IUIElement getRoot() throws LibraryException {
		try {
			UIBrowser browser = WebBrowserManager.getInstance().getBrowser(browserId);
			//browser.doInit();
			String id = (String) browser.executeScript("return JQWEB.getRoot()");
			WEBElement element = new WEBElement(browserId, Long.parseLong(id));
			return (WEBElement) getContext().add(element);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browserId + "] 下  getRoot 失败" + e.getMessage(),e);
		}
	}

	public IUIElement[] getChildren(IUIElement parent) throws LibraryException {
		try {
			UIBrowser browser = WebBrowserManager.getInstance().getBrowser(browserId);
			//browser.doInit();
			String idlist = (String) browser.executeScript("return JQWEB.getChildren(arguments[0])",
					((WEBElement)parent).getElementId() + "");
			JSONArray ja = new JSONArray(idlist);
			List<IUIElement> eles = new ArrayList<IUIElement>();
			for (int i = 0; i < ja.length(); i++) {
				String tid = ja.getString(i);
				WEBElement element = new WEBElement(browserId, Long.parseLong(tid));
				eles.add((WEBElement) getContext().add(element));
			}
			return eles.toArray(new IUIElement[0]);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browserId + "] 下  getChildren 失败" + e.getMessage(),e);
		}
	}

	public IUIElement getParent(IUIElement child) throws LibraryException {
		try {
			UIBrowser browser = WebBrowserManager.getInstance().getBrowser(browserId);
			//browser.doInit();
			String tid = (String) browser.executeScript("return JQWEB.getParent(arguments[0])", ((WEBElement)child).getElementId() + "");
			WEBElement element = new WEBElement(browserId, Long.parseLong(tid));
			return (WEBElement) getContext().add(element);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browserId + "] 下  getParent 失败" + e.getMessage(),e);
		}
	}

	public String getText(IUIElement uiElement) throws LibraryException {
		try {
			UIBrowser browser = WebBrowserManager.getInstance().getBrowser(browserId);
			//browser.doInit();
			System.out.println("return JQWEB.getText_tree(\""+((WEBElement)uiElement).getElementId()+"\")");
			String text = (String) browser.executeScript("return JQWEB.getText_tree(arguments[0])",
					((WEBElement)uiElement).getElementId() + "");
			return text;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browserId + "] 下  getText_tree 失败" + e.getMessage(),e);
		}
	}

	public Properties getProperties(IUIElement uiElement) throws LibraryException {
		if (uiElement == null)
			return null;
		
		Properties props = new Properties();
		try {
			UIBrowser browser = WebBrowserManager.getInstance().getBrowser(browserId);
			if (browser == null)
				return null;
			
			//browser.doInit();
			String r = (String) browser.executeScript("return JQWEB.getProperties(arguments[0])", ((WEBElement)uiElement).getElementId() + "");
			if (r == null || r.isEmpty())
				return props;
			
			JSONObject propja = new JSONObject(r);
			@SuppressWarnings("unchecked")
			Iterator<String> keys = propja.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				props.setProperty(key, propja.getString(key));
			}
			return props;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行browser[" + browserId + "] 下  getProperties 失败" + e.getMessage(),e);
		}
	}

}
