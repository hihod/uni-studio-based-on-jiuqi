package com.jiuqi.etl.rpa.ui.image.snapshot;

import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.image.snapshot.SnapShotTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class SnapShotProperty implements IPropertySource {
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	private SnapShotTaskModel model;
	private IPropertyDescriptor[] pds;
	private static final String KEY_TARGETPARAM = "Ԫ��";
	private static final String KEY_TARGETDELAY = "ѡ��ʱ";
	private static final String KEY_PATHPICKER = "ѡ����";
	private static final String KEY_IMAGE_DATA = "ͼ��";
	private static final String KEY_FILE_PATH = "���·��";
	public SnapShotProperty(TaskContext context) {
	    integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
	    integerParamStrs = new String[integerParams.length];
	    for (int i = 0; i < integerParams.length; i++) {
	        integerParamStrs[i] = integerParams[i].getName();
	    }
		this.model = (SnapShotTaskModel) context.getTaskModel();
		TextPropertyDescriptor imageData = new TextPropertyDescriptor(KEY_IMAGE_DATA, KEY_IMAGE_DATA);
		TextPropertyDescriptor filePath = new TextPropertyDescriptor(KEY_FILE_PATH, KEY_FILE_PATH);
		filePath.setCategory("����");
		imageData.setCategory("���");
		TextPropertyDescriptor targetParam = new TextPropertyDescriptor(KEY_TARGETPARAM, KEY_TARGETPARAM);
		UIPickerPopupPropertyDescriptor picker = new UIPickerPopupPropertyDescriptor(KEY_PATHPICKER,KEY_PATHPICKER, this.model.getTaskTarget());
		EditableComboPropertyDescriptor targetDelay = new EditableComboPropertyDescriptor(KEY_TARGETDELAY, KEY_TARGETDELAY,integerParamStrs);
		targetDelay.setValidator(numberValidate);
		targetParam.setCategory("Ŀ��");
		picker.setCategory("Ŀ��");
		targetDelay.setCategory("Ŀ��");
		pds = new IPropertyDescriptor[]{/*Ŀ��*/targetParam,picker,targetDelay,
										/*���*//*imageData,*/filePath};
	
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
		}else if(id.equals(KEY_IMAGE_DATA)) {
			if(StringUtils.isNotEmpty(this.model.getOutputImageData())){
				return this.model.getOutputImageData();
			}
			return "";
		} else if(id.equals(KEY_FILE_PATH)) {
			if(this.model.getSavePath()!=null){
				return this.model.getSavePath();
			}else{
				return "";
			}
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
		}else if(id.equals(KEY_IMAGE_DATA)) {
			if(Integer.parseInt((String)value)==0)
				this.model.setOutputImageData((String) value);
		} else if(id.equals(KEY_FILE_PATH)) {
			this.model.setSavePath((String) value);
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
