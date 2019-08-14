package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.swt.widgets.Display;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.drawer.UIADrawerLibrary;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.picker.IUIAPickerListener;
import com.jiuqi.rpa.lib.picker.UIAPickerCallback;
import com.jiuqi.rpa.lib.picker.UIAPickerLibrary;

public class NonePathPicker{
	UIAPickerLibrary pLibrary;
	Context context;
	FindLibraryManager findLibraryManager;
	UIADrawerLibrary uiaDrawerLibrary;
	boolean findwindow;
	Path path;
	boolean stopFlag = false;
	
	void startPicker(boolean findwindow, Path path) {
		this.path = path;
		this.pLibrary = new UIAPickerLibrary();
		this.context = new Context();
		this.findwindow = findwindow;
		this.findLibraryManager = new FindLibraryManager(context);
		this.uiaDrawerLibrary = new UIADrawerLibrary();
		
		try {
			UIAPickerCallback uiaPickerCallback = new UIAPickerCallback();
			UIAPickerCallback.setListener(new UIAPickerListener());
			pLibrary.startPick(uiaPickerCallback);
			
			while (!stopFlag){
				if (!Display.getCurrent().readAndDispatch())
					Display.getCurrent().sleep();
			}
				
		} catch (LibraryException e) {
			e.printStackTrace();
		}
}

	private void endPick() throws LibraryException {
		pLibrary.endPick();
		uiaDrawerLibrary.endDraw();
		context.close();
	}

	class UIAPickerListener implements IUIAPickerListener {
		private Rect lastRect = new Rect();

		public void onMouseMove(int x, int y) {
			try {
				synchronized (this) {
					Point p = new Point(x, y);
					Rect rect = findwindow ? findLibraryManager.getWinodwRect(p) : findLibraryManager.getRect(p);
					if (rect.equals(lastRect))
						return;

					uiaDrawerLibrary.startDraw(rect, "#ff0000");
					lastRect = rect;
				}
			} catch (LibraryException e) {
				e.printStackTrace();
			}
		}

		public void onMouseLeftUp(int x, int y) {
			System.out.println("onMouseLeftUp");
			try {
				endPick();

				Point p = new Point(x, y);
				IUIElement uiElement = findwindow ? findLibraryManager.getWindow(p) : findLibraryManager.get(p);

				// Path¼ÓÔØµ½±à¼­Æ÷
				Path _path = uiElement.getPath();
				
				path.setWeb(_path.isWeb());
				path.getElements().clear();
				path.getElements().addAll(_path.getElements());
				stopFlag = true;
			} catch (LibraryException e) {
				e.printStackTrace();
			}
		}

		public void onKeyEscapeUp() {
			System.out.println("onKeyEscapeUp");
			try {
				endPick();
				stopFlag = true;
			} catch (LibraryException e) {
				e.printStackTrace();
			}
		}

	}

	protected void checkSubclass() {
	}
}
