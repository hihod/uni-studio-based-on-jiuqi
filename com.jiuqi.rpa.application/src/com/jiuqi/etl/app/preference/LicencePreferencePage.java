package com.jiuqi.etl.app.preference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.jiuqi.bi.authz.ErrorCode;
import com.jiuqi.bi.authz.licence.LicenceInfo;
import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.app.util.ETLLicenceUtils;
import com.jiuqi.etl.authz.ETLLicenceException;
import com.jiuqi.etl.authz.ETLLicenceManager;
import com.jiuqi.etl.storage.StorageException;
import com.jiuqi.etl.storage.StorageProviderManager;
import com.jiuqi.widgets.dialog.ErrorDialog;

public class LicencePreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	private Cursor WAIT = Display.getDefault().getSystemCursor(SWT.CURSOR_WAIT);
	private String code;
	private Composite composite;
	private Composite upperComp;
	
	private Label _machineCode;
	private Label _expire;
	private Label _dfMaxCount;
	private Label _username;
	private Label _unitname;
	
	private Text machineCode;
	private Label expire;
	private Label dfMaxCount;
	private Label username;
	private Label unitname;
	
	private Button copyBtn;
	private Button installBtn;
	private FileDialog fileDialog;
	
	private Group infoGroup;
	private Label info;
	
	@Override
	protected Control createContents(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		composite.setLayout(gridLayout);

		//������Ȩ��Ϣ�ؼ�
		createControls(composite);
						
		fileDialog = new FileDialog(getShell(), SWT.OPEN);
		fileDialog.setFilterExtensions(new String[]{"*.licence"});

		noDefaultAndApplyButton();		
		applyDialogFont(composite);
		return composite;
	}

	private void createControls(Composite parent) {
		//��Ȩ��Ϣ
		if (upperComp != null)
			upperComp.dispose();
		upperComp = new Composite(parent, SWT.NONE);
		GridLayout upperGridLayout = new GridLayout();
		upperGridLayout.numColumns = 3;
		upperGridLayout.verticalSpacing = 10;
		upperComp.setLayout(upperGridLayout);
		

		try {
			code = StorageProviderManager.getInstance().getStorageProvider().getLicenceStorage().getMachineCode();
		} catch (StorageException e) {
			ErrorDialog.openError(getShell(), "����", "��ȡ������ʱ���ִ���", e);
		}			
		_machineCode = new Label(upperComp, SWT.NONE);
		_machineCode.setText("�����루" + (StorageProviderManager.getInstance().isServerMode()?"������":"����") + "����");
		machineCode = new Text(upperComp, SWT.BORDER);
		machineCode.setSize(400, 20);
		machineCode.setEditable(false);
		machineCode.setText(code);
		copyBtn = new Button(upperComp, SWT.NONE);
		copyBtn.setText("����");
		copyBtn.addSelectionListener(
			new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					Clipboard clipboard = new Clipboard(Display.getDefault());
					clipboard.setContents(new String[] { code },
							new Transfer[] { TextTransfer.getInstance() });
					clipboard.dispose();
				}
			}
		);
		boolean getLicenceError = false;
		try {
			LicenceInfo licenceInfo = ETLLicenceManager.getLicenceInfo();
			if (licenceInfo != null ) {			
//				_type = new Label(upperComp, SWT.NONE);
//				_type.setText("���ͣ�");
//				type = new Label(upperComp, SWT.NONE);						
//				type.setText(authType.title());
//				new Label(upperComp, SWT.NONE);
				
				_expire = new Label(upperComp, SWT.NONE);
				_expire.setText("����ʱ�䣺");
				expire = new Label(upperComp, SWT.NONE);			
				Date expireDate = new Date(licenceInfo.getExpiryTime());
				String expireStr = new SimpleDateFormat("yyyy-MM-dd").format(expireDate);
				expire.setText(StringUtils.isEmpty(expireStr)?"<�޹���ʱ��>":expireStr);
				new Label(upperComp, SWT.NONE);
				
				_dfMaxCount = new Label(upperComp, SWT.NONE);
				_dfMaxCount.setText("���������������");
				dfMaxCount = new Label(upperComp, SWT.NONE);
				String mcStr = String.valueOf(0/*ETLLicenceManager.getValue(ETLLicenceManager.DATAFLOW_MAX_COUNT)*/);
				dfMaxCount.setText("0".equals(mcStr)?"������":mcStr);
				new Label(upperComp, SWT.NONE);
				
				_username = new Label(upperComp, SWT.NONE);
				_username.setText("�û���");
				username = new Label(upperComp, SWT.NONE);
				String usernameStr = licenceInfo.getUserName();
				username.setText(StringUtils.isEmpty(usernameStr)?"��":usernameStr);
				new Label(upperComp, SWT.NONE);
				
				_unitname = new Label(upperComp, SWT.NONE);
				_unitname.setText("��λ��");
				unitname = new Label(upperComp, SWT.NONE);
				String unitnameStr = licenceInfo.getCompanyName();
				unitname.setText(StringUtils.isEmpty(unitnameStr)?"��":unitnameStr);
				new Label(upperComp, SWT.NONE);
			}
			else {
				ETLLicenceUtils.loadLicence();					
			}
		} catch (ETLLicenceException e) {
			getLicenceError =true;
			ErrorDialog.openError(getShell(), "����", "��ȡ��Ȩ����ʱ�����쳣", e);
		}				
		
		//��װ��ť
		if (installBtn != null)
			installBtn.dispose();
		installBtn = new Button(composite, SWT.NONE);
		installBtn.setText("��װ��Ȩ�ļ�");
		installBtn.addSelectionListener(
			new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					String licencePath = fileDialog.open();
					if (StringUtils.isEmpty(licencePath))
						return;
					
					File file = new File(licencePath);
					if (file.exists()) {
						Cursor defCursor = composite.getCursor();												
						try {
							composite.setCursor(WAIT);
							
							InputStream is = new FileInputStream(file);
							try{
								byte[] tempBytes = new byte[1024];
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								int byteRead = 0;
								while ((byteRead = is.read(tempBytes)) != -1)
									baos.write(tempBytes, 0, byteRead);						
								byte[] data = baos.toByteArray();
								ETLLicenceManager.addLicence(data);
								ETLLicenceManager.validateMachineCode(code);															
								ETLLicenceUtils.storeLicence(data);//����ʱ������У��
								createControls(composite);
								composite.layout();
								setErrorMessage(null);
								if(StorageProviderManager.getInstance().isServerMode()){
									MessageDialog.openInformation(getShell(), "��ʾ", "��Ȩ��װ�ɹ�����������������ͻ��ˡ�");						
								}
								else{
									MessageDialog.openInformation(getShell(), "��ʾ", "��Ȩ��װ�ɹ����������ͻ��ˡ�");
								}
							} finally {
								is.close();
							}
						} catch (FileNotFoundException e1) {
							ErrorDialog.openError(getShell(), "����", "�Ҳ����ļ�", e1);
						} catch (ETLLicenceException e2) {
							if(e2.getErrorCode() == ErrorCode.MACHINE_CODE_INCORRECT){
								ETLLicenceManager.removeLicence();
							}
							ErrorDialog.openError(getShell(), "����", "�˴���Ȩʧ�ܣ�����ϵ����Ա��", e2);
						} catch (IOException e3) {
							ErrorDialog.openError(getShell(), "����", "����ȡ����", e3);
						} finally {
							composite.setCursor(defCursor);
						}
					} else
						ErrorDialog.openError(getShell(), "����", "�Ҳ����ļ���" + licencePath, new Exception());
						
				}
			}
		);
		
		String infoText = "";
		if(!getLicenceError){
			try {
				StringBuffer buffer = new StringBuffer();
				buffer.append(ETLLicenceManager.getLicenceBaseInfo());
				buffer.append("�����룺");
				for(String code : ETLLicenceManager.getLicenceInfo().getMachineCodes()){
					buffer.append(code).append("\n").append("\t");
				}
				infoText = buffer.toString();
			} 
			catch (ETLLicenceException e) {
				ErrorDialog.openError(getShell(), "����", e.getMessage(), e);
			}		
		}
		if (infoGroup != null)
			infoGroup.dispose();
		
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		infoGroup = new Group(composite, SWT.None|SWT.FILL);		
		infoGroup.setText("������Ϣ");
		infoGroup.setLayoutData(data);
		infoGroup.setLayout(new GridLayout(1, false));
		info = new Label(infoGroup, SWT.NONE);
		info.setText(infoText);
		info.setLayoutData(data);
	}

	public void init(IWorkbench workbench) {	
	}
}
