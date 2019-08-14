package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class RestartAction extends Action
{
	IWorkbenchWindow window;
	
	public RestartAction(IWorkbenchWindow window)
	{
		this.window = window;
		setId("org.eclipse.ui.file.restart");
		setText("÷ÿ∆Ù(&R)");
	}
	
	@Override
	public void run()
	{
	    window.getWorkbench().restart();
	}
}
