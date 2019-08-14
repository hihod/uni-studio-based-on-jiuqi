package com.jiuqi.rpa.widgets.uipicker;

/**
 * 继承shell的 UI选择器
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
	private Path path = new Path();//当前活跃的Path
	private Boolean saveMode = false;//用来判断当前界面的Path是否要保存
	Boolean changed = false;//用来判断当前界面是否有做修改
	//5个顶部按钮 
	private Button saveButton;
	private Button exitButton;
	private Button checkButton;
	private Button screebSelectButton;
	private Button hightlightButton;
	//弹窗自己创建的Context
	private Context context;
	//左侧树形，带有右键菜单
	private TreeViewer vistalTreeViewer;
	private Tree vistalTree;
	//RPA Walker
	private FindLibraryManager findLibraryManager;
	private WebBrowserManager webBrowserManager = WebBrowserManager.getInstance();
	private UIADrawerLibrary drawerLibrary;
	private Boolean hightlightState = false;
	//左侧属性表格
	private TableViewer nodeAttrTableViewer;
	private Table nodeAttrTable;
	SashForm exploreSashForm;
	Composite hideSlideBar;
	GridData gd_hideSlideBar;
	//中部编辑器表格
	private TableViewer selectorEditorTableViewer;
	private Table selectorEditorTable;
	private static String selectorEditorColumnCheck = "CHECK";
	private static String selectorEditorColumnPath = "PATH";
	//右侧选择器属性表格
	private TableViewer selectorAttrTableViewer;
	private Table selectorAttrTable;
	private static String selectorAttrColumnCheck = "CHECK";
	private static String selectorAttrColumnKey = "KEY";
	private static String selectorAttrColumnValue = "VALUE";
	private SashForm nodeAttrSashForm;
	// 中间部分编辑器 	selectorEditorTable的容器
	private Composite editorSashForm;
	// 右侧属性视图  	selectorAttrTable的容器
	private Composite attributeContainer;
	private Composite attributeComp;
	private Button attributeAddBtn;
	private Button attributeDelBtn;
	//用来校验右侧表格当前key是否重复，设置成全局变量
	List<PathAttribute> attrInputList = null;
	private UIATreeWalker walker;
	private WEBTreeWalker webTreeWalker;
	Path storePath = null;
	private Button browserSelection;//选取浏览器
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
	 * 再次编辑，加载Path定位
	 * 
	 * @param parentShell
	 * @param path
	 * @wbp.parser.constructor
	 */
	protected PickerShell(Shell parentShell, Path path) {
		this(parentShell, path, false);
	}
	
	/**
	 * 再次编辑，加载Path定位
	 * 
	 * @param parentShell
	 * @param path
	 * @param findMode
	 */
	protected PickerShell(Shell parentShell, Path path, boolean findWindow) {
		super(parentShell, SWT.SHELL_TRIM);
		this.findWindow = findWindow;
		storePath = (Path) path.clone();
		
		context = new Context();// 弹窗维护一个Context
		walker = new UIATreeWalker(context);
		findLibraryManager = new FindLibraryManager(context);	
		drawerLibrary = new UIADrawerLibrary();
		createContents();// 绘制UI
		centerShell(PickerShell.this.getDisplay(), PickerShell.this.getShell());// 控制界面居中
		loadPath(path);
		//directTree();
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		setText("UI选择器");
		setSize(1280, 800);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite container = new Composite(this, SWT.NONE);
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.verticalSpacing = 3;
		gl_container.marginHeight = 3;
		container.setLayout(gl_container);

		// 定义顶部按钮组
		Composite topButtonsComp = new Composite(container, SWT.NONE);
		RowLayout rl_topButtonsComp = new RowLayout(SWT.HORIZONTAL);
		rl_topButtonsComp.spacing = 5;
		topButtonsComp.setLayout(rl_topButtonsComp);
		topButtonsComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		saveButton = new Button(topButtonsComp, SWT.NONE);
		saveButton.setLayoutData(new RowData(60, SWT.DEFAULT));
		saveButton.setText("保存");
		exitButton = new Button(topButtonsComp, SWT.NONE);
		exitButton.setLayoutData(new RowData(60, SWT.DEFAULT));
		exitButton.setText("退出");
		checkButton = new Button(topButtonsComp, SWT.NONE);
		checkButton.setLayoutData(new RowData(60, SWT.DEFAULT));
		checkButton.setImage(STATUS_WARING);
		checkButton.setText("校验");
		screebSelectButton = new Button(topButtonsComp, SWT.NONE);
		screebSelectButton.setLayoutData(new RowData(60, SWT.DEFAULT));
		screebSelectButton.setText("屏幕选择");
		hightlightButton = new Button(topButtonsComp, SWT.TOGGLE);
		hightlightButton.setText("突出显示");
		browserSelection = new Button(topButtonsComp, SWT.NONE);
		browserSelection.setText("启动浏览器");
		// 设置按钮响应
		setButtonsAction();
		// UI选择器主体布局
		Composite mainComp = new Composite(container, SWT.BORDER);
		mainComp.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_mainComp = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_mainComp.widthHint = 459;
		mainComp.setLayoutData(gd_mainComp);
		// 将SashForm 包裹起来，保证内部SashForm能够进行拖拽
		SashForm mainMoveProvider = new SashForm(mainComp, SWT.NONE);
		// 左侧sashForm
		exploreSashForm = new SashForm(mainMoveProvider, SWT.VERTICAL);
		// 左侧sashForm A （A和B能上下拖拽）
		Composite nodeTreeSashForm =  new Composite(exploreSashForm, SWT.NONE);
		//nodeTreeSashForm.setBackground(SWTResourceManager.getColor(255, 255, 255));
		GridLayout gl_nodeTreeSashForm = new GridLayout(1, false);
		gl_nodeTreeSashForm.horizontalSpacing = 0;
		gl_nodeTreeSashForm.marginHeight = 0;
		gl_nodeTreeSashForm.marginWidth = 0;
		gl_nodeTreeSashForm.verticalSpacing = 0;
		nodeTreeSashForm.setLayout(gl_nodeTreeSashForm);
		//树形上部收起按钮
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
		treeLabel.setText("UI树");
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
		
		// 左侧sashForm B （A和B能上下拖拽）
		nodeAttrSashForm = new SashForm(exploreSashForm, SWT.BORDER);
		exploreSashForm.setWeights(new int[] {450, 260});

		// 中间部分编辑器
		editorSashForm = new Composite(mainMoveProvider, SWT.BORDER);
		// 右侧属性视图
		attributeContainer = new Composite(mainMoveProvider, SWT.NONE);
		mainMoveProvider.setWeights(new int[] {300, 700, 244});
		
		initAllTableFramework();//同时初始化三个表格信息，不包括填充数据
		
		try {
			buildlTree();// 构建左侧树形
			buildlPopList();// 构建左侧树形右键菜单
			vistalTreeViewer.addSelectionChangedListener(new UITreeSelectionChangedListener(this));// 增加树形事件监听   
			setAttributeTableBtnsAction();
		} catch (LibraryException e) {
			e.printStackTrace();
		}
		

	}
	/**
	 * 初始化所有表格的表格信息
	 */
	private void initAllTableFramework(){
		TableLayout layout1 = new TableLayout(); // 专用于表格1的布局
		TableLayout layout2 = new TableLayout(); // 专用于表格2的布局
		TableLayout layout3 = new TableLayout(); // 专用于表格3的布局
		
		// 构造左侧属性列表， 表头两列，可控制宽度，点击左侧树形刷新该表内容
		//节点属性表格
		nodeAttrTableViewer = new TableViewer(nodeAttrSashForm,  SWT.FULL_SELECTION );
		nodeAttrTable = nodeAttrTableViewer.getTable();
		nodeAttrTable.setLinesVisible(true);
		nodeAttrTable.setHeaderVisible(true);
		nodeAttrTable.setLayout(layout1);
		layout1.addColumnData(new ColumnWeightData(100));
		new TableColumn(nodeAttrTable, SWT.NONE).setText("属性");
		layout1.addColumnData(new ColumnWeightData(190));
		new TableColumn(nodeAttrTable, SWT.NONE).setText("值");
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
		//默认隐藏的hideSlideForm
		//hideSlideBar默认隐藏，当树形隐藏的时候hideSlideBar才显示出来
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
		//hideSlideBar中的2个按钮 上下隐藏，左右隐藏
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
		//主体Path 表格
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
		
		//右侧Path属性表格
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
		attributeLabel.setText("属性");
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
		//最开始隐藏左侧列表
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
	 * Path 中间表格可编辑单元 
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
				//加载右侧表格内容
				attrInputList = PathElementHelper.getEditableAttribute(path.isWeb(),pathElemeng);
				selectorAttrTableViewer.setInput(attrInputList);
				setSelectorAttrTableCellEditorDie();	
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	/**
	 * Attribute  右侧表格可编辑单元 能检测到节点情况
	 * @param properties 
	 * @param strarr 下拉内容
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
				//刷新中部表格内容(获取右部属性列表，构建AttributeList 赋值给PathElement 然后刷新中部表格)
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
	 * Attribute  右侧表格可编辑单元 不能检测到节点情况
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
					return "属性名称不合法";
				if (isNameExist(str))
					return "属性名称重复";
				return null;
			}
			
		});
		selectorAttrTableViewer.setCellEditors(cellEditor);
		selectorAttrTableViewer.setColumnProperties(new String[] { selectorAttrColumnCheck, selectorAttrColumnKey, selectorAttrColumnValue });
		selectorAttrTableViewer.setCellModifier(new RPASelectorAttrCellModifier());
	}
	
	/**
	 * 校验右侧表格Key重复
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
	 * 改变中间Path表格勾选提供器
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
	 * 改变右部Attribute表格勾选提供器
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
			//刷新中部表格内容(获取右部属性列表，构建AttributeList 赋值给PathElement 然后刷新中部表格)
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
	 * 构造左侧节点树形
	 * @throws LibraryException 
	 * 
	 * @require buildTreeItems()
	 */
	private void buildlTree() throws LibraryException {
		//构造根节点
		TreeNode rootTreeVirtualItem = new TreeNode(null);
		rootTreeVirtualItem.setChildren( new TreeNode[]{});
		//树形标签提供器
		vistalTreeViewer.setLabelProvider(new UITreeLabelProvider(context));
		//树形内容提供器
		vistalTreeViewer.setContentProvider(new UITreeContentProvider(context));
		vistalTreeViewer.setInput(rootTreeVirtualItem);
	}

	/**
	 * 构建右键菜单
	 */
	private void buildlPopList() {
		Menu menu = new Menu(vistalTree);
		MenuItem selectTreeNode = new MenuItem(menu, SWT.PUSH);
		MenuItem freshTreeNode = new MenuItem(menu, SWT.PUSH);
		selectTreeNode.setText("选定节点");
		selectTreeNode.addSelectionListener(new UITreeSetAsTargetListener(this));
		freshTreeNode.setText("刷新节点");
		freshTreeNode.addSelectionListener(new UITreeRefreshNodeListener(this));
		vistalTree.setMenu(menu);

	}
	
	private void setAttributeTableBtnsAction(){
		// 右侧属性表格添加按钮
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
					/**开始获取当前选中properties**************************************************************************/
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
						System.out.println("构建Path时候无法找到节点元素，跳过:[PickerShell.java]"+e1.getMessage());
					}
					/***结束获取当前选中properties*************************************************************************/
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
		
		// 右侧属性表格删除按钮
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
	 * 构建当前环境Path
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
			//Path 构建完毕
			
		}
		return path; 
	}
	/**
	 * 构建当前环境Path 去掉未勾选的PathElement
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
			//Path 构建完毕
			
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
	 * 定位树节点
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
				//打开浏览器根节点，列出所有浏览器实例

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
				//j从2开始 0是浏览器窗口UI对象 1是HTml节点，用Browserid来定位
				for (int j = 2; j < this.path.getElements().size(); j++) {
					obs = (UINodeObject[]) expandSelection();
					for (int i = 0; i < obs.length; i++) {
							if(Long.toString(((WEBElement)obs[i].getUiElement()).getElementId()).equals(this.path.getElements().get(j).getRuntimeId())){
								//校验RuntimeId
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
								//校验RuntimeId
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
	 * 加载Path 到当前界面
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
	 * 校验当前表格中Path是否存在 
	 * @param path
	 * @return
	 */
	private Boolean checkPathExist(Path path){
		Boolean exist;
		try {
			//判断path是否存在
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
	 * 能定位到当前path的情况 
	 * 绿色按钮状态可用
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
	 * 不能定位到当前path的情况 
	 * 红色按钮状态不可用
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
	 * 设置顶部5个按钮的响应
	 */
	private void setButtonsAction() {
		// 保存按钮事件
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				buildCurrentThinPath(); //构建当前环境Path
				saveMode = true;
				changed = false;
				PickerShell.this.close();
			}
		});
		//退出按钮事件
		exitButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PickerShell.this.close();
			}
		});
		//校验按钮事件
		checkButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				buildCurrentPath(); //构建当前环境Path
				checkPathExist(path);
				//directTree();
			}
		});
		screebSelectButton.addSelectionListener(new PickerSelectionListener(findWindow));
		
		//元素高亮按钮
		hightlightButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//先构建Path ，然后检测是否存在/获取Rect 最后画框
				buildCurrentPath(); //构建当前环境Path
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
	 * 接收一个路径，，将节点列表加载到中部区域，而右部区域只有选中时候再回加载内容
	 * @param path
	 */
	private void refreshElementList(Path path){
		selectorEditorTable.removeAll();
		List<PathElement> pathList = path.getElements();
		//直接将pathList 放到selectorEditorTableViewer中进行渲染
		selectorEditorTableViewer.setInput(pathList);
		selectorAttrTableViewer.setInput(null);
		selectorEditorTableViewer.refresh();
		selectorAttrTableViewer.refresh();
	}

	
	protected void checkSubclass() {}
	public void dispose() {super.dispose();}

	
	/**
	 * 将当前界面维护的Path传给父级，仅当保存按钮时候
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
	 * @return true 标识可以关闭  false 标识不可以关闭
	 */
	Boolean callSaveDialog(){
		if(changed){
			NeesSaveDialog saveDialog = new NeesSaveDialog(getShell());
			int status = saveDialog.open();
			if(status == IDialogConstants.NEXT_ID){
				buildCurrentThinPath(); //构建当前环境Path
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
