package com.jiuqi.etl.rpa.runtime.dialog.message;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;


public class DialogMessageTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.dialog.message";

	@Override
	public TaskModel createTaskModel() {
		return new DialogMessageTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new DialogMessageTaskRunner((DialogMessageTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
