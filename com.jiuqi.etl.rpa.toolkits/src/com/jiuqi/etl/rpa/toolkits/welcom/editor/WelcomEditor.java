package com.jiuqi.etl.rpa.toolkits.welcom.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
* @author 作者：houzhiyuan 2019年6月13日 下午4:33:27
*/
public class WelcomEditor extends EditorPart implements IEditorInput {
	public static final String VIEW_ID = "com.jiuqi.etl.ui.rpa.welcomeditor";
	private WelcomPartControl partControl;
	public void doSave(IProgressMonitor monitor) {
		
	}

	public void doSaveAs() {
		
	}

	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());

	}

	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void createPartControl(Composite parent) {
		partControl = new WelcomPartControl(parent);
		try {
			partControl.create();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				partControl.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void setFocus() {
		
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		
		return null;
	}

	public boolean exists() {
		
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		
		return null;
	}

	public String getName() {
		return "欢迎页面";
	}

	public IPersistableElement getPersistable() {
		
		return null;
	}

	public String getToolTipText() {
		return "欢迎页面";
	}

	public void dispose() {
		super.dispose();
		try {
			if(partControl != null) {
				partControl.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof WelcomEditor)) {
			return false;
		}
		WelcomEditor t = (WelcomEditor)obj;
		if(!t.getName().equals(getName())) {
			return false;
		}
		return true;
	}
}
