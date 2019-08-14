package com.jiuqi.rpa.widgets.dropdown;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TreeItem;
/**
 * 
 * @author liangxiao01
 * 属性视图单元格
 * DropdownMultiListDescriptor 
 */
public class DropdownMultiListCellEditor extends CellEditor {
	private DropdownMultiList ddt;
	private boolean isExpand;
	private int showLines = 5;
	public DropdownMultiListCellEditor(Composite parent, boolean isExpand) {
		super(parent);
		this.isExpand = isExpand;
	}
	
	protected Control createControl(Composite parent) {
		ddt = new DropdownMultiList(parent, SWT.READ_ONLY);
		ddt.setShowLines(showLines);
		ddt.setTextProvider(new ILabelProvider() {
			public void removeListener(ILabelProviderListener listener) {}
			
			public boolean isLabelProperty(Object element, String property) {return false;}
			
			public void dispose() {}
			public void addListener(ILabelProviderListener listener) {}
			public String getText(Object element) {
				TreeItem[] items = ddt.getTreeViewer().getTree().getItems();
				List<String> list = new ArrayList<String>();
				for (TreeItem treeItem : items) {
					if(treeItem.getChecked()){
						list.add(((DropdownMultiListItem)treeItem.getData()).name);
					}
				}
				return list.stream().reduce("", (l, r) -> l + ((l.equals("")||r.equals(""))?"":",") + r);
			}
			public Image getImage(Object element) {return null;}
		});
		return ddt;
	}
	
	public void setContentProvider(IContentProvider provider) {
		ddt.getTreeViewer().setContentProvider(provider);
	}
	
	public void setLabelProvider(ILabelProvider provider) {
		ddt.getTreeViewer().setLabelProvider(provider);
	}
	
	public void setInput(Object input) {
		ddt.getTreeViewer().setInput(input);
		if (isExpand)
			ddt.getTreeViewer().expandAll();
	}

	protected Object doGetValue() {
		TreeItem[] items = ddt.getTreeViewer().getTree().getItems();
		List<Integer> list = new ArrayList<Integer>();
		for (TreeItem treeItem : items) {
			if(treeItem.getChecked()){
				list.add(((DropdownMultiListItem)treeItem.getData()).key);
			}
		}
		int[] re = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			re[i] = list.get(i).intValue();
		}
		return re;
	}

	protected void doSetFocus() {
		ddt.setFocus();
	}

	protected void doSetValue(Object value) {
		TreeItem[] items = ddt.getTreeViewer().getTree().getItems();
		for (TreeItem treeItem : items) {
			DropdownMultiListItem it = (DropdownMultiListItem) treeItem.getData();
			if(((String)value).indexOf(it.name)>=0){
				treeItem.setChecked(true);				
			}
		}
		ddt.setSelection(value);
	}

	
	public TreeViewer getTreeViewer() {
		return ddt.getTreeViewer();
	}
	
	public void setShowLine(int lines) {
		this.showLines = lines;
		if (ddt != null) {
			ddt.setShowLines(lines);
		}
	}
}
