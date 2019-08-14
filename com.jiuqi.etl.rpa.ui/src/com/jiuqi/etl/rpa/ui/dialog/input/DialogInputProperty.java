package com.jiuqi.etl.rpa.ui.dialog.input;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.dialog.input.DialogInputTaskModel;
import com.jiuqi.etl.rpa.ui.MultipleContentPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

public class DialogInputProperty implements IPropertySource {

	private DialogInputTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] stringParamStrs;
	private ParameterModel[] stringParams;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	private static final String KEY_INPUT_LABEL = "��ǩ";
	private static final String KEY_INPUT_TITLE = "����";
	private static final String KEY_PASSWORD = "�Ƿ�����";
	private static final String KEY_OPTIONS = "¼����";
	private static final String KEY_OUTPUT = "������";
	public DialogInputProperty(TaskContext context) {
		this.model = (DialogInputTaskModel) context.getTaskModel();
		stringParams = context.getParameters(new DataType[] {DataType.STRING});
		stringParamStrs = new String[stringParams.length + 1];
		stringParamStrs[0] = "<����������>";
		for (int i = 1; i < stringParams.length + 1; i++) {
			stringParamStrs[i] = stringParams[i - 1].getName();
			if (!"".equals(stringParams[i - 1].getTitle()))
				stringParamStrs[i] += "(" + stringParams[i - 1].getTitle() + ")";
		}
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		TextPropertyDescriptor inputLabel = new TextPropertyDescriptor(KEY_INPUT_LABEL, KEY_INPUT_LABEL);
		inputLabel.setCategory("����");
		TextPropertyDescriptor inputTitle = new TextPropertyDescriptor(KEY_INPUT_TITLE, KEY_INPUT_TITLE);
		inputTitle.setCategory("����");
		ComboBoxPropertyDescriptor isPassword = new ComboBoxPropertyDescriptor(KEY_PASSWORD, KEY_PASSWORD, new String[]{"��","��"});
		isPassword.setCategory("����");
		MultipleContentPropertyDescriptor item = new MultipleContentPropertyDescriptor(KEY_OPTIONS, KEY_OPTIONS,"����¼����","��ѡ���б�ÿ����Ϊһ����Ŀ��������Ŀ������\r1���ı���\r2~3����ѡ��\r4~��������ѡ��\r");
		item.setCategory("����");
		ComboBoxPropertyDescriptor output = new ComboBoxPropertyDescriptor(KEY_OUTPUT, KEY_OUTPUT, stringParamStrs);
		output.setCategory("���");
		pds = new IPropertyDescriptor[]{
										/**/inputLabel,inputTitle,isPassword,item,
										output};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_INPUT_TITLE)) {
			if(this.model.getInputTitle() !=null){
				return this.model.getInputTitle();
			}else{
				return "";
			}
		} else if(id.equals(KEY_INPUT_LABEL)) {
			if(this.model.getInputLabel() !=null){
				return this.model.getInputLabel();
			}else{
				return "";
			}
		} else if(id.equals(KEY_PASSWORD)) {
			if(this.model.getPassword()){return 1;}else{return 0;}
		} else if(id.equals(KEY_OPTIONS)) {
			return StringUtils.join(model.getOptions(),",");
		} else if(id.equals(KEY_OUTPUT)) {
			for (int i = 0; i < stringParams.length; i ++) {
				if (stringParams[i].getName().equals(this.model.getOutputParamName()))
					return i + 1;
			}
			return 0;
		}
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_OPTIONS)) {
			String se = (String)value;
			this.model.setOptions(se.split(","));
		} else if(id.equals(KEY_PASSWORD)) {
			if((int)value ==0){
				this.model.setPassword(false);
			}else{
				this.model.setPassword(true);
			}
		} else if(id.equals(KEY_INPUT_LABEL)) {
			this.model.setInputLabel((String) value);
		} else if(id.equals(KEY_INPUT_TITLE)) {
			this.model.setInputTitle((String) value);
		} else if(id.equals(KEY_OUTPUT)) {
			if((Integer) value ==0){
				this.model.setOutputParamName(null);
			}else{
				this.model.setOutputParamName(stringParams[(Integer) value -1].getName());
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
