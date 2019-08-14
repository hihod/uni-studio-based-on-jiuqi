package com.jiuqi.etl.rpa.ui.image.ocrinvoice;

import java.util.List;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jiuqi.etl.ui.editor.ext.ITaskEditProvider;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

/**
 * 
 * @author lpy
 *
 */
public class OcrInvoiceTaskEditProvider implements ITaskEditProvider {

	@Override
	public IWizard getEditWizard(TaskContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IWorkbenchAction> getContextActionList(IWorkbenchWindow window, TaskContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertySource getPropertySource(TaskContext context) {
		return (IPropertySource) new OcrInvoiceProperty(context);
	}

}
