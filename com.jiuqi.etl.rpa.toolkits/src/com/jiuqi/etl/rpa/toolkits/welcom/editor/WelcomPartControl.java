package com.jiuqi.etl.rpa.toolkits.welcom.editor;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.model.bean.ControlFlowBean;
import com.jiuqi.etl.rpa.toolkits.template.TemplateManager;
import com.jiuqi.etl.rpa.toolkits.welcom.bean.ExampleBean;
import com.jiuqi.etl.rpa.toolkits.welcom.bean.ProcessTemplateBean;
import com.jiuqi.etl.rpa.toolkits.welcom.manager.WelcomContentManager;
import com.jiuqi.etl.rpa.toolkits.welcom.manager.WelcomControlflowManager;
import com.jiuqi.etl.rpa.toolkits.welcom.utils.MessageBoxUtils;
import com.jiuqi.etl.ui.editor.RecentEditorItem;
import com.jiuqi.etl.ui.editor.RecentEditorsManager;


/**
 * 创建欢迎界面
* @author 作者：houzhiyuan 2019年6月14日 上午8:47:30
*/
public class WelcomPartControl {
	public static final String PLUGIN_ID = "com.jiuqi.etl.rpa.toolkits";
	
	public static final String IMG_TOP_BACKGROUND = "icons/welcom/welcom-bg.jpg";
	public static final String IMG_TOP_TITLE = "icons/welcom/welcom-title.png";
	public static final String ICON_HISTORY_ITEM = "icons/welcom/history-normal.png";
	public static final String ICON_HISTORY_ITEM_ADD = "icons/openbrowser16-01.png";
	public static final String ICON_EXAMPLE_ITEM = "icons/welcom/example-normal.png";
	public static final String IMG_DELETE = "icons/welcom/welcom-del.png";
	public static final String IMG_DELETE_SEL = "icons/welcom/welcom-del-sel.png";
	public static final String IMG_ADD = "icons/welcom/welcom-add.png";
	
	public static final String IMG_PROCESS_GROUP = "icons/welcom/process-group.png";
	public static final String IMG_PROCESS_ITEM =  "icons/welcom/process-item.png";
	
	public static final String IMG_BUTTON_NEW = "icons/welcom/button-new-normal.png";
	public static final String IMG_BUTTON_NEW_HOVER = "icons/welcom/button-new-hover.png";
	
	public static final String FONT_FAMALLY = "Microsoft YaHei";
	/**
	 * 最近打开历史记录显示个数
	 */
	public static final int COUNT_RECENT_ITEM = 10;
	
	
	private Composite parent;
	private FormToolkit toolKit;
	private ImageRegistry imgReg;
	
	private Section historySection;
	private Composite historyComposite;
	private Composite actionComposite;
	
	private WelcomContentManager contentManager;
	private Section actionSection;
	private List<Color> colors = new ArrayList<Color>();
	private List<Font> fonts = new ArrayList<Font>();
	
	

	public WelcomPartControl(Composite parent) {
		this.parent = parent;
		contentManager = new WelcomContentManager();
	}

	public void create() throws Exception{
		toolKit = new FormToolkit(this.parent.getDisplay());
		//初始化图标
		imgReg = new ImageRegistry();
		registerImg();
		
		//创建两行,上下两部分，下部分充满
		GridLayout baseGL = new GridLayout();
		baseGL.numColumns =1;
		baseGL.marginLeft = 0;
		baseGL.marginRight = 0;
		baseGL.marginHeight = 0;
		baseGL.marginWidth = 0;
		baseGL.marginBottom = 10;
		baseGL.verticalSpacing =0;
		parent.setLayout(baseGL);
		
		//top
		Composite top = new Composite(parent, SWT.NONE );//SWT.BORDER| SWT.DOWN
		
		
		GridData topGD = new GridData(GridData.FILL_HORIZONTAL);
		
		topGD.heightHint = 90;//控件高度
		top.setLayoutData(topGD);
		//TODO: 创建顶部内容
		createTopControl(top);
		
		//bottom
		Composite bottom  = new Composite(this.parent, SWT.NONE);
		GridLayout bottomLayout = new GridLayout();
		bottomLayout.numColumns = 2;
		bottomLayout.makeColumnsEqualWidth = true;
		bottomLayout.marginHeight = 0;
		bottomLayout.marginWidth = 0;
		bottomLayout.marginLeft = 15;
		bottomLayout.marginRight = 15;
		bottomLayout.marginTop = 10;
		bottom.setLayout(bottomLayout);
		bottom.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//left-bottom
		Composite left = new Composite(bottom, SWT.NONE);
		GridData leftGD = new GridData(GridData.FILL_BOTH);
		left.setLayoutData(leftGD);
		createLeftControl(left);
		
		//right-bottom
		Composite right = new Composite(bottom, SWT.NONE);
		GridData rightGD = new GridData(GridData.FILL_BOTH);
		right.setLayoutData(rightGD);
		createRightControl(right);
		
		//root
		Composite root = new Composite(parent, SWT.NONE);
		GridData gd_root = new GridData();
		//gd_root.heightHint = 30;
		root.setLayoutData(gd_root);
		createRootControl(root);
		
	}
	
