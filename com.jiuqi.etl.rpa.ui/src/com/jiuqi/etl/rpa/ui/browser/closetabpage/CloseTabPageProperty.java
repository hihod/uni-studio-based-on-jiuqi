package com.jiuqi.etl.rpa.ui.browser.closetabpage;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import com.jiuqi.etl.rpa.runtime.browser.closetabpage.CloseTabPageTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
/**
 * 
 * @author wangshanyu
 *
 */
public class CloseTabPageProperty implements IPropertySource {

	private CloseTabPageTaskModel model;
	private IPropertyDescriptor[] pds;
	private static final String KEY_BROWSER_ID_PARAM_NAME = "‰Ø¿¿∆˜ID";

	public CloseTabPageProperty(TaskContext context) {
		this.model = (CloseTabPageTaskModel) context.getTaskModel();
		TextPropertyDescriptor url = new TextPropertyDescriptor(KEY_BROWSER_ID_PARAM_NAME, KEY_BROWSER_ID_PARAM_NAME);
		url.setCategory(" ‰»Î");
		pds = new IPropertyDescriptor[] { url };
	}

	public Object getPropertyValue(Object id) {
		if (id.equals(KEY_BROWSER_ID_PARAM_NAME)) {
			return String.valueOf(this.model.getBrowserIdParamName());
		}
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		if (id.equals(KEY_BROWSER_ID_PARAM_NAME)) {
			this.model.setBrowserIdParamName((String)value);
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
		}
	}
}
