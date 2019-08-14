package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.jiuqi.etl.app.dialog.AboutDialog;

public class AboutAction extends Action implements IWorkbenchAction {
	public static final String ID = "com.jiuqi.etl.about";
	private IWorkbenchWindow window;

	public AboutAction(IWorkbenchWindow workbenchWindow) {
		this.window = workbenchWindow;
		
		setId(ID);
		setText("¹ØÓÚ Uni Studio");
	}
	
	@Override
	public void run() {
		AboutDialog  aboutDialog = new AboutDialog(window.getShell());
		aboutDialog.open();
		super.run();
	}
	
	public void dispose() {

	}

}
