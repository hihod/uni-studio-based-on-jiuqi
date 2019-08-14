package com.jiuqi.etl.rpa.runtime.control.setcheck;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * ���ù�ѡ����ģ�͹���
 * 
 * @author liangxiao01
 */
public class SetCheckTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.control.setCheck";

	@Override
	public TaskModel createTaskModel() {
		return new SetCheckTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SetCheckTaskRunner((SetCheckTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
