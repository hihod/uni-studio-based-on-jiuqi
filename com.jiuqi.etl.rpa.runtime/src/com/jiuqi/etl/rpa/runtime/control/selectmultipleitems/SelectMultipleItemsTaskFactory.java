package com.jiuqi.etl.rpa.runtime.control.selectmultipleitems;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 多选条目任务模型工厂
 * 
 * @author liangxiao01
 */
public class SelectMultipleItemsTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.control.selectMultipleItems";

	@Override
	public TaskModel createTaskModel() {
		return new SelectMultipleItemsTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SelectMultipleItemsTaskRunner((SelectMultipleItemsTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
