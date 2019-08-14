package com.jiuqi.etl.rpa.runtime.window.minimize;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 窗口隐藏任务模型工厂
 * @author liangxiao01
 */
public class WindowMinimizeTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.window.minimize";

	@Override
	public TaskModel createTaskModel() {
		return new WindowMinimizeTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new WindowMinimizeTaskRunner((WindowMinimizeTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
