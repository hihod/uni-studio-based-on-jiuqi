package com.jiuqi.etl.rpa.runtime.window.attach;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

public class WindowAttachTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.window.attach";

	@Override
	public TaskModel createTaskModel() {
		return new WindowAttachTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new WindowAttachTaskRunner((WindowAttachTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
