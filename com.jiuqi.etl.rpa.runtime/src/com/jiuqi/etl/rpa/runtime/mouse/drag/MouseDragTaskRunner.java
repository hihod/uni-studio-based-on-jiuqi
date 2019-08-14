package com.jiuqi.etl.rpa.runtime.mouse.drag;

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
import com.jiuqi.rpa.action.mouse.drag.MouseDragAction;
import com.jiuqi.rpa.action.mouse.drag.MouseDragInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class MouseDragTaskRunner implements ITaskRunner {
	private MouseDragTaskModel taskModel;
	private Env env;

	public MouseDragTaskRunner(MouseDragTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;

		MouseDragInput actionInput = toActionInput(taskModel);
		MouseDragAction action = new MouseDragAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private MouseDragInput toActionInput(MouseDragTaskModel model) throws ETLEngineException {
		MouseDragInput mouseDragInput = new MouseDragInput();
		mouseDragInput.setMaskKeys(taskModel.getMaskKeys());
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
		mouseDragInput.setDelay(delay);
		CursorPosition startPosition = new CursorPosition();
		try{
			startPosition.setOffsetX(Integer.parseInt(taskModel.getStartCursorPosition().getOffsetX()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getStartCursorPosition().getOffsetX());
			if(paramModel!=null){
				startPosition.setOffsetX((int) env.getValue(taskModel.getStartCursorPosition().getOffsetX()));
			}
		}
		try{
			startPosition.setOffsetY(Integer.parseInt(taskModel.getStartCursorPosition().getOffsetY()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getStartCursorPosition().getOffsetY());
			if(paramModel!=null){
				startPosition.setOffsetY((int) env.getValue(taskModel.getStartCursorPosition().getOffsetY()));
			}
		}
		startPosition.setPosition(model.getStartCursorPosition().getPosition());
		CursorPosition endPosition = new CursorPosition();
		try{
			endPosition.setOffsetX(Integer.parseInt(taskModel.getStartCursorPosition().getOffsetX()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getStartCursorPosition().getOffsetX());
			if(paramModel!=null){
				endPosition.setOffsetX((int) env.getValue(taskModel.getStartCursorPosition().getOffsetX()));
			}
		}
		try{
			endPosition.setOffsetY(Integer.parseInt(taskModel.getEndCursorPosition().getOffsetY()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getEndCursorPosition().getOffsetY());
			if(paramModel!=null){
				endPosition.setOffsetY((int) env.getValue(taskModel.getEndCursorPosition().getOffsetY()));
			}
		}
		endPosition.setPosition(model.getEndCursorPosition().getPosition());
		mouseDragInput.setStartCursorPosition(startPosition);
		mouseDragInput.setEndCursorPosition(endPosition);
		
		Target startTarget = new Target();
		String startElementName = taskModel.getStartTaskTarget().getElement();
		if (StringUtils.isNotEmpty(startElementName)) {
			IUIHandler element = new RPAEnvHelper().getElement(env, startElementName);
			startTarget.setElement(element);
		}
		startTarget.setPath(taskModel.getStartTaskTarget().getPath());
		startTarget.setTimeout(Integer.valueOf(taskModel.getStartTaskTarget().getTimeout()));
		mouseDragInput.setStartTarget(startTarget);
		
		Target endTarget = new Target();
		String endElementName = taskModel.getEndTaskTarget().getElement();
		if (StringUtils.isNotEmpty(endElementName)) {
			IUIHandler element = new RPAEnvHelper().getElement(env, endElementName);
			endTarget.setElement(element);
		}
		endTarget.setPath(taskModel.getEndTaskTarget().getPath());
		endTarget.setTimeout(Integer.valueOf(taskModel.getEndTaskTarget().getTimeout()));
		mouseDragInput.setEndTarget(endTarget);

		return mouseDragInput;
	}
}
