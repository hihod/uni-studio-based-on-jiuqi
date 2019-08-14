package com.jiuqi.etl.rpa.runtime.window.hide;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 窗口隐藏任务模型工厂
 * @author liangxiao01
 */
public class WindowHideTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.window.hide";

	@Override
	public TaskModel createTaskModel() {
		return new WindowHideTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new WindowHideTaskRunner((WindowHideTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
