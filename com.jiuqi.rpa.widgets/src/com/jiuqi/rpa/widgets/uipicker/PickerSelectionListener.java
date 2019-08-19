package com.jiuqi.rpa.widgets.uipicker;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

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

public class PickerSelectionListener extends SelectionAdapter {
	private PickerShell shell;
	private Shell globalShell;
	private Context context;
	private FindLibraryManager findLibraryManager;
	private UIADrawerLibrary uiaDrawerLibrary;
	private UIAPickerLibrary pLibrary;
	private boolean findwindow;

	private DataInputStream inputStream;
	private DataOutputStream outputStream;

//	public PickerSelectionListener() {
//		this(false);
//	}

	public PickerSelectionListener(boolean findwindow, DataInputStream inputStream, DataOutputStream outputStream) {
		this.findwindow = findwindow;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		Button button = (Button) event.getSource();
		this.shell = (PickerShell) button.getShell();
		this.globalShell = (Shell) shell.getParent();
		try {
			startPick();
		} catch (LibraryException e) {
			e.printStackTrace();
		}
	}

	private void startPick() throws LibraryException {
		globalShell.setMinimized(true);
		this.pLibrary = new UIAPickerLibrary();
		this.context = new Context();
		findLibraryManager = new FindLibraryManager(context);
		this.uiaDrawerLibrary = new UIADrawerLibrary();

		UIAPickerCallback uiaPickerCallback = new UIAPickerCallback();
		UIAPickerCallback.setListener(new UIAPickerListener());
		pLibrary.startPick(uiaPickerCallback);

	}

	private void endPick() throws LibraryException {
		pLibrary.endPick();
		uiaDrawerLibrary.endDraw();
		context.close();
	}

	/**
	 * ·µ»ØUIPicker shell
	 */
	private void backIntoSh() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				globalShell.setMinimized(false);

				PickerShell pickerShell = PickerSelectionListener.this.shell;
				if (!pickerShell.isDisposed())
					pickerShell.setFocus();
			}
		});
	}

	class UIAPickerListener implements IUIAPickerListener {
		private Rect lastRect = new Rect();

		public void onMouseMove(int x, int y) {
			try {
				synchronized (this) {
					Point p = new Point(x, y);
					Rect rect = findwindow ? findLibraryManager.getWinodwRect(p) : findLibraryManager.getRect(p, inputStream, outputStream);
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
				IUIElement uiElement = findwindow ? findLibraryManager.getWindow(p) : findLibraryManager.get(p, inputStream, outputStream);

				backIntoSh();

				// Path¼ÓÔØµ½±à¼­Æ÷
				Path path = uiElement.getPath(inputStream, outputStream);
				shell.finishPickerLoadPath(path);
				shell.changed = true;
			} catch (LibraryException e) {
				e.printStackTrace();
			}
		}

		public void onKeyEscapeUp() {
			System.out.println("onKeyEscapeUp");
			try {
				endPick();
				backIntoSh();
			} catch (LibraryException e) {
				e.printStackTrace();
			}
		}

	}

}
