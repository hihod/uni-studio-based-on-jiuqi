/*
 * 2010-9-20
 *
 * Õı–«”Ó
 */
package com.jiuqi.etl.app.action.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.jiuqi.etl.app.ApplicationPlugin;

/**
 * @author Õı–«”Ó
 *
 */
public class RefreshAction extends Action {

	public static final String ID = "com.jiuqi.etl.app.action.refresh";
	
	private IWorkbenchWindow window;
	
	public RefreshAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("À¢–¬");
		setImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin("icons/action/refresh.gif"));
	}
	
	@Override
	public void run() {
		IEditorPart editor = window.getActivePage().getActiveEditor();
		if (editor instanceof TraceEditor) {
			TraceEditor traceEditor = (TraceEditor) editor;
			traceEditor.refresh();
		}
	}
	
}
