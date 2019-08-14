package com.jiuqi.etl.rpa.ui.extract.structureddata;

import org.eclipse.jface.wizard.Wizard;

import com.jiuqi.etl.rpa.runtime.extract.structureddata.StructuredDataTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.widgets.extract.StructuredDataMapping;

public class StructuredDataGuide extends Wizard{
	private StructuredDataMapping mappingPage;
	private TaskContext context;
	
	public StructuredDataGuide(TaskContext context) {
		this.context = context;
		mappingPage = new StructuredDataMapping("Ó³Éä×Ö¶Î",context,null);
		this.addPage(mappingPage);
	}

	public boolean performFinish() {
		StructuredDataTaskModel cloned_model = mappingPage.getTaskModel();
		StructuredDataTaskModel origin_model = (StructuredDataTaskModel) context.getTaskModel();
		origin_model.getFields().clear();
		origin_model.getFields().addAll(cloned_model.getFields());
		origin_model.setOutputParamName(cloned_model.getOutputParamName());
		return true;
	}
}
