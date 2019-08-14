package com.jiuqi.etl.rpa.runtime.window.move;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.window.move.WindowMoveAction;
import com.jiuqi.rpa.action.window.move.WindowMoveInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class WindowMoveTaskRunner implements ITaskRunner {
	private WindowMoveTaskModel taskModel;
	private Env env;

	public WindowMoveTaskRunner(WindowMoveTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;

		WindowMoveInput actionInput = toActionInput(taskModel);
		WindowMoveAction action = new WindowMoveAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private WindowMoveInput toActionInput(WindowMoveTaskModel model) throws ETLEngineException {
		WindowMoveInput input = new WindowMoveInput();

		Target target = new Target();
		String elementName = taskModel.getTaskTarget().getElement();
		if (StringUtils.isNotEmpty(elementName)) {
			IUIHandler element = new RPAEnvHelper().getElement(env, elementName);
			target.setElement(element);
		}
		try{
			input.getRect().x = Integer.parseInt(model.getInputX());
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(model.getInputX());
			if(paramModel!=null){
				input.getRect().x = (int) env.getValue(model.getInputX());
			}
		}
		try{
			input.getRect().y = Integer.parseInt(model.getInputY());
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(model.getInputY());
			if(paramModel!=null){
				input.getRect().y = (int) env.getValue(model.getInputY());
			}
		}
		try{
			input.getRect().w = Integer.parseInt(model.getInputW());
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(model.getInputW());
			if(paramModel!=null){
				input.getRect().w = (int) env.getValue(model.getInputW());
			}
		}
		try{
			input.getRect().h = Integer.parseInt(model.getInputH());
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(model.getInputH());
			if(paramModel!=null){
				input.getRect().h = (int) env.getValue(model.getInputH());
			}
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
