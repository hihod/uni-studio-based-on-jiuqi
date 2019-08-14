package com.jiuqi.etl.rpa.toolkits.welcom.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.presentations.IStackPresentationSite;

import com.jiuqi.etl.rpa.toolkits.welcom.editor.WelcomEditor;

/**
 * @author 作者：houzhiyuan 2019年6月25日 下午4:20:12
 */
public class WelcomAction extends Action implements IWorkbenchAction{
	public WelcomAction(IWorkbenchWindow workbenchWindow) {
		setId("com.jiuqi.rpa.toolkits.welcom");
		setText("欢迎页面");
	}

	public void run() {
		// 创建欢迎界面,缺点每次启动都会打开欢迎界面
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IEditorInput editor = new WelcomEditor();
		IEditorPart find = page.findEditor(editor);
		if(find == null) {
			try {
				page.openEditor(editor, WelcomEditor.VIEW_ID, true);
				// 设置最大化
				IWorkbenchPartReference ref = page.getActivePartReference();
				page.setPartState(ref, IStackPresentationSite.STATE_MAXIMIZED);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		else {
			page.bringToTop(find);
		}
		

	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
