package com.jiuqi.etl.rpa.runtime.application.open;

import org.jdom.Element;
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
 * 打开应用
 * 
 * @author liangxiao01
 */
public class OpenApplicationTaskModel extends TaskModel {
	private String outputParamName = "";
	private String applicationPath = "";
	private String args = "";
	private String timeout = "30000";
	private final String ATTR_APP_PATH = "APP_PATH";
	private final String ATTR_APP_ARGS = "APP_ARGS";
	private final String ATTR_APP_TIMEOUT = "APP_TIMEOUT";
	private final String ATTR_OUTPUT_PARAMNAME = "OUTPUT_PARAMNAME";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_APP_PATH, applicationPath);
			e.putOpt(ATTR_APP_ARGS, args);
			e.putOpt(ATTR_APP_TIMEOUT, timeout);
			e.putOpt(ATTR_OUTPUT_PARAMNAME, outputParamName);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.applicationPath = obj.optString(ATTR_APP_PATH);
		this.args = obj.optString(ATTR_APP_ARGS);
		this.timeout = obj.optString(ATTR_APP_TIMEOUT);
		this.outputParamName = obj.optString(ATTR_OUTPUT_PARAMNAME);

	}

	protected void save(Element e, Context context) throws ETLModelException {
		super.save(e, context);
		e.setAttribute(ATTR_APP_PATH, applicationPath);
		e.setAttribute(ATTR_APP_ARGS, args);
		e.setAttribute(ATTR_APP_TIMEOUT, String.valueOf(timeout));
		e.setAttribute(ATTR_OUTPUT_PARAMNAME, String.valueOf(outputParamName));
	}

	protected void load(Element e, Context context) throws ETLModelException {
		super.load(e, context);
		this.setOutputParamName(String.valueOf(e.getAttributeValue(ATTR_OUTPUT_PARAMNAME)));
		this.setApplicationPath(String.valueOf(e.getAttributeValue(ATTR_APP_PATH)));
		this.setTimeout(e.getAttributeValue(ATTR_APP_TIMEOUT));
		this.setArgs(String.valueOf(e.getAttributeValue(ATTR_APP_ARGS)));
	}

	@Override
	public String getId() {
		return OpenApplicationTaskFactory.ID;
	}

	public OpenApplicationTaskModel clone() {
		OpenApplicationTaskModel cloned = (OpenApplicationTaskModel) super.clone();
		return cloned;
	}

	public String getOutputParamName() {
		return outputParamName;
	}

	public void setOutputParamName(String outputParamName) {
		this.outputParamName = outputParamName;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getApplicationPath() {
		return applicationPath;
	}

	public void setApplicationPath(String applicationPath) {
		this.applicationPath = applicationPath;
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
		if(StringUtils.isEmpty(applicationPath)) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置可执行文件路径");
			problems.add(p);
			result &= false;
		} 
		return result;
	}
}
