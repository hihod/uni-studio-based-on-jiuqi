/*
 * 北京久其软件有限公司 版权所有(c) 2009
 * 文件所含类：SwitchWorkspaceAction
 * 作者：	周徐波 	软件研究院组件部
 * 修改记录：
 * 2009-07-28 	周徐波 	创建文件
 */
package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.jiuqi.etl.app.dialog.PickWorkspaceDialog;

/**
 * 类名：切换工作区动作类 <p>
 * 描述：用于切换程序的工作空间。
 */
public class SwitchWorkspaceAction extends Action implements IWorkbenchAction
{
	public static final String ID = "com.jiuqi.etl.switchworkspace";
	private IWorkbenchWindow window;

	public SwitchWorkspaceAction(IWorkbenchWindow workbenchWindow) {
		this.window = workbenchWindow;
		
		setId(ID);
		setText("切换存储目录");
	}

	@Override
	public void run() {
		PickWorkspaceDialog pickWorkspaceDialog = new PickWorkspaceDialog(window.getShell());
		if (pickWorkspaceDialog.open() == Window.OK)
			new RestartAction(window).run();				
		super.run();
	}

	public void dispose() {		
	}
}
