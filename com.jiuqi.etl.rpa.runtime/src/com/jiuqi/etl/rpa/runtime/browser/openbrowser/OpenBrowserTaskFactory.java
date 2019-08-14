package com.jiuqi.etl.rpa.runtime.browser.openbrowser;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 打开浏览器任务模型工厂
 * 
 * @author wangshanyu
 */
public class OpenBrowserTaskFactory extends TaskFactory {
	public static final String ID = "com.jiuqi.etl.rpa.browser.open";

	@Override
	public TaskModel createTaskModel() {
		return new OpenBrowserTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new OpenBrowserTaskRunner((OpenBrowserTaskModel) model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
