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

	// ���ع��췽�������������ù������
	protected ColumnEditWizardPage(String pageName, String title, ImageDescriptor titleImage, ExtractedData extData) {
		super(pageName, title, titleImage);
		isReconfig = true;
		extractedData = extData;
		putExtractedData(extractedData);
	}

	@Override
	public void dispose() {
		//��дdispose����ʹcontrol==null���Ա���page�л�ʱ����ִ��createControlˢ����������
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

		//����ҳǩ
		TabItem tabItem1 = new TabItem(tabFolder, SWT.NULL);
		tabItem1.setText("����");

		//�ı�ҳǩ
		TabItem tabItem2 = new TabItem(tabFolder, SWT.NULL);
		tabItem2.setText("�ı�");
		tabFolder.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent arg0) {
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				//����xml�ı�����ʾ
				if (!isReconfig)
					colText = ((TableSelectorWizardDialog) ColumnEditWizardPage.this.getContainer()).getxmlText();
				text.setText(colText);
				
				//���±���е�����Ϣ
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

		// �����������table
		AutoResizeTableLayout resizeLayout = new AutoResizeTableLayout(table);
		table.setLayout(resizeLayout);

		// ������
		resizeLayout.addColumnData(new ColumnWeightData(20));
		TableColumn nameCol = new TableColumn(table, SWT.NONE);
		nameCol.setText("����");

		// ������
		resizeLayout.addColumnData(new ColumnWeightData(20));
		TableColumn titleCol = new TableColumn(table, SWT.NONE);
		titleCol.setText("����");

		// ������
		resizeLayout.addColumnData(new ColumnWeightData(20));
		TableColumn OperationCol = new TableColumn(table, SWT.NONE);
		OperationCol.setText("����");

		//���Ƕ������ã���WizardDialog�ж�ȡColumn��Ϣ������allParams�ڹ��캯���б���ֵ
		if (!isReconfig)
			allParams = ((TableSelectorWizardDialog) this.getContainer()).getColumnEntityList();
		
		//���������
		tableViewer.setColumnProperties(columnName);
		tableViewer.setContentProvider(new TableViewerContentProvider());
		tableViewer.setLabelProvider(new TableViewerLabelProvider());
		tableViewer.setInput(allParams);

		//���ơ�������ΪeditText
		CellEditor[] cellEditor = new CellEditor[2];
		cellEditor[0] = new TextCellEditor(table, SWT.SINGLE | SWT.BORDER);
		cellEditor[1] = new TextCellEditor(table,SWT.SINGLE | SWT.BORDER);
		tableViewer.setCellEditors(cellEditor);
		tableViewer.setCellModifier(new CellModifier(tableViewer));
		
		//�� ����ѡ�� ��
		setButtonControl(tableViewer.getTable().getItemCount());
		
		//����ӡ���ť
		addButton = new Button(composite1, SWT.BUTTON1);
		addButton.setText("���һ��");
		addButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				addColumn();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		//��ɾ������ť
		deleteButton = new Button(composite1, SWT.BUTTON1);
		deleteButton.setText("ɾ������");
		deleteButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				deleteColumn();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		//�����ơ���ť
		moveupButton = new Button(composite1, SWT.BUTTON1);
		moveupButton.setText("���Ƹ���");
		moveupButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				moveupColumn();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		//�����ơ���ť
		movedownButton = new Button(composite1, SWT.BUTTON1);
		movedownButton.setText("���Ƹ���");
		movedownButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				movedownColumn();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});

		//"�����ѡ"��ť
		clearButton = new Button(composite1, SWT.BUTTON1);
		clearButton.setText("�����ѡ");
		clearButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				clearAllandSelect();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent selectionevent) {
			}
		});
		
		//�ı����棬��ʾxml�ı�
		text = new Text(tabFolder, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL|SWT.H_SCROLL);
		
		//������Ƕ������ã���WizardDialog���ȡxml�ı�������colText�ڹ��캯���б���ֵ
		if (!isReconfig)
			colText = ((TableSelectorWizardDialog) this.getContainer()).getxmlText();

		text.setText(colText);
		text.setEditable(true);
		//ʵʱ��ȡtext�ı����ݣ��Ա��鼰������Ϣ
		text.addModifyListener(new PageModifyListener());

		tabItem1.setControl(composite1);
		tabItem2.setControl(text);
	}

	/**
	 * ��������ơ���ť�������Ϣ
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
		
		//��������Ϣ
		allParams.set(oldPosition, allParams.get(oldPosition + 1));
		allParams.set(oldPosition + 1, model);
		tableViewer.setInput(allParams);
		((TableSelectorWizardDialog) this.getContainer()).setColumnEntityList(allParams);

//		//������չʾ���ݣ��ѷ���
//		if (!isReconfig) {
//			columnStrings = ((TableSelectorWizardDialog) this.getContainer()).getColumnStrings();
//			if (columnStrings.size() > 0) {
//				listSub = columnStrings.get(oldPosition);
//				columnStrings.remove(oldPosition);
//				columnStrings.add(oldPosition + 1, listSub);
//				((TableSelectorWizardDialog) this.getContainer()).setColumnStrings(columnStrings);
//			}
//		}

		//������Path
		allColumnPathList = ((TableSelectorWizardDialog) this.getContainer()).getAllColumnPath();
		Path col = allColumnPathList.get(oldPosition);
		allColumnPathList.remove(oldPosition);
		allColumnPathList.add(oldPosition + 1, col);
		((TableSelectorWizardDialog) this.getContainer()).setAllColumnPath(allColumnPathList);

		//����xml�ı�
		colText = ((TableSelectorWizardDialog) this.getContainer()).getxmlText();
		text.setText(colText);

	}

	/**
	 * ��������ơ���ť�������Ϣ
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
		
		//��������Ϣ
		allParams.set(oldPosition, allParams.get(oldPosition - 1));
		allParams.set(oldPosition - 1, model);
		tableViewer.setInput(allParams);
		((TableSelectorWizardDialog) this.getContainer()).setColumnEntityList(allParams);

		//������չʾ���ݣ��ѷ���
//		if (!isReconfig) {
//			columnStrings = ((TableSelectorWizardDialog) this.getContainer()).getColumnStrings();
//			if (columnStrings.size() > 0) {
//				listSub = columnStrings.get(oldPosition);
//				columnStrings.remove(oldPosition);
//				columnStrings.add(oldPosition - 1, listSub);
//				((TableSelectorWizardDialog) this.getContainer()).setColumnStrings(columnStrings);
//			}
//		}

		//������Path
		if (isReconfig)
			allColumnPathList = ((TableSelectorWizardDialog) this.getContainer()).getAllColumnPath();
		Path col = allColumnPathList.get(oldPosition);
		allColumnPathList.remove(oldPosition);
		allColumnPathList.add(oldPosition - 1, col);
		((TableSelectorWizardDialog) this.getContainer()).setAllColumnPath(allColumnPathList);

		//����xml�ı�
		colText = ((TableSelectorWizardDialog) this.getContainer()).getxmlText();
		text.setText(colText);
	}

    /**
     * �������ѡ����ť��֪ͨWizardDialogɾ����ѡ����Ϣ������¼λ�ã��Ա���ѡ��ɺ���뵽ԭλ�ã�����Ϣ���䣬Path��չʾ���ݱ䣩
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
	 * �����ɾ������ť�󣬸�����Ϣ
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

		//���Ƕ������ã�����Ҫ������չʾ���ݣ�������չʾ����Ϊ�գ�������
		if (!isReconfig) {
			columnStrings = ((TableSelectorWizardDialog) this.getContainer()).getColumnStrings();
			if (columnStrings.size() > 0) {
				columnStrings.remove(index);
				((TableSelectorWizardDialog) this.getContainer()).setColumnStrings(columnStrings);
			}
		}

		if (isReconfig)
			((TableSelectorWizardDialog) this.getContainer()).setRowPath(extractedData.getRowPath());

		//��������Ϣ
		allParams.remove(model);
		tableViewer.setInput(allParams);
		((TableSelectorWizardDialog) this.getContainer()).setColumnEntityList(allParams);

		if (!isReconfig)
			allColumnPathList = ((TableSelectorWizardDialog) this.getContainer()).getAllColumnPath();
		
		//������Path
		allColumnPathList.remove(index);
		((TableSelectorWizardDialog) this.getContainer()).setAllColumnPath(allColumnPathList);

		//����xml�ı�
		colText = ((TableSelectorWizardDialog) this.getContainer()).getxmlText();
		text.setText(colText);

		//���¡���ѡ����ť����
		setButtonControl(allParams.size());
		isReconfig = false;
	}

	/**
	 * �������ӡ���ť����ת�ص�һҳ�������������
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
	 * ����������ѡ����ť��ɾ�����е�ǰ����Ϣ����ת�ص�һҳ
	 */
	protected void clearAllandSelect() {
		isClear=true;
		TableSelectorWizardDialog dialog = (TableSelectorWizardDialog) getContainer();
		
		//��Path
		allColumnPathList=dialog.getAllColumnPath();
		allColumnPathList.clear();
		dialog.setAllColumnPath(allColumnPathList);
		
		//����Ϣ
		allParams=dialog.getColumnEntityList();
		allParams.clear();
		dialog.setColumnEntityList(allParams);
		
//		//��չʾ���ݣ��ѷ���
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
	 * �ػ�����ѡ����ť
	 * @param �������
	 */
	protected void setButtonControl(int rowCount) {
		
		//��ɾ������TableEditor�͡���ѡ����ť
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

			reselectButtons[i].setText("<A>��ѡ</A>");
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
	 * ���һ��
	 * @param ������
	 * @param �б���
	 */
	public void addColumnInfo(String name, String title) {
		allParams.add(new ColumnEntity(name, title));
	}

	/**
	 * �ж��Ƿ�������Ӱ�ť�������
	 * @return isAddColumn
	 */
	public boolean isAddColumn() {
		return isAddColumn;
	}

	/**
	 * �ж��Ƿ�������ѡ��ť����ѡ��
	 * @return isReselectColumn
	 */
	public boolean isReselectColumn() {
		return isReselectColumn;
	}

	/**
	 * �����Ƿ�Ϊ�����
	 * @param flag
	 */
	public void setIsAddcolumn(boolean flag) {
		isAddColumn = flag;
	}

	/**
	 * �����Ƿ�Ϊ��ѡ��
	 * @param flag
	 */
	public void setIsReselectcolumn(boolean flag) {
		isReselectColumn = flag;
	}

	/**
	 * �ж��Ƿ�Ϊ��������
	 * @return isReconfig
	 */
	public boolean getIsReconfig() {
		return isReconfig;
	}

	/**
	 * �����Ƿ�Ϊ��������
	 * @param flag
	 */
	public void setIsReconfig(boolean flag) {
		isReconfig = flag;
	}

	/**
	 * �ж��Ƿ�Ϊ��������в���ѡ
	 */
	public boolean getIsClear() {
		return isClear;
	}
	
	/**
	 * �����Ƿ�Ϊ��������в���ѡ
	 */
	public void setIsClear(boolean flag) {
		isClear=flag;
	}
	
	/**
	 * ��������ʱ����ExtractedData���ݸ���WizardDialog�е���Ϣ
	 * @param ExtractedData
	 */
	public void setExtractedData(ExtractedData extData) {

		// ��������Ϣ
		ArrayList<DataTableField> allColumnPathList = extData.getColumnsList();
		ArrayList<ColumnEntity> colEntities = new ArrayList<ColumnEntity>();
		for (DataTableField ColumnPath : allColumnPathList) {
			colEntities.add(new ColumnEntity(ColumnPath.getName(),ColumnPath.getTitle()));
		}
		((TableSelectorWizardDialog) ColumnEditWizardPage.this.getContainer()).setColumnEntityList(colEntities);

		//����rowPath
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
		
		//������Path
		((TableSelectorWizardDialog) ColumnEditWizardPage.this.getContainer()).setAllColumnPath(extData.getColumnsPath());
	}

	/**
	 * ��ȡExtractedData
	 * @return
	 */
	public ExtractedData getExtractedData() {
		return extractedData;
	}

	/**
	 * ��������ʱ����ExtractedData���ݸ��´�pageҳ�е���Ϣ
	 * @param extData
	 */
	public void putExtractedData(ExtractedData extData) {

		//��������Ϣ
		ArrayList<DataTableField> allColumnList = extData.getColumnsList();
		allParams = new ArrayList<ColumnEntity>();
		for (DataTableField ColumnPath : allColumnList) {
			allParams.add(new ColumnEntity(ColumnPath.getName(),ColumnPath.getTitle()));
		}

		//������Path
		allColumnPathList = extData.getColumnsPath();

		//����xml�ı�
		colText = extData.getText();
	}

	public boolean canFlipToNextPage() {
		return  checkPageFinish();
	}

	private boolean checkPageFinish() {
		
		//���Խ����ı�����Ϊ����������ʧ�ܣ�seterrormessage��������¶�������
		String xmlStr=text.getText();
		try {
			
			//����xml�ı�
			ExtractedData.calInfo(xmlStr);
			
			//�������
			ExtractedData extDataTemp=new ExtractedData(xmlStr);
			ArrayList<DataTableField> tempCols=extDataTemp.getColumnsList();
			
			for(int i=0;i<tempCols.size();i++)
			{
				for(int j=i+1;j<tempCols.size();j++)
				{
					if(tempCols.get(i).getName().equals(tempCols.get(j).getName()))
					{
						setErrorMessage("�ظ�������"+tempCols.get(i).getName());
						return false;
					}
				}
			}
			
			if(tempCols.size()==0)
			{
				setErrorMessage("�����ٱ���һ��");
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
