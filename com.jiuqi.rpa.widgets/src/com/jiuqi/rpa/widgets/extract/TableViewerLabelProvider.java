package com.jiuqi.rpa.widgets.extract;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class TableViewerLabelProvider implements ITableLabelProvider 
{
	@Override
	public void addListener(ILabelProviderListener ilabelproviderlistener) 
	{
	}

	@Override
	public void dispose() 
	{
	}

	@Override
	public boolean isLabelProperty(Object obj, String s) 
	{
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener ilabelproviderlistener) 
	{
	}

	@Override
	public Image getColumnImage(Object obj, int i) 
	{
		return null;
	}

	@Override
	public String getColumnText(Object element, int col) 
	{
		ColumnEntity param = (ColumnEntity) element;
		if (col == 0)
		return param.getName();
		if (col == 1)
		return param.getTitle();
		return null; 
	}
}
