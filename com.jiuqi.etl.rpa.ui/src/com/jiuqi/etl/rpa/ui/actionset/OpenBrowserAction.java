package com.jiuqi.etl.rpa.ui.actionset;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowPulldownDelegate;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.browser.WebBrowserLibary;
import com.jiuqi.rpa.lib.browser.WebBrowserType;

public class OpenBrowserAction implements IWorkbenchWindowPulldownDelegate {
	private Menu parentMenu;
	public static final String ID = "com.jiuqi.etl.rpa.ui.actionset.OpenBrowser";

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}

	public void run(IAction action) {
		try {
			WebBrowserLibary.open(WebBrowserType.CHROME);
		} catch (LibraryException e1) {
			e1.printStackTrace();
		}

	}

	public void selectionChanged(IAction action, ISelection selection) {


	}

	public Menu getMenu(Control parent) {
		parentMenu = new Menu(parent);
		return buildMenuList();
	}
	private Menu buildMenuList(){
		MenuItem menuIE = new MenuItem(parentMenu, SWT.NONE);
		menuIE.setText("Internet Explorer");
		menuIE.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					WebBrowserLibary.open(WebBrowserType.IE);
				} catch (LibraryException e1) {
					e1.printStackTrace();
				}
			}
		});
		MenuItem menuChrome = new MenuItem(parentMenu, SWT.NONE);
		menuChrome.setText("Google Chrome");
		menuChrome.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					WebBrowserLibary.open(WebBrowserType.CHROME);
				} catch (LibraryException e1) {
					e1.printStackTrace();
				}
			}
		});
		MenuItem menuFirefox = new MenuItem(parentMenu, SWT.NONE);
		menuFirefox.setText("Firefox");
		menuFirefox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					WebBrowserLibary.open(WebBrowserType.FIREFOX);
				} catch (LibraryException e1) {
					e1.printStackTrace();
				}
			}
		});
		return parentMenu;
	}
}
