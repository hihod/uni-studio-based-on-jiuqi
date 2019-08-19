package com.jiuqi.rpa.lib.find;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jiuqi.bi.util.OrderGenerator;
import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.browser.BrowserExtensionsUtil;
import com.jiuqi.rpa.lib.browser.UIBrowser;
import com.jiuqi.rpa.lib.browser.WebBrowserManager;
import com.jiuqi.rpa.lib.mouse.MouseClickType;

/**
 * web操作对象
 * 
 * @author wangshanyu
 *
 */
public class WEBElement implements IUIElement {
	private long elementId = -1;
	private long browserId = -1;
	private long id = -1;

	public long getBrowserId() {
		return browserId;
	}

	public WEBElement(long browserId, long elementId) {
		this.browserId = browserId;
		this.elementId = elementId;
		this.id = OrderGenerator.newOrderID();
	}

	public UIBrowser getBrowser() throws LibraryException {
		return WebBrowserManager.getInstance().getBrowser(browserId);
	}

	public long getId() {
		return id;
	}

	public long getElementId() {
		return elementId;
	}

	public void release() throws LibraryException {
		return;
	}

	/**
	 * @author lpy
	 */
	public Rect getRect() throws LibraryException {
		throw new LibraryException("Web端 WEBElement 对象，请调用带参 getPath 方法");
	}

	/**
	 * @author lpy
	 */
	public Rect getRect(DataInputStream inputStream, DataOutputStream outputStream) throws LibraryException {
		try {
//			UIBrowser browser = getBrowser();
//			System.out.println("getRect(" + elementId + ")");
//			String rectStr = (String) browser.executeScript("return JQWEB.getRect(arguments[0])", elementId);
			// 向浏览器发送请求
			String rectStr = BrowserExtensionsUtil.request("JQWEB.getRect", Long.toString(elementId), inputStream,
					outputStream);

			Rect rect = new Rect();
			JSONObject rectjo = new JSONObject(rectStr);
			rect.x = rectjo.getInt("left");
			rect.y = rectjo.getInt("top");
			rect.w = rectjo.getInt("width");
			rect.h = rectjo.getInt("height");
			return rect;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException("执行 ele[" + elementId + "] getRect失败，在 JQWEB.getRect 函数" + e.getMessage(), e);
		}
	}

	/**
	 * @author lpy
	 */
	public Path getPath() throws LibraryException {
		throw new LibraryException("Web端 WEBElement 对象，请调用带参 getPath 方法");
	}

