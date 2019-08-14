package com.jiuqi.etl.rpa.runtime.browser.setwebattribute;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

public class SetWebAttributeTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.browser.setWebAttribute";

	public TaskModel createTaskModel() {
		return new SetWebAttributeTaskModel();
	}

	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SetWebAttributeTaskRunner((SetWebAttributeTaskModel) model);
	}

	public String getTaskId() {
		return ID;
	}

}
