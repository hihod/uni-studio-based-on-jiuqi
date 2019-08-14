package com.jiuqi.etl.rpa.ui.control.getcheck;

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
import com.jiuqi.etl.rpa.runtime.control.getcheck.GetCheckTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class GetCheckProperty implements IPropertySource {

	private GetCheckTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] boolParamStrs;
	private ParameterModel[] boolParams;
	private String[] stringParamStrs;
	private ParameterModel[] stringParams;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	
	private static final String KEY_TARGETPARAM = "Ԫ��";
	private static final String KEY_TARGETDELAY = "ѡ��ʱ";
	private static final String KEY_PATHPICKER = "ѡ����";
	
	private static final String KEY_OUTPUT_CHECK = "��ѡ���";
	private static final String KEY_OUTPUT_STATE = "��ѡ״̬";
	public GetCheckProperty(TaskContext context) {
		this.model = (GetCheckTaskModel) context.getTaskModel();
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		
		boolParams = context.getParameters(new DataType[] {DataType.BOOLEAN});
		boolParamStrs = new String[boolParams.length + 1];
		boolParamStrs[0] = "<����������>";
		for (int i = 1; i < boolParams.length + 1; i++) {
			boolParamStrs[i] = boolParams[i - 1].getName();
			if (!"".equals(boolParams[i - 1].getTitle()))
				boolParamStrs[i] += "(" + boolParams[i - 1].getTitle() + ")";
		}

		stringParams = context.getParameters(new DataType[] {DataType.STRING});
		stringParamStrs = new String[stringParams.length + 1];
		stringParamStrs[0] = "<����������>";
		for (int i = 1; i < stringParams.length + 1; i++) {
			stringParamStrs[i] = stringParams[i - 1].getName();
			if (!"".equals(stringParams[i - 1].getTitle()))
				stringParamStrs[i] += "(" + stringParams[i - 1].getTitle() + ")";
		}
		
		TextPropertyDescriptor targetParam = new TextPropertyDescriptor(KEY_TARGETPARAM, KEY_TARGETPARAM);
		UIPickerPopupPropertyDescriptor picker = new UIPickerPopupPropertyDescriptor(KEY_PATHPICKER,KEY_PATHPICKER, this.model.getTaskTarget());
		EditableComboPropertyDescriptor targetDelay = new EditableComboPropertyDescriptor(KEY_TARGETDELAY, KEY_TARGETDELAY,integerParamStrs);
		targetDelay.setValidator(numberValidate);
		targetParam.setCategory("Ŀ��");
		picker.setCategory("Ŀ��");
		targetDelay.setCategory("Ŀ��");
		
		ComboBoxPropertyDescriptor check = new ComboBoxPropertyDescriptor(KEY_OUTPUT_CHECK, KEY_OUTPUT_CHECK, boolParamStrs);
		check.setDescription("�����Ͳ����������Ƿ�ѡ");
		ComboBoxPropertyDescriptor state = new ComboBoxPropertyDescriptor(KEY_OUTPUT_STATE, KEY_OUTPUT_STATE, stringParamStrs);
		state.setDescription("�ַ��Ͳ�����UNCHECKED��ʾδ��ѡ,CHECKED��ʾ��ѡ,INDETERMINATE��ʾ��ȷ��");
		check.setCategory("���");
		state.setCategory("���");
		pds = new IPropertyDescriptor[]{
										/*Ŀ��*/targetParam,picker,targetDelay,
										/*ͨ��*/check,state};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_TARGETPARAM)) {
			if(StringUtils.isNotEmpty(this.model.getTaskTarget().getElement())){
				return this.model.getTaskTarget().getElement();
			}
			return "";
		} else if(id.equals(KEY_PATHPICKER)) {
			if(this.model.getTaskTarget().getPath()==null || this.model.getTaskTarget().getPath().getElements().size()==0){
				return "<δ����>";
			}
			return "<������>";
		} else if(id.equals(KEY_TARGETDELAY)) {
			return String.valueOf(this.model.getTaskTarget().getTimeout());
		}else if(id.equals(KEY_OUTPUT_CHECK)) {
			for (int i = 0; i < boolParams.length; i ++) {
				if (boolParams[i].getName().equals(this.model.getOutputParamCheck()))
					return i + 1;
			}
			return 0;
		}else if(id.equals(KEY_OUTPUT_STATE)) {
			for (int i = 0; i < stringParams.length; i ++) {
				if (stringParams[i].getName().equals(this.model.getOutputParamState()))
					return i + 1;
			}
			return 0;
		}
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_TARGETPARAM)) {
			this.model.getTaskTarget().setElement((String) value);
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
		}else if(id.equals(KEY_OUTPUT_CHECK)) {
			if((Integer) value ==0){
				this.model.setOutputParamCheck(null);
			}else{
				this.model.setOutputParamCheck(boolParams[(Integer) value -1].getName());
			}
		}else if(id.equals(KEY_OUTPUT_STATE)) {
			if((Integer) value ==0){
				this.model.setOutputParamState(null);
			}else{
				this.model.setOutputParamState(stringParams[(Integer) value -1].getName());
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
