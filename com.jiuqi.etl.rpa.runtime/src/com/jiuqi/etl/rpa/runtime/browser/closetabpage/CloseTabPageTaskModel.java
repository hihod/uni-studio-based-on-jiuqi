package com.jiuqi.etl.rpa.runtime.browser.closetabpage;

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

/**
 * 打开浏览器任务模型
 * 
 * @author wangshanyu
 */
public class CloseTabPageTaskModel extends TaskModel {

	private String browserIdParamName = "";

	private final String ATTR_BROWSER_ID_PARAM_NAME = "CLOSETABPAGE_BROWSERID_PARAMNAME";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_BROWSER_ID_PARAM_NAME, browserIdParamName);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.setBrowserIdParamName(obj.optString(ATTR_BROWSER_ID_PARAM_NAME));
	}

	@Override
	public String getId() {
		return CloseTabPageTaskFactory.ID;
	}

	public CloseTabPageTaskModel clone() {
		CloseTabPageTaskModel cloned = (CloseTabPageTaskModel) super.clone();
		return cloned;
	}

	public String getBrowserIdParamName() {
		return browserIdParamName;
	}

	public void setBrowserIdParamName(String browserIdParamName) {
		this.browserIdParamName = browserIdParamName;
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
		if(StringUtils.isEmpty(browserIdParamName)) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("浏览器ID不能为空");
			problems.add(p);
			result &= false;
		} 
		return result;
	}
}
