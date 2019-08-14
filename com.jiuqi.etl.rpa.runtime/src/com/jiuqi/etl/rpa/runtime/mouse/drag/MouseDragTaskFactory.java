package com.jiuqi.etl.rpa.runtime.mouse.drag;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 鼠标拖拽任务模型工厂
 * @author liangxiao01
 */
public class MouseDragTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.mouse.drag";

	@Override
	public TaskModel createTaskModel() {
		return new MouseDragTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new MouseDragTaskRunner((MouseDragTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
