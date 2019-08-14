package com.jiuqi.etl.rpa.runtime.image.snapshot;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

public class SnapShotTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.image.snapshot";

	@Override
	public TaskModel createTaskModel() {
		return new SnapShotTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SnapShotTaskRunner((SnapShotTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
