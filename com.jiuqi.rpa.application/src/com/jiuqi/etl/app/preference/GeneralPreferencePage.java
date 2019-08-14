/*
 * 北京久其软件有限公司 版权所有(c) 2009
 * 文件所含类：ETLGeneralPreferencePage
 * 作者：	宋雨明 	软件研究院组件部
 * 修改记录：
 * 2009-5-22 	宋雨明 	创建文件
 */
package com.jiuqi.etl.app.preference;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;

import com.jiuqi.etl.app.ApplicationPlugin;
import com.jiuqi.etl.ui.editor.ETLEditorPlugin;


/**
 * 类名：ETL常规选项页<p>
 * 描述：该页用于显示和编辑ETL客户端的常规选项页。
 */
public class GeneralPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
	public static final String REMEMBER_PASSWORD = "rememberPassword";
	
	
	private Button limitEditorCountBtn;
	private Label editorCountLable;
	private Spinner editorCountSpinner;
	private Button showHeapStatusBtn;
	private Button rememberPasswordBtn;
	
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
		showHeapStatusBtn = new Button(composite, SWT.CHECK);
		showHeapStatusBtn.setText("显示内存使用情况");
		showHeapStatusBtn.setSelection(PlatformUI.getPreferenceStore().getBoolean(
		        IWorkbenchPreferenceConstants.SHOW_MEMORY_MONITOR));
		
		rememberPasswordBtn = new Button(composite, SWT.CHECK);
		rememberPasswordBtn.setText("记住登陆密码");
		rememberPasswordBtn.setSelection(ApplicationPlugin.getDefault().getPreferenceStore().getBoolean(
		        REMEMBER_PASSWORD));
		
		limitEditorCountBtn = new Button(composite, SWT.CHECK);
		limitEditorCountBtn.setText("限制打开的编辑器数目");
		limitEditorCountBtn.setSelection(ETLEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				ETLEditorPlugin.PREFERENCE_LIMIT_EDITOR_COUNT));
		limitEditorCountBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				editorCountLable.setEnabled(limitEditorCountBtn.getSelection());
				editorCountSpinner.setEnabled(limitEditorCountBtn.getSelection());
			}
		});
		
		Composite comp = new Composite(composite, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));
		editorCountLable = new Label(comp, SWT.NONE);
		editorCountLable.setText("最多打开的编辑器数目：");
		editorCountLable.setEnabled(limitEditorCountBtn.getSelection());
		editorCountSpinner = new Spinner(comp, SWT.BORDER);
		int count = ETLEditorPlugin.getDefault().getPreferenceStore().getInt(ETLEditorPlugin.PREFERENCE_EDITOR_COUNT);
		editorCountSpinner.setValues(count == 0 ? 64 : count, 5, 200, 0, 1, 5);
		editorCountSpinner.setEnabled(limitEditorCountBtn.getSelection());
	}
	
	@Override
	protected void performDefaults()
	{
		showHeapStatusBtn.setSelection(PlatformUI.getPreferenceStore().getBoolean(
		        IWorkbenchPreferenceConstants.SHOW_MEMORY_MONITOR));
		rememberPasswordBtn.setSelection(ApplicationPlugin.getDefault().getPreferenceStore()
		        .getBoolean(REMEMBER_PASSWORD));
		limitEditorCountBtn.setSelection(ETLEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				ETLEditorPlugin.PREFERENCE_LIMIT_EDITOR_COUNT));
		editorCountLable.setEnabled(limitEditorCountBtn.getSelection());
		editorCountSpinner.setEnabled(limitEditorCountBtn.getSelection());
		editorCountSpinner.setSelection(ETLEditorPlugin.getDefault().getPreferenceStore().getInt(
				ETLEditorPlugin.PREFERENCE_EDITOR_COUNT));
		super.performDefaults();
	}
	
	@Override
	public boolean performOk()
	{
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_MEMORY_MONITOR,
		        showHeapStatusBtn.getSelection());
		ApplicationPlugin.getDefault().getPreferenceStore().setValue(REMEMBER_PASSWORD,
		        rememberPasswordBtn.getSelection());
		ETLEditorPlugin.getDefault().getPreferenceStore().setValue(ETLEditorPlugin.PREFERENCE_LIMIT_EDITOR_COUNT, 
				limitEditorCountBtn.getSelection());
		ETLEditorPlugin.getDefault().getPreferenceStore().setValue(ETLEditorPlugin.PREFERENCE_EDITOR_COUNT, 
				editorCountSpinner.getSelection());
		updateHeapStatus(showHeapStatusBtn.getSelection());
		return super.performOk();
	}
	
    private void updateHeapStatus(boolean selection)
	{
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++)
		{
			IWorkbenchWindow window = windows[i];
			if (window instanceof WorkbenchWindow)
			{
				((WorkbenchWindow) window).showHeapStatus(selection);
			}
		}
	}
}
