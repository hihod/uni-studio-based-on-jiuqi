package com.jiuqi.etl.rpa.toolkits.welcom.editor;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.jiuqi.bi.util.StringUtils;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;

/**
* @author ���ߣ�houzhiyuan 2019��6��24�� ����1:41:43
*/
public class UploadTemplateShell extends Shell {
	private boolean isOK = false;
	private String selFilePath;

	public UploadTemplateShell(Display display) {
		super(display, SWT.MIN);
		createControl();
		centerShell(display, getShell());
	}
	protected void checkSubclass() {
		//���org.eclipse.swt.SWTException: Subclassing not allowed
	}
	public boolean isOK() {
		return this.isOK;
	}
	private void centerShell(Display display, Shell shell) {
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		shell.setLocation(x, y);
	}
	
	private void createControl() {
		setText("�ϴ�����ģ��");
		setSize(333, 159);
		
		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 61, 17);
		lblNewLabel.setText("ѡ���ļ���");
		
		final Text filePathText = new Text(this, SWT.BORDER);
		filePathText.setBounds(10, 33, 248, 23);
		
		Button btnBrowse = new Button(this, SWT.NONE);
		btnBrowse.setBounds(264, 31, 43, 27);
		btnBrowse.setText("���");
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				FileDialog dd = new FileDialog(UploadTemplateShell.this.getShell());
				dd.setFilterExtensions(new String[] {"*.zip"});
				dd.setFilterPath(filePathText.getText());
				String dir = dd.open();
				if (dir != null) {
					filePathText.setText(dir);
					selFilePath = dir;
				}
			}
		});
		
		Button btnOK = new Button(this, SWT.NONE);
		btnOK.setBounds(179, 86, 61, 27);
		btnOK.setText("ȷ��");
		btnOK.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				if(StringUtils.isEmpty(selFilePath)) { 
					MessageBox msg = new MessageBox(UploadTemplateShell.this.getShell(), SWT.ICON_WARNING);
					msg.setText("����");
					msg.setMessage("��ѡ���ϴ��ļ�");
					msg.open();
					return;
				}
				isOK  = true;
				UploadTemplateShell.this.dispose();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		Button btnCancel = new Button(this, SWT.NONE);
	
		btnCancel.setText("ȡ��");
		btnCancel.setBounds(246, 86, 61, 27);
		btnCancel.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				isOK  = false;
				UploadTemplateShell.this.dispose();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	/**
	 * ��ȡѡ����ļ�·��
	 * @return
	 */
	public String getSelectedFilePath() {
		return this.selFilePath;
	}
}
