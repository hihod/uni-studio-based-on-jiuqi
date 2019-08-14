package com.jiuqi.etl.rpa.runtime.application.close;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * πÿ±’”¶”√
 * 
 * @author liangxiao01
 */
public class CloseApplicationTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.application.close";

	@Override
	public TaskModel createTaskModel() {
		return new CloseApplicationTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new CloseApplicationTaskRunner((CloseApplicationTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
