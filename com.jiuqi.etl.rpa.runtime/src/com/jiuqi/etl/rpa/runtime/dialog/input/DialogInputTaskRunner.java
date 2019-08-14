package com.jiuqi.etl.rpa.runtime.dialog.input;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.ETLEnvException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.dialog.input.DialogInput;
import com.jiuqi.rpa.action.dialog.input.DialogInputAction;
import com.jiuqi.rpa.action.dialog.input.DialogInputOutput;
import com.jiuqi.rpa.lib.Context;

public final class DialogInputTaskRunner implements ITaskRunner {
	private DialogInputTaskModel taskModel;
	private Env env;
	public DialogInputTaskRunner(DialogInputTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		DialogInput actionInput = toActionInput(taskModel);
		DialogInputAction action = new DialogInputAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			
			DialogInputOutput output = (DialogInputOutput) action.run(context);
			
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

	private DialogInput toActionInput(DialogInputTaskModel model) {
		DialogInput input = new DialogInput();
		String[] options = new String[model.getOptions().length];
		for (int i = 0; i < options.length; i++) {
			options[i] = env.parseExpr(model.getOptions()[i]);
		}
		input.setInputLabel(env.parseExpr(model.getInputLabel()));
		input.setInputTitle(env.parseExpr(model.getInputTitle()));
		input.setPassword(model.getPassword());
		input.setOptions(options);
		return input;
	}

}
