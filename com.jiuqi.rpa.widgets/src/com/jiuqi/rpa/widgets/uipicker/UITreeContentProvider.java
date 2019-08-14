package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.browser.UIBrowser;
import com.jiuqi.rpa.lib.browser.WebBrowserManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.tree.TreeWalkerManager;
import com.jiuqi.rpa.lib.tree.WEBTreeWalker;

public class UITreeContentProvider implements ITreeContentProvider {
	private Context context;
	private TreeWalkerManager walkerManager;

	public UITreeContentProvider(Context context) {
		this.context = context;
		this.walkerManager = new TreeWalkerManager(context);
	}

	public Object[] getElements(Object inputElement) {
		Object[] uiObjs = new Object[2];

		// ×ÀÃæ
		try {
			uiObjs[0] = new UINodeObject(null, walkerManager.getUIARoot());
		} catch (LibraryException e) {
			e.printStackTrace();
		}

		// ä¯ÀÀÆ÷Ðé½Úµã
		uiObjs[1] = new UINodeVirtualBrowserObj("ä¯ÀÀÆ÷");

		return uiObjs;
	}

	public Object[] getChildren(Object parentElement) {
		try {
			if (parentElement instanceof UINodeVirtualBrowserObj) {
				UINodeVirtualBrowserObj browserObj = (UINodeVirtualBrowserObj) parentElement;
				if(browserObj.getChildern()!=null){
					return browserObj.getChildern();
				}
				UIBrowser[] uiBrowsers = WebBrowserManager.getInstance().getBrowserArray();

				UINodeObject[] uiNodeObjs = new UINodeObject[uiBrowsers.length];
				for (int i = 0; i < uiBrowsers.length; i++) {
					WEBTreeWalker webWalker = new WEBTreeWalker(uiBrowsers[i].getId(), context);
					uiNodeObjs[i] = new UINodeObject(parentElement, webWalker.getRoot());
				}
				browserObj.setChildern(uiNodeObjs);
				return uiNodeObjs;
			} else if (parentElement instanceof UINodeObject) {
				UINodeObject uiNodeObj = (UINodeObject) parentElement;
				if(uiNodeObj.getChildern()!=null){
					return uiNodeObj.getChildern();
				}
				IUIElement[] uiElements = walkerManager.getChildren(uiNodeObj.getUiElement());

				UINodeObject[] uiNodeObjs = new UINodeObject[uiElements.length];
				for (int i = 0; i < uiElements.length; i++)
					uiNodeObjs[i] = new UINodeObject(parentElement, uiElements[i]);
				uiNodeObj.setChildern(uiNodeObjs);
				return uiNodeObjs;
			}
		} catch (LibraryException e) {
			e.printStackTrace();
		}

		return new Object[0];
	}

	public Object getParent(Object element) {
		if (element instanceof UINodeObject) {
			UINodeObject uiNodeObject = (UINodeObject) element;
			return uiNodeObject.getParent();
		}

		if (element instanceof UINodeVirtualBrowserObj)
			return null;

		return null;
	}

	public boolean hasChildren(Object element) {
		return true;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}



	class UINodeVirtualBrowserObj {
		private String text;
		private Object[] childern = null;
		
		public UINodeVirtualBrowserObj(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
		public Object[] getChildern() {
			return childern;
		}

		public void setChildern(Object[] childern) {
			this.childern = childern;
		}
	}

}
