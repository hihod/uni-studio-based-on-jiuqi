/*
 * ��������������޹�˾ ��Ȩ����(c) 2009
 * �ļ������ࣺSwitchWorkspaceAction
 * ���ߣ�	���첨 	����о�Ժ�����
 * �޸ļ�¼��
 * 2009-07-28 	���첨 	�����ļ�
 */
package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.jiuqi.etl.app.dialog.PickWorkspaceDialog;

/**
 * �������л������������� <p>
 * �����������л�����Ĺ����ռ䡣
 */
public class SwitchWorkspaceAction extends Action implements IWorkbenchAction
{
	public static final String ID = "com.jiuqi.etl.switchworkspace";
	private IWorkbenchWindow window;

	public SwitchWorkspaceAction(IWorkbenchWindow workbenchWindow) {
		this.window = workbenchWindow;
		
		setId(ID);
		setText("�л��洢Ŀ¼");
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
