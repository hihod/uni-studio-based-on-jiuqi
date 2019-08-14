package com.jiuqi.etl.app.preference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.jiuqi.bi.util.JqLib;
import com.jiuqi.etl.app.ApplicationPlugin;
import com.jiuqi.etl.preference.IPreference;
import com.jiuqi.etl.preference.PreferenceModel;
import com.jiuqi.etl.storage.StorageProviderManager;
import com.jiuqi.etl.storage.ws.WSPreferenceStorage;
import com.jiuqi.etl.task.sparkflow.SparkFlowTaskFactory;
import com.jiuqi.vis.thunder.client.ThunderClient;
import com.jiuqi.widgets.dialog.ErrorDialog;

public class ThunderPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	public static final int PASSWORD_MAX_LENGTH = 10;

	private Label addressLabel;
	private Text addressText;
	private Label usernameLabel;
	private Text usernameText;
	private Label passwordLabel;
	private Text passwordText;

	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 10;
		layout.marginRight = 20;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL));

		GridData labelGridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);

		addressLabel = new Label(composite, SWT.NONE);
		addressLabel.setLayoutData(labelGridData);
		addressLabel.setText("   地址：");

		addressText = new Text(composite, SWT.BORDER);
		addressText.setLayoutData(textGridData);

		usernameLabel = new Label(composite, SWT.NONE);
		usernameLabel.setLayoutData(labelGridData);
		usernameLabel.setText("用户名：");

		usernameText = new Text(composite, SWT.BORDER);
		usernameText.setLayoutData(textGridData);

		passwordLabel = new Label(composite, SWT.NONE);
		passwordLabel.setLayoutData(labelGridData);
		passwordLabel.setText("   密码：");

		passwordText = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(textGridData);
		
		initValue();
	
		Button testButton = new Button(composite, SWT.CENTER);
		GridData buttonGridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		buttonGridData.horizontalSpan = 2;
		buttonGridData.widthHint = 90;
		testButton.setLayoutData(buttonGridData);
		testButton.setText("测试连接");
		
		final Shell shell = parent.getShell();
		testButton.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				ThunderClient thunderClient = new ThunderClient(addressText.getText());
				thunderClient.setUserInfo(usernameText.getText(), passwordText.getText());
				
				boolean isSuccess = thunderClient.isCanReachable();
				if(isSuccess){
					ErrorDialog dialog = new ErrorDialog(shell, "成功", "连接成功", null);
					dialog.setImage(ErrorDialog.ICON_INFO);
					dialog.open();
				} else {
					ErrorDialog.openWarning(shell, "失败", "连接失败", null);
				}
			}
		});
		
		return composite;
	}

	protected void performDefaults() {
		addressText.setText("");
		usernameText.setText("");
		passwordText.setText("");
		super.performDefaults();
	}

	public boolean performOk() {
		StorageProviderManager spm = StorageProviderManager.getInstance();
		if(spm.isServerMode()){
			try {
				IPreference storage = new WSPreferenceStorage();
				List<PreferenceModel> models = new ArrayList<PreferenceModel>();
				buildThunderPreference(models);
				storage.savePreference(models);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ApplicationPlugin.getDefault().getPreferenceStore().setValue(SparkFlowTaskFactory.PREF_KEY_URL, addressText.getText());
			ApplicationPlugin.getDefault().getPreferenceStore().setValue(SparkFlowTaskFactory.PREF_KEY_USER_NAME, usernameText.getText());
			ApplicationPlugin.getDefault().getPreferenceStore().setValue(SparkFlowTaskFactory.PREF_KEY_PASSWORD, JqLib.encodePassword(passwordText.getText(), PASSWORD_MAX_LENGTH));
		}
		return super.performOk();
	}
	
	private void buildThunderPreference(List<PreferenceModel> models){
		PreferenceModel model1 = new PreferenceModel();
		model1.setCategory(SparkFlowTaskFactory.PREF_CATEGORY_THUNDER);
		model1.setName(SparkFlowTaskFactory.PREF_KEY_URL);
		model1.setValue(addressText.getText());
		model1.setDefaultValue(null);
		models.add(model1);
		
		PreferenceModel model2 = new PreferenceModel();
		model2.setCategory(SparkFlowTaskFactory.PREF_CATEGORY_THUNDER);
		model2.setName(SparkFlowTaskFactory.PREF_KEY_USER_NAME);
		model2.setValue(usernameText.getText());
		model2.setDefaultValue(null);
		models.add(model2);
		
		PreferenceModel model3 = new PreferenceModel();
		model3.setCategory(SparkFlowTaskFactory.PREF_CATEGORY_THUNDER);
		model3.setName(SparkFlowTaskFactory.PREF_KEY_PASSWORD);
		model3.setValue(JqLib.encodePassword(passwordText.getText(), PASSWORD_MAX_LENGTH));
		model3.setDefaultValue(null);
		models.add(model3);
	}
	
	private void initValue(){
		StorageProviderManager spm = StorageProviderManager.getInstance();
		if(spm.isServerMode()){
			try {
				IPreference storage = new WSPreferenceStorage();
				List<PreferenceModel> models = storage.getPreferenceByCategory(SparkFlowTaskFactory.PREF_CATEGORY_THUNDER);
				for(PreferenceModel model : models){
					if(model.getName().equals(SparkFlowTaskFactory.PREF_KEY_URL)){
						addressText.setText(model.getValue());
					} else if(model.getName().equals(SparkFlowTaskFactory.PREF_KEY_USER_NAME)){
						usernameText.setText(model.getValue());
					} else if(model.getName().equals(SparkFlowTaskFactory.PREF_KEY_PASSWORD)){
						passwordText.setText(JqLib.decodePassword(model.getValue()));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			IPreference storage = new LCPreferenceStorage();
			try {
				addressText.setText(storage.getPreferenceByCateAndName(SparkFlowTaskFactory.PREF_CATEGORY_THUNDER, SparkFlowTaskFactory.PREF_KEY_URL).getValue());
				usernameText.setText(storage.getPreferenceByCateAndName(SparkFlowTaskFactory.PREF_CATEGORY_THUNDER, SparkFlowTaskFactory.PREF_KEY_USER_NAME).getValue());
				passwordText.setText(JqLib.decodePassword(storage.getPreferenceByCateAndName(SparkFlowTaskFactory.PREF_CATEGORY_THUNDER, SparkFlowTaskFactory.PREF_KEY_PASSWORD).getValue()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
