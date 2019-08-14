package com.jiuqi.etl.rpa.runtime.find.findrelative;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 查找关联元素任务模型工厂
 * 
 * @author liangxiao01
 */
public class FindRelativeTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.find.findRelative";

	@Override
	public TaskModel createTaskModel() {
		return new FindRelativeTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new FindRelativeTaskRunner((FindRelativeTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
