package com.jiuqi.etl.rpa.runtime.control.gettext;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 获取文本
 * 
 * @author liangxiao01
 */
public class GetTextTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.control.getText";

	@Override
	public TaskModel createTaskModel() {
		return new GetTextTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new GetTextTaskRunner((GetTextTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
