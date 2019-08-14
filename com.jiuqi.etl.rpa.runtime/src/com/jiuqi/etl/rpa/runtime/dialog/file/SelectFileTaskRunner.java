package com.jiuqi.etl.rpa.runtime.dialog.file;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.ETLEnvException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.dialog.file.SelectFileAction;
import com.jiuqi.rpa.action.dialog.file.SelectFileInput;
import com.jiuqi.rpa.action.dialog.file.SelectFileOutput;
import com.jiuqi.rpa.lib.Context;

public final class SelectFileTaskRunner implements ITaskRunner {
	private SelectFileTaskModel taskModel;
	private Env env;
	public SelectFileTaskRunner(SelectFileTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		SelectFileInput actionInput = toActionInput(taskModel);
		SelectFileAction action = new SelectFileAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			SelectFileOutput output = (SelectFileOutput) action.run(context);
			
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

	private SelectFileInput toActionInput(SelectFileTaskModel model) {
		SelectFileInput input = new SelectFileInput();
		input.setFilters(model.getFilter());
		input.setInitPath(env.parseExpr(model.getInitialPath()));
		return input;
	}

}
