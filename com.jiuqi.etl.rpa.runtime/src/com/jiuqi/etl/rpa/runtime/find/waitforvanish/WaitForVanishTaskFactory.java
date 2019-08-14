package com.jiuqi.etl.rpa.runtime.find.waitforvanish;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 等待元素消失任务模型工厂
 * 
 * @author liangxiao01
 */
public class WaitForVanishTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.find.waifForVanish";

	@Override
	public TaskModel createTaskModel() {
		return new WaitForVanishTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new WaitForVanishTaskRunner((WaitForVanishTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