	public void close() throws Exception{
		if(toolKit != null) {
			toolKit.dispose();
		}
		if(this.imgReg != null) {
			this.imgReg.dispose();
		}
		if(this.colors.size() > 0) {
			for(Color c: colors) {
				c.dispose();
			}
		}
		if(this.fonts.size() >0) {
			for(Font f: fonts) {
				f.dispose();
			}
		}
	}
	private void registerImg() throws Exception{
	
		this.imgReg.put(IMG_TOP_BACKGROUND, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_TOP_BACKGROUND));
		this.imgReg.put(IMG_TOP_TITLE, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_TOP_TITLE));
		this.imgReg.put(ICON_HISTORY_ITEM, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, ICON_HISTORY_ITEM));
		ImageDescriptor imgDelDesc = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_DELETE); 
		this.imgReg.put(IMG_DELETE, imgDelDesc);
		this.imgReg.put(IMG_DELETE_SEL, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_DELETE_SEL));
		
		this.imgReg.put(IMG_ADD, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_ADD));
		this.imgReg.put(ICON_HISTORY_ITEM_ADD, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, ICON_HISTORY_ITEM_ADD));
		
		this.imgReg.put(IMG_PROCESS_GROUP, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_PROCESS_GROUP));
		this.imgReg.put(IMG_PROCESS_ITEM, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_PROCESS_ITEM));
		
		this.imgReg.put(ICON_EXAMPLE_ITEM, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, ICON_EXAMPLE_ITEM));
		
		this.imgReg.put(IMG_BUTTON_NEW, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_BUTTON_NEW));
		this.imgReg.put(IMG_BUTTON_NEW_HOVER, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_BUTTON_NEW_HOVER));
		
	}
	private Color createColor(int r, int g, int b) {
		Color c = new Color(parent.getDisplay(), new RGB(r, g, b));
		this.colors.add(c);
		return c;
	}
	private Font createFont(int size, int style) {
		Font f = new Font(this.parent.getDisplay(), FONT_FAMALLY, size, style);
		fonts.add(f);
		return f;
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	private void createTopControl(Composite parent) throws Exception{
		GridLayout parentLayout = new GridLayout();
		parentLayout.numColumns = 2;
		parentLayout.marginWidth = 0;
		parentLayout.marginHeight = 0;
		parentLayout.horizontalSpacing = 0;
		parentLayout.verticalSpacing = 0;
		
		parent.setLayout(parentLayout);
		
		final Image imgTitle = this.imgReg.get(IMG_TOP_TITLE);
		GridData titleGD = new GridData();
		titleGD.widthHint = 382;
		titleGD.heightHint = 90;
		
		Color bgColor = createColor(2,81,88);
		Composite titleC = new Composite(parent, SWT.NONE);
		titleC.setBackground(bgColor);
		titleC.setBackgroundImage(imgTitle);
		titleC.setLayoutData(titleGD);
		
		Composite otherC = new Composite(parent, SWT.NONE);
		GridData gd_otherC =  new GridData(GridData.FILL_HORIZONTAL);
		gd_otherC.heightHint  =90;
		otherC.setLayoutData(gd_otherC);
		otherC.setBackground(bgColor);
		
	}
	
	private void createRootControl(Composite parent) throws Exception{
		
		Color color_check = createColor(83, 83, 83);
		GridLayout gl = new GridLayout(2, false);
		gl.marginLeft = 15;
		/*gl.marginBottom = 10;*/
		/*gl.marginHeight = 0;
		gl.marginWidth = 0;*/
		parent.setLayout(gl);
		final Button btnCheckButton =  new Button(parent, SWT.CHECK);
		btnCheckButton.setForeground(color_check);
		btnCheckButton.setText("");
		boolean isOpen = this.contentManager.getOpenEnable();
		btnCheckButton.setSelection(isOpen);
		btnCheckButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				Button btn = (Button)e.getSource();
				boolean seltion = btn.getSelection();
				contentManager.setOpenEnable(seltion);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		//fixSetBackground(btnCheckButton);
		Label l = new Label(parent, SWT.NONE);
		l.setText("启动时显示该窗口");
		l.setForeground(color_check);
		l.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
			}
			
			public void mouseDown(MouseEvent e) {
				boolean seltion = btnCheckButton.getSelection();
				btnCheckButton.setSelection(!seltion);
				contentManager.setOpenEnable(!seltion);
			}
			
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
	}

		
	private void createLeftControl(Composite parent) throws Exception{
		GridLayout container = new GridLayout();
		container.numColumns = 1;
		parent.setLayout(container);
		
		//上部按钮
		GridData topGD = new GridData(SWT.LEFT);
		topGD.heightHint = 45;
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayoutData(topGD);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 1;
		topLayout.marginWidth = 0;
		topLayout.marginBottom = 10;
		topComposite.setLayout(topLayout);
		
		GridData btnsGridData = new GridData();
		btnsGridData.widthHint = 115;
		btnsGridData.heightHint = 32;
		
		
		final Composite c_btnNew = this.toolKit.createComposite(topComposite);
		c_btnNew.setLayoutData(btnsGridData);
		c_btnNew.setBackgroundImage(this.imgReg.get(IMG_BUTTON_NEW));
		c_btnNew.addMouseTrackListener(new MouseTrackListener() {
			
			public void mouseHover(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExit(MouseEvent e) {
				c_btnNew.setCursor(new Cursor(null, SWT.CURSOR_ARROW));
				c_btnNew.setBackgroundImage(imgReg.get(IMG_BUTTON_NEW));
			}
			
			public void mouseEnter(MouseEvent e) {
				c_btnNew.setCursor(new Cursor(null, SWT.CURSOR_HAND));
				c_btnNew.setBackgroundImage(imgReg.get(IMG_BUTTON_NEW_HOVER));
			}
		});
		c_btnNew.addMouseListener(new MouseListener() {
			
			public void mouseUp(MouseEvent e) {
			}
			
			public void mouseDown(MouseEvent e) {
				Display display = Display.getDefault();
				final NewControlFlowShell shell  = new NewControlFlowShell(display, "新建控制流", null);
				shell.addDisposeListener(new DisposeListener() {
					
					public void widgetDisposed(DisposeEvent e) {
						if(shell.isOK()) {
							ControlFlowBean flowBean = shell.getRestult();
							try {
								WelcomControlflowManager.newControlFlow(flowBean, shell.getSelectPath());
								//刷新打开历史记录
								refreshHistoryOpenContent();
							} catch (Exception e1) {
								MessageBoxUtils.showError("添加失败！");
								e1.printStackTrace();
							}
						}
					}
				});
				shell.open();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
				
			}
			
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		//下部 form
		GridData centerGD = new GridData(GridData.FILL_BOTH );
		Section section = this.toolKit.createSection(parent, Section.TITLE_BAR| SWT.BORDER);
		this.historySection = section;
		section.setText("最近打开");
		section.setFont(createFont(10, SWT.BOLD));
		section.setTitleBarForeground(createColor(2, 81, 88));
		section.setLayoutData(centerGD);
		
		
		section.setTitleBarBackground(createColor(202, 234, 236));
		section.setTitleBarBorderColor(createColor(212, 220, 221));
		/*section.setTitleBarGradientBackground(new org.eclipse.swt.graphics.Color(parent.getDisplay(),
				new RGB(0, 100, 100)));*/
		
		Composite sectionComposite = createHistoryComposite();
		this.historyComposite = sectionComposite;

		buildHistoryOpenContent(sectionComposite);
		//不设置不显示内容
		section.setClient(sectionComposite);
	}
	private Composite createHistoryComposite() throws Exception{
		Composite sectionComposite = this.toolKit.createComposite(this.historySection,SWT.NONE);
		GridLayout pLayout = new GridLayout();
		pLayout.numColumns = 1;
		sectionComposite.setLayout(pLayout);
		sectionComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		return sectionComposite;
	}
	
	private void createRightControl(Composite parent) throws Exception{
		Font sectionFont = createFont(10, SWT.BOLD);
		Color sectionColor = createColor(2, 81, 88);
		Color example_c = createColor(2, 81, 88);
		
		GridLayout containerLayout = new GridLayout();
		containerLayout.makeColumnsEqualWidth  = true;
		containerLayout.numColumns = 1;
		containerLayout.verticalSpacing = 15;
		parent.setLayout(containerLayout);
		
		GridData topGD = new GridData(GridData.FILL_HORIZONTAL);
		topGD.heightHint = 400;
		//topGD.grabExcessVerticalSpace = true;
		Section topSection = this.toolKit.createSection(parent, Section.TITLE_BAR |Section.EXPANDED | Section.TWISTIE |Section.FOCUS_TITLE);
		topSection.setLayoutData(topGD);
		topSection.setText("经典案例");
		topSection.setFont(sectionFont);
		topSection.setForeground(example_c);
		
		/*topSection.setTitleBarBackground(new org.eclipse.swt.graphics.Color(parent.getDisplay(),
				new RGB(0, 134, 139)));
		topSection.setTitleBarBorderColor(new org.eclipse.swt.graphics.Color(parent.getDisplay(),
				new RGB(0, 134, 139)));*/
		
		Composite exampleComposite = this.toolKit.createComposite(topSection, SWT.NONE);
		GridLayout pLayout = new GridLayout();
		pLayout.numColumns = 1;
		exampleComposite.setLayout(pLayout);
		GridData exampleGridData = new GridData(GridData.FILL_HORIZONTAL);
		//exampleGridData.heightHint = 100;
		exampleComposite.setLayoutData(exampleGridData);
		buildExample(exampleComposite);
		topSection.setClient(exampleComposite);
		
		GridData bottomGD = new GridData(GridData.FILL_BOTH);
		Section bottomSection = this.toolKit.createSection(parent, Section.TITLE_BAR);
		this.actionSection = bottomSection;
		bottomSection.setLayoutData(bottomGD);
		bottomSection.setText("通用流程包");
		bottomSection.setFont(sectionFont);
		bottomSection.setForeground(sectionColor);
		
		createProcessSectionToolbar(bottomSection);
		//设置颜色
		/*bottomSection.setTitleBarBackground(new org.eclipse.swt.graphics.Color(parent.getDisplay(),
				new RGB(0, 134, 139)));
		bottomSection.setTitleBarBorderColor(new org.eclipse.swt.graphics.Color(parent.getDisplay(),
				new RGB(0, 134, 139)));*/
		Composite actionComposite = createActionComposite();
		this.actionComposite = actionComposite;
		buildAction(actionComposite);
		bottomSection.setClient(actionComposite);
		
	}
	private Composite createActionComposite() {
		Composite actionComposite = this.toolKit.createComposite(this.actionSection, SWT.NONE);
		
		GridLayout pLayout2 = new GridLayout();
		pLayout2.numColumns = 1;
		actionComposite.setLayout(pLayout2);
		actionComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		return actionComposite;
	}
	private void refreshHistoryOpenContent()throws Exception{
		this.historyComposite.dispose();
		this.historyComposite = createHistoryComposite();
		buildHistoryOpenContent(historyComposite);
		this.historySection.setClient(historyComposite);
		this.historySection.layout();
	}
	//创建最近打开
	private void buildHistoryOpenContent(Composite parent)throws Exception{
		
		final ScrolledComposite sc_history = new ScrolledComposite(parent, SWT.V_SCROLL);
		sc_history.setBackground(createColor(255, 255, 255));
		final Composite sc_container = this.toolKit.createComposite(sc_history);
		
		sc_container.setLayout(new GridLayout(1, false));
		sc_container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		sc_history.setLayoutData(new GridData(GridData.FILL_BOTH));
		sc_history.setLayout(new GridLayout(1,false));
		
		Image itemImg = this.imgReg.get(ICON_HISTORY_ITEM);
		Image imgDel = imgReg.get(IMG_DELETE);
		
		try {
			RecentEditorItem[] rencentItems = RecentEditorsManager.getInstance().list();
			if(rencentItems != null) {
				int displayCount = COUNT_RECENT_ITEM;
				if(rencentItems.length < displayCount) {
					displayCount = rencentItems.length;
				}
				for(int i=0; i<displayCount; i++ ) {
					RecentEditorItem item = rencentItems[i];
					String url = buildHistoryItemUrl(item);
					String strDate = buildHistoryItemTime(item.getTimestamp());
					createHistoryItem(sc_container,itemImg, imgDel,
							item, url, strDate);
				}
			}
			/*//创建item
			createHistoryItem(sc_container,itemImg, imgDel,
					"流程XXXXXXX", "方案1 ： 控制流 / 业务数据 / 前端系统", "2019/6/13");
			createHistoryItem(sc_container,itemImg,imgDel,
					"流程YYYYYYY", "方案2 ： 控制流", "2019/6/13");
			createHistoryItem(sc_container,itemImg, imgDel,
					"产品订阅流", "/ 业务数据 / 前端系统", "2019/6/12");
			createHistoryItem(sc_container,itemImg,imgDel,
					"XXX资源订阅流程", "/ 业务数据 / 资产管理系统", "2019/6/1");
			createHistoryItem(sc_container,itemImg,imgDel,
					"销售数据交换流程", "/ 业务数据 / 资产管理系统", "2019/5/28");
			createHistoryItem(sc_container,itemImg,imgDel,
					"数据资源注册流程", "/ 业务数据 / 资产管理系统", "2019/5/19");
			createHistoryItem(sc_container,itemImg,imgDel,
					"资源注册审核流程", "/ 业务数据 / 资产管理系统", "2019/5/6");
			createHistoryItem(sc_container,itemImg,imgDel,
					"资源订阅审核流程", "/ 业务数据 / BI系统", "2019/5/4");*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sc_history.setContent(sc_container);
		sc_history.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				Point computPoint = sc_container.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = sc_history.getBounds().width;
				if(computPoint.y > sc_history.getBounds().height) {//出现滚动条后
					width -= 16;
				}
				
				sc_container.setSize(width, computPoint.y);
			}
			
			public void controlMoved(ControlEvent e) {}
		});
		Display.getCurrent().asyncExec(new Runnable() {
			
			public void run() {
				sc_history.getBounds();
				Point computPoint = sc_container.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = sc_history.getBounds().width;
				if(computPoint.y > sc_history.getBounds().height) {//出现滚动条后
					width -= 16;
				}
				sc_container.setSize(width, computPoint.y);
				
			}
		});
	}
	private String buildHistoryItemUrl(RecentEditorItem item) throws Exception{
		List<String> paths = item.getPath();
		
		StringBuffer sb = new StringBuffer();
		if(paths != null && paths.size() > 0) {
			sb.append(paths.get(0));
			for(int i=1; i<paths.size(); i++) {
				sb.append(" / ");
				sb.append(paths.get(i));
			}
		}
		
		String strPath = sb.toString();
		String url = item.getSchemaTitle();
		if(!StringUtils.isEmpty(strPath)) {
			url += " ： " + strPath;
		}
		return url;
	}
	private String buildHistoryItemTime(long timestamp) throws Exception{
		Date date = new Date(timestamp);
		SimpleDateFormat format  =new SimpleDateFormat("yyyy/M/dd");
		return format.format(date);
	}
	//创建最近打开item
	private void createHistoryItem(Composite parent,final Image img, final Image imgDel,
			final RecentEditorItem item, String url, String strDate) throws Exception{
		final Composite container = this.toolKit.createComposite(parent, SWT.NONE);
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		//gl.horizontalSpacing = 0;
		container.setLayout(gl);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		//创建第一列
		Label flagCanvas = this.toolKit.createLabel(container, "");
		flagCanvas.setImage(img);
		
		
		GridData flagGD = new GridData(SWT.BORDER);
		flagGD.heightHint = 40;
		flagGD.widthHint = 40;
		flagGD.verticalSpan = 2;
		
		flagCanvas.setLayoutData(flagGD);
		
		//标题
		Font fontTitle = createFont(12, SWT.NORMAL);
		final Color c_title = createColor(0,65, 70);
		final Color c_hover_title = createColor(4, 181, 198);
		final Label titleLabel = this.toolKit.createLabel(container, item.getName(), SWT.NONE);
		titleLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		titleLabel.setFont(fontTitle);
		titleLabel.setForeground(c_title);
		titleLabel.addMouseTrackListener(new MouseTrackListener() {
			public void mouseHover(MouseEvent e) {
				
			}
			public void mouseExit(MouseEvent e) {
				titleLabel.setForeground(c_title);
				titleLabel.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
			}
			public void mouseEnter(MouseEvent e) {
				titleLabel.setForeground(c_hover_title);
				titleLabel.setCursor(new Cursor(null,SWT.CURSOR_HAND));
			}
		});
		titleLabel.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
			}
			public void mouseDown(MouseEvent e) {
				try {
					WelcomControlflowManager.openRecentEditor(item);
					//刷新最新打开历史记录
					refreshHistoryOpenContent();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		//删除图标
		final Label delCanvas = this.toolKit.createLabel(container, "", SWT.NONE);
		delCanvas.setImage(imgDel);
		//delCanvas.setVisible(false);
		GridData delImgGD = new GridData();
		delImgGD.heightHint=16;
		delImgGD.widthHint = 16;
		delImgGD.horizontalAlignment = SWT.RIGHT;
		delCanvas.setLayoutData(delImgGD);
		
		delCanvas.addMouseTrackListener(new MouseTrackListener() {
			public void mouseHover(MouseEvent e) {}
			public void mouseExit(MouseEvent e) {
				delCanvas.setVisible(true);
				delCanvas.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
				delCanvas.setImage(imgReg.get(IMG_DELETE));
			}
			public void mouseEnter(MouseEvent e) {
				delCanvas.setImage(imgReg.get(IMG_DELETE_SEL));
				delCanvas.setCursor(new Cursor(null,SWT.CURSOR_HAND));
			}
		});
		delCanvas.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
			}
			public void mouseDown(MouseEvent e) {
				/*try {
					RecentEditorsManager.getInstance().remove(item);
					refreshHistoryOpenContent();
				} catch (Exception e1) {
					e1.printStackTrace();
					MessageBox msgError = new MessageBox(container.getShell(), SWT.OK|SWT.ICON_ERROR);
					msgError.setText("错误提示");
					msgError.open();
				}*/
				MessageBox msgTip = new MessageBox(container.getShell(), SWT.OK|SWT.CANCEL);
				msgTip.setText("提示");
				msgTip.setMessage("确认删除历史记录【" + titleLabel.getText() + "】？");
				if(msgTip.open() == SWT.OK) {
					try {
						RecentEditorsManager.getInstance().remove(item);
						refreshHistoryOpenContent();
					} catch (Exception e1) {
						e1.printStackTrace();
						MessageBox msgError = new MessageBox(container.getShell(), SWT.OK|SWT.ICON_ERROR);
						msgError.setText("错误提示");
						msgError.open();
					}
				}
			}
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		Font f2 = createFont(9, SWT.NORMAL);
		Color c2 = createColor(141,141,141);
		
		Label urlLabel = this.toolKit.createLabel(container, url, SWT.NONE);
		urlLabel.setFont(f2);
		urlLabel.setForeground(c2);
		urlLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL|SWT.BORDER));
		
		//创建第二列
		Label dateLabel = this.toolKit.createLabel(container, strDate, SWT.NONE);
		dateLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,1,1));
		dateLabel.setFont(f2);
		dateLabel.setForeground(c2);
		
		/*titleLabel.addMouseTrackListener(new historyItemMouseListener(delCanvas));
		urlLabel.addMouseTrackListener(new historyItemMouseListener(delCanvas));
		dateLabel.addMouseTrackListener(new historyItemMouseListener(delCanvas));*/
		
		/*container.addMouseTrackListener(new MouseTrackListener() {
			
			public void mouseHover(MouseEvent e) {
				delCanvas.setVisible(true);
				
			}
			
			public void mouseExit(MouseEvent e) {
				delCanvas.setVisible(false);
				
			}
			
			public void mouseEnter(MouseEvent e) {
				//delCanvas.setVisible(true);
			}
		});*/
		/*container.addMouseMoveListener(new MouseMoveListener() {
			
			public void mouseMove(MouseEvent e) {
				// TODO Auto-generated method stub
				Rectangle rec =  container.getBounds();
				if(e.x > rec.x && e.x < (rec.x + rec.width)
					&& e.y > rec.y && e.y < (rec.y + rec.height)) {//在范围内
					delCanvas.setVisible(true);
				}
				else {//不在范围内
					delCanvas.setVisible(false);
				}
			}
		});*/
	}
	
	
	private void buildExample(Composite parent) throws Exception{
		ScrolledForm scrolledFrom  = this.toolKit.createScrolledForm(parent);
		scrolledFrom.setMinHeight(50);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		FillLayout  fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		scrolledFrom.getBody().setLayout(fillLayout);
		
		Composite scrolledFromComposite = scrolledFrom.getBody();
		Image itemImg = this.imgReg.get(ICON_EXAMPLE_ITEM);
		
		List<ExampleBean> beans = this.contentManager.getExamples();
		if(beans != null && beans.size() > 0) {
			for(ExampleBean bean : beans) {
				createExampleItem(scrolledFromComposite, itemImg, bean, bean.getTitle(),bean.getDesc());
			}
		}
		/*else {//加载测试数据
			createExampleItem(scrolledFromComposite, itemImg,null, "我，机器人","这是一段案例介绍文字，本案例通过某企业销");
			createExampleItem(scrolledFromComposite, itemImg, null,"产品销售案例","这是一段案例介绍文字，本案例通过某企业销");
			createExampleItem(scrolledFromComposite, itemImg, null,"财务报销案例","这是一段案例介绍文字，本案例通过某企业销");
			createExampleItem(scrolledFromComposite, itemImg, null,"人力资源管理案例","这是一段案例介绍文字，本案例通过某企业销");
		}*/
		scrolledFrom.setContent(scrolledFromComposite);
		
	}

	private void createExampleItem(Composite parent, Image img,final ExampleBean bean, String text, String desc) throws Exception{
		Composite container = this.toolKit.createComposite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		//gl.makeColumnsEqualWidth = true;
		container.setLayout(gl);
		Label flagLabel = this.toolKit.createLabel(container, "", SWT.NONE);
		flagLabel.setImage(img);
		GridData gd_flagLabel = new GridData(SWT.NONE);
		gd_flagLabel.verticalSpan = 2;
		gd_flagLabel.widthHint = 36;
		gd_flagLabel.heightHint = 40;
		flagLabel.setLayoutData(gd_flagLabel);
		
		final Label titleLabel = this.toolKit.createLabel(container, text, SWT.NONE);
		
		GridData gd_titleLabel = new GridData();
		gd_titleLabel.horizontalAlignment = GridData.FILL_HORIZONTAL;
		titleLabel.setLayoutData(gd_titleLabel);
		
		Font f_title = createFont(10, SWT.BOLD);
		final Color c_title = createColor(0, 65, 70);
		final Color c_hover_title = createColor(4,181,198);
		titleLabel.setForeground(c_title);
		titleLabel.setFont(f_title);
		
		titleLabel.addMouseTrackListener(new MouseTrackListener() {
			public void mouseHover(MouseEvent e) {
				
			}
			public void mouseExit(MouseEvent e) {
				titleLabel.setForeground(c_title);
				titleLabel.setCursor(new Cursor(null, SWT.CURSOR_ARROW));
			}
			public void mouseEnter(MouseEvent e) {
				titleLabel.setForeground(c_hover_title);
				titleLabel.setCursor(new Cursor(null, SWT.CURSOR_HAND));
			}
		});
		
		titleLabel.addMouseListener(new MouseListener() {
			
			public void mouseUp(MouseEvent e) {
			}
			
			public void mouseDown(MouseEvent e) {
				// TODO 点击标题，打开控制流
				try {
					WelcomControlflowManager.execControlFlow(bean);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		Label descLabel = this.toolKit.createLabel(container,desc , SWT.NONE);
		descLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Font f_desc = createFont(9, SWT.NORMAL);
		Color c_desc = createColor(141, 141, 141);
		descLabel.setFont(f_desc);
		descLabel.setForeground(c_desc);
		/*ImageHyperlink hlImage = this.toolKit.createImageHyperlink(container,SWT.NONE);
		hlImage.setText(text);
		hlImage.setImage(img);
		hlImage.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));*/
	}
	private void refreshAction() throws Exception{
		this.actionComposite.dispose();
		this.actionComposite = createActionComposite();
		buildAction(this.actionComposite);
		//this.actionComposite.layout();
		this.actionSection.setClient(this.actionComposite);
		this.actionSection.layout();
	}
	private void buildAction(Composite parent) throws Exception{
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		sc.setBackground(createColor(255, 255, 255));
		sc.setLayoutData(new GridData(GridData.FILL_BOTH));
		sc.setLayout(new GridLayout(1,false));

		final Composite scrolledFromComposite = this.toolKit.createComposite(sc);// new Composite(sc, SWT.BORDER);
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		scrolledFromComposite.setLayout(gl);
		scrolledFromComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		List<ProcessTemplateBean> lstBean = this.contentManager.getProcessTemplateBeans();
		ProcessTemplateBean[] items = lstBean.toArray(new ProcessTemplateBean[lstBean.size()]);
		refreshAcions(scrolledFromComposite, items);
		
		sc.setContent(scrolledFromComposite);
		sc.addControlListener(new ControlListener() {
			public void controlResized(ControlEvent e) {
				Point computPoint = scrolledFromComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = sc.getBounds().width;
				if(computPoint.y > sc.getBounds().height) {//出现滚动条后
					width -= 16;
				}
				
				scrolledFromComposite.setSize(width, computPoint.y);
			}
			
			public void controlMoved(ControlEvent e) {}
		});
		Display.getCurrent().asyncExec(new Runnable() {
			
			public void run() {
				sc.getBounds();
				Point computPoint = scrolledFromComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = sc.getBounds().width;
				if(computPoint.y > sc.getBounds().height) {//出现滚动条后
					width -= 16;
				}
				scrolledFromComposite.setSize(width, computPoint.y);
				
			}
		});
		  
		/*TreeViewer treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		//TableLayout tLayout = new TableLayout();
		//treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		//treeViewer.getTree().setLayout(tLayout);
		//tLayout.addColumnData(new ColumnWeightData(ColumnWeightData.MINIMUM_WIDTH));
		TreeViewerColumn colText = new TreeViewerColumn(treeViewer, SWT.LEAD);
		colText.getColumn().setWidth(250);
		//tLayout.addColumnData(new ColumnWeightData(30));
		TreeViewerColumn delCol = new TreeViewerColumn(treeViewer, SWT.LEAD);
		delCol.getColumn().setWidth(30);
		
		
		treeViewer.setContentProvider(new ProcessContentProvider());
		treeViewer.setLabelProvider(new ProcessLabelProvider(this.imgReg));
		
		treeViewer.setInput(creatActions());
		treeViewer.expandToLevel(2);*/
		
		/*Composite toolBtns = this.toolKit.createComposite(parent, SWT.NONE);
		GridLayout btnsLayout = new GridLayout();
		
		btnsLayout.numColumns =1;
		toolBtns.setLayout(btnsLayout);*/
		
		/*GridData toolGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,1);
		toolGridData.widthHint=80;
		toolBtns.setLayoutData(toolGridData);
		
		GridData btnGD = new GridData();
		btnGD.widthHint = 65;
		Button btnAdd = this.toolKit.createButton(toolBtns, "添加", SWT.NONE);
		btnAdd.setLayoutData(btnGD);
		Button btnDel = this.toolKit.createButton(toolBtns, "删除", SWT.NONE);
		btnDel.setLayoutData(btnGD);
		Button btnAZ = this.toolKit.createButton(toolBtns, "安装到", SWT.NONE);
		btnAZ.setLayoutData(btnGD);*/
	}
	private void refreshAcions(Composite parent, ProcessTemplateBean[] items) throws Exception{
		for(int i=0;i<items.length;i++) {
			createAcitonItem(parent, items[i]);
		}
	}
	private void createAcitonItem(Composite parent, final ProcessTemplateBean item) throws Exception {
		final Composite container = this.toolKit.createComposite(parent, SWT.NONE);
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 4;

		container.setLayout(gl);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		//分组图标
		Label groupImg = this.toolKit.createLabel(container, "", SWT.NONE);
		groupImg.setImage(this.imgReg.get(IMG_PROCESS_GROUP));
		GridData gi_gridData = new GridData();
		gi_gridData.heightHint = 16;
		gi_gridData.widthHint = 16;
		groupImg.setLayoutData(gi_gridData);
		
		
		
		Label groupTitle = this.toolKit.createLabel(container, item.getTitle());
		GridData groupTitle_gd = new GridData();
		groupTitle_gd.horizontalSpan = 2;
		Font f_groupTile = createFont(10, SWT.NORMAL);
		groupTitle.setFont(f_groupTile);
		Color c_groupTitle = createColor(88, 88, 88);
		groupTitle.setForeground(c_groupTitle);
		groupTitle.setLayoutData(groupTitle_gd);
		
		Label delGroup = this.toolKit.createLabel(container, "");
		GridData gd_delGroup = new GridData();
		gd_delGroup.widthHint = 16;
		delGroup.setLayoutData(gd_delGroup);
		
		if(item.isParent()) {
			Font f_item = createFont(9, SWT.NORMAL);
			final Color c_item = createColor(0,121,132);
			final Color c_hover_item = createColor(4,181,198);
			for(int i=0;i < item.getChildren().size(); i++) {
				final ProcessTemplateBean child = item.getChildren().get(i);
				Label space = this.toolKit.createLabel(container, "", SWT.NONE);
				GridData gd_space = new GridData();
				gd_space.widthHint = 16;
				space.setLayoutData(gd_space);
				
				Label itemImg = this.toolKit.createLabel(container, "", SWT.NONE);
				itemImg.setImage(this.imgReg.get(IMG_PROCESS_ITEM));
				GridData gd_itemImg = new GridData();
				gd_itemImg.horizontalAlignment = SWT.RIGHT;
				//gd_itemImg.horizontalSpan =2;
				gd_itemImg.heightHint = 16;
				gd_itemImg.widthHint = 16;
				itemImg.setLayoutData(gd_itemImg);
				
				final Label itemTitle = this.toolKit.createLabel(container, item.getChildren().get(i).getTitle());
				GridData gd_itemTitle = new GridData(GridData.FILL_HORIZONTAL);
				gd_itemTitle.grabExcessHorizontalSpace = true;
				itemTitle.setLayoutData(gd_itemTitle);
				itemTitle.setFont(f_item);
				itemTitle.setForeground(c_item);
				itemTitle.addMouseTrackListener(new MouseTrackListener() {
					public void mouseHover(MouseEvent e) {
						itemTitle.setCursor(new Cursor(null, SWT.CURSOR_HAND));
					}
					public void mouseExit(MouseEvent e) {
						itemTitle.setForeground(c_item);
					}
					public void mouseEnter(MouseEvent e) {
						itemTitle.setForeground(c_hover_item);
					}
				});
				itemTitle.addMouseListener(new MouseListener() {
					public void mouseUp(MouseEvent e) {
					}
					public void mouseDown(MouseEvent e) {
						Display display = Display.getDefault();
						final NewControlFlowShell shell  = new NewControlFlowShell(display, "添加到控制流",
								itemTitle.getText());
						shell.addDisposeListener(new DisposeListener() {
							
							public void widgetDisposed(DisposeEvent e) {
								if(shell.isOK()) {
									ControlFlowBean flowBean = shell.getRestult();
									try {
										WelcomControlflowManager.addToControlFlow(item, flowBean,
												shell.getSelectPath());
									} catch (Exception e1) {
										MessageBoxUtils.showError("添加失败！");
										e1.printStackTrace();
									}
								}
							}
						});
						shell.open();
						while (!shell.isDisposed()) {
							if (!display.readAndDispatch())
								display.sleep();
						}
					}
					public void mouseDoubleClick(MouseEvent e) {
					}
				});
				
				final Label delItem = this.toolKit.createLabel(container, "", SWT.NONE);
				delItem.setImage(this.imgReg.get(IMG_DELETE));
				GridData gd_delItem = new GridData();
				gd_delItem.horizontalAlignment = SWT.RIGHT;
				gd_delItem.widthHint = 16;
				delItem.setLayoutData(gd_delItem);
				
				delItem.addMouseTrackListener(new MouseTrackListener() {
					public void mouseHover(MouseEvent e) {
					}
					public void mouseExit(MouseEvent e) {
						delItem.setCursor(new Cursor(null, SWT.CURSOR_ARROW));
						delItem.setImage(imgReg.get(IMG_DELETE));
					}
					public void mouseEnter(MouseEvent e) {
						delItem.setImage(imgReg.get(IMG_DELETE_SEL));
						delItem.setCursor(new Cursor(null, SWT.CURSOR_HAND));
					}
				});
				
				delItem.addMouseListener(new MouseListener() {
					
					public void mouseUp(MouseEvent e) {
					}
					
					public void mouseDown(MouseEvent e) {
						MessageBox msgBox = new MessageBox(container.getShell(), SWT.OK|SWT.CANCEL);
						msgBox.setText("提示");
						msgBox.setMessage("确认删除流程包【" +child.getTitle() + "】？");
						if(msgBox.open() == SWT.OK) {
							TemplateManager manager = new TemplateManager();
							boolean isOK = manager.remove(child.getId());
							if(!isOK) {
								MessageBox msgError = new MessageBox(container.getShell(), SWT.OK|SWT.ICON_ERROR);
								msgError.setMessage("删除失败");
								return;
							}
							//重新加载列表
							try {
								refreshAction();
								MessageBox msgOK = new MessageBox(container.getShell(), SWT.OK|SWT.ICON_INFORMATION);
								msgOK.setMessage("删除成功");
								msgOK.open();
							} catch (Exception e2) {
								e2.printStackTrace();
								MessageBox msgError = new MessageBox(container.getShell(), SWT.OK|SWT.ICON_ERROR);
								msgError.setMessage("删除失败");
								msgError.open();
							}
						}
					}
					
					public void mouseDoubleClick(MouseEvent e) {
					}
				});
			}
		}
		
		
		
	}
	/*private ProcessTemplateBean[] creatActions() {
		String pid;
		ProcessTemplateBean b1 =  createAction("1","财税",null);
		pid = b1.getId();
		ProcessTemplateBean b11 = createAction("11","增值税发票认证",pid);
		ProcessTemplateBean b12 = createAction("12","网银流水导出",pid);
		b1.setChildren(new ProcessTemplateBean[] {b11,b12});
		
		ProcessTemplateBean b2 =  createAction("2","人力资源",null);
		pid= b2.getId();
		ProcessTemplateBean b21 = createAction("21","简历筛选",pid);
		ProcessTemplateBean b22 = createAction("22","简历分类与分发",pid);
		b2.setChildren(new ProcessTemplateBean[] {b21,b22});
		
		ProcessTemplateBean b3 =  createAction("3","其他",null);
		
		return new ProcessTemplateBean[] {b1, b2, b3};
	}
	private ProcessTemplateBean createAction(String id, String title, String pid) {
		ProcessTemplateBean bean = new ProcessTemplateBean();
		bean.setId(id);
		bean.setpId(pid);
		bean.setTitle(title);
		return bean;
	}*/
	/**
	 * 创建流程包section的工具栏
	 * @param section
	 * @throws Exception
	 */
	private void createProcessSectionToolbar(Section section) throws Exception{
		final ToolBar toolbar = new ToolBar(section, SWT.HORIZONTAL|SWT.RIGHT);
		section.setTextClient(toolbar);
		
		ToolItem addItem = new ToolItem(toolbar, SWT.PUSH);
		addItem.setImage(this.imgReg.get(IMG_ADD));
		addItem.setToolTipText("添加流程包");
		addItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				Shell shell = toolbar.getShell();
				FileDialog dd = new FileDialog(toolbar.getShell());
				dd.setFilterExtensions(new String[] {"*.zip"});
				String dir = dd.open();
				if (dir != null) {
					try {
						upLoadTemplate(shell, dir);
					} catch (Exception e2) {
						MessageBox msg = new MessageBox(shell, SWT.OK|SWT.ICON_ERROR);
						msg.setText("错误");
						msg.setMessage(e2.getMessage());
						msg.open();
					}
				}
				/*Display display = Display.getDefault();
				final UploadTemplateShell uploadShell = new UploadTemplateShell(display);
				
				uploadShell.addDisposeListener(new DisposeListener() {
					
					public void widgetDisposed(DisposeEvent e) {
						if(uploadShell.isOK()) {
							Shell shell = toolbar.getShell();
							try {
								upLoadTemplate(shell, uploadShell.getSelectedFilePath());
							} catch (Exception e2) {
								MessageBox msg = new MessageBox(shell, SWT.OK|SWT.ICON_ERROR);
								msg.setText("错误");
								msg.setMessage(e2.getMessage());
								msg.open();
							}
							
						}
					}
				});
				uploadShell.open();
				while (!uploadShell.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}*/
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
	}
	private void upLoadTemplate(Shell shell, String filePath) throws Exception{
		try {
			byte[] fileByte = getBytes(filePath);
			boolean isExist = isExistTemplate(fileByte);
			 boolean isUpload = false;
			 if(isExist) {
				 MessageBox msg = new MessageBox(shell, SWT.YES| SWT.NO);
				 msg.setText("提示");
				 msg.setMessage("文件已存在，继续将覆盖已存在文件，是否继续？");
				 if(msg.open() == SWT.YES) {
					 isUpload = true;
				 }
			 }
			 else {
				 isUpload = true;
			 }
			 if(isUpload) {
				 TemplateManager manager = new TemplateManager();
				
				 try {
					 manager.add(fileByte);
					 refreshAction();
				} catch (Exception e) {
					throw new Exception("上传文件失败，" + e.getMessage(), e);
				}
			 }
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("上传文件失败," + e.getMessage().replace("java.lang.Exception: ", ""));
		}
		
	}
	private boolean isExistTemplate(byte[] bytes) throws Exception{
		try {
			TemplateManager mgr = new TemplateManager();
			return mgr.exists(bytes);
		} catch (Exception e) {
			throw new Exception(e);
		}
		
	}
	 public static byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }  
}


