package com.jiuqi.etl.rpa.runtime.application.open;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 获取文本
 * 
 * @author liangxiao01
 */
public class OpenApplicationTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.application.open";

	@Override
	public TaskModel createTaskModel() {
		return new OpenApplicationTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new OpenApplicationTaskRunner((OpenApplicationTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
