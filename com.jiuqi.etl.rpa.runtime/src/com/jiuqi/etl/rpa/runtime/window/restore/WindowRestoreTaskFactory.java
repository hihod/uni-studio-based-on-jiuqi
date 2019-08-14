package com.jiuqi.etl.rpa.runtime.window.restore;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

public class WindowRestoreTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.window.restore";

	@Override
	public TaskModel createTaskModel() {
		return new WindowRestoreTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new WindowRestoreTaskRunner((WindowRestoreTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
