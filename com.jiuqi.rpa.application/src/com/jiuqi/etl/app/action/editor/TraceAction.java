/*
 * 2010-9-20
 *
 * ÍõÐÇÓî
 */
package com.jiuqi.etl.app.action.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.jiuqi.bi.dblib.DBTrace;
import com.jiuqi.etl.app.ApplicationPlugin;

/**
 * @author ÍõÐÇÓî
 *
 */
public class TraceAction extends Action {

	public static final String ID = "com.jiuqi.etl.app.action.trace";
	private IWorkbenchWindow window;
	private RefreshAction refreshAction;
	private AutoRefreshAction autoRefreshAction;
	
	private boolean selection;
	
	public TraceAction(IWorkbenchWindow window, RefreshAction refreshAction, AutoRefreshAction autoRefreshAction) {
		this.window = window;
		this.refreshAction = refreshAction;
		this.autoRefreshAction = autoRefreshAction;
		setId(ID);
		setImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin("icons/action/trace.gif"));
		setSelection(DBTrace.inTracing());
		String text = "Á¬½Ó¸ú×Ù - " + (DBTrace.inTracing() ? "ÕýÔÚ¸ú×Ù" : "Î´¸ú×Ù");
		setText(text);
	}
	
	@Override
	public void run() {
		IEditorPart editor = window.getActivePage().getActiveEditor();
		if (editor instanceof TraceEditor) {
			TraceEditor traceEditor = (TraceEditor) editor;
			setSelection(!this.selection);
			traceEditor.refresh();
			String text = "Á¬½Ó¸ú×Ù - " + (DBTrace.inTracing() ? "ÕýÔÚ¸ú×Ù" : "Î´¸ú×Ù");
			setText(text);
			traceEditor.setPartName(text);
			traceEditor.setTitleToolTip(text);
		}
	}
	
	public void setSelection(boolean selection) {
		this.selection = selection;
		setChecked(selection);
		DBTrace.setTrace(this.selection);
		refreshAction.setEnabled(this.selection);
		autoRefreshAction.setEnable(this.selection);
	}
	
	@Override
	public int getStyle() {
		return AS_CHECK_BOX;
	}
	
}
