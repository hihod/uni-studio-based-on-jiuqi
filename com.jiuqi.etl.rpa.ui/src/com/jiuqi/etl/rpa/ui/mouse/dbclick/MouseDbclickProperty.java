package com.jiuqi.etl.rpa.ui.mouse.dbclick;

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
import com.jiuqi.etl.rpa.runtime.ClickModePackage;
import com.jiuqi.etl.rpa.runtime.MaskKeyPackage;
import com.jiuqi.etl.rpa.runtime.PositionPackage;
import com.jiuqi.etl.rpa.runtime.mouse.dbclick.MouseDbclickTaskModel;
import com.jiuqi.etl.rpa.ui.DropdownMultiListDescriptor;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class MouseDbclickProperty implements IPropertySource {

	private MouseDbclickTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	private static final String KEY_CLICKMODEL = "�����ʽ"; 
	private static final String KEY_DELAYBEFORE = "����ǰ��ʱ";
	private static final String KEY_DELAYAFTER = "��������ʱ";
	private static final String KEY_TARGETPARAM = "Ԫ��";
	private static final String KEY_PATHPICKER = "ѡ����";
	private static final String KEY_TARGETDELAY = "ѡ��ʱ";
	private static final String KEY_OFFSETX = "Xƫ��";
	private static final String KEY_OFFSETY = "Yƫ��";
	private static final String KEY_POSITION = "λ��";
	private static final String KEY_MASKKEY = "��ϼ�";
	private static final String KEY_ATTACH_ELEMENT = "����";
	public MouseDbclickProperty(TaskContext context) {
		this.model = (MouseDbclickTaskModel) context.getTaskModel();
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		ComboBoxPropertyDescriptor clickMode = new ComboBoxPropertyDescriptor(KEY_CLICKMODEL, KEY_CLICKMODEL, ClickModePackage.getTitleArray());
		clickMode.setCategory("����");
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
		
		EditableComboPropertyDescriptor offestX = new EditableComboPropertyDescriptor(KEY_OFFSETX,KEY_OFFSETX,integerParamStrs);
		offestX.setValidator(onlyNumberValidate);
		EditableComboPropertyDescriptor offestY = new EditableComboPropertyDescriptor(KEY_OFFSETY, KEY_OFFSETY,integerParamStrs);
		offestY.setValidator(onlyNumberValidate);
		ComboBoxPropertyDescriptor position = new ComboBoxPropertyDescriptor(KEY_POSITION, KEY_POSITION, PositionPackage.getTitleArray());
		//MultiComboPropertyDescriptor maskKey = new MultiComboPropertyDescriptor(KEY_MASKKEY, KEY_MASKKEY,MaskKeyPackage.getTitleArray());
		DropdownMultiListDescriptor maskKey = new DropdownMultiListDescriptor(KEY_MASKKEY, KEY_MASKKEY,MaskKeyPackage.getTitleArray());
		ComboBoxPropertyDescriptor attach = new ComboBoxPropertyDescriptor(KEY_ATTACH_ELEMENT, KEY_ATTACH_ELEMENT, new String[]{"��","��"});
		attach.setCategory("ѡ��");
		attach.setDescription("��Ԫ�����ڴ����ᵽ���㲢���ý���");
		
		maskKey.setCategory("ѡ��");
		offestX.setCategory("ѡ��");
		offestY.setCategory("ѡ��");
		position.setCategory("ѡ��");
		pds = new IPropertyDescriptor[]{/*����*/clickMode,
										/*ѡ��*/maskKey,position,offestX,offestY,attach,
										/*Ŀ��*/targetParam,picker,targetDelay,
										/*ͨ��*/delayBefore,delayAfter};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_CLICKMODEL)){
			int index = ClickModePackage.getIndex(this.model.getClickMode());
			return index;
		} else if(id.equals(KEY_DELAYBEFORE)) {
			return String.valueOf(this.model.getDelay().getBefore());
		} else if(id.equals(KEY_DELAYAFTER)) {
			return String.valueOf(this.model.getDelay().getAfter());
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
		} else if(id.equals(KEY_OFFSETX)) {
			return String.valueOf(this.model.getCursorPosition().getOffsetX());
		} else if(id.equals(KEY_OFFSETY)) {
			return String.valueOf(this.model.getCursorPosition().getOffsetY());
		} else if(id.equals(KEY_POSITION)) {
			int index = PositionPackage.getIndex(this.model.getCursorPosition().getPosition());
			return index;
		}else if(id.equals(KEY_MASKKEY)) {
			return getMultiComboTitles(this.model.getMaskKeys());
		} else if(id.equals(KEY_ATTACH_ELEMENT)) {
			if(this.model.getAttach()){return 1;}else{return 0;}	
		
		}
		return null;
	}
	private String getMultiComboTitles(int[] arr){
		String[] re = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			re[i] = MaskKeyPackage.getTitleArray()[MaskKeyPackage.getIndex(arr[i])];
		}
		return StringUtils.join(re, ",");
		
	}

	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_CLICKMODEL)){
			this.model.setClickMode(ClickModePackage.getClickMode((Integer) value));
		} else if(id.equals(KEY_DELAYBEFORE)) {
			this.model.getDelay().setBefore((String) value);
		} else if(id.equals(KEY_DELAYAFTER)) {
			this.model.getDelay().setAfter((String) value);
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
		} else if(id.equals(KEY_OFFSETX)) {
			this.model.getCursorPosition().setOffsetX((String) value);
		} else if(id.equals(KEY_OFFSETY)) {
			this.model.getCursorPosition().setOffsetY((String) value);
		} else if(id.equals(KEY_POSITION)) {
			this.model.getCursorPosition().setPosition(PositionPackage.getPosition((Integer) value));
		}else if(id.equals(KEY_MASKKEY)) {
			if(value instanceof int[]){	
				this.model.setMaskKeys((int[])value);
			}
		} else if(id.equals(KEY_ATTACH_ELEMENT)) {
			if((Integer) value ==0){
				this.model.setAttach(false);
			}else{
				this.model.setAttach(true);
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
	//У������
	ICellEditorValidator onlyNumberValidate = new ICellEditorValidator() {
		public String isValid(Object value) {
			try {
				if(StringUtils.isEmpty((String) value)){
					return null;
				}
				Integer.parseInt((String) value);
				return null;
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
