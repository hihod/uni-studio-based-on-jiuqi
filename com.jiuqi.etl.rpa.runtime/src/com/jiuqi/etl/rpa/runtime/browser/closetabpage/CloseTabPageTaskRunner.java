package com.jiuqi.etl.rpa.runtime.browser.closetabpage;

import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.browser.closetabpage.CloseTabPageAction;
import com.jiuqi.rpa.action.browser.closetabpage.CloseTabPageInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.browser.UIBrowser;

/**
 * �ر������ҳǩ����ִ����
 * 
 * @author wangshanyu
 *
 */
public final class CloseTabPageTaskRunner implements ITaskRunner {
	private CloseTabPageTaskModel taskModel;

	public CloseTabPageTaskRunner(CloseTabPageTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		CloseTabPageInput actionInput = toActionInput(taskModel, env);
		CloseTabPageAction action = new CloseTabPageAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private CloseTabPageInput toActionInput(CloseTabPageTaskModel model,Env env) throws ETLEngineException{
		CloseTabPageInput input = new CloseTabPageInput();
		String paramName = model.getBrowserIdParamName();
		UIBrowser browser = (UIBrowser) new RPAEnvHelper().getElement(env, paramName);
		
		if(paramName == null || paramName.isEmpty()) throw new ETLEngineException("�����ID����Ϊ�գ���ָ��Ҫ����������� ID");
		if(browser == null) throw new ETLEngineException("���������Ϊ�գ���ȷ�ϱ������Ƿ���ȷ");
		input.setBrowser(browser);
		return input;
	}

}
