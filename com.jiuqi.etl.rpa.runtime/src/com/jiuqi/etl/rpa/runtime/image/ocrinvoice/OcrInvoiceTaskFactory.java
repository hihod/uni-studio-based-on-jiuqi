package com.jiuqi.etl.rpa.runtime.image.ocrinvoice;

import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.task.TaskFactory;

/**
 * 
 * @author lpy
 *
 */
public class OcrInvoiceTaskFactory extends TaskFactory {

	public static final String ID = "com.jiuqi.etl.rpa.image.ocrinvoice";

	@Override
	public TaskModel createTaskModel() {
		return new OcrInvoiceTaskModel();
	}

	@Override
	public ITaskRunner createTaskRunner(ControlFlowModel ctrlflow, TaskModel model) {
		return new OcrInvoiceTaskRunner((OcrInvoiceTaskModel) model);
	}

	@Override
	public String getTaskId() {
		return ID;
	}

}
