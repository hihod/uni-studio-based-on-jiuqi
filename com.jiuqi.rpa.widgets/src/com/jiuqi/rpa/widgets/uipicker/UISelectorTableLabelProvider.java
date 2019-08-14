package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.jiuqi.rpa.lib.find.PathElement;
import com.jiuqi.rpa.widgets.Activator;

public class UISelectorTableLabelProvider implements ITableLabelProvider {
	private PickerShell pickerShell;

	private final Image CHECKED = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_CHECKED);
	private final Image UNCHECKED = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_UNCHECKED);

	public UISelectorTableLabelProvider(PickerShell pickerShell) {
		this.pickerShell = pickerShell;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		PathElement pathElement = (PathElement) element;
		if (columnIndex == 0) {
			if (pathElement.isEnable()) {
				return CHECKED;
			} else {
				return UNCHECKED;
			}
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		PathElement pathElement = (PathElement) element;
		if (columnIndex == 0) {
			return null;
		}
		if (columnIndex == 1) {
			// 第一列要显示什么数据
			return PathElementHelper.toXMLformat(pickerShell.getCurrentPath().isWeb(), pathElement);
		}
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

}
