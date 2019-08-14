package com.jiuqi.etl.rpa.runtime.extract.getattribute;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * �ı�¼������ģ�͹���
 * 
 * @author liangxiao01
 */
public class GetAttributeTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.extrect.getAttribute";

	@Override
	public TaskModel createTaskModel() {
		return new GetAttributeTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new GetAttributeTaskRunner((GetAttributeTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
