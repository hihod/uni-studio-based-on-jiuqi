package com.jiuqi.etl.rpa.toolkits.template.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.jiuqi.etl.rpa.toolkits.template.dialog.TemplateDialog;

/**
* @author 作者：houzhiyuan 2019年7月1日 上午10:45:06
*/
public class TemplateAction  extends Action implements IWorkbenchAction{
	public static final String ID = "com.jiuqi.rpa.template";
	private IWorkbenchWindow window = null;
	public TemplateAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("通用流程管理");
		
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
