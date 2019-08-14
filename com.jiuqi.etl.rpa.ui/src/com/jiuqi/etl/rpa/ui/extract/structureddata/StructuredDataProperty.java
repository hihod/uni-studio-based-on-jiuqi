package com.jiuqi.etl.rpa.ui.extract.structureddata;

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
import com.jiuqi.etl.rpa.runtime.extract.structureddata.StructuredDataTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class StructuredDataProperty implements IPropertySource {

	private StructuredDataTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] tableParamStrs;
	private ParameterModel[] tableParams;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	private static final String KEY_META_DATA_PICKER = "��ȡԪ����";
	private static final String KEY_NEXT_LINK_TARGETPARAM = "Ԫ��";
	private static final String KEY_NEXT_LINK_TARGETDELAY = "ѡ��ʱ";
	private static final String KEY_NEXT_LINK_PATHPICKER = "ѡ����";
	private static final String KEY_OUTPUT_DATASET = "��ȡ���";
	private static final String KEY_OUTPUT_MAPPING = "��ȡ��ӳ��";
	private static final String KEY_RECORD_COUNT = "���ؼ�¼��";
	private static final String KEY_DELAY_TIME_MS = "��ҳ���ʱ��";
	public StructuredDataProperty(TaskContext context) {
		this.model = (StructuredDataTaskModel) context.getTaskModel();
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		tableParams = context.getParameters(new DataType[]{DataType.DATATABLE});
		tableParamStrs = new String[tableParams.length + 1];
		tableParamStrs[0] = "<����������>";
		for (int i = 1; i < tableParams.length + 1; i++) {
			tableParamStrs[i] = tableParams[i - 1].getName();
			if (!"".equals(tableParams[i - 1].getTitle()))
				tableParamStrs[i] += "(" + tableParams[i - 1].getTitle() + ")";
		}
		
		StrcuturedMetaDataPopupPropertyDescriptor metaDataPickerr = new StrcuturedMetaDataPopupPropertyDescriptor(KEY_META_DATA_PICKER, KEY_META_DATA_PICKER,context);
		metaDataPickerr.setCategory("����");
		ComboBoxPropertyDescriptor outputDataSet = new ComboBoxPropertyDescriptor(KEY_OUTPUT_DATASET, KEY_OUTPUT_DATASET, tableParamStrs);
		outputDataSet.setCategory("���");
		FieldsMappingPopupPropertyDescriptor pickMapping = new FieldsMappingPopupPropertyDescriptor(KEY_OUTPUT_MAPPING, KEY_OUTPUT_MAPPING,context);
		pickMapping.setCategory("���");
		
		TextPropertyDescriptor nextLinkParam = new TextPropertyDescriptor(KEY_NEXT_LINK_TARGETPARAM, KEY_NEXT_LINK_TARGETPARAM);
		UIPickerPopupPropertyDescriptor nextLinkPicker = new UIPickerPopupPropertyDescriptor(KEY_NEXT_LINK_PATHPICKER,KEY_NEXT_LINK_PATHPICKER, this.model.getNextLinkSelector());
		EditableComboPropertyDescriptor nextLinkDelay = new EditableComboPropertyDescriptor(KEY_NEXT_LINK_TARGETDELAY, KEY_NEXT_LINK_TARGETDELAY,integerParamStrs);
		nextLinkDelay.setValidator(numberValidate);
		nextLinkParam.setCategory("��һҳѡ����");
		nextLinkPicker.setCategory("��һҳѡ����");
		nextLinkDelay.setCategory("��һҳѡ����");
		EditableComboPropertyDescriptor recordCount = new EditableComboPropertyDescriptor(KEY_RECORD_COUNT, KEY_RECORD_COUNT,integerParamStrs);
		recordCount.setCategory("ѡ��");
		recordCount.setValidator(numberValidate);
		EditableComboPropertyDescriptor delayTimeMS = new EditableComboPropertyDescriptor(KEY_DELAY_TIME_MS, KEY_DELAY_TIME_MS,integerParamStrs);
		delayTimeMS.setCategory("ѡ��");
		delayTimeMS.setValidator(numberValidate);
		pds = new IPropertyDescriptor[]{/*����*/metaDataPickerr,
										/*���*/outputDataSet,pickMapping,
										/*ѡ��*/recordCount,delayTimeMS,
										nextLinkParam,nextLinkPicker,nextLinkDelay};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_META_DATA_PICKER)) {
			if(StringUtils.isNotEmpty(this.model.getExtractMetaData())){
				return "<������>";
			}
			return "<δ����>";
		} else if(id.equals(KEY_OUTPUT_DATASET)) {
			for (int i = 0; i < tableParams.length; i ++) {
				if (tableParams[i].getName().equals(this.model.getOutputParamName()))
					return i + 1;
			}
			return 0;
		} else if(id.equals(KEY_OUTPUT_MAPPING)) {
			if(this.model.getFields().size()>0){
				return "<������>";
			}
			return "<δ����>";
		} else if(id.equals(KEY_RECORD_COUNT)) {
			return String.valueOf(this.model.getMaxNumberOfResult());
		} else if(id.equals(KEY_DELAY_TIME_MS)) {
			return String.valueOf(this.model.getDelayBetweenPagesMS());
		} else if(id.equals(KEY_NEXT_LINK_TARGETPARAM)) {
			if(StringUtils.isNotEmpty(this.model.getNextLinkSelector().getElement())){
				return this.model.getNextLinkSelector().getElement();
			}
			return "";
		} else if(id.equals(KEY_NEXT_LINK_PATHPICKER)) {
			if(this.model.getNextLinkSelector().getPath()==null || this.model.getNextLinkSelector().getPath().getElements().size()==0){
				return "<δ����>";
			}
			return "<������>";
		} else if(id.equals(KEY_NEXT_LINK_TARGETDELAY)) {
			return String.valueOf(this.model.getNextLinkSelector().getTimeout());
		}
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_META_DATA_PICKER)) {
			this.model.setExtractMetaData((String) value);
		} else if(id.equals(KEY_OUTPUT_DATASET)) {
			if((Integer) value ==0){
				this.model.setOutputParamName(null);
			}else{
				this.model.setOutputParamName(tableParams[(Integer) value -1].getName());
			}
		} else if(id.equals(KEY_OUTPUT_MAPPING)) {
		} else if(id.equals(KEY_RECORD_COUNT)) {
			this.model.setMaxNumberOfResult((String) value);
		} else if(id.equals(KEY_DELAY_TIME_MS)) {
			this.model.setDelayBetweenPagesMS((String) value);
		} else if(id.equals(KEY_NEXT_LINK_TARGETPARAM)) {
			this.model.getNextLinkSelector().setElement((String) value);

		} else if(id.equals(KEY_NEXT_LINK_PATHPICKER)) {
			Path path = new Path();
			try {
				if("".equals(value)){
					this.model.getNextLinkSelector().setPath(null);
				}else{
					path.fromJson(new JSONObject((String)value));
					this.model.getNextLinkSelector().setPath(path);					
				}
			} catch (JSONException e) {
				e.printStackTrace();
				this.model.getNextLinkSelector().setPath(null);
			}
		} else if(id.equals(KEY_NEXT_LINK_TARGETDELAY)) {
			this.model.getNextLinkSelector().setTimeout((String) value);
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
