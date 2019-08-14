package com.jiuqi.rpa.widgets.extract;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.json.JSONException;

import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.etl.rpa.runtime.ExtractedData;
import com.jiuqi.etl.ui.view.parameter.AutoResizeTableLayout;
import com.jiuqi.rpa.lib.find.Path;

public class ColumnEditWizardPage extends WizardPage {

	private Table table;
	private TableViewer tableViewer;
	private Text text;
	private Button addButton, deleteButton, moveupButton, movedownButton,clearButton;
	private List<ColumnEntity> allParams = new ArrayList<ColumnEntity>();
	private String[] columnName = { "name", "title", "defaultValue" };
	private String colText;
	private boolean isAddColumn = false;
	private boolean isReselectColumn = false;
	private boolean isReconfig = false;
	private boolean isClear=false;
	private ArrayList<ArrayList<String>> columnStrings;
	ArrayList<String> listSub = new ArrayList<String>();
	List<Path> allColumnPathList = new ArrayList<Path>();
	private TableEditor[] te;
	private Link[] btn;
	private ExtractedData extractedData;

	protected ColumnEditWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	// 重载构造方法，用于已配置过的情况
	protected ColumnEditWizardPage(String pageName, String title, ImageDescriptor titleImage, ExtractedData extData) {
		super(pageName, title, titleImage);
		isReconfig = true;
		extractedData = extData;
		putExtractedData(extractedData);
	}

	@Override
	public void dispose() {
		//重写dispose方法使control==null，以便在page切换时重新执行createControl刷新数据内容
		super.dispose();
		setControl(null);
	}

