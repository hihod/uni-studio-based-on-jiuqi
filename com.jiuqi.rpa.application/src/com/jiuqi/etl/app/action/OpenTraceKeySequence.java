/*
 * 2010-9-16
 *
 * ÍõÐÇÓî
 */
package com.jiuqi.etl.app.action;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.jiuqi.etl.app.action.editor.TraceEditor;
import com.jiuqi.etl.app.action.editor.TraceEditorInput;
import com.jiuqi.widgets.dialog.ErrorDialog;

/**
 * @author ÍõÐÇÓî
 *
 */
public class OpenTraceKeySequence implements KeySequenceExecutor {

	public void dispose() {
	}

	public String getActivationString() {
		return "letmetrace";
	}

	public void run(IWorkbenchWindow window) {
		try {
			window.getActivePage().openEditor(TraceEditorInput.getInstance(), TraceEditor.ID);
		} catch (PartInitException e) {
			ErrorDialog.openError(window.getShell(), "´íÎó", "´ò¿ªÁ¬½Ó¸ú×Ù±à¼­Æ÷´íÎó£º" + e.getMessage(), e);
		}
	}

}
