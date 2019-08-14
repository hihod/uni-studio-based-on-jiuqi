package com.jiuqi.etl.rpa.toolkits.template.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.jiuqi.etl.rpa.toolkits.template.dialog.TemplateDialog;

/**
* @author ���ߣ�houzhiyuan 2019��7��1�� ����10:45:06
*/
public class TemplateAction  extends Action implements IWorkbenchAction{
	public static final String ID = "com.jiuqi.rpa.template";
	private IWorkbenchWindow window = null;
	public TemplateAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("ͨ�����̹���");
		
	}

	public void run() {
		Display display = Display.getDefault();
		TemplateDialog dialog = new TemplateDialog(display);
		dialog.open();
		//super.run();
	}
	public void dispose() {
	}

}
