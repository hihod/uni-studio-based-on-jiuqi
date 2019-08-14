package com.jiuqi.etl.rpa.ui.browser.navigatetourl;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import com.jiuqi.etl.rpa.runtime.browser.navigettourl.NavigateToUrlTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

/**
 * 
 * @author wangshanyu
 *
 */
public class NavigateToUrlProperty implements IPropertySource {

	private NavigateToUrlTaskModel model;
	private IPropertyDescriptor[] pds;
	private static final String KEY_BROWSER_ID_PARAM_NAME = "‰Ø¿¿∆˜ID";
	private static final String KEY_BROWSER_URL = "URL";

	public NavigateToUrlProperty(TaskContext context) {
		this.model = (NavigateToUrlTaskModel) context.getTaskModel();

		TextPropertyDescriptor type = new TextPropertyDescriptor(KEY_BROWSER_ID_PARAM_NAME, KEY_BROWSER_ID_PARAM_NAME);
		TextPropertyDescriptor url = new TextPropertyDescriptor(KEY_BROWSER_URL, KEY_BROWSER_URL);
		type.setCategory(" ‰»Î");
		url.setCategory(" ‰»Î");
		pds = new IPropertyDescriptor[] { type, url };
	}

	public Object getPropertyValue(Object id) {
		if (id.equals(KEY_BROWSER_ID_PARAM_NAME)) {
			return String.valueOf(this.model.getBrowserIdParamName());
		} else if (id.equals(KEY_BROWSER_URL)) {
			return String.valueOf(this.model.getUrl());
		}
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		if (id.equals(KEY_BROWSER_ID_PARAM_NAME)) {
			this.model.setBrowserIdParamName((String)value);
		} else if (id.equals(KEY_BROWSER_URL)) {
			this.model.setUrl((String) value);
		}
	}

	public Object getEditableValue() {
		return this.model;
	}

	public boolean isPropertySet(Object id) {
		return false;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return pds;
	}

	public void resetPropertyValue(Object id) {
		if (id.equals(KEY_BROWSER_ID_PARAM_NAME)) {
			this.model.setBrowserIdParamName("");
		} else if (id.equals(KEY_BROWSER_URL)) {
			this.model.setUrl("");
		}
	}
}
