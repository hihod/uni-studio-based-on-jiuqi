/*
 * 北京久其软件有限公司 版权所有(c) 2009
 * 文件所含类：PickWorkspaceDialog
 * 作者：	周徐波 	软件研究院组件部
 * 修改记录：
 * 2009-07-28 	周徐波 	创建文件
 */
package com.jiuqi.etl.app.dialog;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.jiuqi.etl.storage.WorkSpaceManager;

/**
 * 类名：工作空间选择对话框<p>
 * 描述：通过此对话框执行工作空间切换。
 */
public class PickWorkspaceDialog extends TitleAreaDialog {	
	private Combo  combo;
	private Button button;
	private String path;

	public PickWorkspaceDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {		
		super.configureShell(newShell);
		newShell.setText("切换工作区");
	}

	@Override
	protected Control createContents(Composite parent) {		
		return super.createContents(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		
		setTitle("选择存储目录");
		setMessage("存储目录：用于存储ETL配置信息与本地方案。");
		
		Composite container = new Composite(area, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		container.setLayout(gridLayout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final Label label = new Label(container, SWT.NONE);
		label.setText("存储目录：");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		combo = new Combo(container, SWT.BORDER);
		combo.setSize(200, 20);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		button = new Button(container, SWT.NONE);
		button.setText("浏览...");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
				directoryDialog.setText("选择存储目录");
				directoryDialog.setMessage("选择一个目录作为程序的存储目录：");
				directoryDialog.setFilterPath(combo.getText().trim());
				String dir = directoryDialog.open();
				if (dir != null)
					combo.setText(dir);
			}
		});
		
		loadWorkspaces();
		return area;
	}
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 200);
	}
	
	@Override
	protected void okPressed() {
		path = combo.getText().trim();
		
		WorkSpaceManager.getInstance().add(path);
		WorkSpaceManager.getInstance().setHideDlg(true);
		super.okPressed();
	}

	private void loadWorkspaces(){
		ArrayList<String> workspaces = WorkSpaceManager.getInstance().getWorkspaces();
		for (int i=0; i<workspaces.size(); i++)
			combo.add(workspaces.get(i));
		combo.setText(WorkSpaceManager.getInstance().getLastWorkspace());	
	}
	
	public String getPath() {
		return path;
	}
	
}
