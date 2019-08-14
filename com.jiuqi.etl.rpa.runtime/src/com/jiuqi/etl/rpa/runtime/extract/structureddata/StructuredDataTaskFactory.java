package com.jiuqi.etl.rpa.runtime.extract.structureddata;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 结构化数据提取
 * 
 * @author liangxiao01
 */
public class StructuredDataTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.extrect.structuredData";

	@Override
	public TaskModel createTaskModel() {
		return new StructuredDataTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new StructuredDataTaskRunner((StructuredDataTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
