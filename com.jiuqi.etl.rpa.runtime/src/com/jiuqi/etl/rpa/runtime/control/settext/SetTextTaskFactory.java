package com.jiuqi.etl.rpa.runtime.control.settext;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * �ı�¼������ģ�͹���
 * 
 * @author liangxiao01
 */
public class SetTextTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.control.setText";

	@Override
	public TaskModel createTaskModel() {
		return new SetTextTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SetTextTaskRunner((SetTextTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
