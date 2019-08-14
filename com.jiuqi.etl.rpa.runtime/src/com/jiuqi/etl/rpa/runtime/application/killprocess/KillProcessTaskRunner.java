package com.jiuqi.etl.rpa.runtime.application.killprocess;

import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.application.killprocess.KillProcessAction;
import com.jiuqi.rpa.action.application.killprocess.KillProcessInput;
import com.jiuqi.rpa.lib.Context;

public final class KillProcessTaskRunner implements ITaskRunner {
	private KillProcessTaskModel taskModel;
	private Env env;
	public KillProcessTaskRunner(KillProcessTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		KillProcessInput actionInput = toActionInput(taskModel);
		KillProcessAction action = new KillProcessAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			action.run(context);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private KillProcessInput toActionInput(KillProcessTaskModel model) {
		KillProcessInput killProcessInput = new KillProcessInput();
		killProcessInput.setProcessName(env.parseExpr(model.getProcessName()));
		return killProcessInput;
	}

}
