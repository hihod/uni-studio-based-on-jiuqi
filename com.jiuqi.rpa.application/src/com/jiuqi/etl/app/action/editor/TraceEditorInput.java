/*
 * 2010-9-15
 *
 * 王星宇
 */
package com.jiuqi.etl.app.action.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.jiuqi.bi.dblib.DBTrace;

/**
 * @author 王星宇
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
		return "连接跟踪 - " + (DBTrace.inTracing() ? "正在跟踪" : "未跟踪");
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
