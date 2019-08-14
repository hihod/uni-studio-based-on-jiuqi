package com.jiuqi.rpa.widgets.extract;

import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TableViewerContentProvider implements IStructuredContentProvider 
{
	@Override
	public void dispose() 
	{
	}

	@Override
	public void inputChanged(Viewer viewer, Object obj, Object obj1) 
	{
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object[] getElements(Object element) 
	{
		if (element instanceof List)
		return ((List) element).toArray();
		else
		return new Object[0]; 
	}

}
