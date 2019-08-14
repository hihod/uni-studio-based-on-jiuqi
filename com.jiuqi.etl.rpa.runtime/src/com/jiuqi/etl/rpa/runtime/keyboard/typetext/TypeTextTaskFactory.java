package com.jiuqi.etl.rpa.runtime.keyboard.typetext;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 文本录入任务模型工厂
 * 
 * @author liangxiao01
 */
public class TypeTextTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.keyboard.typeText";

	@Override
	public TaskModel createTaskModel() {
		return new TypeTextTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new TypeTextTaskRunner((TypeTextTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
