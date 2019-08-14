package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class UINodePropertiesTableLabelProvider implements ITableLabelProvider {

	public String getColumnText(Object element, int columnIndex) {
		UITreeNodeProperty propertie = (UITreeNodeProperty) element;
		if (columnIndex == 0)// 第一列要显示什么数据
			return propertie.getKey();

		if (columnIndex == 1)
			return propertie.getValue();

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

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
}
