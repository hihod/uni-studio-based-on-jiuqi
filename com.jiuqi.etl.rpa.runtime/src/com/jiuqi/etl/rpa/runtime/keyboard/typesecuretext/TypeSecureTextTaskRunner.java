package com.jiuqi.etl.rpa.runtime.keyboard.typesecuretext;

import com.jiuqi.bi.util.JqLib;
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
import com.jiuqi.rpa.action.keyboard.typesecuretext.TypeSecuretextAction;
import com.jiuqi.rpa.action.keyboard.typesecuretext.TypeSecuretextInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class TypeSecureTextTaskRunner implements ITaskRunner {
	private TypeSecureTextTaskModel taskModel;
	private Env env;

	public TypeSecureTextTaskRunner(TypeSecureTextTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;

		TypeSecuretextInput actionInput = toActionInput(taskModel);
		TypeSecuretextAction action = new TypeSecuretextAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private TypeSecuretextInput toActionInput(TypeSecureTextTaskModel model) throws ETLEngineException {
		TypeSecuretextInput typeSecuretextInput = new TypeSecuretextInput();
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
		typeSecuretextInput.setTarget(target);

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
		typeSecuretextInput.setDelay(delay);
		typeSecuretextInput.setTypeMode(taskModel.getTypeMode());
		typeSecuretextInput.setExpression(JqLib.decodePassword(model.getExpression()));
		try{
			typeSecuretextInput.setDelayBetweenKeys(Integer.parseInt(model.getDelayBetweenKeys()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(model.getDelayBetweenKeys());
			if(paramModel!=null){
				typeSecuretextInput.setDelayBetweenKeys((int) env.getValue(model.getDelayBetweenKeys()));
			}
		}
		typeSecuretextInput.setClearBeforeType(model.getClearBeforeType());
		typeSecuretextInput.setAttach(model.getAttach());
		return typeSecuretextInput;
	}

}
