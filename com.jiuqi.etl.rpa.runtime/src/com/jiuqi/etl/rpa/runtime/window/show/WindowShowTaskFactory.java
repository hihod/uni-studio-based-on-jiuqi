package com.jiuqi.etl.rpa.runtime.window.show;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 窗口显示任务模型工厂
 * @author liangxiao01
 */
public class WindowShowTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.window.show";

	@Override
	public TaskModel createTaskModel() {
		return new WindowShowTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new WindowShowTaskRunner((WindowShowTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
