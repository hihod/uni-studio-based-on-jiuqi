package com.jiuqi.etl.rpa.ui.image.ocrinvoice;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.image.ocrinvoice.OcrInvoiceTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

/**
 * 
 * @author lpy
 *
 */
public class OcrInvoiceProperty implements IPropertySource {

	private OcrInvoiceTaskModel model;
	private IPropertyDescriptor[] propertyDescriptors;

	private String[] strParamStrs;
	private ParameterModel[] strParams;

	// “输入”组
	private static final String KEY_APP_KEY = "APP KEY";
	private static final String KEY_APP_SECRET = "APP SECRET";
	private static final String KEY_IMAGE_URL = "图片URL";
	private static final String KEY_IMAGE_FILE_PATH = "图片文件路径";
	private static final String KEY_IMAGE_BASE64_DATA = "图片BASE64数据";

	// “输出”组
	private static final String KEY_OCR_RESULT = "OCR识别结果";

	public OcrInvoiceProperty(TaskContext context) {
		this.model = (OcrInvoiceTaskModel) context.getTaskModel();

		strParams = context.getParameters(new DataType[] { DataType.STRING });
		strParamStrs = new String[strParams.length + 1];
		strParamStrs[0] = "<不关联参数>";
		for (int i = 1; i < strParams.length + 1; i++) {
			strParamStrs[i] = strParams[i - 1].getName();
			if (!"".equals(strParams[i - 1].getTitle()))
				strParamStrs[i] += "(" + strParams[i - 1].getTitle() + ")";
		}

		TextPropertyDescriptor appKey = new TextPropertyDescriptor(KEY_APP_KEY, KEY_APP_KEY);
		TextPropertyDescriptor appSecret = new TextPropertyDescriptor(KEY_APP_SECRET, KEY_APP_SECRET);
		TextPropertyDescriptor imageUrl = new TextPropertyDescriptor(KEY_IMAGE_URL, KEY_IMAGE_URL);
		TextPropertyDescriptor imageFilePath = new TextPropertyDescriptor(KEY_IMAGE_FILE_PATH, KEY_IMAGE_FILE_PATH);
		TextPropertyDescriptor imageBase64Data = new TextPropertyDescriptor(KEY_IMAGE_BASE64_DATA,
				KEY_IMAGE_BASE64_DATA);
		appKey.setCategory("输入");
		appSecret.setCategory("输入");
		imageUrl.setCategory("输入");
		imageFilePath.setCategory("输入");
		imageBase64Data.setCategory("输入");
		ComboBoxPropertyDescriptor ocrResult = new ComboBoxPropertyDescriptor(KEY_OCR_RESULT, KEY_OCR_RESULT,
				strParamStrs);
		ocrResult.setCategory("输出");
		propertyDescriptors = new IPropertyDescriptor[] { appKey, appSecret, imageUrl, imageFilePath, imageBase64Data,
				ocrResult };
	}

	@Override
	public Object getEditableValue() {
		return this.model;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(KEY_APP_KEY)) {
			return this.model.getAppKey() != null ? this.model.getAppKey() : "必填项";
		} else if (id.equals(KEY_APP_SECRET)) {
			return this.model.getAppSecret() != null ? this.model.getAppSecret() : "必填项";
		} else if (id.equals(KEY_IMAGE_URL)) {
			return this.model.getImageUrl() != null ? this.model.getImageUrl() : "";
		} else if (id.equals(KEY_IMAGE_FILE_PATH)) {
			return this.model.getImageFilePath() != null ? this.model.getImageFilePath() : "";
		} else if (id.equals(KEY_IMAGE_BASE64_DATA)) {
			return this.model.getImageBase64Data() != null ? this.model.getImageBase64Data() : "";
		} else if (id.equals(KEY_OCR_RESULT)) {
			for (int i = 0; i < strParams.length; i++) {
				if (strParams[i].getName().equals(this.model.getOcrResult()))
					return i + 1;
			}
			return 0;
		}
		return null;
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(KEY_APP_KEY)) {
			this.model.setAppKey(value.toString());
		} else if (id.equals(KEY_APP_SECRET)) {
			this.model.setAppSecret(value.toString());
		} else if (id.equals(KEY_IMAGE_URL)) {
			this.model.setImageUrl(value.toString());
		} else if (id.equals(KEY_IMAGE_FILE_PATH)) {
			this.model.setImageFilePath(value.toString());
		} else if (id.equals(KEY_IMAGE_BASE64_DATA)) {
			this.model.setImageBase64Data(value.toString());
		} else if (id.equals(KEY_OCR_RESULT)) {
			if ((Integer) value == 0) {
				this.model.setOcrResult(null);
			} else {
				this.model.setOcrResult(strParams[(Integer) value - 1].getName());
			}
		}
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
	}

}
