package com.jiuqi.etl.rpa.runtime.application.close;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.application.close.CloseApplicationAction;
import com.jiuqi.rpa.action.application.close.CloseApplicationInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class CloseApplicationTaskRunner implements ITaskRunner {
	private Env env;
	private CloseApplicationTaskModel taskModel;

	public CloseApplicationTaskRunner(CloseApplicationTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;

		CloseApplicationInput actionInput = toActionInput(taskModel);
		CloseApplicationAction action = new CloseApplicationAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private CloseApplicationInput toActionInput(CloseApplicationTaskModel model) throws ETLEngineException {
		CloseApplicationInput typetextInput = new CloseApplicationInput();
		Target target = new Target();
		if (StringUtils.isNotEmpty(taskModel.getTaskTarget().getElement())) {
			String elementName = taskModel.getTaskTarget().getElement();
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
		}		typetextInput.setTarget(target);
		return typetextInput;
	}

}
