/*
 * 北京久其软件有限公司 版权所有(c) 2009
 * 文件所含类：EditorPreferencePage
 * 作者：	宋雨明 	软件研究院组件部
 * 修改记录：
 * 2009-6-2 	宋雨明 	创建文件
 */
package com.jiuqi.etl.app.preference;

import org.eclipse.gef.SnapToGrid;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.jiuqi.etl.ui.editor.ETLEditor;
import com.jiuqi.etl.ui.editor.ETLEditorManagement;
import com.jiuqi.etl.ui.editor.ETLEditorPlugin;

/**
 * 类名：编辑器选项页<p>
 * 描述：该类的对象是选项对话框中的一个选项页，用于展示和设置编辑器可以设置的选项。
 */
public class EditorPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
	private Button snapToGridButton;
//	private Button showGridButton;
	private Label label;
	private Spinner gridSpaceSpinner;
	private Spinner spinner;
	private Label label2;
	private Button autoVerifyButton;
	
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
		IPreferenceStore prefStore = ETLEditorPlugin.getDefault().getPreferenceStore();
		boolean snapToGrid = prefStore.getBoolean(SnapToGrid.PROPERTY_GRID_ENABLED);
//		boolean gridVisible = prefStore.getBoolean(SnapToGrid.PROPERTY_GRID_VISIBLE);
		int gridSpace = prefStore.getInt(SnapToGrid.PROPERTY_GRID_SPACING);
		boolean autoVerify = prefStore.getBoolean(ETLEditor.AUTO_VERIFY);
		int maxHistorySize = prefStore.getInt(ETLEditorPlugin.MAX_HISTORY_SIZE);
		
		snapToGridButton = new Button(composite, SWT.CHECK);
		snapToGridButton.setText("网格对齐");
		snapToGridButton.setSelection(snapToGrid);
		
//		showGridButton = new Button(composite, SWT.CHECK);
//		showGridButton.setText("显示网格");
//		showGridButton.setSelection(gridVisible);
		
		Composite gridSpacePanel = new Composite(composite, SWT.NONE);
		GridLayout gridSpacePanel_layout = new GridLayout(3, false);
		gridSpacePanel_layout.marginTop = 0;
		gridSpacePanel_layout.marginBottom = 0;
		gridSpacePanel_layout.verticalSpacing = 0;
		gridSpacePanel.setLayout(gridSpacePanel_layout);
		label = new Label(gridSpacePanel, SWT.NONE);
		label.setText("网格大小：");
		gridSpaceSpinner = new Spinner(gridSpacePanel, SWT.BORDER);
		gridSpaceSpinner.setValues(gridSpace, 1, 50, 0, 1, 5);
		label2 = new Label(gridSpacePanel, SWT.NONE);
		label2.setText("像素");
		
		Composite gridPanel = new Composite(composite, SWT.NONE);
		GridLayout gridPanel_layout = new GridLayout(2, false);
		gridPanel_layout.marginTop = 0;
		gridPanel_layout.marginBottom = 0;
		gridPanel_layout.verticalSpacing = 0;
		gridPanel.setLayout(gridPanel_layout);
		label = new Label(gridPanel, SWT.NONE);
		label.setText("最大历史记录数：");
		spinner = new Spinner(gridPanel, SWT.BORDER);
		int size = maxHistorySize == 0 ? 10 : maxHistorySize;
		spinner.setValues(size, 5, 200, 0, 1, 5);
		ETLEditorManagement.setMaxHistorySize(size);
		
		autoVerifyButton = new Button(composite, SWT.CHECK);
		autoVerifyButton.setText("自动检查错误");
		autoVerifyButton.setSelection(autoVerify);
    }
	
	@Override
	protected void performDefaults()
	{
		IPreferenceStore prefStore = ETLEditorPlugin.getDefault().getPreferenceStore();
		boolean snapToGrid = prefStore.getBoolean(SnapToGrid.PROPERTY_GRID_ENABLED);
//		boolean gridVisible = prefStore.getBoolean(SnapToGrid.PROPERTY_GRID_VISIBLE);
		int gridSpace = prefStore.getInt(SnapToGrid.PROPERTY_GRID_SPACING);
		boolean autoVerify = prefStore.getBoolean(ETLEditor.AUTO_VERIFY);
		snapToGridButton.setSelection(snapToGrid);
//		showGridButton.setSelection(gridVisible);
		gridSpaceSpinner.setSelection(gridSpace);
		spinner.setSelection(10);
		ETLEditorManagement.setMaxHistorySize(10);
		autoVerifyButton.setSelection(autoVerify);
	    super.performDefaults();
	}
	
	@Override
	public boolean performOk()
	{
		boolean snapToGrid = snapToGridButton.getSelection();
//		boolean gridVisible = showGridButton.getSelection();
		int gridSpace = gridSpaceSpinner.getSelection();
		int sum = spinner.getSelection();
		ETLEditorManagement.setMaxHistorySize(sum);
		boolean autoVerify = autoVerifyButton.getSelection();
		IPreferenceStore prefStore = ETLEditorPlugin.getDefault().getPreferenceStore();
		prefStore.setValue(SnapToGrid.PROPERTY_GRID_ENABLED, snapToGrid);
//		prefStore.setValue(SnapToGrid.PROPERTY_GRID_VISIBLE, gridVisible);
		prefStore.setValue(SnapToGrid.PROPERTY_GRID_SPACING, gridSpace);
		prefStore.setValue(ETLEditor.AUTO_VERIFY, autoVerify);
		prefStore.setValue(ETLEditorPlugin.MAX_HISTORY_SIZE, sum);
		
		//不直接生效，下次打开时生效
//		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		IWorkbenchPage activePage = window.getActivePage();
//		for (IEditorReference editorRef : activePage.getEditorReferences())
//		{
//			ETLEditor editor = (ETLEditor) editorRef.getEditor(false);
//			editor.setGridSnapEnable(snapToGrid);
////			editor.setGridVisible(gridVisible);
//			editor.setGridSpace(gridSpace);
//		}
	    return super.performOk();
	}
}
