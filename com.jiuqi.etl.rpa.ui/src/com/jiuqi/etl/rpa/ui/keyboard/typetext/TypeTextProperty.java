package com.jiuqi.etl.rpa.ui.keyboard.typetext;

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
import com.jiuqi.etl.rpa.runtime.TypeModePackage;
import com.jiuqi.etl.rpa.runtime.keyboard.typetext.TypeTextTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class TypeTextProperty implements IPropertySource {

	private TypeTextTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] integerParamStrs;
	private ParameterModel[] integerParams;
	private static final String KEY_DELAYBEFORE = "操作前延时";
	private static final String KEY_DELAYAFTER = "操作后延时";
	private static final String KEY_TARGETPARAM = "元素";
	private static final String KEY_TYPEMODEL = "录入方式"; 
	private static final String KEY_TARGETDELAY = "选择超时";
	private static final String KEY_PATHPICKER = "选择器";
	private static final String KEY_EXPRESSION = "文本内容";
	private static final String KEY_CLEAR_BEFORE_TYPE = "录入前清空";
	private static final String KEY_DELAY_BETWEEN_KEYS = "按键间延时";
	private static final String KEY_ATTACH_ELEMENT = "激活";
	public TypeTextProperty(TaskContext context) {
		this.model = (TypeTextTaskModel) context.getTaskModel();
		integerParams = context.getParameters(new DataType[] {DataType.INTEGER});
		integerParamStrs = new String[integerParams.length];
		for (int i = 0; i < integerParams.length; i++) {
			integerParamStrs[i] = integerParams[i].getName();
		}
		TextPropertyDescriptor textInput = new TextPropertyDescriptor(KEY_EXPRESSION,KEY_EXPRESSION);
		ComboBoxPropertyDescriptor typeMode = new ComboBoxPropertyDescriptor(KEY_TYPEMODEL, KEY_TYPEMODEL, TypeModePackage.getTitleArray());
		textInput.setCategory("输入");
		typeMode.setCategory("输入");
		
		ComboBoxPropertyDescriptor clearBeforeType = new ComboBoxPropertyDescriptor(KEY_CLEAR_BEFORE_TYPE, KEY_CLEAR_BEFORE_TYPE, new String[]{"否","是"});
		EditableComboPropertyDescriptor delayBetweenKeys = new EditableComboPropertyDescriptor(KEY_DELAY_BETWEEN_KEYS, KEY_DELAY_BETWEEN_KEYS,integerParamStrs);
		ComboBoxPropertyDescriptor attach = new ComboBoxPropertyDescriptor(KEY_ATTACH_ELEMENT, KEY_ATTACH_ELEMENT, new String[]{"否","是"});
		attach.setCategory("选项");
		attach.setDescription("将元素所在窗口提到顶层并设置焦点");
		delayBetweenKeys.setValidator(numberValidate);
		clearBeforeType.setCategory("选项");
		delayBetweenKeys.setCategory("选项");
		
		EditableComboPropertyDescriptor delayBefore = new EditableComboPropertyDescriptor(KEY_DELAYBEFORE, KEY_DELAYBEFORE,integerParamStrs);
		delayBefore.setValidator(numberValidate);
		EditableComboPropertyDescriptor delayAfter = new EditableComboPropertyDescriptor(KEY_DELAYAFTER, KEY_DELAYAFTER,integerParamStrs);
		delayAfter.setValidator(numberValidate);
		delayBefore.setCategory("通用");
		delayAfter.setCategory("通用");
		
		TextPropertyDescriptor targetParam = new TextPropertyDescriptor(KEY_TARGETPARAM, KEY_TARGETPARAM);
		UIPickerPopupPropertyDescriptor picker = new UIPickerPopupPropertyDescriptor(KEY_PATHPICKER,KEY_PATHPICKER, this.model.getTaskTarget());
		EditableComboPropertyDescriptor targetDelay = new EditableComboPropertyDescriptor(KEY_TARGETDELAY, KEY_TARGETDELAY,integerParamStrs);
		targetDelay.setValidator(numberValidate);
		targetParam.setCategory("目标");
		picker.setCategory("目标");
		targetDelay.setCategory("目标");
		
		
		pds = new IPropertyDescriptor[]{/*输入*/textInput,typeMode,
										/*选项*/clearBeforeType,delayBetweenKeys,attach,
										/*目标*/targetParam,picker,targetDelay,
										/*通用*/delayBefore,delayAfter};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_TYPEMODEL)) {
			int index = TypeModePackage.getIndex(this.model.getTypeMode());
			return index;
		} else if(id.equals(KEY_DELAYBEFORE)) {
			return String.valueOf(this.model.getDelay().getBefore());
		} else if(id.equals(KEY_DELAYAFTER)) {
			return String.valueOf(this.model.getDelay().getAfter());
		} else if(id.equals(KEY_EXPRESSION)) {
			return String.valueOf(this.model.getExpression());
		} else if(id.equals(KEY_CLEAR_BEFORE_TYPE)) {
			if(this.model.getClearBeforeType()){return 1;}else{return 0;}
		} else if(id.equals(KEY_ATTACH_ELEMENT)) {
			if(this.model.getAttach()){return 1;}else{return 0;}	
		} else if(id.equals(KEY_DELAY_BETWEEN_KEYS)) {
			return String.valueOf(this.model.getDelayBetweenKeys());
		} else if(id.equals(KEY_TARGETPARAM)) {
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
		}
		return null;
	}


	public void setPropertyValue(Object id, Object value) {
		if(id.equals(KEY_TYPEMODEL)) {
			this.model.setTypeMode(TypeModePackage.getClickMode((Integer) value));
		} else if(id.equals(KEY_DELAYBEFORE)) {
			this.model.getDelay().setBefore((String) value);
		} else if(id.equals(KEY_DELAYAFTER)) {
			this.model.getDelay().setAfter((String) value);
		} else if(id.equals(KEY_EXPRESSION)) {
			this.model.setExpression((String) value);;
		} else if(id.equals(KEY_CLEAR_BEFORE_TYPE)) {
			if((Integer) value ==0){
				this.model.setClearBeforeType(false);
			}else{
				this.model.setClearBeforeType(true);
			}
		} else if(id.equals(KEY_ATTACH_ELEMENT)) {
			if((Integer) value ==0){
				this.model.setAttach(false);
			}else{
				this.model.setAttach(true);
			}
		} else if(id.equals(KEY_DELAY_BETWEEN_KEYS)) {
			this.model.setDelayBetweenKeys((String) value);
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
}
