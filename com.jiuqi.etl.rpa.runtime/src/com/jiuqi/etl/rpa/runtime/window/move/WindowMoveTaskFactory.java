package com.jiuqi.etl.rpa.runtime.window.move;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

public class WindowMoveTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.window.move";

	@Override
	public TaskModel createTaskModel() {
		return new WindowMoveTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new WindowMoveTaskRunner((WindowMoveTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
