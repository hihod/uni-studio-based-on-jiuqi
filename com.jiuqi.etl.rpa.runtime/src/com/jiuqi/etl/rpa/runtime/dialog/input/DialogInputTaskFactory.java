package com.jiuqi.etl.rpa.runtime.dialog.input;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 文本录入任务模型工厂
 * 
 * @author liangxiao01
 */
public class DialogInputTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.dialog.input";

	@Override
	public TaskModel createTaskModel() {
		return new DialogInputTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new DialogInputTaskRunner((DialogInputTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
