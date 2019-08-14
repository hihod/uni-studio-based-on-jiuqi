package com.jiuqi.etl.rpa.ui.find.findrelative;

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
import com.jiuqi.etl.rpa.runtime.PositionPackage;
import com.jiuqi.etl.rpa.runtime.find.findrelative.FindRelativeTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class FindRelativeProperty implements IPropertySource {

	private FindRelativeTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	private static final String KEY_TARGETPARAM = "元素";
	private static final String KEY_TARGETDELAY = "选择超时";
	private static final String KEY_PATHPICKER = "选择器";
	private static final String KEY_OFFSETX = "X偏移";
	private static final String KEY_OFFSETY = "Y偏移";
	private static final String KEY_POSITION = "位置";
	private static final String KEY_OUTPUT_PARAM = "关联的元素";
	public FindRelativeProperty(TaskContext context) {
		this.model = (FindRelativeTaskModel) context.getTaskModel();
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		TextPropertyDescriptor targetParam = new TextPropertyDescriptor(KEY_TARGETPARAM, KEY_TARGETPARAM);
		UIPickerPopupPropertyDescriptor picker = new UIPickerPopupPropertyDescriptor(KEY_PATHPICKER,KEY_PATHPICKER, this.model.getTaskTarget());
		EditableComboPropertyDescriptor targetDelay = new EditableComboPropertyDescriptor(KEY_TARGETDELAY, KEY_TARGETDELAY,integerParamStrs);
		targetDelay.setValidator(numberValidate);
		targetParam.setCategory("目标");
		picker.setCategory("目标");
		targetDelay.setCategory("目标");
		
		EditableComboPropertyDescriptor offestX = new EditableComboPropertyDescriptor(KEY_OFFSETX,KEY_OFFSETX,integerParamStrs);
		offestX.setValidator(onlyNumberValidate);
		EditableComboPropertyDescriptor offestY = new EditableComboPropertyDescriptor(KEY_OFFSETY, KEY_OFFSETY,integerParamStrs);
		offestY.setValidator(onlyNumberValidate);
		ComboBoxPropertyDescriptor position = new ComboBoxPropertyDescriptor(KEY_POSITION, KEY_POSITION, PositionPackage.getTitleArray());
		offestX.setCategory("输入");
		offestY.setCategory("输入");
		position.setCategory("输入");
		TextPropertyDescriptor element = new TextPropertyDescriptor(KEY_OUTPUT_PARAM, KEY_OUTPUT_PARAM);
		element.setCategory("输出");
		element.setDescription("持有关联元素的变量");
		pds = new IPropertyDescriptor[]{/*目标*/targetParam,picker,targetDelay,
										/*输出*/position,offestX,offestY,element};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_TARGETPARAM)) {
			if(StringUtils.isNotEmpty(this.model.getTaskTarget().getElement())){
				return this.model.getTaskTarget().getElement();
			}
			return "";
		} else if(id.equals(KEY_PATHPICKER)) {
			if(this.model.getTaskTarget().getPath()==null || this.model.getTaskTarget().getPath().getElements().size()==0){
				return "<未配置>";
			}
			return "<已配置>";
		} else if(id.equals(KEY_TARGETDELAY)) {
			return String.valueOf(this.model.getTaskTarget().getTimeout());
		}else if(id.equals(KEY_OUTPUT_PARAM)) {
			if(StringUtils.isNotEmpty(this.model.getOutputParamName())){
				return this.model.getOutputParamName();
			}
			return "";
		} else if(id.equals(KEY_OFFSETX)) {
			return String.valueOf(this.model.getCursorPosition().getOffsetX());
		} else if(id.equals(KEY_OFFSETY)) {
			return String.valueOf(this.model.getCursorPosition().getOffsetY());
		} else if(id.equals(KEY_POSITION)) {
			int index = PositionPackage.getIndex(this.model.getCursorPosition().getPosition());
			return index;
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
		}else if(id.equals(KEY_OUTPUT_PARAM)) {
			this.model.setOutputParamName((String) value);
		} else if(id.equals(KEY_OFFSETX)) {
			this.model.getCursorPosition().setOffsetX((String) value);
		} else if(id.equals(KEY_OFFSETY)) {
			this.model.getCursorPosition().setOffsetY((String) value);
		} else if(id.equals(KEY_POSITION)) {
			this.model.getCursorPosition().setPosition(PositionPackage.getPosition((Integer) value));
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
		//校验数字
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
					return "参数不存在";
				}
			}
		};
}
