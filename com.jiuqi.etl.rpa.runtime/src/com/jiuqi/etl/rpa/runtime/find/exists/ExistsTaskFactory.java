package com.jiuqi.etl.rpa.runtime.find.exists;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * �ı�¼������ģ�͹���
 * 
 * @author liangxiao01
 */
public class ExistsTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.find.exists";

	@Override
	public TaskModel createTaskModel() {
		return new ExistsTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new ExistsTaskRunner((ExistsTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
