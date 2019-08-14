package com.jiuqi.etl.rpa.ui.mouse.drag;

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
import com.jiuqi.etl.rpa.runtime.MaskKeyPackage;
import com.jiuqi.etl.rpa.runtime.PositionPackage;
import com.jiuqi.etl.rpa.runtime.mouse.drag.MouseDragTaskModel;
import com.jiuqi.etl.rpa.ui.DropdownMultiListDescriptor;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class MouseDragProperty implements IPropertySource {

	private MouseDragTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
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
	public MouseDragProperty(TaskContext context) {
		this.model = (MouseDragTaskModel) context.getTaskModel();

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
		

		EditableComboPropertyDescriptor startOffestX = new EditableComboPropertyDescriptor("start"+KEY_OFFSETX,KEY_OFFSETX,integerParamStrs);
		startOffestX.setValidator(onlyNumberValidate);
		EditableComboPropertyDescriptor startOffestY = new EditableComboPropertyDescriptor("start"+KEY_OFFSETY, KEY_OFFSETY,integerParamStrs);
		startOffestY.setValidator(onlyNumberValidate);
		ComboBoxPropertyDescriptor startPosition = new ComboBoxPropertyDescriptor("start"+KEY_POSITION, KEY_POSITION, PositionPackage.getTitleArray());
		TextPropertyDescriptor startTargetParam = new TextPropertyDescriptor("start"+KEY_TARGETPARAM, KEY_TARGETPARAM);
		UIPickerPopupPropertyDescriptor startPicker = new UIPickerPopupPropertyDescriptor("start"+KEY_PATHPICKER,KEY_PATHPICKER, this.model.getStartTaskTarget());
		EditableComboPropertyDescriptor startTargetDelay = new EditableComboPropertyDescriptor("start"+KEY_TARGETDELAY, KEY_TARGETDELAY,integerParamStrs);
		startTargetDelay.setValidator(numberValidate);
		startOffestX.setCategory("���");
		startOffestY.setCategory("���");
		startPosition.setCategory("���");
		startTargetParam.setCategory("���");
		startPicker.setCategory("���");
		startTargetDelay.setCategory("���");
		
		EditableComboPropertyDescriptor endOffestX = new EditableComboPropertyDescriptor("end"+KEY_OFFSETX,KEY_OFFSETX,integerParamStrs);
		endOffestX.setValidator(onlyNumberValidate);
		EditableComboPropertyDescriptor endOffestY = new EditableComboPropertyDescriptor("end"+KEY_OFFSETY, KEY_OFFSETY,integerParamStrs);
		endOffestY.setValidator(onlyNumberValidate);
		ComboBoxPropertyDescriptor endPosition = new ComboBoxPropertyDescriptor("end"+KEY_POSITION, KEY_POSITION, PositionPackage.getTitleArray());
		TextPropertyDescriptor endTargetParam = new TextPropertyDescriptor("end"+KEY_TARGETPARAM, KEY_TARGETPARAM);
		UIPickerPopupPropertyDescriptor endPicker = new UIPickerPopupPropertyDescriptor("end"+KEY_PATHPICKER,KEY_PATHPICKER, this.model.getEndTaskTarget());
		EditableComboPropertyDescriptor endTargetDelay = new EditableComboPropertyDescriptor("end"+KEY_TARGETDELAY, KEY_TARGETDELAY,integerParamStrs);
		endTargetDelay.setValidator(numberValidate);
		endOffestX.setCategory("�յ�");
		endOffestY.setCategory("�յ�");
		endPosition.setCategory("�յ�");
		endTargetParam.setCategory("�յ�");
		endPicker.setCategory("�յ�");
		endTargetDelay.setCategory("�յ�");
		
		//MultiComboPropertyDescriptor maskKey = new MultiComboPropertyDescriptor(KEY_MASKKEY, KEY_MASKKEY,MaskKeyPackage.getTitleArray());
		DropdownMultiListDescriptor maskKey = new DropdownMultiListDescriptor(KEY_MASKKEY, KEY_MASKKEY,MaskKeyPackage.getTitleArray());
		maskKey.setCategory("ѡ��");
		ComboBoxPropertyDescriptor attach = new ComboBoxPropertyDescriptor(KEY_ATTACH_ELEMENT, KEY_ATTACH_ELEMENT, new String[]{"��","��"});
		attach.setCategory("ѡ��");
		attach.setDescription("��Ԫ�����ڴ����ᵽ���㲢���ý���");
		
		
		pds = new IPropertyDescriptor[]{/*��*/startTargetParam,startPicker,startTargetDelay,startPosition,startOffestX,startOffestY,
										/*��*/endTargetParam,endPicker,endTargetDelay,endPosition,endOffestX,endOffestY,
										/*ͨ��*/delayBefore,delayAfter,
										/*ѡ��*/maskKey,attach};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_DELAYBEFORE)) {
			return String.valueOf(this.model.getDelay().getBefore());
		} else if(id.equals(KEY_DELAYAFTER)) {
			return String.valueOf(this.model.getDelay().getAfter());
		} else if(id.equals("start"+KEY_TARGETPARAM)) {
			if(StringUtils.isNotEmpty(this.model.getStartTaskTarget().getElement())){
				return this.model.getStartTaskTarget().getElement();
			}
			return "";
		} else if(id.equals("start"+KEY_PATHPICKER)) {
			if(this.model.getStartTaskTarget().getPath()==null || this.model.getStartTaskTarget().getPath().getElements().size()==0){
				return "<δ����>";
			}
			return "<������>";
		} else if(id.equals("start"+KEY_TARGETDELAY)) {
			return String.valueOf(this.model.getStartTaskTarget().getTimeout());
		} else if(id.equals("start"+KEY_OFFSETX)) {
			return String.valueOf(this.model.getStartCursorPosition().getOffsetX());
		} else if(id.equals("start"+KEY_OFFSETY)) {
			return String.valueOf(this.model.getStartCursorPosition().getOffsetY());
		} else if(id.equals("start"+KEY_POSITION)) {
			int index = PositionPackage.getIndex(this.model.getStartCursorPosition().getPosition());
			return index;
		} else if(id.equals("end"+KEY_TARGETPARAM)) {
			if(StringUtils.isNotEmpty(this.model.getEndTaskTarget().getElement())){
				return this.model.getEndTaskTarget().getElement();
			}
			return "";
		} else if(id.equals("end"+KEY_PATHPICKER)) {
			if(this.model.getEndTaskTarget().getPath()==null || this.model.getEndTaskTarget().getPath().getElements().size()==0){
				return "<δ����>";
			}
			return "<������>";
		} else if(id.equals("end"+KEY_TARGETDELAY)) {
			return String.valueOf(this.model.getEndTaskTarget().getTimeout());
		} else if(id.equals("end"+KEY_OFFSETX)) {
			return String.valueOf(this.model.getEndCursorPosition().getOffsetX());
		} else if(id.equals("end"+KEY_OFFSETY)) {
			return String.valueOf(this.model.getEndCursorPosition().getOffsetY());
		} else if(id.equals("end"+KEY_POSITION)) {
			int index = PositionPackage.getIndex(this.model.getEndCursorPosition().getPosition());
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
		if(id.equals(KEY_DELAYBEFORE)) {
			this.model.getDelay().setBefore((String) value);
		} else if(id.equals(KEY_DELAYAFTER)) {
			this.model.getDelay().setAfter((String) value);
		} else if(id.equals("start"+KEY_TARGETPARAM)) {
			this.model.getStartTaskTarget().setElement((String) value);
		} else if(id.equals("start"+KEY_PATHPICKER)) {
			Path path = new Path();
			try {
				if("".equals(value)){
					this.model.getStartTaskTarget().setPath(null);
				}else{
					path.fromJson(new JSONObject((String)value));
					this.model.getStartTaskTarget().setPath(path);					
				}
			} catch (JSONException e) {
				e.printStackTrace();
				this.model.getStartTaskTarget().setPath(null);
			}
		} else if(id.equals("start"+KEY_TARGETDELAY)) {
			this.model.getStartTaskTarget().setTimeout((String) value);
		} else if(id.equals("start"+KEY_OFFSETX)) {
			this.model.getStartCursorPosition().setOffsetX((String) value);
		} else if(id.equals("start"+KEY_OFFSETY)) {
			this.model.getStartCursorPosition().setOffsetY((String) value);
		} else if(id.equals("start"+KEY_POSITION)) {
			this.model.getStartCursorPosition().setPosition(PositionPackage.getPosition((Integer) value));
		} else if(id.equals("end"+KEY_TARGETPARAM)) {
			this.model.getEndTaskTarget().setElement((String) value);
		} else if(id.equals("end"+KEY_PATHPICKER)) {
			Path path = new Path();
			try {
				if("".equals(value)){
					this.model.getEndTaskTarget().setPath(null);
				}else{
					path.fromJson(new JSONObject((String)value));
					this.model.getEndTaskTarget().setPath(path);					
				}
			} catch (JSONException e) {
				e.printStackTrace();
				this.model.getEndTaskTarget().setPath(null);
			}
		} else if(id.equals("end"+KEY_TARGETDELAY)) {
			this.model.getEndTaskTarget().setTimeout((String) value);
		} else if(id.equals("end"+KEY_OFFSETX)) {
			this.model.getEndCursorPosition().setOffsetX((String) value);
		} else if(id.equals("end"+KEY_OFFSETY)) {
			this.model.getEndCursorPosition().setOffsetY((String) value);
		} else if(id.equals("end"+KEY_POSITION)) {
			this.model.getEndCursorPosition().setPosition(PositionPackage.getPosition((Integer) value));
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
