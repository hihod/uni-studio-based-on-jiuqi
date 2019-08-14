package com.jiuqi.rpa.widgets.extract;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import com.jiuqi.etl.ui.editor.ETLEditor;

public class CellModifier implements ICellModifier {
	private TableViewer tv;
	private List<ColumnEntity> allParams = new ArrayList<ColumnEntity>();

	public CellModifier(TableViewer tv) {
		this.tv = tv;

	}

	@Override
	public boolean canModify(Object obj, String s) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		ColumnEntity o = (ColumnEntity) element;

		if (property.equals("name")) {
			return o.getName();
		}

		else if (property.equals("title")) {
			return o.getTitle();
		}

		else {
			return 0;
		}
	}

	private boolean checkDuplicate(List<ColumnEntity> params, String name) {
		for (ColumnEntity param : params) {
			if (param.getName().equalsIgnoreCase(name)) {
				return false;
			}
		}
		return true;
	}

	private boolean checkValidate(String name) {
		if (name == null || name.length() == 0 || !name.matches("[a-zA-Z_@][a-zA-Z0-9_@]*")) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void modify(Object element, String property, Object value) {

		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		ETLEditor etlEditor = (ETLEditor) editorPart;
		
		allParams = ((TableSelectorWizardDialog) (this.tv.getTable().getParent().getShell().getData())).getColumnEntityList();

		etlEditor.getControlFlowEditor().setDirty(true);
		etlEditor.getControlFlowEditor().firePropertyChange(IEditorPart.PROP_DIRTY);

		TableItem item = (TableItem) element;
		ColumnEntity o = (ColumnEntity) item.getData();

		if (property.equals("name")) {
			String newName = (String) value;
			if (checkDuplicate(allParams, newName) && checkValidate(newName)) {
				o.setName(newName);
			}
		}

		else if (property.equals("title")) {
			String newTitle = (String) value;
			o.setTitle(newTitle);
		}
		
		else {
			throw new RuntimeException("错误的列别名:" + property);
		}
		tv.refresh();

	}
}
