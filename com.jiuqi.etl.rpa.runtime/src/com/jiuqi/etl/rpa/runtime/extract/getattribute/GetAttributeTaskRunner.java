package com.jiuqi.etl.rpa.runtime.extract.getattribute;

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
import com.jiuqi.rpa.action.extract.getattribute.GetAttributeAction;
import com.jiuqi.rpa.action.extract.getattribute.GetAttributeInput;
import com.jiuqi.rpa.action.extract.getattribute.GetAttributeOutput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class GetAttributeTaskRunner implements ITaskRunner {
	private GetAttributeTaskModel taskModel;
	private Env env;

	public GetAttributeTaskRunner(GetAttributeTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		GetAttributeInput actionInput = toActionInput(taskModel);
		GetAttributeAction action = new GetAttributeAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			GetAttributeOutput output = (GetAttributeOutput) action.run(context);
			
			String pName = taskModel.getOutputParamName();
			if (StringUtils.isNotEmpty(pName))
				env.setValue(pName, output.getValue());
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		} catch (ETLEnvException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private GetAttributeInput toActionInput(GetAttributeTaskModel model) throws ETLEngineException {
		GetAttributeInput typetextInput = new GetAttributeInput();
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
		typetextInput.setAttributeName(env.parseExpr(model.getAttributeName()));
		return typetextInput;
	}

}
