package com.jiuqi.etl.rpa.ui.control.settext;

import java.util.List;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jiuqi.etl.ui.editor.ext.ITaskEditProvider;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

public class SetTextTaskEditProvider implements ITaskEditProvider {

	public IWizard getEditWizard(TaskContext context) {
		return null;
	}

	public List<IWorkbenchAction> getContextActionList(IWorkbenchWindow window, TaskContext context) {
		return null;
	}

	public IPropertySource getPropertySource(TaskContext context) {
		return (IPropertySource) new SetTextProperty(context);
	}

}
