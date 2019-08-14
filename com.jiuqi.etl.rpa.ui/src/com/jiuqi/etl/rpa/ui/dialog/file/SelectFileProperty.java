package com.jiuqi.etl.rpa.ui.dialog.file;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.dialog.file.SelectFileTaskModel;
import com.jiuqi.etl.rpa.ui.MultipleContentPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

public class SelectFileProperty implements IPropertySource {

	private SelectFileTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] paramStrs;
	private ParameterModel[] params;
	private static final String KEY_FILTER = "过滤器";
	private static final String KEY_INIT_PATH = "初始路径";
	private static final String KEY_FILE_PATH = "文件路径";
	public SelectFileProperty(TaskContext context) {
		this.model = (SelectFileTaskModel) context.getTaskModel();
		
		params = context.getParameters(new DataType[]{DataType.STRING});
		paramStrs = new String[params.length + 1];
		paramStrs[0] = "<不关联参数>";
		for (int i = 1; i < params.length + 1; i++) {
			paramStrs[i] = params[i - 1].getName();
			if (!"".equals(params[i - 1].getTitle()))
				paramStrs[i] += "(" + params[i - 1].getTitle() + ")";
		}
		MultipleContentPropertyDescriptor filter = new MultipleContentPropertyDescriptor(KEY_FILTER, KEY_FILTER,"设置过滤规则","用于过滤文件类型");
		filter.setCategory("输入");
		TextPropertyDescriptor initPath = new TextPropertyDescriptor(KEY_INIT_PATH, KEY_INIT_PATH);
		initPath.setCategory("输入");
		ComboBoxPropertyDescriptor filePath = new ComboBoxPropertyDescriptor(KEY_FILE_PATH, KEY_FILE_PATH, paramStrs);
		filePath.setCategory("输出");
		
		
		pds = new IPropertyDescriptor[]{/**/filter,initPath,filePath};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_FILTER)) {
			if(this.model.getFilter()!=null){
				return StringUtils.join(this.model.getFilter(),",");
			}else{
				return "";
			}
		} else if(id.equals(KEY_INIT_PATH)) {
			if(this.model.getInitialPath()!=null){
				return this.model.getInitialPath();
			}else{
				return "";
			}
		} else if(id.equals(KEY_FILE_PATH)) {
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
		if(id.equals(KEY_FILTER)) {
			String se = (String)value;
			this.model.setFilter(se.split(","));
		} else if(id.equals(KEY_INIT_PATH)) {
			this.model.setInitialPath((String) value);
		}else if(id.equals(KEY_FILE_PATH)) {
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

	ICellEditorValidator numberValidate = new ICellEditorValidator() {
		public String isValid(Object value) {
			try {
				int v = Integer.parseInt((String) value);
				if (v < 0)
					return "不能输入负数";
			} catch (Exception e) {
				return "请输入数字";
			}
			
			return null;
		}
	};
}
