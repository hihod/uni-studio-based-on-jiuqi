package com.jiuqi.etl.rpa.runtime.window.close;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

public class WindowCloseTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.window.close";

	@Override
	public TaskModel createTaskModel() {
		return new WindowCloseTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new WindowCloseTaskRunner((WindowCloseTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
