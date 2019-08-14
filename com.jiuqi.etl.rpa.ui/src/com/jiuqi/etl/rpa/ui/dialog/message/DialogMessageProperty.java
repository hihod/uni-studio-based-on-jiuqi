package com.jiuqi.etl.rpa.ui.dialog.message;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.MButtonSuitePackage;
import com.jiuqi.etl.rpa.runtime.dialog.message.DialogMessageTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

public class DialogMessageProperty implements IPropertySource {

	private DialogMessageTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] stringParamStrs;
	private ParameterModel[] stringParams;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	private static final String KEY_INPUT_MSG = "��Ϣ";
	private static final String KEY_INPUT_TITLE = "����";
	private static final String KEY_BUTTONSUITE = "��ť��";
	private static final String KEY_MESSAGE_RESULT = "�������";
	public DialogMessageProperty(TaskContext context) {
		this.model = (DialogMessageTaskModel) context.getTaskModel();
		
		stringParams = context.getParameters(new DataType[]{DataType.STRING});
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
		TextPropertyDescriptor inputMsg = new TextPropertyDescriptor(KEY_INPUT_MSG, KEY_INPUT_MSG);
		inputMsg.setCategory("����");
		TextPropertyDescriptor inputTitle = new TextPropertyDescriptor(KEY_INPUT_TITLE, KEY_INPUT_TITLE);
		inputTitle.setCategory("����");
		ComboBoxPropertyDescriptor buttonSuite = new ComboBoxPropertyDescriptor(KEY_BUTTONSUITE, KEY_BUTTONSUITE, MButtonSuitePackage.getTitleArray());
		buttonSuite.setCategory("����");
		ComboBoxPropertyDescriptor outputParam = new ComboBoxPropertyDescriptor(KEY_MESSAGE_RESULT, KEY_MESSAGE_RESULT, stringParamStrs);
		outputParam.setCategory("���");
		
		
		pds = new IPropertyDescriptor[]{
										/**/inputTitle,inputMsg,buttonSuite,outputParam};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_MESSAGE_RESULT)) {
			for (int i = 0; i < stringParams.length; i ++) {
				if (stringParams[i].getName().equals(this.model.getOutputParamName()))
					return i + 1;
			}
			this.model.setOutputParamName(null);
			return 0;
		} else if(id.equals(KEY_INPUT_TITLE)) {
			if(this.model.getInputTitle() !=null){
				return this.model.getInputTitle();
			}else{
				return "";
			}
		} else if(id.equals(KEY_INPUT_MSG)) {
			if(this.model.getInputMsg() !=null){
				return this.model.getInputMsg();
			}else{
				return "";
			}
		} else if(id.equals(KEY_BUTTONSUITE)) {
			return MButtonSuitePackage.getIndex(this.model.getButtonGroup());
		}
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_MESSAGE_RESULT)) {
			if((Integer) value ==0){
				this.model.setOutputParamName(null);
			}else{
				this.model.setOutputParamName(stringParams[(Integer) value -1].getName());
			}
		} else if(id.equals(KEY_INPUT_MSG)) {
			this.model.setInputMsg((String) value);
		} else if(id.equals(KEY_INPUT_TITLE)) {
			this.model.setInputTitle((String) value);
		} else if(id.equals(KEY_BUTTONSUITE)) {
			this.model.setButtonGroup(MButtonSuitePackage.getMButtonSuite((Integer) value).value());
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
