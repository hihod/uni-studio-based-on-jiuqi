package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;


public class NeesSaveDialog extends Dialog {

	protected NeesSaveDialog(Shell parentShell) {
		super(parentShell);
	}

	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		Button button1 = createButton(parent, IDialogConstants.NEXT_ID, "保存",true);
		button1.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				setReturnCode(IDialogConstants.NEXT_ID);
				getShell().dispose();
			}
		});
		Button button2 = createButton(parent, IDialogConstants.BACK_ID,"不保存", false);
		button2.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				setReturnCode(IDialogConstants.BACK_ID);
				getShell().dispose();
			}
		});
		Button button3 = createButton(parent, IDialogConstants.FINISH_ID, "取消",false);
		button3.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				setReturnCode(IDialogConstants.FINISH_ID);
				getShell().dispose();
			}
		});
		
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("提示");
	}
	
	protected Control createDialogArea(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label label = new Label(composite, SWT.NONE);
		label.setFont(new Font(getShell().getDisplay(), "Microsoft YaHei UI", 10, SWT.NORMAL));
		label.setBounds(10, 25, 273, 30);
		label.setText("选择器已修改，是否保存？");
		return parent;
	}
}
