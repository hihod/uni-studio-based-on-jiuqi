package com.jiuqi.etl.rpa.runtime.browser.openbrowser;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.browser.openbrowser.OpenBrowserAction;
import com.jiuqi.rpa.action.browser.openbrowser.OpenBrowserInput;
import com.jiuqi.rpa.action.browser.openbrowser.OpenBrowserOutput;
import com.jiuqi.rpa.lib.Context;

/**
 * 打开浏览器任务执行器
 * 
 * @author wangshanyu
 *
 */
public final class OpenBrowserTaskRunner implements ITaskRunner {
	private OpenBrowserTaskModel taskModel;

	public OpenBrowserTaskRunner(OpenBrowserTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		OpenBrowserInput actionInput = toActionInput(taskModel,env);

		OpenBrowserOutput output;
		OpenBrowserAction action = new OpenBrowserAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			output = (OpenBrowserOutput) action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
		
		String elementName = taskModel.getOutputParam();
		if (StringUtils.isNotEmpty(elementName) && output.getBrowser() != null) {
			new RPAEnvHelper().putElement(env, elementName, output.getBrowser());
		}
	}

	private OpenBrowserInput toActionInput(OpenBrowserTaskModel model,Env env) {
		OpenBrowserInput input = new OpenBrowserInput();
		input.setBrowserType(model.getBrowserType());
		input.setUrl(env.parseExpr(model.getUrl()));
		return input;
	}

}
