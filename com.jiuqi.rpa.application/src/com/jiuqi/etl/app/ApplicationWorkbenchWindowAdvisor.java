package com.jiuqi.etl.app;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.presentations.IStackPresentationSite;

import com.jiuqi.etl.rpa.toolkits.welcom.editor.WelcomEditor;
import com.jiuqi.etl.rpa.toolkits.welcom.manager.WelcomContentManager;
import com.jiuqi.etl.storage.StorageProviderManager;
import com.jiuqi.etl.ui.view.ETLViewsPlugin;
import com.jiuqi.etl.ui.view.ext.SchemeFolderManager;


public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor
{
	
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer)
	{
		super(configurer);
	}
	
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer)
	{
		return new ApplicationActionBarAdvisor(configurer);
	}
	
	public void preWindowOpen()
	{
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1024, 768));
		configurer.setShowStatusLine(true);
		PlatformUI.getPreferenceStore().setDefault(
		        IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);		
	}
	
	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		
		String statusLine = StorageProviderManager.getInstance().getURLInfo();
		getWindowConfigurer().getActionBarConfigurer().getStatusLineManager().setMessage(statusLine);
		
		//删除服务器视图“资源锁”、“脚本”节点
		SchemeFolderManager manager = ETLViewsPlugin.getDefault().getSchemeFolderManager();
		manager.removeSchemeFolder("com.jiuqi.etl.ext.lockFolder");
		manager.removeSchemeFolder("com.jiuqi.etl.ext.scriptFolder");
		
		boolean openEnable = WelcomContentManager.getInstance().getOpenEnable();
		if(openEnable) {
			//创建欢迎界面,缺点每次启动都会打开欢迎界面
			  IWorkbenchPage page = PlatformUI.getWorkbench()
				      .getActiveWorkbenchWindow().getActivePage();
			  
			  IEditorInput editor = new WelcomEditor();
			  try {
				page.openEditor(editor, WelcomEditor.VIEW_ID, true);
				//设置最大化
				IWorkbenchPartReference ref = page.getActivePartReference();
				page.setPartState(ref, IStackPresentationSite.STATE_MAXIMIZED);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}
}
