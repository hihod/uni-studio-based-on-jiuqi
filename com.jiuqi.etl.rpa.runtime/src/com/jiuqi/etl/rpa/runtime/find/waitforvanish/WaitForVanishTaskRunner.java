package com.jiuqi.etl.rpa.runtime.find.waitforvanish;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.find.waitforvanish.WaitForVanishAction;
import com.jiuqi.rpa.action.find.waitforvanish.WaitForVanishInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class WaitForVanishTaskRunner implements ITaskRunner {
	private WaitForVanishTaskModel taskModel;
	Env env;
	public WaitForVanishTaskRunner(WaitForVanishTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		WaitForVanishInput actionInput = toActionInput(taskModel);
		WaitForVanishAction action = new WaitForVanishAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private WaitForVanishInput toActionInput(WaitForVanishTaskModel model) throws ETLEngineException {
		WaitForVanishInput input = new WaitForVanishInput();
		
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
		input.setTarget(target);
		input.setWaitNotActive(taskModel.isWaitNotActive());
		input.setWaitNotVisible(taskModel.isWaitNotVisible());
		return input;
	}

}
