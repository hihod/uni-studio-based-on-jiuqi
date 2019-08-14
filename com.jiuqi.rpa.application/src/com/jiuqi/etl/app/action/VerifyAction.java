/*
 * ��������������޹�˾ ��Ȩ����(c) 2009
 * �ļ������ࣺVeriyControlFlowAction
 * ���ߣ�	������ 	����о�Ժ�����
 * �޸ļ�¼��
 * 2009-5-8 	������ 	�����ļ�
 */
package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.jiuqi.etl.app.ApplicationPlugin;
import com.jiuqi.etl.ui.editor.ETLEditor;
import com.jiuqi.etl.ui.editor.ETLInput;
import com.jiuqi.etl.ui.editor.model.ControlFlowDiagramModel;
import com.jiuqi.etl.ui.view.problem.ProblemView;
import com.jiuqi.etl.ui.view.server.ServerView;
import com.jiuqi.widgets.dialog.ErrorDialog;

public class VerifyAction extends Action implements IWorkbenchAction
{
	private static final String ID = "com.jiuqi.etl.verify";
	
	private static final String IMG 		= "icons/action/verify.gif";
	private static final String IMG_DISABLE = "icons/action/verify_disable.gif";
	
	private IWorkbenchWindow window;
	private IPartListener enableListener;
	
	public VerifyAction(IWorkbenchWindow workbenchWindow)
    {
		this.window = workbenchWindow; 
		setId("verify");
		setActionDefinitionId(ID);
		setText("��֤(&V)@F7");
		setToolTipText("�Կ��������д�����֤");
		setImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG));
		setDisabledImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG_DISABLE));
		setEnabled(false);
		
		enableListener = new IPartListener() {
			public void partActivated(IWorkbenchPart part){
				refreshEnable();
			}
			public void partClosed(IWorkbenchPart part){
				refreshEnable();
			}
			public void partOpened(IWorkbenchPart part){}
			public void partBroughtToTop(IWorkbenchPart part){}
			public void partDeactivated(IWorkbenchPart part){}
			
			private void refreshEnable()
			{
				IWorkbenchPage activePage = window.getActivePage();
				if (activePage != null)
				{
					IEditorPart activeEditor = activePage.getActiveEditor();
					if (activeEditor instanceof ETLEditor)
						setEnabled(true);
					else
						setEnabled(false);
				}
			}
        };
		window.getPartService().addPartListener(enableListener);
    }
	
	@Override
	public void run()
	{
		try
        {
			IEditorPart activeEditor = window.getActivePage().getActiveEditor();
			if (activeEditor instanceof ETLEditor)
			{
				ETLEditor editor = (ETLEditor) activeEditor;
			    ETLInput input = (ETLInput) editor.getEditorInput();
			    ControlFlowDiagramModel diagModel = input.getControlFlowDiagramModel();
			    diagModel.verify();
			    input.getControlFlowWrapper().setProblemLevel(diagModel.getProblemLevel());
			    //ˢ����ͼ
		    	ProblemView problemView = (ProblemView) window.getActivePage().showView(ProblemView.VIEW_ID);
		        problemView.refresh();
		        ServerView serverView = (ServerView) window.getActivePage().findView(ServerView.VIEW_ID);
		        if (serverView != null)
		        	serverView.getTreeViewer().update(input.getControlFlowWrapper(), null);
		        editor.refreshCanvas();
		        editor.refreshOutlinePage();
			}
        }
        catch (Exception e)
        {
	        ErrorDialog.openError(window.getShell(), null, null, e);
        }
	}

	public void dispose()
    {
		window.getPartService().removePartListener(enableListener);
    }
}
