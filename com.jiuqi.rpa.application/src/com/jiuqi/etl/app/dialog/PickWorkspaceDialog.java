/*
 * ��������������޹�˾ ��Ȩ����(c) 2009
 * �ļ������ࣺPickWorkspaceDialog
 * ���ߣ�	���첨 	����о�Ժ�����
 * �޸ļ�¼��
 * 2009-07-28 	���첨 	�����ļ�
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
 * �����������ռ�ѡ��Ի���<p>
 * ������ͨ���˶Ի���ִ�й����ռ��л���
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
		newShell.setText("�л�������");
	}

	@Override
	protected Control createContents(Composite parent) {		
		return super.createContents(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		
		setTitle("ѡ��洢Ŀ¼");
		setMessage("�洢Ŀ¼�����ڴ洢ETL������Ϣ�뱾�ط�����");
		
		Composite container = new Composite(area, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		container.setLayout(gridLayout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final Label label = new Label(container, SWT.NONE);
		label.setText("�洢Ŀ¼��");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		combo = new Combo(container, SWT.BORDER);
		combo.setSize(200, 20);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		button = new Button(container, SWT.NONE);
		button.setText("���...");
		button.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
				directoryDialog.setText("ѡ��洢Ŀ¼");
				directoryDialog.setMessage("ѡ��һ��Ŀ¼��Ϊ����Ĵ洢Ŀ¼��");
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
