package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.jiuqi.etl.app.ApplicationPlugin;
import com.jiuqi.etl.engine.ETLEngineManager;
import com.jiuqi.etl.engine.IExecutor;
import com.jiuqi.etl.model.DataflowModel;
import com.jiuqi.etl.ui.editor.DataFlowEditor;
import com.jiuqi.etl.ui.editor.ETLEditor;
import com.jiuqi.etl.ui.editor.IPageChangeListener;
import com.jiuqi.etl.ui.editor.model.DataFlowDiagramModel;
import com.jiuqi.etl.ui.view.console.ETLConsoleFactory;
import com.jiuqi.widgets.dialog.ErrorDialog;


public class RunDataFlowAction extends Action implements IWorkbenchAction
{
	public static final String ID = "com.jiuqi.etl.runDF";
	private static final String IMG_RUN_DF = "icons/action/run_df.gif";
	private static final String IMG_RUN_DF_DISABLE = "icons/action/run_df_disable.gif";
	
	private IWorkbenchWindow window;
	private IPageChangeListener pageChangeListener;
	private IPartListener partListener;
	
	public RunDataFlowAction(IWorkbenchWindow workbenchWindow)
	{
		this.window = workbenchWindow;
		setId("runDF");
		setActionDefinitionId(ID);
		setText("运行数据流(&D)@F6");
		setToolTipText("执行编辑器中的数据流");
		setImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG_RUN_DF));
		setDisabledImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG_RUN_DF_DISABLE));
		setEnabled(false);
		pageChangeListener = new IPageChangeListener() {
			public void pageChange(MultiPageEditorPart multipageEditor, IEditorPart activeEditor)
			{
				if (activeEditor instanceof DataFlowEditor)
					setEnabled(true);
				else
					setEnabled(false);
			}
		};
		partListener = new IPartListener() {
			public void partActivated(IWorkbenchPart part)
			{
				if (part instanceof ETLEditor)
				{
					ETLEditor editor = (ETLEditor) part;
					editor.addPageChangeListener(pageChangeListener);
					if (editor.getActiveEditor() instanceof DataFlowEditor)
						setEnabled(true);
					else
						setEnabled(false);
				} else
					setEnabled(false);
			}
			public void partBroughtToTop(IWorkbenchPart part){}
			public void partClosed(IWorkbenchPart part){
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
			public void partDeactivated(IWorkbenchPart part)
			{
				if (part instanceof ETLEditor)
					((ETLEditor) part).removePageChangeListener(pageChangeListener);
			}
			public void partOpened(IWorkbenchPart part){}
		};
		window.getPartService().addPartListener(partListener);
	}
	
	@Override
	public void run()
	{
		try
		{
			IEditorPart activeEditor = window.getActivePage().getActiveEditor();
			if (activeEditor instanceof ETLEditor)
			{
				ETLEditor etlEditor = (ETLEditor) activeEditor;
				IEditorPart inneractiveEditor = etlEditor.getActiveEditor();
				if (inneractiveEditor  instanceof DataFlowEditor)
				{
					DataFlowEditor editor = (DataFlowEditor) inneractiveEditor ;
					DataFlowDiagramModel model = (DataFlowDiagramModel) editor.getGraphicalViewer()
					        .getContents().getModel();
					DataflowModel flow = (DataflowModel) model.getGraph();
					IExecutor exe = ETLEngineManager.getExecutor(flow.clone());
					if (!ETLConsoleFactory.isRuning())
					{
						ETLConsoleFactory.start(exe);
					}
				}
			}
		}
		catch (Exception e)
		{
			ErrorDialog.openWarning(window.getShell(), null, null, e);
		}
	}
	
	public void dispose()
	{
		window.getPartService().removePartListener(partListener);
	}
}
