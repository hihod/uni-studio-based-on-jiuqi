package com.jiuqi.etl.rpa.ui.dialog.directory;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.dialog.directory.SelectDirectoryTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

public class SelectDirectoryProperty implements IPropertySource {

	private SelectDirectoryTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] paramStrs;
	private ParameterModel[] params;
	private static final String KEY_INIT_PATH = "初始路径";
	private static final String KEY_DIR_PATH = "目录路径";
	public SelectDirectoryProperty(TaskContext context) {
		this.model = (SelectDirectoryTaskModel) context.getTaskModel();
		
		params = context.getParameters(new DataType[]{DataType.STRING});
		paramStrs = new String[params.length + 1];
		paramStrs[0] = "<不关联参数>";
		for (int i = 1; i < params.length + 1; i++) {
			paramStrs[i] = params[i - 1].getName();
			if (!"".equals(params[i - 1].getTitle()))
				paramStrs[i] += "(" + params[i - 1].getTitle() + ")";
		}
		TextPropertyDescriptor initPath = new TextPropertyDescriptor(KEY_INIT_PATH, KEY_INIT_PATH);
		initPath.setCategory("输入");
		ComboBoxPropertyDescriptor filePath = new ComboBoxPropertyDescriptor(KEY_DIR_PATH, KEY_DIR_PATH, paramStrs);
		filePath.setCategory("输出");
		
		
		pds = new IPropertyDescriptor[]{/**/initPath,filePath};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_INIT_PATH)) {
			if(this.model.getInitPath()!=null){
				return this.model.getInitPath();
			}else{
				return "";
			}
		} else if(id.equals(KEY_DIR_PATH)) {
			for (int i = 0; i < params.length; i ++) {
				if (params[i].getName().equals(this.model.getOutputParamName()))
					return i + 1;
			}
			this.model.setOutputParamName(null);
			return 0;
		}
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_INIT_PATH)) {
			this.model.setInitPath((String) value);
		}else if(id.equals(KEY_DIR_PATH)) {
			if((Integer) value ==0){
				this.model.setOutputParamName(null);
			}else{
				this.model.setOutputParamName(params[(Integer) value -1].getName());
			}
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
