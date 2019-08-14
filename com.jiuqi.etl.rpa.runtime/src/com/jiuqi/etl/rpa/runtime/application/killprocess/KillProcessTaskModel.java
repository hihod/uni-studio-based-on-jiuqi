package com.jiuqi.etl.rpa.runtime.application.killprocess;

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
 * 
 * 
 * @author liangxiao01
 */
public class KillProcessTaskModel extends TaskModel {
	private String processName = "";

	private final String ATTR_PROCESSNAME = "processName";

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.put(ATTR_PROCESSNAME, processName);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.processName = obj.optString(ATTR_PROCESSNAME);

	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Override
	public String getId() {
		return KillProcessTaskFactory.ID;
	}

	public KillProcessTaskModel clone() {
		KillProcessTaskModel cloned = (KillProcessTaskModel) super.clone();
		return cloned;
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
		if(StringUtils.isEmpty(processName)) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置进程名");
			problems.add(p);
			result &= false;
		} 
		return result;
	}
}
