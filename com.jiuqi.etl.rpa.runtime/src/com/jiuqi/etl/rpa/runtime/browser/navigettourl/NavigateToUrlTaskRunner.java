package com.jiuqi.etl.rpa.runtime.browser.navigettourl;

import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.browser.navigatetourl.NavigateToUrlAction;
import com.jiuqi.rpa.action.browser.navigatetourl.NavigateToUrlInput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.browser.UIBrowser;

/**
 * 关闭浏览器页签任务执行器
 * 
 * @author wangshanyu
 *
 */
public final class NavigateToUrlTaskRunner implements ITaskRunner {
	private NavigateToUrlTaskModel taskModel;

	public NavigateToUrlTaskRunner(NavigateToUrlTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		NavigateToUrlInput actionInput = toActionInput(taskModel, env);
		NavigateToUrlAction action = new NavigateToUrlAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private NavigateToUrlInput toActionInput(NavigateToUrlTaskModel model,Env env) throws ETLEngineException{
		NavigateToUrlInput input = new NavigateToUrlInput();
		String paramName = model.getBrowserIdParamName();
//		Long browserid = (Long)env.getValue(paramName);
		UIBrowser browser = (UIBrowser) new RPAEnvHelper().getElement(env, paramName);
		if(paramName == null || paramName.isEmpty()) throw new ETLEngineException("浏览器ID参数为空，请指定要导航的浏览器 ID");
		if(browser == null) throw new ETLEngineException("浏览器变量为空，请确认变量名是否正确");
		input.setBrowser(browser);
		input.setUrl(env.parseExpr(model.getUrl()));
		return input;
	}

}
