package com.jiuqi.etl.rpa.runtime.control.selectitem;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 单选条目任务模型工厂
 * 
 * @author liangxiao01
 */
public class SelectItemTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.control.selectItem";

	@Override
	public TaskModel createTaskModel() {
		return new SelectItemTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SelectItemTaskRunner((SelectItemTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
