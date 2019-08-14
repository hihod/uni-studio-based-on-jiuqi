package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class UITreeRefreshNodeListener extends SelectionAdapter {
	private PickerShell pickerShell;
	
	public UITreeRefreshNodeListener(PickerShell pickerShell) {
		this.pickerShell = pickerShell;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		TreeSelection selection = (TreeSelection) pickerShell.getVistalTreeViewer().getSelection();
		if (selection == null || selection.isEmpty())
			return;
		Object sel  = ((IStructuredSelection) selection).getFirstElement();
		if(sel instanceof UINodeObject){
			((UINodeObject)sel).setChildern(null);
		}
		pickerShell.getVistalTreeViewer().refresh(selection.getFirstElement());
	}
	
}
