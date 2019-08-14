package com.jiuqi.etl.rpa.ui.extract.structureddata;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jiuqi.etl.rpa.runtime.extract.structureddata.StructuredDataTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;

/**
 * UIÑ¡ÔñÆ÷Ãæ°å
 * 
 * @author liangxiao01
 */
public class MappingDialogEditor extends DialogCellEditor {
	TaskContext context;
	public MappingDialogEditor(Composite parent, TaskContext context) {
		super(parent);
		this.context = context;
	}
	

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		WizardDialog dlg = new WizardDialog(cellEditorWindow.getShell(),new StructuredDataGuide(context));
		int windowReturnCode = dlg.open();
		if(windowReturnCode==Window.OK){
			return ((StructuredDataTaskModel)(context.getTaskModel())).getFields();
		}else{
			return null;
		}
		
	}

	@Override
	protected void updateContents(Object value) {
		if (value == null || "<Î´ÅäÖÃ>".equals(value) || "".equals(value)) {
			super.updateContents("<Î´ÅäÖÃ>");
		} else if ("<ÒÑÅäÖÃ>".equals(value)) {
			super.updateContents("<ÒÑÅäÖÃ>");
		} else {
			super.updateContents("<ÒÑÅäÖÃ>");
		}

	}

}