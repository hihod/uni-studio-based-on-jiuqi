package com.jiuqi.etl.rpa.runtime.mouse.hover;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * �����������ģ�͹���
 * @author liangxiao01
 */
public class MouseHoverTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.mouse.hover";

	@Override
	public TaskModel createTaskModel() {
		return new MouseHoverTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new MouseHoverTaskRunner((MouseHoverTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
