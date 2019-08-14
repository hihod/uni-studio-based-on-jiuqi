package com.jiuqi.etl.rpa.runtime.keyboard.typesecuretext;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 密文录入任务模型工厂
 * 
 * @author liangxiao01
 */
public class TypeSecureTextTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.keyboard.typeSecureText";

	@Override
	public TaskModel createTaskModel() {
		return new TypeSecureTextTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new TypeSecureTextTaskRunner((TypeSecureTextTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
