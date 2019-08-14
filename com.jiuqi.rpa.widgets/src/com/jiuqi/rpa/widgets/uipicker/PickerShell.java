package com.jiuqi.rpa.widgets.uipicker;

/**
 * �̳�shell�� UIѡ����
 * @author liangxiao01
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.layout.RowData;

import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.lib.browser.WebBrowserManager;
import com.jiuqi.rpa.lib.drawer.UIADrawerLibrary;
import com.jiuqi.rpa.lib.find.FindLibraryManager;
import com.jiuqi.rpa.lib.find.IUIElement;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.lib.find.PathAttribute;
import com.jiuqi.rpa.lib.find.PathElement;
import com.jiuqi.rpa.lib.find.WEBElement;
import com.jiuqi.rpa.lib.tree.UIATreeWalker;
import com.jiuqi.rpa.lib.tree.WEBTreeWalker;
import com.jiuqi.rpa.widgets.Activator;

import org.eclipse.swt.widgets.Label;


public class PickerShell extends Shell {
	Display display = Display.getDefault();
	
	private final Image ADD = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_ADD);
	private final Image DELETE = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_DELETE);
	private final Image VSWITCH = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_VSWITCH);
	private final Image ESWITCH = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_ESWITCH);
	private final Image HSWITCH = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_HSWITCH);
	private final Image STATUS_WARING = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_STATUS_WARNING);
	private final Image STATUS_ERROR = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_STATUS_ERROR);
	private final Image STATUS_SUCCESS = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_STATUS_SUCCESS);
	private Path path = new Path();//��ǰ��Ծ��Path
	private Boolean saveMode = false;//�����жϵ�ǰ�����Path�Ƿ�Ҫ����
	Boolean changed = false;//�����жϵ�ǰ�����Ƿ������޸�
	//5��������ť 
	private Button saveButton;
	private Button exitButton;
	private Button checkButton;
	private Button screebSelectButton;
	private Button hightlightButton;
	//�����Լ�������Context
	private Context context;
	//������Σ������Ҽ��˵�
	private TreeViewer vistalTreeViewer;
	private Tree vistalTree;
	//RPA Walker
	private FindLibraryManager findLibraryManager;
	private WebBrowserManager webBrowserManager = WebBrowserManager.getInstance();
	private UIADrawerLibrary drawerLibrary;
	private Boolean hightlightState = false;
	//������Ա��
	private TableViewer nodeAttrTableViewer;
	private Table nodeAttrTable;
	SashForm exploreSashForm;
	Composite hideSlideBar;
	GridData gd_hideSlideBar;
	//�в��༭�����
	private TableViewer selectorEditorTableViewer;
	private Table selectorEditorTable;
	private static String selectorEditorColumnCheck = "CHECK";
	private static String selectorEditorColumnPath = "PATH";
	//�Ҳ�ѡ�������Ա��
	private TableViewer selectorAttrTableViewer;
	private Table selectorAttrTable;
	private static String selectorAttrColumnCheck = "CHECK";
	private static String selectorAttrColumnKey = "KEY";
	private static String selectorAttrColumnValue = "VALUE";
	private SashForm nodeAttrSashForm;
	// �м䲿�ֱ༭�� 	selectorEditorTable������
	private Composite editorSashForm;
	// �Ҳ�������ͼ  	selectorAttrTable������
	private Composite attributeContainer;
	private Composite attributeComp;
	private Button attributeAddBtn;
	private Button attributeDelBtn;
	//����У���Ҳ���ǰkey�Ƿ��ظ������ó�ȫ�ֱ���
	List<PathAttribute> attrInputList = null;
	private UIATreeWalker walker;
	private WEBTreeWalker webTreeWalker;
	Path storePath = null;
	private Button browserSelection;//ѡȡ�����
	private boolean findWindow;
	
	Context getContext() {
		return context;
	}
	
	TreeViewer getVistalTreeViewer() {
		return vistalTreeViewer;
	}
	
	TableViewer getNodeAttrTableViewer() {
		return nodeAttrTableViewer;
	}
	
	Path getCurrentPath() {
		return path;
	}
	
	public WebBrowserManager getWebBrowserManager() {
		return webBrowserManager;
	}
	public void centerShell(Display display, Shell shell) {
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		shell.setLocation(x, y);
	}
	

	/**
	 * �ٴα༭������Path��λ
	 * 
	 * @param parentShell
	 * @param path
	 * @wbp.parser.constructor
	 */
	protected PickerShell(Shell parentShell, Path path) {
		this(parentShell, path, false);
	}
	
	/**
	 * �ٴα༭������Path��λ
	 * 
	 * @param parentShell
	 * @param path
	 * @param findMode
	 */
	protected PickerShell(Shell parentShell, Path path, boolean findWindow) {
		super(parentShell, SWT.SHELL_TRIM);
		this.findWindow = findWindow;
		storePath = (Path) path.clone();
		
		context = new Context();// ����ά��һ��Context
		walker = new UIATreeWalker(context);
		findLibraryManager = new FindLibraryManager(context);	
		drawerLibrary = new UIADrawerLibrary();
		createContents();// ����UI
		centerShell(PickerShell.this.getDisplay(), PickerShell.this.getShell());// ���ƽ������
		loadPath(path);
		//directTree();
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		setText("UIѡ����");
		setSize(1280, 800);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite container = new Composite(this, SWT.NONE);
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.verticalSpacing = 3;
		gl_container.marginHeight = 3;
		container.setLayout(gl_container);

		// ���嶥����ť��
		Composite topButtonsComp = new Composite(container, SWT.NONE);
		RowLayout rl_topButtonsComp = new RowLayout(SWT.HORIZONTAL);
		rl_topButtonsComp.spacing = 5;
		topButtonsComp.setLayout(rl_topButtonsComp);
		topButtonsComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		saveButton = new Button(topButtonsComp, SWT.NONE);
		saveButton.setLayoutData(new RowData(60, SWT.DEFAULT));
		saveButton.setText("����");
		exitButton = new Button(topButtonsComp, SWT.NONE);
		exitButton.setLayoutData(new RowData(60, SWT.DEFAULT));
		exitButton.setText("�˳�");
		checkButton = new Button(topButtonsComp, SWT.NONE);
		checkButton.setLayoutData(new RowData(60, SWT.DEFAULT));
		checkButton.setImage(STATUS_WARING);
		checkButton.setText("У��");
		screebSelectButton = new Button(topButtonsComp, SWT.NONE);
		screebSelectButton.setLayoutData(new RowData(60, SWT.DEFAULT));
		screebSelectButton.setText("��Ļѡ��");
		hightlightButton = new Button(topButtonsComp, SWT.TOGGLE);
		hightlightButton.setText("ͻ����ʾ");
		browserSelection = new Button(topButtonsComp, SWT.NONE);
		browserSelection.setText("���������");
		// ���ð�ť��Ӧ
		setButtonsAction();
		// UIѡ�������岼��
		Composite mainComp = new Composite(container, SWT.BORDER);
		mainComp.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_mainComp = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_mainComp.widthHint = 459;
		mainComp.setLayoutData(gd_mainComp);
		// ��SashForm ������������֤�ڲ�SashForm�ܹ�������ק
		SashForm mainMoveProvider = new SashForm(mainComp, SWT.NONE);
		// ���sashForm
		exploreSashForm = new SashForm(mainMoveProvider, SWT.VERTICAL);
		// ���sashForm A ��A��B��������ק��
		Composite nodeTreeSashForm =  new Composite(exploreSashForm, SWT.NONE);
		//nodeTreeSashForm.setBackground(SWTResourceManager.getColor(255, 255, 255));
		GridLayout gl_nodeTreeSashForm = new GridLayout(1, false);
		gl_nodeTreeSashForm.horizontalSpacing = 0;
		gl_nodeTreeSashForm.marginHeight = 0;
		gl_nodeTreeSashForm.marginWidth = 0;
		gl_nodeTreeSashForm.verticalSpacing = 0;
		nodeTreeSashForm.setLayout(gl_nodeTreeSashForm);
		//�����ϲ�����ť
		Composite treeToolkitBar = new Composite(nodeTreeSashForm, SWT.NONE);
		GridData gd_treeToolkitBar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_treeToolkitBar.widthHint = 278;
		treeToolkitBar.setLayoutData(gd_treeToolkitBar);
		GridLayout gl_treeToolkitBar = new GridLayout(4, false);
		gl_treeToolkitBar.verticalSpacing = 0;
		gl_treeToolkitBar.marginWidth = 0;
		gl_treeToolkitBar.marginHeight = 0;
		treeToolkitBar.setLayout(gl_treeToolkitBar);
		
		Label treeLabel = new Label(treeToolkitBar, SWT.NONE);
		GridData gd_treeLabel = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_treeLabel.widthHint = 222;
		treeLabel.setLayoutData(gd_treeLabel);
		treeLabel.setText("UI��");
		Button btnExpand = new Button(treeToolkitBar, SWT.NONE);
		btnExpand.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				directTree();
			}
		});
		btnExpand.setImage(ESWITCH);
		Button btnVSwitch = new Button(treeToolkitBar, SWT.NONE);
		btnVSwitch.setImage(VSWITCH);
		btnVSwitch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				nodeAttrSashForm.setVisible(!nodeAttrSashForm.getVisible());
				if(hideSlideBar.getVisible()){
					gd_hideSlideBar.widthHint = 30;
					hideSlideBar.setLayoutData(gd_hideSlideBar);
				}else{
					gd_hideSlideBar.widthHint = 0;
					hideSlideBar.setLayoutData(gd_hideSlideBar);
				}
				hideSlideBar.layout();
				nodeAttrSashForm.getParent().layout();
				
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		Button btnHSwitch = new Button(treeToolkitBar, SWT.NONE);
		btnHSwitch.setImage(HSWITCH);
		btnHSwitch.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				exploreSashForm.setVisible(!exploreSashForm.getVisible());
				hideSlideBar.setVisible(!hideSlideBar.getVisible());
				if(hideSlideBar.getVisible()){
					gd_hideSlideBar.widthHint = 30;
					hideSlideBar.setLayoutData(gd_hideSlideBar);
				}else{
					gd_hideSlideBar.widthHint = 0;
					hideSlideBar.setLayoutData(gd_hideSlideBar);
				}
				hideSlideBar.layout();
				exploreSashForm.getParent().layout();
				hideSlideBar.getParent().layout();
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		Composite treeComposite = new Composite(nodeTreeSashForm, SWT.BORDER);
		treeComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite.widthHint = 313;
		gd_composite.heightHint = 419;
		treeComposite.setLayoutData(gd_composite);
		vistalTreeViewer =  new TreeViewer(treeComposite,SWT.VIRTUAL);
		vistalTree = vistalTreeViewer.getTree();
		
		// ���sashForm B ��A��B��������ק��
		nodeAttrSashForm = new SashForm(exploreSashForm, SWT.BORDER);
		exploreSashForm.setWeights(new int[] {450, 260});

		// �м䲿�ֱ༭��
		editorSashForm = new Composite(mainMoveProvider, SWT.BORDER);
		// �Ҳ�������ͼ
		attributeContainer = new Composite(mainMoveProvider, SWT.NONE);
		mainMoveProvider.setWeights(new int[] {300, 700, 244});
		
		initAllTableFramework();//ͬʱ��ʼ�����������Ϣ���������������
		
		try {
			buildlTree();// �����������
			buildlPopList();// ������������Ҽ��˵�
			vistalTreeViewer.addSelectionChangedListener(new UITreeSelectionChangedListener(this));// ���������¼�����   
			setAttributeTableBtnsAction();
		} catch (LibraryException e) {
			e.printStackTrace();
		}
		

	}
	/**
	 * ��ʼ�����б��ı����Ϣ
	 */
	private void initAllTableFramework(){
		TableLayout layout1 = new TableLayout(); // ר���ڱ��1�Ĳ���
		TableLayout layout2 = new TableLayout(); // ר���ڱ��2�Ĳ���
		TableLayout layout3 = new TableLayout(); // ר���ڱ��3�Ĳ���
		
		// ������������б� ��ͷ���У��ɿ��ƿ�ȣ�����������ˢ�¸ñ�����
		//�ڵ����Ա��
		nodeAttrTableViewer = new TableViewer(nodeAttrSashForm,  SWT.FULL_SELECTION );
		nodeAttrTable = nodeAttrTableViewer.getTable();
		nodeAttrTable.setLinesVisible(true);
		nodeAttrTable.setHeaderVisible(true);
		nodeAttrTable.setLayout(layout1);
		layout1.addColumnData(new ColumnWeightData(100));
		new TableColumn(nodeAttrTable, SWT.NONE).setText("����");
		layout1.addColumnData(new ColumnWeightData(190));
		new TableColumn(nodeAttrTable, SWT.NONE).setText("ֵ");
		nodeAttrTable.getColumn(0).pack();
		nodeAttrTable.getColumn(1).pack();
		nodeAttrTableViewer.setContentProvider(new UINodePropertiesTableContentProvider());
		nodeAttrTableViewer.setLabelProvider(new UINodePropertiesTableLabelProvider());
		GridLayout gl_editorSashForm = new GridLayout(2, false);
		gl_editorSashForm.horizontalSpacing = 0;
		gl_editorSashForm.marginHeight = 0;
		gl_editorSashForm.marginWidth = 0;
		editorSashForm.setLayout(gl_editorSashForm);
		layout2.addColumnData(new ColumnWeightData(25));
		layout2.addColumnData(new ColumnWeightData(595));
		//Ĭ�����ص�hideSlideForm
		//hideSlideBarĬ�����أ����������ص�ʱ��hideSlideBar����ʾ����
		hideSlideBar = new Composite(editorSashForm, SWT.NONE);
		gd_hideSlideBar = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_hideSlideBar.widthHint = 0;
		hideSlideBar.setLayoutData(gd_hideSlideBar);
		GridLayout gl_hideSlideBar = new GridLayout(1, false);
		gl_hideSlideBar.marginWidth = 0;
		gl_hideSlideBar.horizontalSpacing = 0;
		gl_hideSlideBar.marginHeight = 0;
		hideSlideBar.setLayout(gl_hideSlideBar);
		hideSlideBar.setVisible(false);
		//hideSlideBar�е�2����ť �������أ���������
		Button slideHBtn = new Button(hideSlideBar, SWT.NONE);
		slideHBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		slideHBtn.setImage(HSWITCH);
		slideHBtn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				
				exploreSashForm.setVisible(!exploreSashForm.getVisible());
				hideSlideBar.setVisible(!hideSlideBar.getVisible());
				if(hideSlideBar.getVisible()){
					gd_hideSlideBar.widthHint = 30;
					hideSlideBar.setLayoutData(gd_hideSlideBar);
				}else{
					gd_hideSlideBar.widthHint = 0;
					hideSlideBar.setLayoutData(gd_hideSlideBar);
				}
				hideSlideBar.layout();
				exploreSashForm.getParent().layout();  
				hideSlideBar.getParent().layout();
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		Button slideVBtn = new Button(hideSlideBar, SWT.NONE);
		slideVBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		slideVBtn.setImage(VSWITCH);
		slideVBtn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				nodeAttrSashForm.setVisible(!nodeAttrSashForm.getVisible());
				if(hideSlideBar.getVisible()){
					gd_hideSlideBar.widthHint = 30;
					hideSlideBar.setLayoutData(gd_hideSlideBar);
				}else{
					gd_hideSlideBar.widthHint = 0;
					hideSlideBar.setLayoutData(gd_hideSlideBar);
				}
				hideSlideBar.layout();
				nodeAttrSashForm.getParent().layout();
				
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		//����Path ���
		selectorEditorTableViewer = new TableViewer(editorSashForm,  SWT.FULL_SELECTION);
		selectorEditorTableViewer.setContentProvider(new UISelectorTableContentProvider());
		selectorEditorTableViewer.setLabelProvider(new UISelectorTableLabelProvider(this));
		selectorEditorTable = selectorEditorTableViewer.getTable();
		selectorEditorTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		selectorEditorTable.setLayout(layout2);
		new TableColumn(selectorEditorTable, SWT.NONE).setText(selectorEditorColumnCheck);
		new TableColumn(selectorEditorTable, SWT.NONE).setText(selectorEditorColumnPath);
		selectorEditorTable.getColumn(0).pack();
		selectorEditorTable.getColumn(1).pack();
		selectorEditorTable.setLinesVisible(true);
		setSelectorEditorTableCellEditor();
		
		//�Ҳ�Path���Ա��
		layout3.addColumnData(new ColumnWeightData(30));
		layout3.addColumnData(new ColumnWeightData(150));
		layout3.addColumnData(new ColumnWeightData(180));
		GridLayout gl_attributeContainer = new GridLayout(1, false);
		gl_attributeContainer.verticalSpacing = 0;
		gl_attributeContainer.marginWidth = 0;
		gl_attributeContainer.marginHeight = 0;
		gl_attributeContainer.horizontalSpacing = 0;
		attributeContainer.setLayout(gl_attributeContainer);
		Composite attributeToolkit = new Composite(attributeContainer, SWT.NONE);
		GridLayout gl_attributeToolkit = new GridLayout(3, false);
		gl_attributeToolkit.verticalSpacing = 0;
		gl_attributeToolkit.marginWidth = 0;
		gl_attributeToolkit.marginHeight = 0;
		attributeToolkit.setLayout(gl_attributeToolkit);
		attributeToolkit.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		Label attributeLabel = new Label(attributeToolkit, SWT.NONE);
		attributeLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		attributeLabel.setText("����");
		attributeDelBtn = new Button(attributeToolkit, SWT.NONE);
		attributeDelBtn.setImage(DELETE);
		attributeAddBtn = new Button(attributeToolkit, SWT.NONE);
		attributeAddBtn.setImage(ADD);
		attributeComp = new Composite(attributeContainer, SWT.BORDER);
		attributeComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		attributeComp.setLayout(new FillLayout(SWT.HORIZONTAL));
		selectorAttrTableViewer = new TableViewer(attributeComp,  SWT.FULL_SELECTION);
		selectorAttrTableViewer.setContentProvider(new UISelectorAttributeTableContentProvider());
		selectorAttrTableViewer.setLabelProvider(new UISelectorAttributeTableLabelProvider());
		selectorAttrTable = selectorAttrTableViewer.getTable();
		selectorAttrTable.setLinesVisible(true);
		selectorAttrTable.setLayout(layout3);
		new TableColumn(selectorAttrTable, SWT.NONE).setText(selectorAttrColumnCheck);
		new TableColumn(selectorAttrTable, SWT.NONE).setText(selectorAttrColumnKey);
		new TableColumn(selectorAttrTable, SWT.NONE).setText(selectorAttrColumnValue);
		selectorAttrTable.getColumn(0).pack();
		selectorAttrTable.getColumn(1).pack();
		selectorAttrTable.getColumn(2).pack();
		selectorAttrTable.setLinesVisible(true);
		/*
		//�ʼ��������б�
		exploreSashForm.setVisible(false);
		hideSlideBar.setVisible(true);
		if(hideSlideBar.getVisible()){
			gd_hideSlideBar.widthHint = 30;
			hideSlideBar.setLayoutData(gd_hideSlideBar);
		}
		hideSlideBar.layout();
		exploreSashForm.getParent().layout();
		hideSlideBar.getParent().layout();*/
	}
	
	/**
	 * Path �м���ɱ༭��Ԫ 
	 */
	private void  setSelectorEditorTableCellEditor() {
		CellEditor[] cellEditor = new CellEditor[2];
		cellEditor[0] = new CheckboxCellEditor(selectorEditorTable, SWT.CHECK);
		cellEditor[1] = null;
		selectorEditorTableViewer.setCellEditors(cellEditor);
		selectorEditorTableViewer.setColumnProperties(new String[] { selectorEditorColumnCheck, selectorEditorColumnPath });
		selectorEditorTableViewer.setCellModifier(new RPASelectorEditorCellModifier());
		selectorEditorTable.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				PathElement pathElemeng = (PathElement) e.item.getData();
				//�����Ҳ�������
				attrInputList = PathElementHelper.getEditableAttribute(path.isWeb(),pathElemeng);
				selectorAttrTableViewer.setInput(attrInputList);
				setSelectorAttrTableCellEditorDie();	
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	/**
	 * Attribute  �Ҳ���ɱ༭��Ԫ �ܼ�⵽�ڵ����
	 * @param properties 
	 * @param strarr ��������
	 */
	private void  setSelectorAttrTableCellEditorAlive(final Properties properties, final String[] strarr){
		CellEditor[] cellEditor = new CellEditor[3];
		cellEditor[1] = new ComboBoxCellEditor(selectorAttrTable,strarr , SWT.READ_ONLY);
		cellEditor[2] = new TextCellEditor(selectorAttrTable);
		cellEditor[0] = new CheckboxCellEditor(selectorAttrTable, SWT.CHECK);
		selectorAttrTableViewer.setCellEditors(cellEditor);
		selectorAttrTableViewer.setColumnProperties(new String[] { selectorAttrColumnCheck, selectorAttrColumnKey, selectorAttrColumnValue });
		selectorAttrTableViewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if(selectorAttrColumnKey.equals(property) && !"".equals(((PathAttribute)element).getName())){
					return false;
				}
				return true;
				
			}
			private int getIndex(String name) {
				for(int i=0; i<strarr.length; i++) {
					if(strarr[i].equals(name))
						return i;
				}
				return -1;
			}
			public Object getValue(Object element, String property) {
				PathAttribute attrModel = (PathAttribute) element;
				if (selectorAttrColumnCheck.equals(property))
					return attrModel.isEnable();
				else if (selectorAttrColumnKey.equals(property))
					return getIndex(attrModel.getName());
				else if (selectorAttrColumnValue.equals(property))
					return attrModel.getValue();
				return null;
			}
			
			public void modify(Object element, String property, Object value) {
				if(value==null)
					return;
				TableItem pathItem = (TableItem) element;
				PathAttribute attrModel = (PathAttribute) pathItem.getData();
				if (selectorAttrColumnCheck.equals(property)){
					attrModel.setEnable(((Boolean)value).booleanValue());
				}else if (selectorAttrColumnKey.equals(property)){
					if((Integer)value ==-1){
						attrModel.setName("");
					}else{
						attrModel.setName(strarr[(Integer)value]);		
						attrModel.setValue(properties.getProperty(strarr[(Integer)value], ""));	
					}
				}
				else if (selectorAttrColumnValue.equals(property)){
					attrModel.setValue(((String)value).toString());
				}
				selectorAttrTableViewer.refresh();
				//ˢ���в��������(��ȡ�Ҳ������б�����AttributeList ��ֵ��PathElement Ȼ��ˢ���в����)
				TableItem[] selectorAttrTableItems = selectorAttrTable.getItems();
				PathElement pathElement = (PathElement) selectorEditorTable.getItem(selectorEditorTable.getSelectionIndex()).getData();
				PathAttribute protectedAttr = PathElementHelper.getControlType(path.isWeb(),pathElement);
				pathElement.getAttributes().clear();
				pathElement.getAttributes().add(protectedAttr);
				for (TableItem tableItem : selectorAttrTableItems) {
					PathAttribute attribute = (PathAttribute) tableItem.getData();
					if(!"".equals(attribute.getName())){
						pathElement.getAttributes().add(attribute);					
					}
				}
				selectorEditorTableViewer.refresh();
				changed = true;
			}
		});
	}
	
	/**
	 * Attribute  �Ҳ���ɱ༭��Ԫ ���ܼ�⵽�ڵ����
	 */
	private void  setSelectorAttrTableCellEditorDie(){
		CellEditor[] cellEditor = new CellEditor[3];
		cellEditor[1] = new TextCellEditor(selectorAttrTable);
		cellEditor[2] = new TextCellEditor(selectorAttrTable);
		cellEditor[0] = new CheckboxCellEditor(selectorAttrTable, SWT.CHECK);
		cellEditor[1].setValidator(new ICellEditorValidator() {
			public String isValid(Object value) {
				String str = value.toString();
				if (!str.matches("[a-zA-Z_][a-zA-Z0-9_]*"))
					return "�������Ʋ��Ϸ�";
				if (isNameExist(str))
					return "���������ظ�";
				return null;
			}
			
		});
		selectorAttrTableViewer.setCellEditors(cellEditor);
		selectorAttrTableViewer.setColumnProperties(new String[] { selectorAttrColumnCheck, selectorAttrColumnKey, selectorAttrColumnValue });
		selectorAttrTableViewer.setCellModifier(new RPASelectorAttrCellModifier());
	}
	
	/**
	 * У���Ҳ���Key�ظ�
	 * @param fieldName
	 * @return
	 */
	private boolean isNameExist(String attributeName) {
		for(PathAttribute f : attrInputList) {
			if (attributeName.equalsIgnoreCase(f.getName()))
				return true;
		}
		return false;
	}
	/**
	 * �ı��м�Path���ѡ�ṩ��
	 * @author liangxiao01
	 *
	 */
	private class RPASelectorEditorCellModifier implements ICellModifier {
		public boolean canModify(Object element, String property) {
			return true;
		}
		public Object getValue(Object element, String property) {
			PathElement pathElement = (PathElement) element;
			if (selectorEditorColumnCheck.equals(property))
				return pathElement.isEnable();
			else
				return null;
		}
		public void modify(Object element, String property, Object value) {
			TableItem pathItem = (TableItem) element;
			PathElement pathElement = (PathElement) pathItem.getData();
			pathElement.setEnable(((Boolean)value).booleanValue());
			selectorEditorTableViewer.refresh();
			changed = true;
		}
	}
	
	/**
	 * �ı��Ҳ�Attribute���ѡ�ṩ��
	 * @author liangxiao01
	 *
	 */
	private class RPASelectorAttrCellModifier implements ICellModifier {
		public boolean canModify(Object element, String property) {
			if(selectorAttrColumnKey.equals(property) && !"".equals(((PathAttribute)element).getName())){
				return false;
			}
			return true;
		}
		public Object getValue(Object element, String property) {
			PathAttribute attrModel = (PathAttribute) element;
			if (selectorAttrColumnCheck.equals(property))
				return attrModel.isEnable();
			else if (selectorAttrColumnKey.equals(property))
				return attrModel.getName();
			else if (selectorAttrColumnValue.equals(property))
				return attrModel.getValue();
			return null;
		}
		
		public void modify(Object element, String property, Object value) {
			if(value==null)
				return;
			TableItem pathItem = (TableItem) element;
			PathAttribute attrModel = (PathAttribute) pathItem.getData();
			if (selectorAttrColumnCheck.equals(property)){
				//selectorAttrTable.getSelectionIndex();
				attrModel.setEnable(((Boolean)value).booleanValue());
			}
			else if (selectorAttrColumnKey.equals(property))
				attrModel.setName(((String)value).toString());
			else if (selectorAttrColumnValue.equals(property))
				attrModel.setValue(((String)value).toString());
			selectorAttrTableViewer.refresh();
			//ˢ���в��������(��ȡ�Ҳ������б�����AttributeList ��ֵ��PathElement Ȼ��ˢ���в����)
			TableItem[] selectorAttrTableItems = selectorAttrTable.getItems();
			PathElement pathElement = (PathElement) selectorEditorTable.getItem(selectorEditorTable.getSelectionIndex()).getData();
			PathAttribute protectedAttr = PathElementHelper.getControlType(path.isWeb(),pathElement);
			pathElement.getAttributes().clear();
			if(protectedAttr!=null)
				pathElement.getAttributes().add(protectedAttr);
			for (TableItem tableItem : selectorAttrTableItems) {
				PathAttribute attribute = (PathAttribute) tableItem.getData();
				if(!"".equals(attribute.getName())){
					pathElement.getAttributes().add(attribute);					
				}
			}
			selectorEditorTableViewer.refresh();
			changed = true;
		}
	}
	/**
	 * �������ڵ�����
	 * @throws LibraryException 
	 * 
	 * @require buildTreeItems()
	 */
	private void buildlTree() throws LibraryException {
		//������ڵ�
		TreeNode rootTreeVirtualItem = new TreeNode(null);
		rootTreeVirtualItem.setChildren( new TreeNode[]{});
		//���α�ǩ�ṩ��
		vistalTreeViewer.setLabelProvider(new UITreeLabelProvider(context));
		//���������ṩ��
		vistalTreeViewer.setContentProvider(new UITreeContentProvider(context));
		vistalTreeViewer.setInput(rootTreeVirtualItem);
	}

	/**
	 * �����Ҽ��˵�
	 */
	private void buildlPopList() {
		Menu menu = new Menu(vistalTree);
		MenuItem selectTreeNode = new MenuItem(menu, SWT.PUSH);
		MenuItem freshTreeNode = new MenuItem(menu, SWT.PUSH);
		selectTreeNode.setText("ѡ���ڵ�");
		selectTreeNode.addSelectionListener(new UITreeSetAsTargetListener(this));
		freshTreeNode.setText("ˢ�½ڵ�");
		freshTreeNode.addSelectionListener(new UITreeRefreshNodeListener(this));
		vistalTree.setMenu(menu);

	}
	
	private void setAttributeTableBtnsAction(){
		// �Ҳ����Ա����Ӱ�ť
		attributeAddBtn.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
				if(selectorEditorTable.getSelection().length>0){
					
					List<PathAttribute> pathAttributeList = (List<PathAttribute>) selectorAttrTableViewer.getInput();
					PathAttribute attribute = new PathAttribute();
					attribute.setEnable(true);
					attribute.setName("");
					attribute.setValue("");
					pathAttributeList.add(attribute);
					selectorAttrTableViewer.setInput(pathAttributeList);
					/**��ʼ��ȡ��ǰѡ��properties**************************************************************************/
					Path indexPath = new Path();
					int index = selectorEditorTable.getSelectionIndex();
					indexPath.setWeb(path.isWeb());
					for (int j = 0; j <= index ; j++) {
						indexPath.getElements().add((PathElement)path.getElements().get(j));
					}
					Properties properties = null;
					try{
						if(indexPath.isWeb()){
							if(webTreeWalker ==null || index == 0){
								properties = null;
							}else{
								properties = webTreeWalker.getProperties(findLibraryManager.findFirst(indexPath));													
							}
						}else{
							IUIElement el = findLibraryManager.findFirst(indexPath);
							properties = walker.getProperties(el);		
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						System.out.println("����Pathʱ���޷��ҵ��ڵ�Ԫ�أ�����:[PickerShell.java]"+e1.getMessage());
					}
					/***������ȡ��ǰѡ��properties*************************************************************************/
					if(properties !=null){
						Set<String> propertyNameSet = properties.stringPropertyNames();
						setSelectorAttrTableCellEditorAlive(properties,propertyNameSet.toArray(new String[propertyNameSet.size()]));						
					}else{
						setSelectorAttrTableCellEditorDie();
					}
					changed = true;
				}
			}
		});
		
		// �Ҳ����Ա��ɾ����ť
		attributeDelBtn.addSelectionListener(new SelectionAdapter() {
			
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
    			PathElement pathElement = (PathElement) selectorEditorTable.getItem(selectorEditorTable.getSelectionIndex()).getData();
				if(pathElement.getAttributes().size()>1){
					IStructuredSelection s = (IStructuredSelection) selectorAttrTableViewer.getSelection();
	                if (!s.isEmpty()) {
	                	for (Iterator<PathAttribute> it = s.iterator(); it.hasNext();) {
	                		PathAttribute attribute = (PathAttribute) it.next();
	                		selectorAttrTableViewer.remove(attribute);   
	                    	List<PathAttribute> list = (List<PathAttribute>)selectorAttrTableViewer.getInput();
	                    	list.remove(attribute);
	                    }
	                	TableItem[] selectorAttrTableItems = selectorAttrTable.getItems();
	                	PathAttribute protectedAttr = PathElementHelper.getControlType(path.isWeb(),pathElement);
		    			pathElement.getAttributes().clear();
		    			pathElement.getAttributes().add(protectedAttr);
		    			for (TableItem tableItem : selectorAttrTableItems) {
		    				PathAttribute attribute = (PathAttribute) tableItem.getData();
		    				if(!"".equals(attribute.getName())){
		    					pathElement.getAttributes().add(attribute);					
		    				}
		    			}
		    			selectorEditorTableViewer.refresh();
	                }
	                
				}
				changed = true;
			}
			
		});
	}
	
	/**
	 * ������ǰ����Path
	 * @return
	 */
	private Path buildCurrentPath(){
		TableItem[] items = selectorEditorTable.getItems();
		if(items.length>0){
			path.getElements().clear();
			for (TableItem tableItem : items) {
				PathElement element = (PathElement) tableItem.getData();
				//if(element.isEnable()){
					path.getElements().add(element);					
				//}
			}
			//Path �������
			
		}
		return path; 
	}
	/**
	 * ������ǰ����Path ȥ��δ��ѡ��PathElement
	 * @return
	 */
	private Path buildCurrentThinPath(){
		TableItem[] items = selectorEditorTable.getItems();
		if(items.length>0){
			path.getElements().clear();
			for (TableItem tableItem : items) {
				PathElement element = (PathElement) tableItem.getData();
				if(element.isEnable()){
					path.getElements().add(element);					
				}
			}
			//Path �������
			
		}
		return path; 
	}
	public void finishPickerLoadPath(final Path cpath){
		this.path = cpath;
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (isDisposed())
					return;
				
				setCursor(new Cursor(display, SWT.CURSOR_WAIT));
				refreshElementList(cpath);
				canFindPathStatus();
				setCursor(null);
			}
		});
		//directTree();
		
	}
	
	/**
	 * ��λ���ڵ�
	 * TODO liangxiao01
	 */
	public void directTree(){
		
		vistalTreeViewer.getControl().setRedraw(false);
		List<Object> segmentList = new ArrayList<Object>();
		if(this.path.isWeb()){
			TreeItem webRoot = vistalTreeViewer.getTree().getItems()[1];
			segmentList.add(webRoot.getData());
			TreePath tp = new TreePath(segmentList.toArray());
			TreeSelection ts = new TreeSelection(tp);
			vistalTreeViewer.setSelection(ts);
			try {
				List<String> runtimeIdList = new ArrayList<String>();
				WEBElement element = (WEBElement) findLibraryManager.findFirst(this.path, runtimeIdList);
				//����������ڵ㣬�г����������ʵ��

				webTreeWalker = new WEBTreeWalker(element.getBrowserId(), context);
				UINodeObject[] obs = (UINodeObject[]) expandSelection();
				for (int i = 0; i < obs.length; i++) {
					if(element.getBrowserId()==((WEBElement) obs[i].getUiElement()).getBrowserId()){
						segmentList.add(obs[i]);
						tp = new TreePath(segmentList.toArray());
						ts = new TreeSelection(tp);
						vistalTreeViewer.setSelection(ts);
						break;
					}
				}
				//j��2��ʼ 0�����������UI���� 1��HTml�ڵ㣬��Browserid����λ
				for (int j = 2; j < this.path.getElements().size(); j++) {
					obs = (UINodeObject[]) expandSelection();
					for (int i = 0; i < obs.length; i++) {
							if(Long.toString(((WEBElement)obs[i].getUiElement()).getElementId()).equals(this.path.getElements().get(j).getRuntimeId())){
								//У��RuntimeId
								segmentList.add(obs[i]);
								tp = new TreePath(segmentList.toArray());
								ts = new TreeSelection(tp);
								vistalTreeViewer.setSelection(ts);
							}
					}
				}
				
			} catch (LibraryException e) {
				e.printStackTrace();
			}
		}else{
			TreeItem uiRoot = vistalTreeViewer.getTree().getItems()[0];
			segmentList.add(uiRoot.getData());
			TreePath tp = new TreePath(segmentList.toArray());
			TreeSelection ts = new TreeSelection(tp);
			vistalTreeViewer.setSelection(ts);

			try {
				List<String> runtimeIdList = new ArrayList<String>();
				findLibraryManager.findFirst(this.path, runtimeIdList);
				for (int j = 0; j < this.path.getElements().size(); j++) {
					UINodeObject[] obs = (UINodeObject[]) expandSelection();
					for (int i = 0; i < obs.length; i++) {
							Properties p = walker.getProperties(obs[i].getUiElement());
							if(p.getProperty("RuntimeId").equals(runtimeIdList.get(j))){
								//У��RuntimeId
								segmentList.add(obs[i]);
								tp = new TreePath(segmentList.toArray());
								ts = new TreeSelection(tp);
								vistalTreeViewer.setSelection(ts);
							}
					}
				}
			} catch (LibraryException e) {
				e.printStackTrace();
			}
			
		}
		vistalTreeViewer.getControl().setRedraw(true);
		
	}
	private Object[] expandSelection(){
		Object sel = ((IStructuredSelection) vistalTreeViewer.getSelection()).getFirstElement();
		ITreeContentProvider provider = (ITreeContentProvider) vistalTreeViewer.getContentProvider();
	    //if (!provider.hasChildren(sel))
	    //	return null;
	    if (!vistalTreeViewer.getExpandedState(sel))
	    	vistalTreeViewer.expandToLevel(sel, 1);
	    return provider.getChildren(sel);
	    	//vistalTreeViewer.collapseToLevel(sel, AbstractTreeViewer.ALL_LEVELS);
	    	//else
	}

	/**
	 * ����Path ����ǰ����
	 * @param path
	 */
	public void loadPath(final Path cpath){
		this.path = cpath;
		getDisplay().asyncExec(new Runnable() {
			public void run() {
				refreshElementList(cpath);
			}
		});
		new Thread(new Runnable(){
		    public void run(){
		    	List<String> runtimeIdList = new ArrayList<String>();
		    	try {
		    		IUIElement element = findLibraryManager.findFirst(cpath, runtimeIdList);
					if (element != null) {
						Path _path = element.getPath();
						List<PathElement>  elList = _path.getElements();
						for (PathElement pathElement : elList) {
							if(pathElement.getRuntimeId()==null)
								continue;
							for (int i = 0; i < runtimeIdList.size(); i++) {
								if(runtimeIdList.get(i)==null)
									continue;
								if(runtimeIdList.get(i).equals(pathElement.getRuntimeId())){
									pathElement.getAttributes().clear();
									pathElement.getAttributes().addAll(cpath.getElements().get(i).getAttributes());
									break;
								}
							}
						}
						path = _path;
						getDisplay().asyncExec(new Runnable() {
							public void run() {
								canFindPathStatus();
								refreshElementList(path);
							}
						});
					}
		    	} catch (Exception e) {
					e.printStackTrace();
					getDisplay().asyncExec(new Runnable() {
						public void run() {
							canotFindPathStatus();
						}
					});
				}
		    }
		}).start();
	}

	/**
	 * У�鵱ǰ�����Path�Ƿ���� 
	 * @param path
	 * @return
	 */
	private Boolean checkPathExist(Path path){
		Boolean exist;
		try {
			//�ж�path�Ƿ����
			exist = findLibraryManager.exists(path);
		} catch (Exception e1) {
			exist = false;
			e1.printStackTrace();
		}
		if(exist){
			canFindPathStatus();
		}else{
			canotFindPathStatus();
		}
		return exist;
	}
	/**
	 * �ܶ�λ����ǰpath����� 
	 * ��ɫ��ť״̬����
	 */
	private void canFindPathStatus(){
		if(path.getElements().size()==0){
			hightlightButton.setEnabled(false);
			checkButton.setImage(STATUS_WARING);	
		}else{
			hightlightButton.setEnabled(true);
			checkButton.setImage(STATUS_SUCCESS);			
		}
	}
	/**
	 * ���ܶ�λ����ǰpath����� 
	 * ��ɫ��ť״̬������
	 */
	private void canotFindPathStatus(){
		if(path.getElements().size()==0){
			hightlightButton.setEnabled(false);
			checkButton.setImage(STATUS_WARING);	
		}else{
			hightlightButton.setEnabled(false);
			checkButton.setImage(STATUS_ERROR);			
		}
	}

	
	/**
	 * ���ö���5����ť����Ӧ
	 */
	private void setButtonsAction() {
		// ���水ť�¼�
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				buildCurrentThinPath(); //������ǰ����Path
				saveMode = true;
				changed = false;
				PickerShell.this.close();
			}
		});
		//�˳���ť�¼�
		exitButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PickerShell.this.close();
			}
		});
		//У�鰴ť�¼�
		checkButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				buildCurrentPath(); //������ǰ����Path
				checkPathExist(path);
				//directTree();
			}
		});
		screebSelectButton.addSelectionListener(new PickerSelectionListener(findWindow));
		
		//Ԫ�ظ�����ť
		hightlightButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//�ȹ���Path ��Ȼ�����Ƿ����/��ȡRect ��󻭿�
				buildCurrentPath(); //������ǰ����Path
				try {
					drawerLibrary.endDraw();
					if(!hightlightState){
						if(checkPathExist(path)){
							IUIElement element =  findLibraryManager.findFirst(path);
							if (element != null) {
								hightlightButton.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
								hightlightButton.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
								
								Rect rect = findLibraryManager.getRect(element);
								if (rect != null) {
									drawerLibrary.startDraw(rect, "#0000FF");
									hightlightState = true;
								}
							}
						}
					} else {
						drawerLibrary.endDraw();
						hightlightButton.setBackground(null);
						hightlightButton.setForeground(null);
						hightlightState = false;
					}
				} catch (LibraryException e1) {
					e1.printStackTrace();
				}
			}
		});
		browserSelection.addSelectionListener(new BrowserDropdownListener(browserSelection));
	}
	
	/**
	 * ����һ��·���������ڵ��б���ص��в����򣬶��Ҳ�����ֻ��ѡ��ʱ���ٻؼ�������
	 * @param path
	 */
	private void refreshElementList(Path path){
		selectorEditorTable.removeAll();
		List<PathElement> pathList = path.getElements();
		//ֱ�ӽ�pathList �ŵ�selectorEditorTableViewer�н�����Ⱦ
		selectorEditorTableViewer.setInput(pathList);
		selectorAttrTableViewer.setInput(null);
		selectorEditorTableViewer.refresh();
		selectorAttrTableViewer.refresh();
	}

	
	protected void checkSubclass() {}
	public void dispose() {super.dispose();}

	
	/**
	 * ����ǰ����ά����Path�����������������水ťʱ��
	 * @return
	 */
	public Path getPickerPath(){
		return this.path;
	}
	public Boolean isSaveMode(){
		return this.saveMode;
	}
	public void releasePicker() {
		this.context.close();
		try {
			this.drawerLibrary.endDraw();
		} catch (LibraryException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @return true ��ʶ���Թر�  false ��ʶ�����Թر�
	 */
	Boolean callSaveDialog(){
		if(changed){
			NeesSaveDialog saveDialog = new NeesSaveDialog(getShell());
			int status = saveDialog.open();
			if(status == IDialogConstants.NEXT_ID){
				buildCurrentThinPath(); //������ǰ����Path
				saveMode = true;
				return true;
			}else if(status == IDialogConstants.FINISH_ID){
				return false;
			}else if(status == IDialogConstants.BACK_ID){
				if(storePath!=null ){
					this.path = (Path) storePath.clone();
				}
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
}
