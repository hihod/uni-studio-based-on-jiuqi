package com.jiuqi.etl.rpa.ui.extract.structureddata;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.etl.widgets.descriptor.PopupPropertyDescriptor;

public class StrcuturedMetaDataPopupPropertyDescriptor extends PopupPropertyDescriptor {
	private TaskContext context;
	public StrcuturedMetaDataPopupPropertyDescriptor(Object id, String displayName, TaskContext context) {
		super(id, displayName);
		this.context = context;
	}

	@Override
	protected DialogCellEditor createCellEditor(Composite parent) {
		return new MetaDataDialogEditor(parent,context);
	}
	public TaskContext getContext(){
		return this.context;
	}	
}
