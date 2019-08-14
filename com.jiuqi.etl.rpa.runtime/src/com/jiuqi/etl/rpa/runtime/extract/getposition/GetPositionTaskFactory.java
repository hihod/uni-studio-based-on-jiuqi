package com.jiuqi.etl.rpa.runtime.extract.getposition;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 获取位置模型工厂
 * 
 * @author liangxiao01
 */
public class GetPositionTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.extrect.getPosition";

	@Override
	public TaskModel createTaskModel() {
		return new GetPositionTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new GetPositionTaskRunner((GetPositionTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
