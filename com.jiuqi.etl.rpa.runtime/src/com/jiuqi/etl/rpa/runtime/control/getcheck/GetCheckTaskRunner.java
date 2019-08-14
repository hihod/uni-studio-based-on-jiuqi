package com.jiuqi.etl.rpa.runtime.control.getcheck;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.ETLEnvException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.control.getcheck.GetCheckAction;
import com.jiuqi.rpa.action.control.getcheck.GetCheckInput;
import com.jiuqi.rpa.action.control.getcheck.GetCheckOutput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class GetCheckTaskRunner implements ITaskRunner {
	private GetCheckTaskModel taskModel;
	private Env env;

	public GetCheckTaskRunner(GetCheckTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		
		GetCheckInput actionInput = toActionInput(taskModel);
		GetCheckAction action = new GetCheckAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			GetCheckOutput output = (GetCheckOutput) action.run(context);
			
			String checkedParamName = taskModel.getOutputParamCheck();
			if (StringUtils.isNotEmpty(checkedParamName))
				env.setValue(checkedParamName, output.getCheck());
			
			String checkStateParamName = taskModel.getOutputParamState();
			if (StringUtils.isNotEmpty(checkStateParamName))
				env.setValue(checkStateParamName, output.getCheckState().toString());
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		} catch (ETLEnvException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private GetCheckInput toActionInput(GetCheckTaskModel model) throws ETLEngineException {
		GetCheckInput typetextInput = new GetCheckInput();
		
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
