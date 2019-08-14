package com.jiuqi.etl.rpa.runtime.control.hightlight;


import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.control.hightlight.HightlightAction;
import com.jiuqi.rpa.action.control.hightlight.HightlightInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class HightlightTaskRunner implements ITaskRunner {
	private HightlightTaskModel taskModel;
	private Env env;
	public HightlightTaskRunner(HightlightTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		
		HightlightInput actionInput = toActionInput(taskModel);
		HightlightAction action = new HightlightAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(),e);
		}
	}
	private HightlightInput toActionInput(HightlightTaskModel model) throws ETLEngineException{
		HightlightInput hightlightInput = new HightlightInput();
		hightlightInput.setColor(model.getHightlightColor());
		try{
			hightlightInput.setTime(Integer.parseInt(model.getHightlightTime()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(model.getHightlightTime());
			if(paramModel!=null){
				hightlightInput.setTime((int) env.getValue(model.getHightlightTime()));
			}
		}
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
		hightlightInput.setTarget(target);
		return hightlightInput;
	}
	
}
