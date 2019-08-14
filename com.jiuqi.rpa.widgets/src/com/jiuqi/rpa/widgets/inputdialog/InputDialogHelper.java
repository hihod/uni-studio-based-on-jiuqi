package com.jiuqi.rpa.widgets.inputdialog;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;

public class InputDialogHelper {
	private static String resultText = "";

	public static String open(String title, String label, Boolean password, String[] options) {
		Display display = Display.getDefault();

		display.syncExec(new Runnable() {
			@Override
			public void run() {
				InputDialog dialog = new InputDialog(display, title, label, password, options);
				dialog.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent e) {
						if (dialog.okPress)
							resultText = dialog.result;
					}
				});
				
				dialog.open();
				dialog.forceActive();
				while (!dialog.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
			}
		});

		return resultText;
	}
	
	public static void main(String[] args) {
		String retTxt = open("提问", "问题", false, new String[] { "梁霄0" });
		System.out.println(retTxt);
	}
	
}
