package com.jiuqi.etl.rpa.runtime.find.find;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.find.find.FindAction;
import com.jiuqi.rpa.action.find.find.FindInput;
import com.jiuqi.rpa.action.find.find.FindOutput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class FindTaskRunner implements ITaskRunner {
	private FindTaskModel taskModel;
	private Env env;

	public FindTaskRunner(FindTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		FindInput actionInput = toActionInput(taskModel);
		FindAction action = new FindAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			FindOutput output = (FindOutput) action.run(context);
			
			String elementName = taskModel.getOutputParamName();
			if (StringUtils.isNotEmpty(elementName) && output.getElement() != null)
				new RPAEnvHelper().putElement(env, elementName, output.getElement());
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private FindInput toActionInput(FindTaskModel model) throws ETLEngineException {
		FindInput typetextInput = new FindInput();
		Target target = new Target();
		String elementName = taskModel.getTaskTarget().getElement();
		if (StringUtils.isNotEmpty(elementName)) {
			IUIHandler element = new RPAEnvHelper().getElement(env, elementName);
			target.setElement(element);
		}
		target.setPath(taskModel.getTaskTarget().getPath());
		try{
			target.setTimeout(Integer.parseInt(taskModel.getTaskTarget().getTimeout()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getTaskTarget().getTimeout());
			if(paramModel!=null){
				target.setTimeout((int) env.getValue(taskModel.getTaskTarget().getTimeout()));
			}
		}
		typetextInput.setTarget(target);
		return typetextInput;
	}

}
