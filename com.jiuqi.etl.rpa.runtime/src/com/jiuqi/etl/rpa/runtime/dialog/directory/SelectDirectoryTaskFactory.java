package com.jiuqi.etl.rpa.runtime.dialog.directory;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;


public class SelectDirectoryTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.dialog.directory";

	@Override
	public TaskModel createTaskModel() {
		return new SelectDirectoryTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SelectDirectoryTaskRunner((SelectDirectoryTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
