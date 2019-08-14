package com.jiuqi.etl.rpa.runtime.extract.getposition;

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
import com.jiuqi.rpa.action.extract.getposition.GetPositionAction;
import com.jiuqi.rpa.action.extract.getposition.GetPositionInput;
import com.jiuqi.rpa.action.extract.getposition.GetPositionOutput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class GetPositionTaskRunner implements ITaskRunner {
	private GetPositionTaskModel taskModel;
	private Env env;

	public GetPositionTaskRunner(GetPositionTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		GetPositionInput actionInput = toActionInput(taskModel);
		GetPositionAction action = new GetPositionAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			GetPositionOutput output = (GetPositionOutput) action.run(context);
			
			if (output.getRect() != null) {
				String pNameX = taskModel.getOutputParamX();
				if (StringUtils.isNotEmpty(pNameX))
					env.setValue(pNameX, output.getRect().x);
				
				String pNameY = taskModel.getOutputParamY();
				if (StringUtils.isNotEmpty(pNameY))
					env.setValue(pNameY, output.getRect().y);
				
				String pNameW = taskModel.getOutputParamW();
				if (StringUtils.isNotEmpty(pNameW))
					env.setValue(pNameW, output.getRect().w);
				
				String pNameH = taskModel.getOutputParamH();
				if (StringUtils.isNotEmpty(pNameH))
					env.setValue(pNameH, output.getRect().h);
			}
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		} catch (ETLEnvException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private GetPositionInput toActionInput(GetPositionTaskModel model) throws ETLEngineException {
		GetPositionInput typetextInput = new GetPositionInput();
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
