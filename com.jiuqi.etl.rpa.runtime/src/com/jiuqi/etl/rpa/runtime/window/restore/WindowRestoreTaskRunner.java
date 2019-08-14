package com.jiuqi.etl.rpa.runtime.window.restore;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.window.restore.WindowRestoreAction;
import com.jiuqi.rpa.action.window.restore.WindowRestoreInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class WindowRestoreTaskRunner implements ITaskRunner {
	private WindowRestoreTaskModel taskModel;
	private Env env;

	public WindowRestoreTaskRunner(WindowRestoreTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;

		WindowRestoreInput actionInput = toActionInput(taskModel);
		WindowRestoreAction action = new WindowRestoreAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private WindowRestoreInput toActionInput(WindowRestoreTaskModel model) throws ETLEngineException {
		WindowRestoreInput input = new WindowRestoreInput();

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
		return input;
	}
}
