package com.jiuqi.etl.rpa.runtime.keyboard.sendhotkey;


import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 文本录入任务模型工厂
 * 
 * @author liangxiao01
 */
public class SendHotKeyTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.keyboard.sendHotkey";

	@Override
	public TaskModel createTaskModel() {
		return new SendHotKeyTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new SendHotKeyTaskRunner((SendHotKeyTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
