package com.jiuqi.etl.rpa.ui.extract.getattribute;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.extract.getattribute.GetAttributeTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class GetAttributeProperty implements IPropertySource {

	private GetAttributeTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] stringParamStrs;
	private ParameterModel[] stringParams;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	private static final String KEY_TARGETPARAM = "元素";
	private static final String KEY_TARGETDELAY = "选择超时";
	private static final String KEY_PATHPICKER = "选择器";
	private static final String KEY_ATTRIBUTE_NAME = "属性名";
	private static final String KEY_OUPTU_PARAM_NAME = "属性值";
	public GetAttributeProperty(TaskContext context) {
		this.model = (GetAttributeTaskModel) context.getTaskModel();
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		stringParams = context.getParameters(new DataType[] {DataType.STRING});
		stringParamStrs = new String[stringParams.length + 1];
		stringParamStrs[0] = "<不关联参数>";
		for (int i = 1; i < stringParams.length + 1; i++) {
			stringParamStrs[i] = stringParams[i - 1].getName();
			if (!"".equals(stringParams[i - 1].getTitle()))
				stringParamStrs[i] += "(" + stringParams[i - 1].getTitle() + ")";
		}
		
		TextPropertyDescriptor inputText = new TextPropertyDescriptor(KEY_ATTRIBUTE_NAME, KEY_ATTRIBUTE_NAME);
		inputText.setCategory("输入");


		ComboBoxPropertyDescriptor outputParam = new ComboBoxPropertyDescriptor(KEY_OUPTU_PARAM_NAME, KEY_OUPTU_PARAM_NAME,stringParamStrs);
		outputParam.setCategory("输出");
		
		TextPropertyDescriptor targetParam = new TextPropertyDescriptor(KEY_TARGETPARAM, KEY_TARGETPARAM);
		UIPickerPopupPropertyDescriptor picker = new UIPickerPopupPropertyDescriptor(KEY_PATHPICKER,KEY_PATHPICKER, this.model.getTaskTarget());
		EditableComboPropertyDescriptor targetDelay = new EditableComboPropertyDescriptor(KEY_TARGETDELAY, KEY_TARGETDELAY,integerParamStrs);
		targetDelay.setValidator(numberValidate);
		targetParam.setCategory("目标");
		picker.setCategory("目标");
		targetDelay.setCategory("目标");
		
		
		pds = new IPropertyDescriptor[]{/*目标*/targetParam,picker,targetDelay,
										/*输入*/inputText,
										/*输出*/outputParam};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_ATTRIBUTE_NAME)) {
			return String.valueOf(this.model.getAttributeName());
		} else if(id.equals(KEY_TARGETPARAM)) {
			if(StringUtils.isNotEmpty(this.model.getTaskTarget().getElement())){
				return this.model.getTaskTarget().getElement();
			}
			return "";
		} else if(id.equals(KEY_OUPTU_PARAM_NAME)) {
			for (int i = 0; i < stringParams.length; i ++) {
				if (stringParams[i].getName().equals(this.model.getOutputParamName()))
					return i + 1;
			}
			return 0;
		} else if(id.equals(KEY_PATHPICKER)) {
			if(this.model.getTaskTarget().getPath()==null || this.model.getTaskTarget().getPath().getElements().size()==0){
				return "<未配置>";
			}
			return "<已配置>";
		} else if(id.equals(KEY_TARGETDELAY)) {
			return String.valueOf(this.model.getTaskTarget().getTimeout());
		}
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_ATTRIBUTE_NAME)) {
			this.model.setAttributeName((String) value);
		} else if(id.equals(KEY_TARGETPARAM)) {
			this.model.getTaskTarget().setElement((String) value);
		} else if(id.equals(KEY_OUPTU_PARAM_NAME)) {
			if((Integer) value ==0){
				this.model.setOutputParamName(null);
			}else{
				this.model.setOutputParamName(stringParams[(Integer) value -1].getName());
			}
		} else if(id.equals(KEY_PATHPICKER)) {
			Path path = new Path();
			try {
				if("".equals(value)){
					this.model.getTaskTarget().setPath(null);
				}else{
					path.fromJson(new JSONObject((String)value));
					this.model.getTaskTarget().setPath(path);					
				}
			} catch (JSONException e) {
				e.printStackTrace();
				this.model.getTaskTarget().setPath(null);
			}
		} else if(id.equals(KEY_TARGETDELAY)) {
			this.model.getTaskTarget().setTimeout((String) value);
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

	//正整数
	ICellEditorValidator numberValidate = new ICellEditorValidator() {
		public String isValid(Object value) {
			try {
				if(StringUtils.isEmpty((String) value)){
					return null;
				}
				int v = Integer.parseInt((String) value);
				if (v < 0){
					return "不能输入负数";					
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
				return "参数不存在";
			}
		}
	};
}
