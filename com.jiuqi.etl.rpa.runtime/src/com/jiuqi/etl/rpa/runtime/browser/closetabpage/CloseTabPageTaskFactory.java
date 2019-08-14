package com.jiuqi.etl.rpa.runtime.browser.closetabpage;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 关闭浏览器页签任务模型工厂
 * 
 * @author wangshanyu
 */
public class CloseTabPageTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.browser.closePage";

	@Override
	public TaskModel createTaskModel() {
		return new CloseTabPageTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new CloseTabPageTaskRunner((CloseTabPageTaskModel)model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
