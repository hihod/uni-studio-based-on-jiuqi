package com.jiuqi.etl.rpa.runtime.mouse.dbclick;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * Êó±êË«»÷ÈÎÎñ
 * @author liangxiao01
 */
public class MouseDbclickTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.mouse.dbclick";

	@Override
	public TaskModel createTaskModel() {
		return new MouseDbclickTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new MouseDbclickTaskRunner((MouseDbclickTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
