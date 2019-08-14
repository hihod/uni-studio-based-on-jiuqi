package com.jiuqi.etl.rpa.ui.control.selectmultipleitems;

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
import com.jiuqi.etl.rpa.runtime.control.selectmultipleitems.SelectMultipleItemsTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.MultipleContentPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class SelectMultipleItemsProperty implements IPropertySource {

	private SelectMultipleItemsTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	
	private static final String KEY_DELAYBEFORE = "����ǰ��ʱ";
	private static final String KEY_DELAYAFTER = "��������ʱ";
	private static final String KEY_TARGETPARAM = "Ԫ��";
	private static final String KEY_TARGETDELAY = "ѡ��ʱ";
	private static final String KEY_PATHPICKER = "ѡ����";
	private static final String KEY_ITEMS = "��Ŀ����";
	private static final String KEY_CLEAR_ORIGIN = "���ԭ��ѡ��";
	public SelectMultipleItemsProperty(TaskContext context) {
		this.model = (SelectMultipleItemsTaskModel) context.getTaskModel();
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		EditableComboPropertyDescriptor delayBefore = new EditableComboPropertyDescriptor(KEY_DELAYBEFORE, KEY_DELAYBEFORE,integerParamStrs);
		delayBefore.setValidator(numberValidate);
		EditableComboPropertyDescriptor delayAfter = new EditableComboPropertyDescriptor(KEY_DELAYAFTER, KEY_DELAYAFTER,integerParamStrs);
		delayAfter.setValidator(numberValidate);
		delayBefore.setCategory("ͨ��");
		delayAfter.setCategory("ͨ��");
		
		TextPropertyDescriptor targetParam = new TextPropertyDescriptor(KEY_TARGETPARAM, KEY_TARGETPARAM);
		UIPickerPopupPropertyDescriptor picker = new UIPickerPopupPropertyDescriptor(KEY_PATHPICKER,KEY_PATHPICKER, this.model.getTaskTarget());
		EditableComboPropertyDescriptor targetDelay = new EditableComboPropertyDescriptor(KEY_TARGETDELAY, KEY_TARGETDELAY,integerParamStrs);
		targetDelay.setValidator(numberValidate);
		targetParam.setCategory("Ŀ��");
		picker.setCategory("Ŀ��");
		targetDelay.setCategory("Ŀ��");
		MultipleContentPropertyDescriptor item = new MultipleContentPropertyDescriptor(KEY_ITEMS, KEY_ITEMS,"���ö�ѡ��Ŀ","��Ŀ�������飬��ʽ��\tAAA\r\t\t\tBBB\r\t\t\tCCC");
		item.setCategory("����");
		ComboBoxPropertyDescriptor clearOrigin = new ComboBoxPropertyDescriptor(KEY_CLEAR_ORIGIN, KEY_CLEAR_ORIGIN, new String[]{"��","��"});
		clearOrigin.setCategory("����");
		pds = new IPropertyDescriptor[]{/*Ŀ��*/targetParam,picker,targetDelay,
										/*ͨ��*/delayBefore,delayAfter,
										/*����*/item};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_DELAYBEFORE)) {
			return String.valueOf(this.model.getDelay().getBefore());
		} else if(id.equals(KEY_DELAYAFTER)) {
			return String.valueOf(this.model.getDelay().getAfter());
		} else if(id.equals(KEY_CLEAR_ORIGIN)) {
			if(this.model.getClearOrigin()){return 0;}else{return 1;}
		} else if(id.equals(KEY_ITEMS)){	
			return StringUtils.join( model.getItems(),",");
		} else if(id.equals(KEY_TARGETPARAM)) {
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
		}
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_DELAYBEFORE)) {
			this.model.getDelay().setBefore((String) value);
		} else if(id.equals(KEY_CLEAR_ORIGIN)) {
			if(Integer.parseInt((String) value) ==0){
				this.model.setClearOrigin(true);
			}else{
				this.model.setClearOrigin(false);
			}
		}else if(id.equals(KEY_DELAYAFTER)) {
			this.model.getDelay().setAfter((String) value);
		} else if(id.equals(KEY_ITEMS)) {
			String se = (String)value;
			this.model.setItems(se.split(","));
		} else if(id.equals(KEY_TARGETPARAM)) {
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
