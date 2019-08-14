package com.jiuqi.etl.rpa.runtime.browser.openbrowser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.ETLModelException;
import com.jiuqi.etl.model.IModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.model.graph.Context;
import com.jiuqi.etl.model.problem.IProblems;
import com.jiuqi.etl.model.problem.Problem;
import com.jiuqi.etl.model.problem.Problem.Level;
import com.jiuqi.rpa.lib.browser.WebBrowserType;

/**
 * 打开浏览器任务模型
 * 
 * @author wangshanyu
 */
public class OpenBrowserTaskModel extends TaskModel {
	private WebBrowserType browserType = WebBrowserType.CHROME;
	private String url = "";
	private String outputParam = "";
	private final String ATTR_BROWSER_TYPE = "OPENBROWSER_TYPE";
	private final String ATTR_URL = "OPENBROWSER_URL";
	private final String ATTR_OUTPUT = "OPENBROWSER_OUTPUT";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_BROWSER_TYPE, this.browserType.value);
			e.putOpt(ATTR_OUTPUT, this.outputParam);
			e.putOpt(ATTR_URL, URLEncoder.encode(this.url, "UTF-8"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			throw new ETLModelException("当前OpenBrowserAction【URLEncoder】加密Url出错！", e1);
		}
	}
	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		String browserTypeValue = obj.optString(ATTR_BROWSER_TYPE);
		if (StringUtils.isNotEmpty(browserTypeValue))
			this.setBrowserType(WebBrowserType.valueOf(browserTypeValue.toUpperCase()));
		else
			this.setBrowserType(WebBrowserType.CHROME);
		String encodedUrl = obj.optString(ATTR_URL);
		String decodedUrl = null;
		try {
			decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			throw new ETLModelException("当前OpenBrowserAction【URLDecoder】解析Url出错！", e1);
		}
		this.setUrl(decodedUrl);
		this.setOutputParam(obj.optString(ATTR_OUTPUT));
	}

	@Override
	public String getId() {
		return OpenBrowserTaskFactory.ID;
	}

	public OpenBrowserTaskModel clone() {
		OpenBrowserTaskModel cloned = (OpenBrowserTaskModel) super.clone();
		return cloned;
	}

	public WebBrowserType getBrowserType() {
		return browserType;
	}

	public String getOutputParam() {
		return outputParam;
	}

	public void setOutputParam(String outputParam) {
		this.outputParam = outputParam;
	}

	public void setBrowserType(WebBrowserType browserType) {
		this.browserType = browserType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		
		return result;
	}
}
