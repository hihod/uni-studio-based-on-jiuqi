package com.jiuqi.etl.rpa.runtime.find.findrelative;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.find.findrelative.FindRelativeAction;
import com.jiuqi.rpa.action.find.findrelative.FindRelativeInput;
import com.jiuqi.rpa.action.find.findrelative.FindRelativeOutput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class FindRelativeTaskRunner implements ITaskRunner {
	private FindRelativeTaskModel taskModel;
	private Env env;

	public FindRelativeTaskRunner(FindRelativeTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		FindRelativeInput actionInput = toActionInput(taskModel);
		FindRelativeAction action = new FindRelativeAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			FindRelativeOutput output = (FindRelativeOutput) action.run(context);
			
			String elementName = taskModel.getOutputParamName();
			if (StringUtils.isNotEmpty(elementName) && output.getElement() != null)
				new RPAEnvHelper().putElement(env, elementName, output.getElement());
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private FindRelativeInput toActionInput(FindRelativeTaskModel model) throws ETLEngineException {
		FindRelativeInput input = new FindRelativeInput();
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
		CursorPosition cursorPosition = new CursorPosition();
		try{
			cursorPosition.setOffsetX(Integer.parseInt(taskModel.getCursorPosition().getOffsetX()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getCursorPosition().getOffsetX());
			if(paramModel!=null){
				cursorPosition.setOffsetX((int) env.getValue(taskModel.getCursorPosition().getOffsetX()));
			}
		}
		try{
			cursorPosition.setOffsetY(Integer.parseInt(taskModel.getCursorPosition().getOffsetY()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getCursorPosition().getOffsetY());
			if(paramModel!=null){
				cursorPosition.setOffsetY((int) env.getValue(taskModel.getCursorPosition().getOffsetY()));
			}
		}
		cursorPosition.setPosition(taskModel.getCursorPosition().getPosition());
		input.setCursorPosition(cursorPosition);
		return input;
	}

}
