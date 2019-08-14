package com.jiuqi.etl.rpa.ui.window.attach;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.window.attach.WindowAttachTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class WindowAttachProperty implements IPropertySource {
	private static final String KEY_PATHPICKER = "ѡ����";
	private static final String KEY_TARGETDELAY = "ѡ��ʱ";
	private static final String KEY_OUTPUTTPARAM = "Ӧ�ô���";
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	
	private WindowAttachTaskModel model;
	private IPropertyDescriptor[] pds;
	
	public WindowAttachProperty(TaskContext context) {
		this.model = (WindowAttachTaskModel) context.getTaskModel();
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		
		UIPickerPopupPropertyDescriptor picker = new UIPickerPopupPropertyDescriptor(KEY_PATHPICKER,KEY_PATHPICKER, this.model.getPath(), true);
		EditableComboPropertyDescriptor targetDelay = new EditableComboPropertyDescriptor(KEY_TARGETDELAY, KEY_TARGETDELAY,integerParamStrs);
		targetDelay.setValidator(numberValidate);
		picker.setCategory("Ŀ��");
		targetDelay.setCategory("Ŀ��");
		TextPropertyDescriptor outputElement = new TextPropertyDescriptor(KEY_OUTPUTTPARAM, KEY_OUTPUTTPARAM);
		outputElement.setCategory("���");
		pds = new IPropertyDescriptor[]{/*Ŀ��*/picker,targetDelay,outputElement};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_PATHPICKER)) {
			if(this.model.getPath()==null || this.model.getPath().getElements().size()==0){
				return "<δ����>";
			}
			return "<������>";
		} else if(id.equals(KEY_TARGETDELAY)) {
			return String.valueOf(this.model.getTimeout());
		} else if(id.equals(KEY_OUTPUTTPARAM)) {
			if(StringUtils.isNotEmpty(this.model.getOutputParamName())){
				return this.model.getOutputParamName();
			}
			return "";
		} 
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_PATHPICKER)) {
			Path path = new Path();
			try {
				if("".equals(value)){
					this.model.setPath(null);
				}else{
					path.fromJson(new JSONObject((String)value));
					this.model.setPath(path);					
				}
			} catch (JSONException e) {
				e.printStackTrace();
				this.model.setPath(null);
			}
		} else if(id.equals(KEY_TARGETDELAY)) {
			this.model.setTimeout((String) value);
		} else if(id.equals(KEY_OUTPUTTPARAM)) {
			this.model.setOutputParamName((String) value);
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

	//������
		ICellEditorValidator numberValidate = new ICellEditorValidator() {
			public String isValid(Object value) {
				try {
					if(StringUtils.isEmpty((String) value)){
						return null;
					}
					int v = Integer.parseInt((String) value);
					if (v < 0){
						return "�������븺��";					
					}else{
						return null;
					}
				} catch (Exception e) {
					String paramName = ((String) value).trim();
					for (int i = 0; i < integerParamStrs.length; i++) {
						if(paramName.equals(integerParamStrs[i])){
							return null;
						}
					}
					return "����������";
				}
			}
		};
}
