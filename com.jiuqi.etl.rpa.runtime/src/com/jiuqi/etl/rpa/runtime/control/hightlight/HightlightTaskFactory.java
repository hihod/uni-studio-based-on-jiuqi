package com.jiuqi.etl.rpa.runtime.control.hightlight;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 获取文本
 * 
 * @author liangxiao01
 */
public class HightlightTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.control.hightlight";

	@Override
	public TaskModel createTaskModel() {
		return new HightlightTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new HightlightTaskRunner((HightlightTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
