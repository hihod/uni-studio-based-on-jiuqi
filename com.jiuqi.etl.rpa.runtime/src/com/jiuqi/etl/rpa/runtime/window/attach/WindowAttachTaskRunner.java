package com.jiuqi.etl.rpa.runtime.window.attach;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.window.attach.WindowAttachAction;
import com.jiuqi.rpa.action.window.attach.WindowAttachInput;
import com.jiuqi.rpa.action.window.attach.WindowAttachOutput;
import com.jiuqi.rpa.lib.Context;

public final class WindowAttachTaskRunner implements ITaskRunner {
	private WindowAttachTaskModel taskModel;
	private Env env;
	public WindowAttachTaskRunner(WindowAttachTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		WindowAttachInput actionInput = toActionInput(taskModel);
		WindowAttachAction action = new WindowAttachAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			WindowAttachOutput output = (WindowAttachOutput) action.run(context);
			
			String elementName = taskModel.getOutputParamName();
			if (StringUtils.isNotEmpty(elementName) && output.getElement() != null)
				new RPAEnvHelper().putElement(env, elementName, output.getElement());
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private WindowAttachInput toActionInput(WindowAttachTaskModel model) throws ETLEngineException {
		WindowAttachInput input = new WindowAttachInput();
		input.setPath(taskModel.getPath());
		try{
			input.setTimeout(Integer.parseInt(taskModel.getTimeout()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getTimeout());
			if(paramModel!=null){
				input.setTimeout((int) env.getValue(taskModel.getTimeout()));
			}
		}
		return input;
	}
}
