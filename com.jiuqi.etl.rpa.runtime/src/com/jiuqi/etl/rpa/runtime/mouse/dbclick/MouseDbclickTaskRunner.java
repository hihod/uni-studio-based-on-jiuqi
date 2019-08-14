package com.jiuqi.etl.rpa.runtime.mouse.dbclick;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.CursorPosition;
import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.mouse.dbclick.MouseDbclickAction;
import com.jiuqi.rpa.action.mouse.dbclick.MouseDbclickInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class MouseDbclickTaskRunner implements ITaskRunner {
	private MouseDbclickTaskModel taskModel;
	private Env env;

	public MouseDbclickTaskRunner(MouseDbclickTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		MouseDbclickInput actionInput = toActionInput(taskModel);
		MouseDbclickAction action = new MouseDbclickAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private MouseDbclickInput toActionInput(MouseDbclickTaskModel model) throws ETLEngineException {
		MouseDbclickInput mouseDbclickInput = new MouseDbclickInput();
		mouseDbclickInput.setMaskKeys(taskModel.getMaskKeys());
		mouseDbclickInput.setClickMode(taskModel.getClickMode());
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
		mouseDbclickInput.setCursorPosition(cursorPosition);
		Target target = new Target();
		String elementName = taskModel.getTaskTarget().getElement();
		if (StringUtils.isNotEmpty(elementName)) {
			IUIHandler element = new RPAEnvHelper().getElement(env, elementName);
			target.setElement(element);
		}
		target.setPath(taskModel.getTaskTarget().getPath());
		target.setTimeout(Integer.valueOf(taskModel.getTaskTarget().getTimeout()));
		mouseDbclickInput.setTarget(target);
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
		mouseDbclickInput.setDelay(delay);
		return mouseDbclickInput;
	}
}
