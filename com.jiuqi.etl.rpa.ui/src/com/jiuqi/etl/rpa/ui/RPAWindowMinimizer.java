package com.jiuqi.etl.rpa.ui;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.jiuqi.etl.rpa.execute.IWindowMinimizer;

/**
 * RPA窗体最小化器
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class RPAWindowMinimizer implements IWindowMinimizer {
	private Shell mainShell;

	public void setMinimize(boolean minimize) {
		if (minimize) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null)
				mainShell = window.getShell();
		}
		
		if (mainShell == null)
			return;
		
		mainShell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				mainShell.setMinimized(minimize);
			}
		});
	}

}
