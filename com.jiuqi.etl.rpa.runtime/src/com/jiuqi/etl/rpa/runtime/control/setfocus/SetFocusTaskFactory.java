package com.jiuqi.etl.rpa.runtime.control.setfocus;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * …Ë÷√Ωπµ„
 * 
 * @author liangxiao01
 */
public class SetFocusTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.control.setFocus";

	@Override
	public TaskModel createTaskModel() {
		return new SetFocusTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SetFocusTaskRunner((SetFocusTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
