package com.jiuqi.rpa.widgets.uipicker;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.jiuqi.rpa.lib.find.PathAttribute;

public class UISelectorAttributeTableContentProvider implements IStructuredContentProvider {

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		return ((List<PathAttribute>) inputElement).toArray();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

}
