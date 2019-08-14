package com.jiuqi.rpa.widgets.uipicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.tree.TreeWalkerManager;
import com.jiuqi.rpa.widgets.uipicker.UITreeContentProvider.UINodeVirtualBrowserObj;

public class UITreeSelectionChangedListener implements ISelectionChangedListener {
	private PickerShell pickerShell;
	private TreeWalkerManager walkerManager;

	public UITreeSelectionChangedListener(PickerShell pickerShell) {
		this.pickerShell = pickerShell;
		this.walkerManager = new TreeWalkerManager(pickerShell.getContext());
	}

	public void selectionChanged(SelectionChangedEvent event) {
		TreeSelection treeSelection = (TreeSelection) event.getSelection();
		if (treeSelection == null || treeSelection.isEmpty())
			return;

		Object object = treeSelection.getFirstElement();

		Properties properties = new Properties();
		if (object instanceof UINodeVirtualBrowserObj) {
			// TODO
		} else if (object instanceof UINodeObject) {
			IUIElement uiElement = ((UINodeObject) object).getUiElement();
			if (uiElement == null)
				return;

			try {
				properties = walkerManager.getProperties(uiElement);
			} catch (LibraryException e) {
				e.printStackTrace();
			}
		}

		Set<String> propertyNameSet = properties.stringPropertyNames();
		List<UITreeNodeProperty> propertiesList = new ArrayList<UITreeNodeProperty>(properties.size());
		for (String str : propertyNameSet) {
			String value = properties.getProperty(str, "");
			propertiesList.add(new UITreeNodeProperty(str, value));
		}
		pickerShell.getNodeAttrTableViewer().setInput(propertiesList);
	}

}
