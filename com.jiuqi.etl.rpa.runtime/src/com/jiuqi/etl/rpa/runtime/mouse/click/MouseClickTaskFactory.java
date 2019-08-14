package com.jiuqi.etl.rpa.runtime.mouse.click;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 鼠标单击任务模型工厂
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class MouseClickTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.mouse.click";

	@Override
	public TaskModel createTaskModel() {
		return new MouseClickTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new MouseClickTaskRunner((MouseClickTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
