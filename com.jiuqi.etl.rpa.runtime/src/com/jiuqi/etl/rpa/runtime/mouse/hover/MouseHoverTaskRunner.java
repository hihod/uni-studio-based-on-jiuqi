package com.jiuqi.etl.rpa.runtime.mouse.hover;

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
import com.jiuqi.rpa.action.mouse.hover.MouseHoverAction;
import com.jiuqi.rpa.action.mouse.hover.MouseHoverInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class MouseHoverTaskRunner implements ITaskRunner {
	private MouseHoverTaskModel taskModel;
	private Env env;

	public MouseHoverTaskRunner(MouseHoverTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;

		MouseHoverInput actionInput = toActionInput(taskModel);
		MouseHoverAction action = new MouseHoverAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private MouseHoverInput toActionInput(MouseHoverTaskModel model) throws ETLEngineException {
		MouseHoverInput mouseHoverInput = new MouseHoverInput();
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
		mouseHoverInput.setCursorPosition(cursorPosition);
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
		mouseHoverInput.setTarget(target);
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
		mouseHoverInput.setDelay(delay);
		return mouseHoverInput;
	}
}
