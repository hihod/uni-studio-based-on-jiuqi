package com.jiuqi.rpa.lib.tree;

import java.util.Iterator;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.ContextProvider;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.UIAElement;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA元素树形操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIATreeWalker extends ContextProvider implements ITreeWalker {
	/**
	 * 构造器
	 * 
	 * @param context 活动上下文
	 */
	public UIATreeWalker(Context context) {
		super(context);
	}

	public IUIElement getRoot() throws LibraryException {
		try {
			long id = JQUIA.tree_getRoot();
			return (UIAElement) getContext().add(new UIAElement(id));
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public IUIElement[] getChildren(IUIElement parent) throws LibraryException {
		try {
			long[] ids = JQUIA.tree_getChildren(parent.getId());
			IUIElement[] children = new IUIElement[ids.length];
			for (int i = 0; i < ids.length; i++)
				children[i] = (UIAElement) getContext().add(new UIAElement(ids[i]));
			return children;
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public IUIElement getParent(IUIElement child) throws LibraryException {
		try {
			long id = JQUIA.tree_getParent(child.getId());
			return (UIAElement) getContext().add(new UIAElement(id));
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public String getText(IUIElement uiElement) throws LibraryException {
		try {
			return JQUIA.tree_getText(uiElement.getId());
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public Properties getProperties(IUIElement uiElement) throws LibraryException {
		try {
			Properties properties = new Properties();
			
			String propertiesJsonStr = JQUIA.tree_getProperties(uiElement.getId());
			JSONObject propertiesJson = new JSONObject(propertiesJsonStr);

			@SuppressWarnings("unchecked")
			Iterator<String> iterator = propertiesJson.keys();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String value = propertiesJson.optString(key);
				properties.put(key, value);
			}
			
			return properties;
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		} catch (JSONException e) {
			throw new LibraryException("属性列表转json失败", e);
		}
	}

}
