package com.jiuqi.etl.rpa.runtime.image.ocrinvoice;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.ETLModelException;
import com.jiuqi.etl.model.IModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.model.graph.Context;
import com.jiuqi.etl.model.problem.IProblems;
import com.jiuqi.etl.model.problem.Problem;
import com.jiuqi.etl.model.problem.Problem.Level;

/**
 * 
 * @author lpy
 *
 */
public class OcrInvoiceTaskModel extends TaskModel {

	// “输入”组
	private String appKey;
	private String appSecret;
	private String imageUrl;
	private String imageFilePath;
	private String imageBase64Data;
	// “输出”组
	private String ocrResult;

	// “输入”组
	private final String ATTR_APP_KEY = "APP_KEY";
	private final String ATTR_APP_SECRET = "APP_SECRET";
	private final String ATTR_IMAGE_URL = "IMAGE_URL";
	private final String ATTR_IMAGE_FILE_PATH = "IMAGE_FILE_PATH";
	private final String ATTR_IMAGE_BASE64_DATA = "IMAGE_BASE64_DATA";
	// “输出”组
	private final String ATTR_OCR_RESULT = "OCR_RESULT";

	@Override
	public String getId() {
		return OcrInvoiceTaskFactory.ID;
	}

	@Override
	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.put(ATTR_APP_KEY, appKey);
			e.put(ATTR_APP_SECRET, appSecret);
			e.put(ATTR_IMAGE_URL, imageUrl);
			e.put(ATTR_IMAGE_FILE_PATH, imageFilePath);
			e.put(ATTR_IMAGE_BASE64_DATA, imageBase64Data);
			e.put(ATTR_OCR_RESULT, ocrResult);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		this.appKey = e.optString(ATTR_APP_KEY).equals("") ? "必填项" : e.optString(ATTR_APP_KEY);
		this.appSecret = e.optString(ATTR_APP_SECRET).equals("") ? "必填项" : e.optString(ATTR_APP_SECRET);
		this.imageUrl = e.optString(ATTR_IMAGE_URL);
		this.imageFilePath = e.optString(ATTR_IMAGE_FILE_PATH);
		this.imageBase64Data = e.optString(ATTR_IMAGE_BASE64_DATA);
		this.ocrResult = e.optString(ATTR_OCR_RESULT);
	}

	@Override
	public OcrInvoiceTaskModel clone() {
		OcrInvoiceTaskModel cloned = (OcrInvoiceTaskModel) super.clone();
		return cloned;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}

	public String getImageBase64Data() {
		return imageBase64Data;
	}

	public void setImageBase64Data(String imageBase64Data) {
		this.imageBase64Data = imageBase64Data;
	}

	public String getOcrResult() {
		return ocrResult;
	}

	public void setOcrResult(String ocrResult) {
		this.ocrResult = ocrResult;
	}
	
	@Override
	public boolean validate(IProblems problems, IModel parent) {
		boolean result =  super.validate(problems, parent);
		if (!(parent instanceof ControlFlowModel)) {
			Problem problem = new Problem();
			problem.setLevel(Level.ERROR);
			problem.setParent(parent);
			problem.setSource(this);
			problem.setDescription("无法验证适配器的正确性");
			problems.add(problem);
			return false;
		}
		if(getAppKey()==null||getAppKey().equals("")||getAppKey().equals("必填项")) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置必填项AppKey");
			problems.add(p);
			result &= false;
		} 
		if(getAppSecret()==null||getAppSecret().equals("")||getAppSecret().equals("必填项")) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置必填项AppSecret");
			problems.add(p);
			result &= false;
		} 
		if((getImageUrl()==null&&getImageFilePath()==null&&getImageBase64Data()==null)||(getImageUrl().equals("")&&getImageFilePath().equals("")&&getImageBase64Data().equals(""))) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("图片URL、图片文件路径及图片BASE64数据需至少设置一项");
			problems.add(p);
			result &= false;
		} 
		return result;
	}

}
