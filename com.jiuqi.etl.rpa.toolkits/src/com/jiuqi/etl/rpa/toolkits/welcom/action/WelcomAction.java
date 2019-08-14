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
 * @author ���ߣ�houzhiyuan 2019��6��25�� ����4:20:12
 */
public class WelcomAction extends Action implements IWorkbenchAction{
	public WelcomAction(IWorkbenchWindow workbenchWindow) {
		setId("com.jiuqi.rpa.toolkits.welcom");
		setText("��ӭҳ��");
	}

	public void run() {
		// ������ӭ����,ȱ��ÿ����������򿪻�ӭ����
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IEditorInput editor = new WelcomEditor();
		IEditorPart find = page.findEditor(editor);
		if(find == null) {
			try {
				page.openEditor(editor, WelcomEditor.VIEW_ID, true);
				// �������
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
