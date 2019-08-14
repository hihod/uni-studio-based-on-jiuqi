package com.jiuqi.rpa.widgets.extract;

import com.jiuqi.etl.rpa.runtime.ExtractedData;
import com.jiuqi.etl.rpa.runtime.extract.structureddata.StructuredDataTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.json.JSONObject;

public class TableSelectorWizard extends Wizard implements INewWizard {

	WelcomeWizardPage pageWelcome1;
	WelcomeWizardPage pageWelcome2;
	StandardHTMLTableWizardPage pageStandardHTMLTable;
	TableDisplayWizardPage pageTableDisplay;
	ColumnDefineWizardPage pageColumnDefine;
	ColumnEditWizardPage pageColumnEdit;
	StructuredDataMapping mappingPage;

	boolean reselect = false;
	boolean rowValidate = true;
	TaskContext context;

	public TaskContext getContext() {
		return context;
	}

	StructuredDataTaskModel cloned_model;

	public TableSelectorWizard(TaskContext context) {
		super();
		
		setWindowTitle("表格提取向导");
		this.context = context;
		this.cloned_model = ((StructuredDataTaskModel) context.getTaskModel()).clone();

		pageWelcome1 = new WelcomeWizardPage("Welcome1", "表格提取-开始",null, false);
		this.addPage(pageWelcome1);

		pageStandardHTMLTable = new StandardHTMLTableWizardPage("StandardHTMLTable", "表格提取-标准HTML表格",null);
		this.addPage(pageStandardHTMLTable);

		pageWelcome2 = new WelcomeWizardPage("Welcome2", "表格提取-选择第二个元素",null, true);
		this.addPage(pageWelcome2);

		pageColumnDefine = new ColumnDefineWizardPage("ColumnDefine", "表格提取-定义列",null);
		this.addPage(pageColumnDefine);

		pageColumnEdit = new ColumnEditWizardPage("ColumnEdit", "表格提取-编辑列",null);
		this.addPage(pageColumnEdit);

		pageTableDisplay = new TableDisplayWizardPage("TableDisplay", "表格提取-提取结果",null);
		this.addPage(pageTableDisplay);

		mappingPage = new StructuredDataMapping("映射字段", context, cloned_model);
		this.addPage(mappingPage);
	}

	public TableSelectorWizard(ExtractedData extData, TaskContext context) {
		super();
		setWindowTitle("表格提取向导");
		this.context = context;
		this.cloned_model = ((StructuredDataTaskModel) context.getTaskModel()).clone();
		pageColumnEdit=new ColumnEditWizardPage("ColumnEdit", "表格提取-编辑列", null,extData);
		this.addPage(pageColumnEdit);
		
		pageTableDisplay = new TableDisplayWizardPage("TableDisplay", "表格提取-提取结果", cloned_model,null);
		this.addPage(pageTableDisplay);

		mappingPage = new StructuredDataMapping("映射字段", context, cloned_model);
		this.addPage(mappingPage);

		pageWelcome1=new WelcomeWizardPage("Welcome1", "表格提取-开始", null,false);
		this.addPage(pageWelcome1);

		pageStandardHTMLTable=new StandardHTMLTableWizardPage("StandardHTMLTable", "表格提取-标准HTML表格", null);
		this.addPage(pageStandardHTMLTable);
			
		pageWelcome2=new WelcomeWizardPage("Welcome2", "表格提取-选择第二个元素",null,true);
		this.addPage(pageWelcome2);
				
		pageColumnDefine=new ColumnDefineWizardPage("ColumnDefine", "表格提取-定义列", null);
		this.addPage(pageColumnDefine);	


	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canFinish() {
		if (this.getContainer().getCurrentPage() == mappingPage)
			return true;
		else
			return false;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (this.getContainer().getCurrentPage() == pageStandardHTMLTable && pageStandardHTMLTable.isFullTable()) {
			return pageColumnEdit;
		}else if (this.getContainer().getCurrentPage() == pageColumnEdit && (pageColumnEdit.isAddColumn() || pageColumnEdit.isReselectColumn() || pageColumnEdit.getIsClear())) {
			pageWelcome1.setPreviousPage(null);
			return pageWelcome1;
		}else if (this.getContainer().getCurrentPage() == pageColumnDefine) {
			return pageColumnEdit;
		}else if (this.getContainer().getCurrentPage() == pageWelcome1
				&& (pageColumnEdit.isAddColumn() || pageColumnEdit.isReselectColumn())) {
			return pageWelcome2;
		}else if (this.getContainer().getCurrentPage() == pageWelcome1 && !pageWelcome1.isStandardHTMLTable()) {
			return pageWelcome2;
		}else if (this.getContainer().getCurrentPage() == pageColumnEdit) {
			pageColumnEdit.setPreviousPage(null);
			reselect = false;
			rowValidate = true;
			return super.getNextPage(page);
		}else if (this.getContainer().getCurrentPage() == pageWelcome2) {
			this.getContainer().getCurrentPage().setPreviousPage(pageWelcome1);
			if (pageColumnEdit.isReselectColumn() || reselect) {
				// 重选过来的，不到定义列界面
				reselect = true;
				pageColumnEdit.setIsReselectcolumn(false);
				return pageColumnEdit;
			}else if (!pageWelcome2.isRowValidate() || !rowValidate) {
				// pageWelcome2.setIsRowValidate(true);
				rowValidate = false;
				return pageColumnEdit;// 新列不合法，不能定义列，直接跳转到列编辑，未实现
			}else {
				return super.getNextPage(page);
			}
		}

		else if (this.getContainer().getCurrentPage() == mappingPage) {
			ExtractedData exData = new ExtractedData(pageTableDisplay.getExtractMetaString());
			JSONObject jsonObj = ExtractedData.getJSONObject(exData.getColumnsList(), exData.getRowPath(), exData.getColumnsPath());
			cloned_model.setExtractMetaData(jsonObj.toString());
			mappingPage.buildTableAndLink();
			return super.getNextPage(page);
		} else {
			return super.getNextPage(page);
		}

	}

	@Override
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		final WizardDialog dialog = (WizardDialog) getContainer();

		dialog.addPageChangingListener(new IPageChangingListener() {

			@Override
			public void handlePageChanging(PageChangingEvent pagechangingevent) {
			}
		});

		dialog.addPageChangedListener(new IPageChangedListener() {
			public void pageChanged(PageChangedEvent event) {
				getNextPage(dialog.getCurrentPage());
			}
		});
	}

	@Override
	public boolean performFinish() {

		StructuredDataTaskModel curr_model = (StructuredDataTaskModel) context.getTaskModel();
		curr_model.setOutputParamName(cloned_model.getOutputParamName());
		curr_model.setExtractMetaData(cloned_model.getExtractMetaData());
		curr_model.getFields().clear();
		curr_model.getFields().addAll(cloned_model.getFields());
		// TODO 赋值
		System.out.println("finish!");
		return true;
	}

}
