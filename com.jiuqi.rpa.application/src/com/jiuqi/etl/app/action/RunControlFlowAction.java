package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.jiuqi.etl.app.ApplicationPlugin;
import com.jiuqi.etl.engine.ETLEngineManager;
import com.jiuqi.etl.engine.IExecutor;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.ui.editor.ETLEditor;
import com.jiuqi.etl.ui.editor.ext.childEditor.IChildEditor;
import com.jiuqi.etl.ui.editor.ext.childEditor.IParentEditor;
import com.jiuqi.etl.ui.editor.ETLInput;
import com.jiuqi.etl.ui.view.console.ETLConsoleFactory;
import com.jiuqi.widgets.dialog.ErrorDialog;

public class RunControlFlowAction extends Action implements IWorkbenchAction
{
	private static final String ID = "com.jiuqi.etl.runCF";
	
	private static final String IMG 			= "icons/action/run_cf.gif";
	private static final String IMG_DISABLE 	= "icons/action/run_cf_disable.gif";
	
	private IWorkbenchWindow window;
	private IPartListener enableListener;
	
	public RunControlFlowAction(IWorkbenchWindow workbenchWindow)
	{
		this.window = workbenchWindow;
		setId("runCF");
		setActionDefinitionId(ID);
		setText("���п�����(&C)@F4");
		setToolTipText("�ڱ������п�����");
		setImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG));
		setDisabledImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG_DISABLE));
		setEnabled(false);
		
		enableListener = new IPartListener(){
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
					if (activeEditor instanceof ETLEditor){
						setEnabled(true);
						return;
					}
					//�����ETL�༭�����ӱ༭Ҳ�������п�����
					if (activeEditor instanceof IChildEditor){
						IChildEditor childEditor = (IChildEditor)activeEditor;
						if(childEditor.getParentEditor() instanceof ETLEditor){
							setEnabled(true);
							return;
						}
					}
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
			ETLEditor etlEditor = null;
			if (activeEditor instanceof ETLEditor){
				etlEditor = (ETLEditor) activeEditor;
			}
			//�����ETL�༭�����ӱ༭Ҳ�������п�����
			else if (activeEditor instanceof IChildEditor){
				IChildEditor childEditor = (IChildEditor)activeEditor;
				IParentEditor parentEditor = childEditor.getParentEditor();
				if(parentEditor instanceof ETLEditor){
					etlEditor = (ETLEditor) parentEditor;
				}
			}
			if(etlEditor == null){
				return;
			}
			for(IChildEditor childeEditor : etlEditor.getChildEditors()){
				if(childeEditor.isDirty()){
					boolean c = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "������ִ����ʾ", "�ӱ༭��δ���棬�Ƿ񱣴沢ִ�У�");
					if(c){
						childeEditor.doSave(null);
					}else{
						return;
					}
//					ErrorDialog.openWarning(null, "������ִ����ʾ", "��ִ�еĿ��������ӱ༭��δ���棬ִ�еĿ�����������༭�Ĳ�һ�£�" ,null);
//					break;
				}
			}
			ETLInput input = (ETLInput) etlEditor.getEditorInput();
			ControlFlowModel model = (ControlFlowModel) input.getControlFlowDiagramModel().getGraph();
			IExecutor exe = ETLEngineManager.getExecutor(model.clone());
			if (!ETLConsoleFactory.isRuning()){
				ETLConsoleFactory.start(exe);
			}
		}
		catch (Exception e)
		{
			ErrorDialog.openWarning(window.getShell(), null, null, e);
		}
	}

	public void dispose()
    {
		window.getPartService().removePartListener(enableListener);
    }
}
