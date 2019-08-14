package com.jiuqi.etl.rpa.runtime.window.maximize;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 窗口隐藏任务模型工厂
 * @author liangxiao01
 */
public class WindowMaximizeTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.window.maximize";

	@Override
	public TaskModel createTaskModel() {
		return new WindowMaximizeTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new WindowMaximizeTaskRunner((WindowMaximizeTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
