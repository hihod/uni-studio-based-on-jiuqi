package com.jiuqi.etl.app;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import com.jiuqi.etl.app.action.AboutAction;
import com.jiuqi.etl.app.action.KeySequenceAction;
import com.jiuqi.etl.app.action.NewAction;
import com.jiuqi.etl.app.action.OpenTraceKeySequence;
import com.jiuqi.etl.app.action.RestartAction;
import com.jiuqi.etl.app.action.RunControlFlowAction;
import com.jiuqi.etl.app.action.RunDataFlowAction;
import com.jiuqi.etl.app.action.ShowSplashKeySequence;
import com.jiuqi.etl.app.action.SwitchWorkspaceAction;
import com.jiuqi.etl.app.action.VerifyAction;
import com.jiuqi.etl.storage.StorageProviderManager;
import com.jiuqi.etl.ui.editor.ETLEditor;
import com.jiuqi.etl.ui.editor.ETLEditorPlugin;
import com.jiuqi.etl.ui.editor.action.SelectLineAction;
import com.jiuqi.etl.ui.view.server.ImportControlFlowAction;
import com.jiuqi.etl.ui.view.server.MaintainAction;
import com.jiuqi.etl.ui.view.server.PublishAction;
import com.jiuqi.etl.rpa.toolkits.welcom.action.WelcomAction;
import com.jiuqi.etl.rpa.toolkits.template.action.TemplateAction;


public class ApplicationActionBarAdvisor extends ActionBarAdvisor
{
	private static final String IMG_MAINTAIN = "icons/action/maintain.gif";
	private static final String IMG_MAINTAIN_DISABLE = "icons/action/maintain_disable.gif";
	private static final String IMG_PUBLISH = "icons/action/publish.gif";
	private static final String IMG_PUBLISH_DISABLE = "icons/action/publish_disable.gif";
	
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer)
    {
	    super(configurer);
    }

	//文件菜单
	private IAction newAction;
	private IAction closeAction;
	private IAction closeAllAction;
	private IAction saveAction;
	private IAction saveAllAction;
	private IAction saveAsAction;
	private IAction importControlFlowAction;
//	private IAction propertyAction;
//	private IAction refreshAction;
	private IAction switchWorkspaceAction;
	private IAction restartAction;
	private IAction exitAction;
	//编辑菜单
	private IAction cutAction;
	private IAction copyAction;
	private IAction pasteAction;
	private IAction deleteAction;
	private IAction renameAction;
	private IAction selectAllAction;
	//控制流菜单
	private IAction maintainAction;
	private IAction publishAction;
	private IAction selectLineAction;
	private IAction verifyAction;
	private IAction runCFAction;
	private IAction runDFAction;
	//窗口菜单
	private IAction resetPerspectiveAction;
	private IAction preferencesAction;
	//帮助菜单
//	private IAction helpContentsAction;
	private IAction aboutAction;
	private KeySequenceAction keySequenceAction;
	//前进、后退
	private IAction backAction;
	private IAction forwardAction;
	
	//欢迎
	private IAction welcomAction;
	private IAction templateAction;
	
	protected void makeActions(IWorkbenchWindow window)
	{
		//文件
		newAction = new NewAction(window);
		register(newAction);
		closeAction = ActionFactory.CLOSE.create(window);
		register(closeAction);
		closeAllAction = ActionFactory.CLOSE_ALL.create(window);
		register(closeAllAction);
		saveAction 	= ActionFactory.SAVE.create(window);
		register(saveAction);
		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		register(saveAllAction);
		saveAsAction = ActionFactory.SAVE_AS.create(window);
		saveAsAction.setText("导出(&E)");
		register(saveAsAction);
		importControlFlowAction = new ImportControlFlowAction(window);
		register(importControlFlowAction);
		
		switchWorkspaceAction = new SwitchWorkspaceAction(window);
		register(switchWorkspaceAction);
		restartAction = new RestartAction(window);
		register(restartAction);
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);
		//编辑
		cutAction = ActionFactory.CUT.create(window);
		register(cutAction);
		copyAction = ActionFactory.COPY.create(window);
		register(copyAction);
		pasteAction = ActionFactory.PASTE.create(window);
		register(pasteAction);
		deleteAction = ActionFactory.DELETE.create(window);
		deleteAction.setText("删除(&D)@Delete");		
		register(deleteAction);
		renameAction = ActionFactory.RENAME.create(window);		
		register(renameAction);
		selectAllAction = ActionFactory.SELECT_ALL.create(window);
		register(selectAllAction);
		
		//控制流
		verifyAction = new VerifyAction(window);
		register(verifyAction);
		runCFAction = new RunControlFlowAction(window);
		register(runCFAction);
		runDFAction = new RunDataFlowAction(window);
		register(runDFAction);
		maintainAction = new RetargetAction(MaintainAction.ID, "维护(&M)@F8");
		maintainAction.setActionDefinitionId("com.jiuqi.etl.maintain");
		maintainAction.setToolTipText("维护");
		maintainAction.setImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG_MAINTAIN));
		maintainAction.setDisabledImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG_MAINTAIN_DISABLE));
		window.getPartService().addPartListener((RetargetAction)maintainAction);
		register(maintainAction);
		publishAction = new RetargetAction(PublishAction.ID, "发布(&P)@F9");
		publishAction.setActionDefinitionId("com.jiuqi.etl.publish");
		publishAction.setToolTipText("发布");
		publishAction.setImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG_PUBLISH));
		publishAction.setDisabledImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG_PUBLISH_DISABLE));
		window.getPartService().addPartListener((RetargetAction)publishAction);
		register(publishAction);
		selectLineAction = new SelectLineAction(window);
		register(selectLineAction);
		
		//窗口
		resetPerspectiveAction = ActionFactory.RESET_PERSPECTIVE.create(window);
		resetPerspectiveAction.setText("重置布局(&R)");
		register(resetPerspectiveAction);
		preferencesAction = ActionFactory.PREFERENCES.create(window);
		preferencesAction.setText("选项(&P)");
		register(preferencesAction);
		//帮助
