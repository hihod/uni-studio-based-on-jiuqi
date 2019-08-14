package com.jiuqi.rpa.widgets.extract;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.etl.rpa.runtime.ExtractedData;
import com.jiuqi.etl.rpa.runtime.extract.structureddata.StructuredDataTaskModel;
import com.jiuqi.etl.ui.view.parameter.AutoResizeTableLayout;
import com.jiuqi.rpa.lib.find.Path;

public class TableDisplayWizardPage extends WizardPage {
	private Table table;
	private TableViewer tableViewer;
	private List<ColumnEntity> allParams = new ArrayList<ColumnEntity>();
	private ArrayList<ArrayList<String>> columnStrings;
	private boolean pageVisible;
	
	protected TableDisplayWizardPage(String pageName, String title,ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	public TableDisplayWizardPage(String pageName, String title, StructuredDataTaskModel cloned_Model,
			ImageDescriptor titleImage) {
		this(pageName, title, titleImage);
	}
	
	public void dispose(){   
		 super.dispose();   
		 setControl(null);   
	} 
	
	@Override
	public void createControl(Composite parent) {
		this.setPageComplete(false);
		final Composite composite = new Composite(parent, SWT.NONE);
		final FillLayout fillLayout=new FillLayout(SWT.HORIZONTAL);
		composite.setLayout(fillLayout);
		
		
		setControl(composite);
		
		tableViewer = new TableViewer(composite,SWT.BORDER|SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		//四列填充整个table
		AutoResizeTableLayout resizeLayout=new AutoResizeTableLayout(table);
		table.setLayout(resizeLayout);
		
		allParams=((TableSelectorWizardDialog)this.getContainer()).getColumnEntityList(); 
		columnStrings=((TableSelectorWizardDialog)this.getContainer()).getColumnStrings();
		//动态生成列
		for(ColumnEntity column:allParams){
			resizeLayout.addColumnData(new ColumnWeightData(20));
			TableColumn col=new TableColumn(table, SWT.NONE);
			col.setText(column.getName());
		}
		
		if(columnStrings.size()>0){
			int maxRowCount=0;
			for(int i=0;i<columnStrings.size();i++){
				if(columnStrings.get(i).size()>maxRowCount){
					maxRowCount=columnStrings.get(i).size();
				}
			}
			
			for (int i = 0; i < maxRowCount; i++){  //i为行数，j为列数
			   TableItem item = new TableItem(table, SWT.NONE);
			   for(int j = 0;j<columnStrings.size();j++){
				   if(columnStrings.get(j).size()<i+1){
					   item.setText(j, "");
				   }else{
					   item.setText(j, columnStrings.get(j).get(i));
				   }
			   }
			} 
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.pageVisible = visible;
		super.setVisible(visible);
	}
	
	public boolean isTableDisplayPageVisible(){
		return pageVisible;
	}
	
	public boolean canFlipToNextPage(){
        return getNextPage() != null;
    }
	public String getExtractMetaString() {
		ArrayList<DataTableField> columnsList = ((TableSelectorWizardDialog)this.getContainer()).getColumnsList();
		Path rowPath = ((TableSelectorWizardDialog)this.getContainer()).getRowPath();
		ArrayList<Path> columnsPathList = ((TableSelectorWizardDialog)this.getContainer()).getColumnsPathList();
		return ExtractedData.getXMLText(columnsList,
				rowPath,columnsPathList);
	}
}
