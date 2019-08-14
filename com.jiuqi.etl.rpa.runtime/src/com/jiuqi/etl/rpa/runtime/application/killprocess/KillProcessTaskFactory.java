package com.jiuqi.etl.rpa.runtime.application.killprocess;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * πÿ±’”¶”√
 * 
 * @author liangxiao01
 */
public class KillProcessTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.application.killProcess";

	@Override
	public TaskModel createTaskModel() {
		return new KillProcessTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new KillProcessTaskRunner((KillProcessTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
