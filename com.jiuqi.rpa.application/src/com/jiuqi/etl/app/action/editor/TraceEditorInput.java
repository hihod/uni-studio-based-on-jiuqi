/*
 * 2010-9-15
 *
 * ������
 */
package com.jiuqi.etl.app.action.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.jiuqi.bi.dblib.DBTrace;

/**
 * @author ������
 *
 */
public class TraceEditorInput implements IEditorInput {
	
	private static TraceEditorInput instance = new TraceEditorInput();
	
	private TraceEditorInput() {};
	
	public static TraceEditorInput getInstance() {
		return instance;
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return "���Ӹ��� - " + (DBTrace.inTracing() ? "���ڸ���" : "δ����");
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return getName();
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

}
