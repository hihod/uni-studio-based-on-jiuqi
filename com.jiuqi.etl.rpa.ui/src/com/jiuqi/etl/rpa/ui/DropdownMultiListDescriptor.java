package com.jiuqi.etl.rpa.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.jiuqi.rpa.action.VirtualKey;
import com.jiuqi.rpa.widgets.dropdown.DropdownMultiListCellEditor;
import com.jiuqi.rpa.widgets.dropdown.DropdownMultiListItem;
/**
 * 多选下拉
 * @author liangxiao01
 * 每个属性视图单独实现
 * editor.setInput(new DropdownMultiListItem[]{ALT, CTRL,SHIFT,WIN}); 填充内容
 */
public class DropdownMultiListDescriptor extends PropertyDescriptor {
	String[] nodeList = null;

	public DropdownMultiListDescriptor(String id, String displayName, String[] nodeList) {
		super(id, displayName);
		this.nodeList = nodeList;
	}

	public CellEditor createPropertyEditor(Composite parent) {
		DropdownMultiListCellEditor editor = new DropdownMultiListCellEditor(parent, false);
		
		editor.setLabelProvider(new ILabelProvider() {
			public void removeListener(ILabelProviderListener listener) {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void dispose() {
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public String getText(Object element) {
				if(element instanceof DropdownMultiListItem) {
					DropdownMultiListItem ti = (DropdownMultiListItem)element;
					return ti.name;					
				}else{
					return "";
				}
			}

			public Image getImage(Object element) {
				return null;
			}
		});
		editor.setContentProvider(new ITreeContentProvider() {

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			public void dispose() {

			}
			public Object[] getElements(Object inputElement) {
				return (Object[]) inputElement;
			}
			public boolean hasChildren(Object element) {
				return false;
			}
			public Object getParent(Object element) {
				return null;
			}
			public Object[] getChildren(Object parentElement) {
				return null;
			}
		});
		DropdownMultiListItem ALT = new DropdownMultiListItem("ALT",VirtualKey.VK_MENU);
		DropdownMultiListItem CTRL = new DropdownMultiListItem("CTRL",VirtualKey.VK_CONTROL);
		DropdownMultiListItem SHIFT = new DropdownMultiListItem("SHIFT",VirtualKey.VK_SHIFT);
		DropdownMultiListItem WIN = new DropdownMultiListItem("WIN",VirtualKey.VK_LWIN);
			
		editor.setInput(new DropdownMultiListItem[]{ALT, CTRL,SHIFT,WIN});
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}
}

