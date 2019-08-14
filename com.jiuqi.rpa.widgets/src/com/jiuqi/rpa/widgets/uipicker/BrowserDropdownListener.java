package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.browser.WebBrowserLibary;
import com.jiuqi.rpa.lib.browser.WebBrowserType;


public class BrowserDropdownListener extends SelectionAdapter {
	private Menu menu;
	private static String BROWSER_ROOT_KEY = "WEBROOTKEY";
	public BrowserDropdownListener(Button dropdown) {
		final PickerShell shell = (PickerShell) dropdown.getShell();
		menu = new Menu(shell);
		
		MenuItem menuIE = new MenuItem(menu, SWT.NONE);
		menuIE.setText("Internet Explorer");
		menuIE.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					WebBrowserLibary.open(WebBrowserType.IE);
					shell.getVistalTreeViewer().refresh(shell.getVistalTreeViewer().getData(BROWSER_ROOT_KEY));
				} catch (LibraryException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		MenuItem menuChrome = new MenuItem(menu, SWT.NONE);
		menuChrome.setText("Google Chrome");
		menuChrome.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					WebBrowserLibary.open(WebBrowserType.CHROME);
					shell.getVistalTreeViewer().refresh(shell.getVistalTreeViewer().getData(BROWSER_ROOT_KEY));
				} catch (LibraryException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		MenuItem menuFirefox = new MenuItem(menu, SWT.NONE);
		menuFirefox.setText("Firefox");
		menuFirefox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					WebBrowserLibary.open(WebBrowserType.FIREFOX);
					shell.getVistalTreeViewer().refresh(shell.getVistalTreeViewer().getData(BROWSER_ROOT_KEY));
				} catch (LibraryException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public void widgetSelected(SelectionEvent event) {
		Button btn = (Button) event.widget;
		Rectangle rect = btn.getBounds();
		Point pt = btn.getParent().toDisplay(new Point(rect.x, rect.y));
		menu.setLocation(pt.x, pt.y + rect.height);
		menu.setVisible(true);
	}
}
