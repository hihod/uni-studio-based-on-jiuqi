package com.jiuqi.etl.app.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.console.ConsolePlugin;

import com.jiuqi.etl.env.log.Level;
import com.jiuqi.etl.ui.view.console.ETLConsoleFactory;

public class ConslePreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
	private Group logLevelGroup;
	private Button debugLevelBtn;
	private Button warningLevelBtn;
	private Button errorLevelBtn;
	private Button traceLevelBtn;
	private Button clearBeforeExecBtn;
	
	public void init(IWorkbench workbench)
	{
	}
	
	@Override
	protected Control createContents(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createControls(composite);
		applyDialogFont(composite);
		return composite;
	}

	private void createControls(Composite composite)
    {
		boolean clearBeforeExec = ETLConsoleFactory.clearBeforeExec;
		Level level = ETLConsoleFactory.logLevel;
			
		clearBeforeExecBtn = new Button(composite, SWT.CHECK);
		clearBeforeExecBtn.setText("执行前自动清空");
		clearBeforeExecBtn.setSelection(clearBeforeExec);
		
		logLevelGroup = new Group(composite, SWT.NONE);
		logLevelGroup.setText("日志级别");
		logLevelGroup.setLayout(new GridLayout(4, false));
		logLevelGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		logLevelGroup.addListener(SWT.MouseDoubleClick, new Listener(){
			public void handleEvent(Event event) {
				if((event.stateMask & SWT.CTRL) != 0) {
					if (!traceLevelBtn.getVisible()) {
						traceLevelBtn.setVisible(true);
						traceLevelBtn.setSelection(true);
						
						debugLevelBtn.setSelection(false);
						errorLevelBtn.setSelection(false);
						warningLevelBtn.setSelection(false);
					}
				}
			}
		});
		
		errorLevelBtn = new Button(logLevelGroup, SWT.RADIO);
		errorLevelBtn.setText("关键");
		errorLevelBtn.setSelection(Level.ERROR == level);
		
		warningLevelBtn = new Button(logLevelGroup, SWT.RADIO);
		warningLevelBtn.setText("一般");
		warningLevelBtn.setSelection(Level.WARNING == level);
		
		debugLevelBtn = new Button(logLevelGroup, SWT.RADIO);
		debugLevelBtn.setText("详细");
		debugLevelBtn.setSelection(Level.DEBUG == level);

		traceLevelBtn = new Button(logLevelGroup, SWT.RADIO);
		traceLevelBtn.setText("跟踪");
		traceLevelBtn.setSelection(Level.TRACE == level);
		if(Level.TRACE != level)
			traceLevelBtn.setVisible(false);
		
    }
	
	@Override
	protected void performDefaults()
	{
		clearBeforeExecBtn.setSelection(ETLConsoleFactory.clearBeforeExec);
		Level	level = ETLConsoleFactory.logLevel;
		traceLevelBtn.setSelection(level == Level.TRACE);
		debugLevelBtn.setSelection(level == Level.DEBUG);
		warningLevelBtn.setSelection(level == Level.WARNING);
		errorLevelBtn.setSelection(level == Level.ERROR);
		
		super.performDefaults();
	}
	
	@Override
	public boolean performOk()
	{
		IPreferenceStore store = ConsolePlugin.getDefault().getPreferenceStore();
		
		boolean clearBeforeExec = clearBeforeExecBtn.getSelection();
		Level	logLevel = Level.WARNING;
		if (traceLevelBtn.getSelection())
			logLevel = Level.TRACE;
		else if (debugLevelBtn.getSelection())
			logLevel = Level.DEBUG;
		else if (warningLevelBtn.getSelection())
			logLevel = Level.WARNING;
		else if (errorLevelBtn.getSelection())
			logLevel = Level.ERROR;
		
		store.setValue(ETLConsoleFactory.CLEAR_BEFORE_EXEC, clearBeforeExec);
		store.setValue(ETLConsoleFactory.LOG_LEVEL, logLevel.value());

		ETLConsoleFactory.setClearBeforeExec(clearBeforeExec);
		ETLConsoleFactory.setLogLevel(logLevel);
		
		return super.performOk();
	}
}
