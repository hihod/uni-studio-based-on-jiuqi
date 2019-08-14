package com.jiuqi.etl.rpa.runtime.dialog.directory;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.ETLEnvException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.dialog.dictionary.SelectDirectoryAction;
import com.jiuqi.rpa.action.dialog.dictionary.SelectDirectoryInput;
import com.jiuqi.rpa.action.dialog.dictionary.SelectDirectoryOutput;
import com.jiuqi.rpa.lib.Context;

public final class SelectDirectoryTaskRunner implements ITaskRunner {
	private SelectDirectoryTaskModel taskModel;
	private Env env;
	public SelectDirectoryTaskRunner(SelectDirectoryTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		SelectDirectoryInput actionInput = toActionInput(taskModel);
		SelectDirectoryAction action = new SelectDirectoryAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			SelectDirectoryOutput output = (SelectDirectoryOutput) action.run(context);
			
			String pName = taskModel.getOutputParamName();
			if (StringUtils.isNotEmpty(pName))
				env.setValue(pName, output.getFilePath());
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		} catch (ETLEnvException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private SelectDirectoryInput toActionInput(SelectDirectoryTaskModel model) {
		SelectDirectoryInput input = new SelectDirectoryInput();
		input.setInitPath(env.parseExpr(model.getInitPath()));
		return input;
	}

}
