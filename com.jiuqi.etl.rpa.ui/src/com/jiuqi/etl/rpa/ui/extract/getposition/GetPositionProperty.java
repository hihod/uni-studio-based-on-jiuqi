package com.jiuqi.etl.rpa.ui.extract.getposition;

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
import com.jiuqi.etl.rpa.runtime.extract.getposition.GetPositionTaskModel;
import com.jiuqi.etl.rpa.ui.EditableComboPropertyDescriptor;
import com.jiuqi.etl.rpa.ui.UIPickerPopupPropertyDescriptor;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.lib.find.Path;

public class GetPositionProperty implements IPropertySource {

	private GetPositionTaskModel model;
	private IPropertyDescriptor[] pds;
	private String[] paramStrs;
	private ParameterModel[] params;
	private static final String KEY_TARGETPARAM = "元素";
	private static final String KEY_TARGETDELAY = "选择超时";
	private static final String KEY_PATHPICKER = "选择器";
	private static final String KEY_X = "X";
	private static final String KEY_Y = "Y";
	private static final String KEY_W = "W";
	private static final String KEY_H = "H";
	public GetPositionProperty(TaskContext context) {
		this.model = (GetPositionTaskModel) context.getTaskModel();
		
		params = context.getParameters(new DataType[]{DataType.INTEGER});
		paramStrs = new String[params.length + 1];
		paramStrs[0] = "<不关联参数>";
		for (int i = 1; i < params.length + 1; i++) {
			paramStrs[i] = params[i - 1].getName();
			if (!"".equals(params[i - 1].getTitle()))
				paramStrs[i] += "(" + params[i - 1].getTitle() + ")";
		}
		ComboBoxPropertyDescriptor outputParamX = new ComboBoxPropertyDescriptor(KEY_X, KEY_X,paramStrs);
		outputParamX.setCategory("输出");
		ComboBoxPropertyDescriptor outputParamY = new ComboBoxPropertyDescriptor(KEY_Y, KEY_Y,paramStrs);
		outputParamY.setCategory("输出");
		ComboBoxPropertyDescriptor outputParamW = new ComboBoxPropertyDescriptor(KEY_W, KEY_W,paramStrs);
		outputParamW.setCategory("输出");
		ComboBoxPropertyDescriptor outputParamH = new ComboBoxPropertyDescriptor(KEY_H, KEY_H,paramStrs);
		outputParamH.setCategory("输出");
		
		
		TextPropertyDescriptor targetParam = new TextPropertyDescriptor(KEY_TARGETPARAM, KEY_TARGETPARAM);
		UIPickerPopupPropertyDescriptor picker = new UIPickerPopupPropertyDescriptor(KEY_PATHPICKER,KEY_PATHPICKER, this.model.getTaskTarget());
		EditableComboPropertyDescriptor targetDelay = new EditableComboPropertyDescriptor(KEY_TARGETDELAY, KEY_TARGETDELAY,paramStrs);
		targetDelay.setValidator(numberValidate);
		targetParam.setCategory("目标");
		picker.setCategory("目标");
		targetDelay.setCategory("目标");
		
		
		pds = new IPropertyDescriptor[]{/**/targetParam,picker,targetDelay,
										/**/outputParamX,outputParamY,outputParamW,outputParamH};
	
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(KEY_X)){
			for (int i = 0; i < params.length; i ++) {
				if (params[i].getName().equals(this.model.getOutputParamX()))
					return i + 1;
			}
			this.model.setOutputParamX(null);
			return 0;
		}else if(id.equals(KEY_Y)){
			for (int i = 0; i < params.length; i ++) {
				if (params[i].getName().equals(this.model.getOutputParamY()))
					return i + 1;
			}
			this.model.setOutputParamY(null);
			return 0;
		}else if(id.equals(KEY_W)){
			for (int i = 0; i < params.length; i ++) {
				if (params[i].getName().equals(this.model.getOutputParamW()))
					return i + 1;
			}
			this.model.setOutputParamW(null);
			return 0;
		}else if(id.equals(KEY_H)){
			for (int i = 0; i < params.length; i ++) {
				if (params[i].getName().equals(this.model.getOutputParamH()))
					return i + 1;
			}
			this.model.setOutputParamH(null);
			return 0;
		}else if(id.equals(KEY_TARGETPARAM)) {
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
		if(id.equals(KEY_X)) {
			if((Integer) value ==0){
				this.model.setOutputParamX(null);
			}else{
				this.model.setOutputParamX(params[(Integer) value -1].getName());
			}
		} else if(id.equals(KEY_Y)) {
			if((Integer) value ==0){
				this.model.setOutputParamY(null);
			}else{
				this.model.setOutputParamY(params[(Integer) value -1].getName());
			}
		} else if(id.equals(KEY_W)) {
			if((Integer) value ==0){
				this.model.setOutputParamW(null);
			}else{
				this.model.setOutputParamW(params[(Integer) value -1].getName());
			}
		} else if(id.equals(KEY_H)) {
			if((Integer) value ==0){
				this.model.setOutputParamH(null);
			}else{
				this.model.setOutputParamH(params[(Integer) value -1].getName());
			}
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
					for (int i = 0; i < paramStrs.length; i++) {
						if(paramName.equals(paramStrs[i])){
							return null;
						}
					}
					return "参数不存在";
				}
			}
		};
}
