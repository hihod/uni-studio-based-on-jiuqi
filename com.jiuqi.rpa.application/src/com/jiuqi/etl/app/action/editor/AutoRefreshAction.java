/*
 * 2010-9-20
 *
 * 王星宇
 */
package com.jiuqi.etl.app.action.editor;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * @author 王星宇
 *
 */
public class AutoRefreshAction extends ControlContribution {
	
	public static final String ID = "com.jiuqi.etl.app.action.autorefresh";
	
	private static int selection = 0;
	
	private Control lastControl;
	private IWorkbenchWindow window;
	
	private boolean enabled;
	
	private static final int[] values = {0, 1, 5, 10, 30, 60};
	private static final String[] texts = {"不自动刷新", "每一秒刷新一次", "每5秒刷新一次", "每10秒刷新一次", "每30秒刷新一次", "每一分钟刷新一次"};
	
	public static int getDelay() {
		return values[selection];
	}
	
	public AutoRefreshAction(IWorkbenchWindow window) {
		super(ID);
		this.window = window;
	}

	@Override
	protected Control createControl(Composite parent) {
		if (lastControl != null) {
			if (!lastControl.isDisposed()) {
				lastControl.dispose();
			}
			lastControl = null;
		}
		
		final Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setItems(texts);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selection = combo.getSelectionIndex();
				IEditorPart editorPart = window.getActivePage().getActiveEditor();
				if (editorPart instanceof TraceEditor) {
					TraceEditor traceEditor = (TraceEditor) editorPart;
					traceEditor.setDelay(values[selection]);
				}
			}
		});
		combo.setVisibleItemCount(6);
		combo.setToolTipText("设置自动刷新");
		
		lastControl = combo;
		combo.select(selection);
		combo.setEnabled(enabled);
		return combo;
	}
	
	public void setEnable(boolean enabled) {
		this.enabled = enabled;
		if (lastControl != null && !lastControl.isDisposed())
			lastControl.setEnabled(enabled);
	}
	
	@Override
	public void dispose() {
		if (lastControl != null) {
			if (!lastControl.isDisposed())
				lastControl.dispose();
			lastControl = null;
		}
	}

}