	@Override
	public void createControl(Composite parent) {
		isAddColumn = false;
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(6, false);
		
		final GridLayout gd = new GridLayout(1, false);
		gd.marginBottom = 50;
		gd.marginTop = 50;
		gd.marginLeft = 50;
		gd.marginRight = 50;

		final FillLayout fillLayout = new FillLayout(SWT.NONE);
		composite.setLayout(fillLayout);
		setControl(composite);

		final TabFolder tabFolder = new TabFolder(composite, SWT.BOTTOM);
		tabFolder.setLayout(fillLayout);

		//配置页签
		TabItem tabItem1 = new TabItem(tabFolder, SWT.NULL);
		tabItem1.setText("配置");

		//文本页签
		TabItem tabItem2 = new TabItem(tabFolder, SWT.NULL);
		tabItem2.setText("文本");
		tabFolder.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				//更新xml文本的显示
				if (!isReconfig)
					colText = ((TableSelectorWizardDialog) ColumnEditWizardPage.this.getContainer()).getxmlText();
				text.setText(colText);
				
				//更新表格中的列信息
				tableViewer.setInput(allParams);
				setButtonControl(allParams.size());
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
			}
		});

		final Composite composite1 = new Composite(tabFolder, SWT.NONE);
		composite1.setLayout(gridLayout);

		tableViewer = new TableViewer(composite1, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 10));

		// 四列填充整个table
		AutoResizeTableLayout resizeLayout = new AutoResizeTableLayout(table);
		table.setLayout(resizeLayout);

		// 名称列
		resizeLayout.addColumnData(new ColumnWeightData(20));
		TableColumn nameCol = new TableColumn(table, SWT.NONE);
		nameCol.setText("名称");

		// 标题列
		resizeLayout.addColumnData(new ColumnWeightData(20));
		TableColumn titleCol = new TableColumn(table, SWT.NONE);
		titleCol.setText("标题");

		// 操作列
		resizeLayout.addColumnData(new ColumnWeightData(20));
		TableColumn OperationCol = new TableColumn(table, SWT.NONE);
		OperationCol.setText("操作");

		//不是二次配置，从WizardDialog中读取Column信息，否则allParams在构造函数中被赋值
		if (!isReconfig)
			allParams = ((TableSelectorWizardDialog) this.getContainer()).getColumnEntityList();
		
		//填充表格内容
		tableViewer.setColumnProperties(columnName);
		tableViewer.setContentProvider(new TableViewerContentProvider());
		tableViewer.setLabelProvider(new TableViewerLabelProvider());
		tableViewer.setInput(allParams);

		//名称、标题列为editText
		CellEditor[] cellEditor = new CellEditor[2];
		cellEditor[0] = new TextCellEditor(table, SWT.SINGLE | SWT.BORDER);
		cellEditor[1] = new TextCellEditor(table,SWT.SINGLE | SWT.BORDER);
		tableViewer.setCellEditors(cellEditor);
		tableViewer.setCellModifier(new CellModifier(tableViewer));
		
		//画 ‘重选’ 列
		setButtonControl(tableViewer.getTable().getItemCount());
		
		//“添加”按钮
		addButton = new Button(composite1, SWT.BUTTON1);
		addButton.setText("添加一列");
		addButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				addColumn();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		//“删除”按钮
		deleteButton = new Button(composite1, SWT.BUTTON1);
		deleteButton.setText("删除该列");
		deleteButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				deleteColumn();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		//“上移”按钮
		moveupButton = new Button(composite1, SWT.BUTTON1);
		moveupButton.setText("上移该列");
		moveupButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				moveupColumn();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		//“下移”按钮
		movedownButton = new Button(composite1, SWT.BUTTON1);
		movedownButton.setText("下移该列");
		movedownButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				movedownColumn();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		//"清除重选"按钮
		clearButton = new Button(composite1, SWT.BUTTON1);
		clearButton.setText("清除重选");
		clearButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				clearAllandSelect();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});
		
		//文本界面，显示xml文本
		text = new Text(tabFolder, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL|SWT.H_SCROLL);
		
		//如果不是二次配置，从WizardDialog里读取xml文本，否则colText在构造函数中被赋值
		if (!isReconfig)
			colText = ((TableSelectorWizardDialog) this.getContainer()).getxmlText();

		text.setText(colText);
		text.setEditable(true);
		//实时获取text文本内容，以便检查及更新信息
		text.addModifyListener(new PageModifyListener());

		tabItem1.setControl(composite1);
		tabItem2.setControl(text);
	}

	/**
	 * 点击“下移”按钮后更新信息
	 */
	protected void movedownColumn() {
		if (isReconfig) {
			setExtractedData(extractedData);
		}
		
		int oldPosition;
		StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		
		ColumnEntity model = (ColumnEntity) selection.getFirstElement();
		oldPosition = allParams.indexOf(model);
		if (oldPosition == allParams.size() - 1) {
			return;
		}
		
		//更新列信息
		allParams.set(oldPosition, allParams.get(oldPosition + 1));
		allParams.set(oldPosition + 1, model);
		tableViewer.setInput(allParams);
		((TableSelectorWizardDialog) this.getContainer()).setColumnEntityList(allParams);

//		//更新列展示内容，已废弃
//		if (!isReconfig) {
//			columnStrings = ((TableSelectorWizardDialog) this.getContainer()).getColumnStrings();
//			if (columnStrings.size() > 0) {
//				listSub = columnStrings.get(oldPosition);
//				columnStrings.remove(oldPosition);
//				columnStrings.add(oldPosition + 1, listSub);
//				((TableSelectorWizardDialog) this.getContainer()).setColumnStrings(columnStrings);
//			}
//		}

		//更新列Path
		allColumnPathList = ((TableSelectorWizardDialog) this.getContainer()).getAllColumnPath();
		Path col = allColumnPathList.get(oldPosition);
		allColumnPathList.remove(oldPosition);
		allColumnPathList.add(oldPosition + 1, col);
		((TableSelectorWizardDialog) this.getContainer()).setAllColumnPath(allColumnPathList);

		//更新xml文本
		colText = ((TableSelectorWizardDialog) this.getContainer()).getxmlText();
		text.setText(colText);

	}

	/**
	 * 点击“上移”按钮后更新信息
	 */
	protected void moveupColumn() {
		if (isReconfig) {
			setExtractedData(extractedData);
		}

		int oldPosition;
		StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		
		ColumnEntity model = (ColumnEntity) selection.getFirstElement();
		oldPosition = allParams.indexOf(model);
		if (oldPosition == 0) {
			return;
		}
		
		//更新列信息
		allParams.set(oldPosition, allParams.get(oldPosition - 1));
		allParams.set(oldPosition - 1, model);
		tableViewer.setInput(allParams);
		((TableSelectorWizardDialog) this.getContainer()).setColumnEntityList(allParams);

		//更新列展示内容，已废弃
//		if (!isReconfig) {
//			columnStrings = ((TableSelectorWizardDialog) this.getContainer()).getColumnStrings();
//			if (columnStrings.size() > 0) {
//				listSub = columnStrings.get(oldPosition);
//				columnStrings.remove(oldPosition);
//				columnStrings.add(oldPosition - 1, listSub);
//				((TableSelectorWizardDialog) this.getContainer()).setColumnStrings(columnStrings);
//			}
//		}

		//更新列Path
		if (isReconfig)
			allColumnPathList = ((TableSelectorWizardDialog) this.getContainer()).getAllColumnPath();
		Path col = allColumnPathList.get(oldPosition);
		allColumnPathList.remove(oldPosition);
		allColumnPathList.add(oldPosition - 1, col);
		((TableSelectorWizardDialog) this.getContainer()).setAllColumnPath(allColumnPathList);

		//更新xml文本
		colText = ((TableSelectorWizardDialog) this.getContainer()).getxmlText();
		text.setText(colText);
	}

    /**
     * 点击“重选”按钮后，通知WizardDialog删除所选列信息，并记录位置，以便重选完成后插入到原位置（列信息不变，Path和展示内容变）
     */
	protected void reselectColumn() {
		if (isReconfig) {
			setExtractedData(extractedData);
		}

		StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		
		ColumnEntity model = (ColumnEntity) selection.getFirstElement();
		((TableSelectorWizardDialog) this.getContainer()).setIndex((allParams.lastIndexOf(model)));
		isReselectColumn = true;
		TableSelectorWizardDialog dialog = (TableSelectorWizardDialog) getContainer();
		dialog.buttonPressed(IDialogConstants.NEXT_ID);
		isReconfig = false;
	}

	/**
	 * 点击“删除”按钮后，更新信息
	 */
	protected void deleteColumn() {
		if (isReconfig) {
			setExtractedData(extractedData);
		}

		StructuredSelection selection = (StructuredSelection) tableViewer.getSelection();
		if (selection.isEmpty()) {
			return;
		}
		
		ColumnEntity model = (ColumnEntity) selection.getFirstElement();
		int index = allParams.lastIndexOf(model);

		//不是二次配置，才需要更新列展示内容，否则列展示内容为空，不处理
		if (!isReconfig) {
			columnStrings = ((TableSelectorWizardDialog) this.getContainer()).getColumnStrings();
			if (columnStrings.size() > 0) {
				columnStrings.remove(index);
				((TableSelectorWizardDialog) this.getContainer()).setColumnStrings(columnStrings);
			}
		}

		if (isReconfig)
			((TableSelectorWizardDialog) this.getContainer()).setRowPath(extractedData.getRowPath());

		//更新列信息
		allParams.remove(model);
		tableViewer.setInput(allParams);
		((TableSelectorWizardDialog) this.getContainer()).setColumnEntityList(allParams);

		if (!isReconfig)
			allColumnPathList = ((TableSelectorWizardDialog) this.getContainer()).getAllColumnPath();
		
		//更新列Path
		allColumnPathList.remove(index);
		((TableSelectorWizardDialog) this.getContainer()).setAllColumnPath(allColumnPathList);

		//更新xml文本
		colText = ((TableSelectorWizardDialog) this.getContainer()).getxmlText();
		text.setText(colText);

		//更新“重选”按钮个数
		setButtonControl(allParams.size());
		isReconfig = false;
	}

	/**
	 * 点击“添加”按钮后，跳转回第一页，引导添加新列
	 */
	protected void addColumn() {
		if (isReconfig) {
			setExtractedData(extractedData);
		}

		isAddColumn = true;
		TableSelectorWizardDialog dialog = (TableSelectorWizardDialog) getContainer();
		dialog.buttonPressed(IDialogConstants.NEXT_ID);
		isReconfig = false;
	}
	
	/**
	 * 点击“清除重选”按钮后，删除所有当前列信息，跳转回第一页
	 */
	protected void clearAllandSelect() {
		isClear=true;
		TableSelectorWizardDialog dialog = (TableSelectorWizardDialog) getContainer();
		
		//列Path
		allColumnPathList=dialog.getAllColumnPath();
		allColumnPathList.clear();
		dialog.setAllColumnPath(allColumnPathList);
		
		//列信息
		allParams=dialog.getColumnEntityList();
		allParams.clear();
		dialog.setColumnEntityList(allParams);
		
//		//列展示内容，已废弃
//		columnStrings=dialog.getColumnStrings();
//		columnStrings.clear();
//		dialog.setColumnStrings(columnStrings);
		
		//rowPath
		dialog.setCurRowPath("");
		dialog.setRowPath(null);
		dialog.buttonPressed(IDialogConstants.NEXT_ID);
		isClear=false;
		isReconfig=false;
	}

	/**
	 * 重画“重选”按钮
	 * @param 表格行数
	 */
	protected void setButtonControl(int rowCount) {
		
		//先删除所有TableEditor和“重选”按钮
		if (te != null) {
			for (TableEditor TE : te) {
				TE.dispose();
			}

			for (Link BTN : btn) {
				BTN.dispose();
			}
		}

		TableEditor[] reselectEditors = new TableEditor[rowCount];
		Link[] reselectButtons = new Link[rowCount];
		TableItem[] items = tableViewer.getTable().getItems();

		for (int i = 0; i < rowCount; i++) {

			reselectEditors[i] = new TableEditor(table);
			reselectButtons[i] = new Link(table, SWT.PUSH);

			reselectButtons[i].setText("<A>重选</A>");
			reselectButtons[i].setToolTipText(String.valueOf(i));
			reselectButtons[i].computeSize(SWT.DEFAULT, table.getItemHeight());

			reselectEditors[i].grabHorizontal = true;
			reselectEditors[i].minimumHeight = reselectButtons[i].getSize().y;
			reselectEditors[i].minimumWidth = reselectButtons[i].getSize().x;

			reselectEditors[i].setEditor(reselectButtons[i], items[i], 2);

			reselectButtons[i].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					int t = Integer.parseInt(((Link) event.getSource()).getToolTipText());
					tableViewer.getTable().setSelection(t);
					reselectColumn();
				}
			});
		}
		te = reselectEditors;
		btn = reselectButtons;
	}

	/**
	 * 添加一列
	 * @param 列名称
	 * @param 列标题
	 */
	public void addColumnInfo(String name, String title) {
		allParams.add(new ColumnEntity(name, title));
	}

	/**
	 * 判断是否点击了添加按钮以添加列
	 * @return isAddColumn
	 */
	public boolean isAddColumn() {
		return isAddColumn;
	}

	/**
	 * 判断是否点击了重选按钮以重选列
	 * @return isReselectColumn
	 */
	public boolean isReselectColumn() {
		return isReselectColumn;
	}

	/**
	 * 设置是否为添加列
	 * @param flag
	 */
	public void setIsAddcolumn(boolean flag) {
		isAddColumn = flag;
	}

	/**
	 * 设置是否为重选列
	 * @param flag
	 */
	public void setIsReselectcolumn(boolean flag) {
		isReselectColumn = flag;
	}

	/**
	 * 判断是否为二次配置
	 * @return isReconfig
	 */
	public boolean getIsReconfig() {
		return isReconfig;
	}

	/**
	 * 设置是否为二次配置
	 * @param flag
	 */
	public void setIsReconfig(boolean flag) {
		isReconfig = flag;
	}

	/**
	 * 判断是否为清除所有列并重选
	 */
	public boolean getIsClear() {
		return isClear;
	}
	
	/**
	 * 设置是否为清除所有列并重选
	 */
	public void setIsClear(boolean flag) {
		isClear=flag;
	}
	
	/**
	 * 二次配置时，以ExtractedData内容更新WizardDialog中的信息
	 * @param ExtractedData
	 */
	public void setExtractedData(ExtractedData extData) {

		// 更新列信息
		ArrayList<DataTableField> allColumnPathList = extData.getColumnsList();
		ArrayList<ColumnEntity> colEntities = new ArrayList<ColumnEntity>();
		for (DataTableField ColumnPath : allColumnPathList) {
			colEntities.add(new ColumnEntity(ColumnPath.getName(),ColumnPath.getTitle()));
		}
		((TableSelectorWizardDialog) ColumnEditWizardPage.this.getContainer()).setColumnEntityList(colEntities);

		//更新rowPath
		String curRowPath = "";
		try {
			curRowPath = extData.getRowPath().toJson().toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		((TableSelectorWizardDialog) ColumnEditWizardPage.this.getContainer()).setCurRowPath(curRowPath);
		((TableSelectorWizardDialog) ColumnEditWizardPage.this.getContainer()).setRowPath(extData.getRowPath());
		try {
			((TableSelectorWizardDialog) ColumnEditWizardPage.this.getContainer()).setCurRowPath(extData.getRowPath().toJson().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//更新列Path
		((TableSelectorWizardDialog) ColumnEditWizardPage.this.getContainer()).setAllColumnPath(extData.getColumnsPath());
	}

	/**
	 * 获取ExtractedData
	 * @return
	 */
	public ExtractedData getExtractedData() {
		return extractedData;
	}

	/**
	 * 二次配置时，以ExtractedData内容更新此page页中的信息
	 * @param extData
	 */
	public void putExtractedData(ExtractedData extData) {

		//更新列信息
		ArrayList<DataTableField> allColumnList = extData.getColumnsList();
		allParams = new ArrayList<ColumnEntity>();
		for (DataTableField ColumnPath : allColumnList) {
			allParams.add(new ColumnEntity(ColumnPath.getName(),ColumnPath.getTitle()));
		}

		//更新列Path
		allColumnPathList = extData.getColumnsPath();

		//更新xml文本
		colText = extData.getText();
	}

	public boolean canFlipToNextPage() {
		return  checkPageFinish();
	}

	private boolean checkPageFinish() {
		
		//尝试解析文本内容为对象，若解析失败，seterrormessage，否则更新对象内容
		String xmlStr=text.getText();
		try {
			
			//解析xml文本
			ExtractedData.calInfo(xmlStr);
			
			//检查重名
			ExtractedData extDataTemp=new ExtractedData(xmlStr);
			ArrayList<DataTableField> tempCols=extDataTemp.getColumnsList();
			
			for(int i=0;i<tempCols.size();i++)
			{
				for(int j=i+1;j<tempCols.size();j++)
				{
					if(tempCols.get(i).getName().equals(tempCols.get(j).getName()))
					{
						setErrorMessage("重复的列名"+tempCols.get(i).getName());
						return false;
					}
				}
			}
			
			if(tempCols.size()==0)
			{
				setErrorMessage("请至少保留一列");
				return false;
			}
			
			setErrorMessage(null);
			return true;
		} catch (Exception e2) {
			setErrorMessage(e2.getMessage());
			return false;
		}
	}

	private class PageModifyListener implements ModifyListener {

		public void modifyText(ModifyEvent e) {
			boolean pageValidate=checkPageFinish();
			setPageComplete(pageValidate);
			if(pageValidate)
			{
				refreshInfo(text.getText());
			}
		}

	}
	
	private void refreshInfo(String xmlStr) {
		extractedData=new ExtractedData(xmlStr);
		putExtractedData(extractedData);
		setExtractedData(extractedData);
		tableViewer.getTable().clearAll();
		tableViewer.setInput(allParams);
		setButtonControl(allParams.size());
	}

}
