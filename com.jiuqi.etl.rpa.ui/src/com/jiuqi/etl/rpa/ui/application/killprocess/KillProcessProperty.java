package com.jiuqi.etl.rpa.ui.application.killprocess;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import com.jiuqi.etl.rpa.runtime.application.killprocess.KillProcessTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

public class KillProcessProperty implements IPropertySource {

	private KillProcessTaskModel model;
	private IPropertyDescriptor[] pds;
	private final String KEY_PROCESSNAME = "进程名";

	public KillProcessProperty(TaskContext context) {
		this.model = (KillProcessTaskModel) context.getTaskModel();
		
		TextPropertyDescriptor processName = new TextPropertyDescriptor(KEY_PROCESSNAME, KEY_PROCESSNAME);
		processName.setCategory("输入");
		pds = new IPropertyDescriptor[]{/*输入*/processName};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_PROCESSNAME)) {
			return String.valueOf(this.model.getProcessName());
		}
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_PROCESSNAME)) {
			this.model.setProcessName((String)value);
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
	public void resetPropertyValue(Object id) {}

}
