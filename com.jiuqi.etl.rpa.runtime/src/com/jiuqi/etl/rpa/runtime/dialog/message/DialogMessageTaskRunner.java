package com.jiuqi.etl.rpa.runtime.dialog.message;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.ETLEnvException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.dialog.message.DialogMessageAction;
import com.jiuqi.rpa.action.dialog.message.DialogMessageInput;
import com.jiuqi.rpa.action.dialog.message.DialogMessageOutput;
import com.jiuqi.rpa.lib.Context;

public final class DialogMessageTaskRunner implements ITaskRunner {
	private DialogMessageTaskModel taskModel;
	private Env env;
	public DialogMessageTaskRunner(DialogMessageTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		DialogMessageInput actionInput = toActionInput(taskModel);
		DialogMessageAction action = new DialogMessageAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			DialogMessageOutput output = (DialogMessageOutput) action.run(context);
			
			String pName = taskModel.getOutputParamName();
			if (StringUtils.isNotEmpty(pName))
				env.setValue(pName, output.getResult());
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		} catch (ETLEnvException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private DialogMessageInput toActionInput(DialogMessageTaskModel model) {
		DialogMessageInput input = new DialogMessageInput();

		input.setInputTitle(env.parseExpr(model.getInputTitle()));
		input.setInputMsg(env.parseExpr(model.getInputMsg()));
		input.setButtonSuite(model.getButtonGroup());
		return input;
	}

}
