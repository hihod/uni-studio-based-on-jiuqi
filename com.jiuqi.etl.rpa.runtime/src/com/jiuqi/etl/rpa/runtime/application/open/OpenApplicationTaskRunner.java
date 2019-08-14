package com.jiuqi.etl.rpa.runtime.application.open;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.application.open.OpenApplicationAction;
import com.jiuqi.rpa.action.application.open.OpenApplicationInput;
import com.jiuqi.rpa.action.application.open.OpenApplicationOutput;
import com.jiuqi.rpa.lib.Context;

public final class OpenApplicationTaskRunner implements ITaskRunner {
	private OpenApplicationTaskModel taskModel;
	private Env env;
	
	public OpenApplicationTaskRunner(OpenApplicationTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		OpenApplicationInput actionInput = toActionInput(taskModel);
		OpenApplicationAction action = new OpenApplicationAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			OpenApplicationOutput output = (OpenApplicationOutput) action.run(context);
			
			String elementName = taskModel.getOutputParamName();
			if (StringUtils.isNotEmpty(elementName) && output.getElement() != null)
				new RPAEnvHelper().putElement(env, elementName, output.getElement());
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private OpenApplicationInput toActionInput(OpenApplicationTaskModel model) {
		OpenApplicationInput input = new OpenApplicationInput();
		input.setPath(env.parseExpr(model.getApplicationPath()));
		try{
			input.setTimeout(Integer.parseInt(model.getTimeout()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(model.getTimeout());
			if(paramModel!=null){
				input.setTimeout((int) env.getValue(model.getTimeout()));
			}
		}
		input.setArgs(env.parseExpr(model.getArgs()));
		return input;
	}

}
