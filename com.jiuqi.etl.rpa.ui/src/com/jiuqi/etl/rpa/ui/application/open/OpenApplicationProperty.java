package com.jiuqi.etl.rpa.ui.application.open;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.application.open.OpenApplicationTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

public class OpenApplicationProperty implements IPropertySource {
	private OpenApplicationTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	private static final String KEY_APP_PATH = "Ӧ�ó���";
	private static final String KEY_OPEN_TIMEOUT = "�򿪳�ʱ";
	private static final String KEY_ARGS = "����";
	private static final String KEY_OUTPUTTPARAM = "Ӧ�ô���";

	public OpenApplicationProperty(TaskContext context) {
		this.model = (OpenApplicationTaskModel) context.getTaskModel();
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		TextPropertyDescriptor applicationPath = new TextPropertyDescriptor(KEY_APP_PATH, KEY_APP_PATH);
		EditableComboPropertyDescriptor timeout = new EditableComboPropertyDescriptor(KEY_OPEN_TIMEOUT, KEY_OPEN_TIMEOUT,integerParamStrs);
		timeout.setValidator(numberValidate);
		TextPropertyDescriptor args = new TextPropertyDescriptor(KEY_ARGS, KEY_ARGS);
		applicationPath.setCategory("����");
		applicationPath.setDescription("��ִ�г��������ڵ�����·��");
		args.setCategory("����");
		args.setDescription("��ִ�г��������������ѡ�");
		timeout.setCategory("����");
		TextPropertyDescriptor outputText = new TextPropertyDescriptor(KEY_OUTPUTTPARAM, KEY_OUTPUTTPARAM);
		outputText.setCategory("���");
		outputText.setDescription("���д��ڶ���ı���");
		pds = new IPropertyDescriptor[] { /* ���� */applicationPath, args, timeout, /* ��� */outputText };

	}

	public Object getPropertyValue(Object id) {
		if (id.equals(KEY_OPEN_TIMEOUT)) {
			return String.valueOf(this.model.getTimeout());
		} else if (id.equals(KEY_APP_PATH)) {
			return this.model.getApplicationPath();
		} else if (id.equals(KEY_OUTPUTTPARAM)) {
			if (StringUtils.isNotEmpty(this.model.getOutputParamName())) {
				return this.model.getOutputParamName();
			}
			return "";
		} else if (id.equals(KEY_ARGS)) {
			return this.model.getArgs();
		}
		return null;
	}

	public void setPropertyValue(Object id, Object value) {
		if (id.equals(KEY_OPEN_TIMEOUT)) {
			this.model.setTimeout((String) value);
		} else if (id.equals(KEY_APP_PATH)) {
			this.model.setApplicationPath((String) value);
		} else if (id.equals(KEY_OUTPUTTPARAM)) {
			this.model.setOutputParamName((String) value);
		} else if (id.equals(KEY_ARGS)) {
			this.model.setArgs((String) value);
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
	}

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
