package com.jiuqi.etl.rpa.ui.browser.openbrowser;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import com.jiuqi.etl.rpa.runtime.BrowserTypePackage;
import com.jiuqi.etl.rpa.runtime.browser.openbrowser.OpenBrowserTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

/**
 * 
 * @author wangshanyu
 *
 */
public class OpenBrowserProperty implements IPropertySource {

	private OpenBrowserTaskModel model;
	private IPropertyDescriptor[] pds;
	private static final String KEY_BROWSER_TYPE = "‰Ø¿¿∆˜¿‡–Õ";
	private static final String KEY_BROWSER_URL = "URL";
	private static final String KEY_BROWSER_OUTPUT = "‰Ø¿¿∆˜ID";
	public OpenBrowserProperty(TaskContext context) {
		this.model = (OpenBrowserTaskModel) context.getTaskModel();

		ComboBoxPropertyDescriptor type = new ComboBoxPropertyDescriptor(KEY_BROWSER_TYPE, KEY_BROWSER_TYPE,
				BrowserTypePackage.getTitleArray());
		TextPropertyDescriptor url = new TextPropertyDescriptor(KEY_BROWSER_URL, KEY_BROWSER_URL);
		type.setCategory(" ‰»Î");
		url.setCategory(" ‰»Î");
		TextPropertyDescriptor output = new TextPropertyDescriptor(KEY_BROWSER_OUTPUT, KEY_BROWSER_OUTPUT);
		output.setCategory(" ‰≥ˆ");
		pds = new IPropertyDescriptor[] { type, url,output };
	}

	public Object getPropertyValue(Object id) {
		if (id.equals(KEY_BROWSER_TYPE)) {
			int index = BrowserTypePackage.getIndex(this.model.getBrowserType());
			return index;
		} else if (id.equals(KEY_BROWSER_URL)) {
			return String.valueOf(this.model.getUrl());
		} else if(id.equals(KEY_BROWSER_OUTPUT)) {
			return String.valueOf(this.model.getOutputParam());
		}
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		if (id.equals(KEY_BROWSER_TYPE)) {
			this.model.setBrowserType(BrowserTypePackage.getBrowserType((Integer) value));
		} else if (id.equals(KEY_BROWSER_URL)) {
			this.model.setUrl((String) value);
		} else if(id.equals(KEY_BROWSER_OUTPUT)) {
			this.model.setOutputParam((String)value);
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
		if (id.equals(KEY_BROWSER_TYPE)) {
			this.model.setBrowserType(null);
		} else if (id.equals(KEY_BROWSER_URL)) {
			this.model.setUrl("");
		} else if(id.equals(KEY_BROWSER_OUTPUT)) {
			this.model.setOutputParam("");
		}
	}
}
