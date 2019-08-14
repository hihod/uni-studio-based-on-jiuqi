package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.Path;

public class UITreeSetAsTargetListener extends SelectionAdapter {
	private PickerShell pickerShell;
	
	public UITreeSetAsTargetListener(PickerShell pickerShell) {
		this.pickerShell = pickerShell;
	}
	
	@Override
	public void widgetSelected(SelectionEvent event) {
		ISelection selection = pickerShell.getVistalTreeViewer().getSelection();
		if (selection.isEmpty())
			return;
		
		TreeSelection treeSelection = (TreeSelection) selection;
		Object object = treeSelection.getFirstElement();
		if (!(object instanceof UINodeObject))
			return;
		
		IUIElement uiElement = ((UINodeObject) object).getUiElement();
		if (uiElement == null)
			return;
		
		try {
			Path path = uiElement.getPath();
			if (path != null)
				pickerShell.loadPath(path);
		} catch (LibraryException e) {
			e.printStackTrace();
		}
	}
	
}
