package com.jiuqi.etl.app;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.jiuqi.bi.authz.licence.LicenceInfo;
import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.app.preference.LCPreferenceStorage;
import com.jiuqi.etl.app.util.ETLLicenceUtils;
import com.jiuqi.etl.authz.ETLLicenceException;
import com.jiuqi.etl.authz.ETLLicenceManager;
import com.jiuqi.etl.preference.PreferenceManager;
import com.jiuqi.etl.storage.StorageProviderManager;
import com.jiuqi.etl.storage.WorkSpaceManager;
import com.jiuqi.widgets.dialog.ErrorDialog;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {
	// ����ʱ���ʱ��Ϊ20�죬��ʽ����ʱ��Ϊ7��
	private final static int WARN_DAY = 7;

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static String PRE_LAST_EXPIRY_TIME = "lastExpiryTime";
	
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(
				"application.args");
		
		if (args.length > 0) {
			ApplicationConsole.run(args);
			return IApplication.EXIT_OK;
		} else {
			// ���ù�������ʹ������Ĺ�����
			Platform.getInstanceLocation().set(new URL("file", null, WorkSpaceManager.getInstance().getLastWorkspace()), false);

			int result = 0;
			StorageProviderManager storageMgr = StorageProviderManager.getInstance();
			Display display = PlatformUI.createDisplay();

			//RPA��Ʒ
			result = tryOneLogin(storageMgr, true);
			WorkSpaceManager.getInstance().setHideDlg(false);
			
			//ע��ϵͳѡ����Ҫ����etlִ��ģʽ��������ڴ򿪵�¼��֮��
			PreferenceManager preMgr = PreferenceManager.getInstance();
			preMgr.registerPreference(new LCPreferenceStorage());
			
			if (result == 0) {
				// ������Ȩ����
				try {
					ETLLicenceUtils.loadLicence();
					// ETLLicenceManager.validateModule();
					LicenceInfo licenceInfo = ETLLicenceManager.getLicenceInfo();
					Calendar now = Calendar.getInstance();
					Calendar expiry = Calendar.getInstance();
					expiry.setTimeInMillis(licenceInfo.getExpiryTime());
					if (now.after(expiry)) {
						ErrorDialog.openWarning(null, "��Ȩ��ʾ", "�����Ȩ���� " + DATE_FORMAT.format(expiry.getTime()) + " ʧЧ��������������Ȩ��", null);
					} else {
						now.add(Calendar.DAY_OF_MONTH, WARN_DAY);
						IPreferenceStore preStore = ApplicationPlugin.getDefault().getPreferenceStore();
						boolean showWarn = false;
						if (now.after(expiry)) {
							String lastExpiryTime = preStore.getString(PRE_LAST_EXPIRY_TIME);
							if(StringUtils.isEmpty(lastExpiryTime)){
								 showWarn = true;
							} else {
								try{
									Date lastExpiryDate = DATE_FORMAT.parse(lastExpiryTime);
									Calendar lastExpiry = Calendar.getInstance();
									lastExpiry.setTime(lastExpiryDate);
									if(lastExpiry.get(Calendar.YEAR) == expiry.get(Calendar.YEAR)
											&& lastExpiry.get(Calendar.MONTH) == expiry.get(Calendar.MONTH)
											&& lastExpiry.get(Calendar.DAY_OF_MONTH) == expiry.get(Calendar.DAY_OF_MONTH)){
										// ��Ȩδ���£��������˱�����Ȩ������ʾ
									} else {
										 showWarn = true;
									}
								} catch (ParseException e){
									 showWarn = true;
								}
							}
						}
						if(showWarn){
							MessageDialogWithToggle dialog = MessageDialogWithToggle.openWarning(
									null, "��Ȩ��ʾ", "�����Ȩ���� " + DATE_FORMAT.format(expiry.getTime()) + " ʧЧ���뾡������������Ȩ��", 
									"������Ȩ������ʾ", false, null, null);
							if(dialog.getToggleState()){
								preStore.setValue(PRE_LAST_EXPIRY_TIME, DATE_FORMAT.format(expiry.getTime()));
							} else {
								preStore.setValue(PRE_LAST_EXPIRY_TIME, "");
							}
						}
					}
				} catch (ETLLicenceException e) {
					ErrorDialog.openWarning(null, "", "������Ȩ�����쳣", e);
				}
				try {
					int returnCode = PlatformUI.createAndRunWorkbench(display,
							new ApplicationWorkbenchAdvisor());
					if (returnCode == PlatformUI.RETURN_RESTART)
						return IApplication.EXIT_RESTART;
					else
						return IApplication.EXIT_OK;
				} finally {
					display.dispose();
				}
			}
			return IApplication.EXIT_OK;
		}
	}

	private String getWorkspacePath() {
		String instanceLocation = Platform.getInstanceLocation().getURL()
				.getPath();
		return instanceLocation.substring(1);
	}
	
	private int tryOneLogin(StorageProviderManager storageMgr, boolean forceLocal) {
		int result = -1;
		boolean success;

		success = storageMgr.login(getWorkspacePath());
		if (!success) {
			result = 4;
			MessageDialog.openInformation(null, "�޷���¼", "δ֪ԭ���±���ģʽ�޷�����");
		} else {
			result = 0;
		}
		return result;
	}

	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
