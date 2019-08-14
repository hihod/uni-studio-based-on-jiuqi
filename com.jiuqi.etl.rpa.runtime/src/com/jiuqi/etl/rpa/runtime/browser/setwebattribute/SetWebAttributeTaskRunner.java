package com.jiuqi.etl.rpa.runtime.browser.setwebattribute;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.browser.setwebattribute.SetWebAttributeAction;
import com.jiuqi.rpa.action.browser.setwebattribute.SetWebAttributeInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class SetWebAttributeTaskRunner implements ITaskRunner {
	private SetWebAttributeTaskModel taskModel;
	private Env env;

	public SetWebAttributeTaskRunner(SetWebAttributeTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		
		SetWebAttributeInput actionInput = toActionInput(taskModel);
		SetWebAttributeAction action = new SetWebAttributeAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private SetWebAttributeInput toActionInput(SetWebAttributeTaskModel model) throws ETLEngineException {
		SetWebAttributeInput input = new SetWebAttributeInput();
		input.setAttributeName(env.parseExpr(model.getAttributeName()));
		input.setAttributeValue(env.parseExpr(model.getAttributeValue()));
		Delay delay = new Delay();
		try{
			delay.setBefore(Integer.parseInt(taskModel.getDelay().getBefore()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getDelay().getBefore());
			if(paramModel!=null){
				delay.setBefore((int) env.getValue(taskModel.getDelay().getBefore()));
			}
		}
		try{
			delay.setAfter(Integer.parseInt(taskModel.getDelay().getAfter()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getDelay().getAfter());
			if(paramModel!=null){
				delay.setAfter((int) env.getValue(taskModel.getDelay().getAfter()));
			}
		}
		input.setDelay(delay);
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
		}
		input.setTarget(target);
		return input;
	}

}