	/**
	 * @author lpy
	 */
	public Path getPath(DataInputStream inputStream, DataOutputStream outputStream) throws LibraryException {
		Path path = new Path();
		try {
//			UIBrowser browser = getBrowser();
//			String pathStr = (String) browser.executeScript("return JQWEB.getPath(arguments[0])", elementId);
			// 向浏览器扩展发送请求
			String pathStr = BrowserExtensionsUtil.request("JQWEB.getPath", Long.toString(elementId), inputStream,
					outputStream);

			System.out.println(pathStr);
			// FIXED:add window node at first
			JSONArray elements = new JSONArray(pathStr);
			JSONObject pathobj = new JSONObject();
			pathobj.put("elements", elements);
			pathobj.put("isWeb", true);
			path.fromJson(pathobj);
			path.getElements().add(0, generWindowNode(inputStream, outputStream));
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "]getPath失败！" + e.getMessage(), e);
		}
		return path;
	}

	private PathElement generWindowNode(DataInputStream inputStream, DataOutputStream outputStream)
			throws LibraryException {
		try {
			// 向浏览器扩展发送请求
			String response = BrowserExtensionsUtil.request("JQWEB.getBrowserInfo", "", inputStream, outputStream);

			PathElement windowNode = new PathElement();
//			UIBrowser browser = getBrowser();
			JSONObject browser = new JSONObject(response);
			PathAttribute application = new PathAttribute();
			PathAttribute title = new PathAttribute();
			PathAttribute url = new PathAttribute();

			application.setEnable(true);
			application.setName("Application");
//			application.setValue(WebBrowserType.parseTypeToProcessName(browser.getType()));
			application.setValue(browser.getString("application"));
			windowNode.getAttributes().add(application);

			title.setEnable(true);
			title.setName("title");
//			title.setValue(browser.getPageTitle());
			title.setValue(browser.getString("title"));
			windowNode.getAttributes().add(title);

			url.setEnable(true);
			url.setName("url");
//			url.setValue(browser.getCurrentUrl());
			url.setValue(browser.getString("url"));
			windowNode.getAttributes().add(url);
			windowNode.setEnable(true);

			return windowNode;

		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(e.getMessage(), e);
		}
	}

	public void scrollIntoView() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			browser.executeScript("return JQWEB.scrollIntoView(arguments[0])", elementId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] scrollIntoView失败 " + e.getMessage(), e);
		}
	}

	public void setFocus() throws LibraryException {
		try {
			UIBrowser uiBrowser = getBrowser();

			IUIElement window = new FindLibraryManager(new Context()).getWindow(this);
			if (window != null)
				window.setFocus();

			uiBrowser.executeScript("return JQWEB.setFocus(arguments[0])", elementId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] setFocus失败 " + e.getMessage(), e);
		}
	}

	public boolean isChecked() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			Long result = (Long) browser.executeScript("return JQWEB.isChecked(arguments[0])", elementId);
			if (result == 1)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] ischecked 失败！" + e.getMessage(), e);
		}
		return false;
	}

	public void setChecked(boolean checked) throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			browser.executeScript("return JQWEB.setChecked(arguments[0],arguments[1])", elementId,
					checked ? "true" : "false");

		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] setChecked 失败！" + e.getMessage(), e);

		}
	}

	public int getCheckState() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			long r = (Long) browser.executeScript("return JQWEB.getCheckState(arguments[0])", elementId);
			return (int) r;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] getCheckState 失败！" + e.getMessage(), e);
		}
	}

	public String getText() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			String r = (String) browser.executeScript("return JQWEB.getText(arguments[0])", elementId);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] getCheckState 失败！" + e.getMessage(), e);
		}
	}

	public void setText(String text) throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			browser.executeScript("return JQWEB.setText(arguments[0],arguments[1])", elementId, text);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] setText 失败！" + e.getMessage(), e);
		}
	}

	public boolean isPassword() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			String r = (String) browser.executeScript("return JQWEB.isPassword(arguments[0])", elementId);
			if (StringUtils.isEmpty(r))
				return false;
			return Boolean.parseBoolean(r);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] isPassword 失败！" + e.getMessage(), e);
		}
	}

	public void clearSelection() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			browser.executeScript("return JQWEB.clearSelection(arguments[0] )", elementId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] clearSelection 失败！" + e.getMessage(), e);
		}
	}

	public void selectItems(String[] items) throws LibraryException {
		try {
			UIBrowser browser = getBrowser();
			browser.executeScript("return JQWEB.selectItems(arguments[0],arguments[1])", elementId, items);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] selectItems 失败！" + e.getMessage(), e);
		}
	}

	public void clearText() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			browser.executeScript("return JQWEB.clearText(arguments[0])", elementId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] clearText 失败！" + e.getMessage(), e);
		}
	}

	public void simulateTypeText(String text) throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			browser.executeScript("return JQWEB.setText(arguments[0],arguments[1])", elementId, text);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] simulateTypeText 失败！" + e.getMessage(), e);
		}
	}

	public void sendHotkey(int hotkey, int[] maskkeys) throws LibraryException {
		return;
	}

	/**
	 * 获取元素属性
	 */
	public String getAttributeValue(String attrName) throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			String r = (String) browser.executeScript("return JQWEB.getAttributeValue(arguments[0],arguments[1])",
					elementId, attrName);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] getAttributeValue 失败！" + e.getMessage(), e);
		}
	}

	public boolean enable() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			String r = (String) browser.executeScript("return JQWEB.enable(arguments[0])", elementId);
			if (StringUtils.isEmpty(r))
				return false;
			return Boolean.parseBoolean(r);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] enable 失败！" + e.getMessage(), e);
		}
	}

	public boolean visible() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			String r = (String) browser.executeScript("return JQWEB.isElementVisible(arguments[0])", elementId);
			if (StringUtils.isEmpty(r))
				return false;
			return Boolean.parseBoolean(r);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] visible 失败！" + e.getMessage(), e);
		}
	}

	/**
	 * 判断是否为标准table内元素
	 * 
	 * @return
	 * @throws LibraryException
	 */
	public boolean isTable() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			String r = (String) browser.executeScript("return JQWEB.isTable(arguments[0])", elementId);
			if (StringUtils.isEmpty(r))
				return false;
			return Boolean.parseBoolean(r);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] isTable 失败！" + e.getMessage(), e);
		}
	}

	/**
	 * 整表提取数据
	 * 
	 * @return
	 * @throws LibraryException
	 */
	public String getPageDataByTable() throws LibraryException {
		if (!isTable()) {
			throw new LibraryException("该元素不是table内元素！");
		}
		try {
			UIBrowser browser = getBrowser();
			String data = (String) browser.executeScript("return JQWEB.getTableData(arguments[0])", elementId);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] getPageDataByTable 失败！" + e.getMessage(), e);
		}
	}

	/**
	 * 只用于获取整表的列信息，用于展示，获取数据请用{@link WEBElement#getPageDataByTable()}
	 * 
	 * @return
	 * @throws LibraryException
	 */
	public WebTableColumnsInfo getTableCols() throws LibraryException {
		if (!isTable()) {
			throw new LibraryException("该元素不是table内元素！");
		}
		try {
			UIBrowser browser = getBrowser();

			String colsInfo = (String) browser.executeScript("return JQWEB.getTableColInfo(arguments[0])", elementId);
			WebTableColumnsInfo wtci = new WebTableColumnsInfo();
			wtci.fromJSON(colsInfo);
			return wtci;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] getTableCols 失败！" + e.getMessage(), e);
		}
	}

	/**
	 * 获取表格数据，<em>注意：当前元素必须为表格的根节点，如table节点</em>
	 */
	public String getPageData(Path[] columns) throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			JSONArray columnsJA = new JSONArray();
			for (Path p : columns) {
				columnsJA.put(p.toJson());
			}

			String r = (String) browser.executeScript("return JQWEB.getPageData(arguments[0],arguments[1])", elementId,
					columnsJA.toString());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] getPageData 失败！" + e.getMessage(), e);
		}
	}

	public void simulateClick(MouseClickType clickType, int mousekey) throws LibraryException {
		try {
			UIBrowser browser = getBrowser();
			browser.executeScript("return JQWEB.click(arguments[0],arguments[1],arguments[2])", elementId,
					clickType.value(), mousekey);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] click 失败！" + e.getMessage(), e);
		}

	}

	/**
	 * @deprecated
	 * @throws LibraryException
	 */
	public void click() throws LibraryException {
		try {
			UIBrowser browser = getBrowser();

			browser.executeScript("return JQWEB.click(arguments[0])", elementId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] click 失败！" + e.getMessage(), e);
		}

	}

	public void setWebAttributeValue(String attrName, String attrValue) throws LibraryException {
		try {
			UIBrowser browser = getBrowser();
			browser.executeScript("return JQWEB.setWebAttributeValue(arguments[0],arguments[1],arguments[2])",
					elementId, attrName, attrValue);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LibraryException(
					"执行browser[" + browserId + "]下的ele[" + elementId + "] click 失败！" + e.getMessage(), e);
		}
	}

	public UIBrowser getOwnerBrowser() {
		return WebBrowserManager.getInstance().getBrowser(browserId);
	}

}
