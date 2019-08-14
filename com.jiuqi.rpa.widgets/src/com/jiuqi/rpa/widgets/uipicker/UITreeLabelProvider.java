package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.tree.TreeWalkerManager;
import com.jiuqi.rpa.widgets.Activator;
import com.jiuqi.rpa.widgets.uipicker.UITreeContentProvider.UINodeVirtualBrowserObj;

public class UITreeLabelProvider extends LabelProvider {
	private TreeWalkerManager walkerManager;
	private final Image NODE_IMAGE = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_FIELD);

	public UITreeLabelProvider(Context context) {
		this.walkerManager = new TreeWalkerManager(context);
	}
	
	@Override
	public Image getImage(Object element) {
//		if (element instanceof UINodeObject) {
//			IUIElement uiElement = ((UINodeObject) element).getUiElement();
//			
//		}
		
		return NODE_IMAGE;
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof UINodeObject) {
			IUIElement uiElement = ((UINodeObject) element).getUiElement();
			try {
				return walkerManager.getText(uiElement);
			} catch (LibraryException e) {
				e.printStackTrace();
				return "<unknown>";
			}
		} else if (element instanceof UINodeVirtualBrowserObj)
			return ((UINodeVirtualBrowserObj) element).getText();
		
		return null;
	}
	
}