//		helpContentsAction = ActionFactory.HELP_CONTENTS.create(window);
//		helpContentsAction.setText("帮助内容(&H)");
//		register(helpContentsAction);
//		aboutAction = ActionFactory.ABOUT.create(window);
//		aboutAction.setText("关于(&A)");
		aboutAction = new AboutAction(window);
		register(aboutAction);
		keySequenceAction = new KeySequenceAction(window);
		keySequenceAction.addKeySequenceExecutor(new OpenTraceKeySequence());
		keySequenceAction.addKeySequenceExecutor(new ShowSplashKeySequence());
		register(keySequenceAction);
		//前进、后退
		backAction = ETLEditorPlugin.getDefault().getBackAction(window);
		register(backAction);
		forwardAction = ETLEditorPlugin.getDefault().getForwardAction(window);
		register(forwardAction);
		
		welcomAction = new WelcomAction(window);
		register(welcomAction);
		
		templateAction = new TemplateAction(window);
		register(templateAction);
	}
	
	protected void fillMenuBar(IMenuManager menuBar)
	{
		MenuManager fileMenu = new MenuManager("文件(&F)", "file");
		fileMenu.add(newAction);
		fileMenu.add(new GroupMarker("newPosition"));
		fileMenu.add(new Separator());
		fileMenu.add(closeAction);
		fileMenu.add(closeAllAction);
		fileMenu.add(new Separator());
		fileMenu.add(saveAction);
		fileMenu.add(saveAllAction);
		fileMenu.add(saveAsAction);
		fileMenu.add(importControlFlowAction);
		fileMenu.addMenuListener(new IMenuListener() {			
			public void menuAboutToShow(IMenuManager manager) {
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				importControlFlowAction.setEnabled(editorPart != null && editorPart instanceof ETLEditor);
			}
		});
//		fileMenu.add(new Separator());
//		fileMenu.add(refreshAction);
//		fileMenu.add(propertyAction);
		fileMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		fileMenu.add(switchWorkspaceAction);
		fileMenu.add(restartAction);
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);
		menuBar.add(fileMenu);
		
		MenuManager editMenu = new MenuManager("编辑(&E)", "edit");
		editMenu.add(renameAction);
		editMenu.add(cutAction);
		editMenu.add(copyAction);
		editMenu.add(pasteAction);
		editMenu.add(new Separator());
		editMenu.add(deleteAction);		
		editMenu.add(selectAllAction);
		editMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(editMenu);
		
		MenuManager cfMenu = new MenuManager("控制流(&C)", "controflow");
		cfMenu.add(runCFAction);
		cfMenu.add(runDFAction);
		cfMenu.add(verifyAction);
		if (StorageProviderManager.getInstance().isServerMode())
		{
			cfMenu.add(new Separator());
			cfMenu.add(maintainAction);
			cfMenu.add(publishAction);
		}
		cfMenu.add(new Separator());
		cfMenu.add(templateAction);
		menuBar.add(cfMenu);
		
		MenuManager windowMenu = new MenuManager("窗口(&W)", "window");
		windowMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		windowMenu.add(resetPerspectiveAction);
		windowMenu.add(preferencesAction);
		menuBar.add(windowMenu);
		
		MenuManager helpMenu = new MenuManager("帮助(&H)", "help");
		helpMenu.add(welcomAction);
//		helpMenu.add(helpContentsAction);
		helpMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		helpMenu.add(aboutAction);
		
		menuBar.add(helpMenu);
	}
	
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar)
	{
	    ToolBarManager fileTools = new ToolBarManager(SWT.FLAT);
	    fileTools.add(newAction);
	    fileTools.add(saveAction);
	    fileTools.add(saveAllAction);
	    coolBar.add(fileTools);
	    
//	    ToolBarManager editTools = new ToolBarManager(SWT.FLAT);
//	    editTools.add(cutAction);
//	    editTools.add(copyAction);
//	    editTools.add(pasteAction);
//	    editTools.add(deleteAction);
////	    editTools.add(propertyAction);
//	    coolBar.add(editTools);
	    
	    ToolBarManager cfTools = new ToolBarManager(SWT.FLAT);
	    cfTools.add(runCFAction);
	    cfTools.add(runDFAction);
	    cfTools.add(new Separator());
	    cfTools.add(verifyAction);	    
	    if (StorageProviderManager.getInstance().isServerMode())
		{
	    	cfTools.add(new Separator());
		    cfTools.add(maintainAction);
		    cfTools.add(publishAction);
		}
	    coolBar.add(cfTools);
	    
	    ToolBarManager actionTools = new ToolBarManager(SWT.FLAT);
	    actionTools.add(backAction);
	    actionTools.add(forwardAction);
	    coolBar.add(actionTools);
	}
	
	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		super.fillStatusLine(statusLine);	
	}
}
