package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.jiuqi.rpa.lib.find.PathAttribute;
import com.jiuqi.rpa.widgets.Activator;

public class UISelectorAttributeTableLabelProvider implements ITableLabelProvider {
	private final Image CHECKED = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_CHECKED);
	private final Image UNCHECKED = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_UNCHECKED);

	public String getColumnText(Object element, int columnIndex) {
		PathAttribute attrModel = (PathAttribute) element;
		if (columnIndex == 0) {
			return null;
		}
		if (columnIndex == 1) {
			return attrModel.getName();
		}
		if (columnIndex == 2) {
			return attrModel.getValue();
		}
		return null;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		PathAttribute attrModel = (PathAttribute) element;
		if (columnIndex == 0) {
			if (attrModel.isEnable()) {
				return CHECKED;
			} else {
				return UNCHECKED;
			}
		}
		return null;
	}
	
	public void removeListener(ILabelProviderListener listener) {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void dispose() {
	}

	public void addListener(ILabelProviderListener listener) {
	}

}
