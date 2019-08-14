package com.jiuqi.etl.rpa.runtime.dialog.file;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;


public class SelectFileTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.dialog.file";

	@Override
	public TaskModel createTaskModel() {
		return new SelectFileTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SelectFileTaskRunner((SelectFileTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
