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
		setText("运行控制流(&C)@F4");
		setToolTipText("在本地运行控制流");
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
					//如果是ETL编辑器的子编辑也允许运行控制流
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
			//如果是ETL编辑器的子编辑也允许运行控制流
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
					boolean c = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "控制流执行提示", "子编辑器未保存，是否保存并执行？");
					if(c){
						childeEditor.doSave(null);
					}else{
						return;
					}
//					ErrorDialog.openWarning(null, "控制流执行提示", "将执行的控制流的子编辑器未保存，执行的控制流可能与编辑的不一致！" ,null);
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
