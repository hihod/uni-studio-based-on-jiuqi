package com.jiuqi.etl.rpa.toolkits.welcom.editor;

import org.eclipse.swt.widgets.Shell;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.jiuqi.widgets.dropdowntree.DropdownEvent;
import com.jiuqi.widgets.dropdowntree.DropdownListener;
import com.jiuqi.widgets.dropdowntree.DropdownTree;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.model.bean.ControlFlowBean;
import com.jiuqi.etl.model.bean.ControlFlowFolderBean;
import com.jiuqi.etl.model.bean.SchemeBean;
import com.jiuqi.etl.rpa.toolkits.welcom.utils.MessageBoxUtils;
import com.jiuqi.etl.rpa.toolkits.welcom.utils.ShellUtils;
import com.jiuqi.etl.storage.IControlFlowStorage;
import com.jiuqi.etl.storage.ISchemeStorage;
import com.jiuqi.etl.storage.IStorageProvider;
import com.jiuqi.etl.storage.StorageException;
import com.jiuqi.etl.storage.StorageProviderManager;
import com.jiuqi.etl.ui.view.ETLViewsPlugin;
import com.jiuqi.etl.util.SpecialCharValider;

/**
 * @author ���ߣ�houzhiyuan 2019��6��26�� ����2:21:30
 */
public class NewControlFlowShell extends Shell {
	private static final String ICON_SCHEME_BEAN = "icons/tree/scheme_bean.gif";
	private static final String ICON_CONTROLFLOW_STORAGE = "icons/tree/controlflow_stor.gif";
	private static final String ICON_CONTROLFLOW_FOLDER = "icons/tree/scheme_bean.gif";

	private ImageRegistry imgReg;

	private Text txtProcessTitle;
	private Label lb_error;
	private Button btnOK;
	private Button btnCancel;

	private IStorageProvider provider;

	private DropdownTree flowFolderTree;

	private boolean isOK;
	private String flowTitle;
	private ControlFlowBean controlFlowBean;

	public NewControlFlowShell(Display display,String shellTitle, String flowFitle) {
		super(display, SWT.MIN);
		setText(shellTitle);
		setSize(406, 163);
		ShellUtils.centerShell(display, getShell());

		registerImg();
		this.flowTitle = flowFitle;

		provider = StorageProviderManager.getInstance().getStorageProvider();
		createControls();
		init();
		addShellListener(new ShellListener() {
			public void shellIconified(ShellEvent e) {
			}
			
			public void shellDeiconified(ShellEvent e) {
			}
			
			public void shellDeactivated(ShellEvent e) {
			}
			
			public void shellClosed(ShellEvent e) {
			}
			
			public void shellActivated(ShellEvent e) {
				if(StringUtils.isNotEmpty(NewControlFlowShell.this.flowTitle)) {
					txtProcessTitle.setText(NewControlFlowShell.this.flowTitle);
				}
				
			}
		});
	}

	protected void checkSubclass() {
		// ���org.eclipse.swt.SWTException: Subclassing not allowed
	}

	public boolean isOK() {
		return this.isOK;
	}

	/**
	 * ��ȡѡ��Ŀ�����Ŀ¼������
	 * 
	 * @return
	 */
	public ControlFlowBean getRestult() {
		return this.controlFlowBean;
	}
	/**
	 * ��ȡѡ��Ŀ¼��·��������ѡ���Ŀ¼�ڵ㣬��ѡ��������ڵ㣬�򷵻ؿ�
	 * @return
	 */
	public List<ControlFlowFolderBean> getSelectPath() {
		TreeViewer treeViewer = flowFolderTree.getTreeViewer();
		Tree tree = treeViewer.getTree();
		TreeItem item = tree.getSelection()[0];
		List<ControlFlowFolderBean> paths = new ArrayList<ControlFlowFolderBean>();
		while(item != null) {
			ControlFlowFolderBean bean = (ControlFlowFolderBean)item.getData();
			if(bean == null || StringUtils.isEmpty(bean.getGuid())) {
				break;
			}
			paths.add(0, bean);
			item = item.getParentItem();
		}
		return paths;
	}

	private void registerImg() {
		imgReg = new ImageRegistry();
		imgReg.put(ICON_SCHEME_BEAN, ETLViewsPlugin.imageDescriptorFromPlugin(ICON_SCHEME_BEAN));
		imgReg.put(ICON_CONTROLFLOW_STORAGE, ETLViewsPlugin.imageDescriptorFromPlugin(ICON_CONTROLFLOW_STORAGE));
		imgReg.put(ICON_CONTROLFLOW_FOLDER, ETLViewsPlugin.imageDescriptorFromPlugin(ICON_CONTROLFLOW_FOLDER));
	}

	private void createControls() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		setLayout(gridLayout);

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 15;
		gl_composite.marginRight = 10;
		gl_composite.marginLeft = 10;
		gl_composite.marginTop = 10;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Label lblNewLabel = new Label(composite, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("���⣺");

		txtProcessTitle = new Text(composite, SWT.BORDER);
		txtProcessTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		//txtProcessTitle.setText(title);
		txtProcessTitle.addModifyListener(new MyModifyListener());

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setText("����Ŀ¼��");

		flowFolderTree = new DropdownTree(composite, SWT.READ_ONLY | SWT.BORDER);
		flowFolderTree.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		flowFolderTree.addModifyListener(new MyModifyListener());

		Composite composite_1 = new Composite(this, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.marginLeft = 10;
		composite_1.setLayout(gl_composite_1);
		GridData gd_composite_1 = new GridData(GridData.FILL_HORIZONTAL);
		gd_composite_1.heightHint = 35;
		composite_1.setLayoutData(gd_composite_1);

		lb_error = new Label(composite_1, SWT.NONE);
		lb_error.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		btnOK = new Button(composite_1, SWT.NONE);
		GridData gd_btnOK = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOK.widthHint = 60;
		btnOK.setLayoutData(gd_btnOK);

		btnOK.setText("ȷ ��");

		btnOK.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				try {
					controlFlowBean = new ControlFlowBean();
					controlFlowBean.setTitle(txtProcessTitle.getText());
					ControlFlowFolderBean folder = (ControlFlowFolderBean) flowFolderTree.getSelection();
					if (folder == null) {
						MessageBoxUtils.showWarnning("��ѡ������Ŀ¼");
						return;
					}
					controlFlowBean.setSchemeGuid(folder.getSchemeGuid());
					controlFlowBean.setFolderGuid(folder.getGuid());
					isOK = true;
					NewControlFlowShell.this.dispose();
				} catch (Exception e2) {
					e2.printStackTrace();
					MessageBoxUtils.showError("��ӵ�������ʧ��");
					return;
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnCancel = new Button(composite_1, SWT.NONE);
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 60;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("ȡ ��");
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isOK = false;
				NewControlFlowShell.this.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		/*
		 * FillLayout fillLayout = new FillLayout(); fillLayout.type = SWT.VERTICAL;
		 */
	}

	private void init() {
		buildFlowFolderTree();
	}

	private void buildFlowFolderTree() {
		flowFolderTree.setShowLines(15);
		flowFolderTree.setTextProvider(new LabelProvider() {

		});

		flowFolderTree.addDropdownListener(new DropdownListener() {
			public void itemSelected(DropdownEvent e) {
				if (e.data instanceof ControlFlowFolderBean) {
					e.doit = true;
				} else {
					e.doit = false;
				}
			}

			public void dropdown(DropdownEvent arg0) {
			}
		});

		TreeViewer treeViewer = flowFolderTree.getTreeViewer();
		
		treeViewer.setContentProvider(new ITreeContentProvider() {

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			public void dispose() {
			}

			public Object[] getElements(Object inputElement) {
				if (!(inputElement instanceof SchemeBean[])) {
					return null;
				}
				return (SchemeBean[]) inputElement;
			}

			public boolean hasChildren(Object element) {
				return true;
			}

			public Object getParent(Object element) {
				return null;
			}

			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof SchemeBean) {
					SchemeBean schemeBean = (SchemeBean) parentElement;
					// �����ڵ�ʱ�������������ڵ�
					return new ControlFlowFolderBean[] { getControlFlowBean(schemeBean) };
				} else if (parentElement instanceof ControlFlowFolderBean) {
					ControlFlowFolderBean bean = (ControlFlowFolderBean) parentElement;
					try {
						IControlFlowStorage controlFlowStorage = provider.getControlFlowStorage(bean.getSchemeGuid());
						return getFolderChildren(controlFlowStorage, bean.getGuid());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		});
		treeViewer.setLabelProvider(new ILabelProvider() {

			public void removeListener(ILabelProviderListener listener) {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void dispose() {
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public String getText(Object element) {
				String text = null;
				if (element instanceof SchemeBean) {
					SchemeBean schemeBean = (SchemeBean) element;
					text = schemeBean.getTitle();
				} else if (element instanceof ControlFlowFolderBean) {
					ControlFlowFolderBean bean = (ControlFlowFolderBean) element;
					text = bean.getTitle();
				}
				return text;
			}

			public Image getImage(Object element) {
				Image img = null;
				if (element instanceof SchemeBean) {
					img = imgReg.get(ICON_SCHEME_BEAN);
				} else if (element instanceof ControlFlowFolderBean) {
					ControlFlowFolderBean bean = (ControlFlowFolderBean) element;
					if (StringUtils.isEmpty(bean.getGuid())) {
						img = imgReg.get(ICON_CONTROLFLOW_STORAGE);
					} else {
						img = imgReg.get(ICON_CONTROLFLOW_FOLDER);
					}
				}
				return img;
			}
		});
		SchemeBean[] root = getSchemalNode();
		treeViewer.setInput(root);
		treeViewer.expandToLevel(2);
	}

	private Object[] getFolderChildren(IControlFlowStorage storage, String guid) throws StorageException {
		ControlFlowFolderBean[] folders = storage.getChildControlFlowFolders(guid);
		sortObjects(folders);
		Object[] result = new Object[folders.length];
		System.arraycopy(folders, 0, result, 0, folders.length);
		return result;
	}

	private void sortObjects(Object[] objects) {
		if (objects == null || objects.length == 0)
			return;

		List<Object> objectList = new ArrayList<Object>(objects.length);
		for (Object object : objects)
			objectList.add(object);

		final Comparator<Object> c = Collator.getInstance(java.util.Locale.CHINA);
		Collections.sort(objectList, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				if (o1 == null || o2 == null)
					return 0;

				return c.compare(o1.toString(), o2.toString());
			}
		});

		for (int index = 0; index < objects.length; index++) {
			objects[index] = objectList.get(index);
		}
	}

	private SchemeBean[] getSchemalNode() {
		SchemeBean[] schemes = null;
		try {
			ISchemeStorage schemeStorage = this.provider.getSchemeStorage(null);
			schemes = schemeStorage.getAllScheme();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return schemes;
	}

	private ControlFlowFolderBean getControlFlowBean(SchemeBean schemeBean) {
		ControlFlowFolderBean bean = new ControlFlowFolderBean();
		bean.setSchemeGuid(schemeBean.getGuid());
		bean.setTitle("������");
		bean.setGuid("");
		return bean;
	}

	private class MyModifyListener implements ModifyListener {

		public void modifyText(ModifyEvent e) {
			String title = txtProcessTitle.getText();
			title = title.trim();
			String errorMsg = null;
			
			String folderTitle = flowFolderTree.getText();
			
			if (StringUtils.isEmpty(title)) {
				errorMsg = "���ⲻ��Ϊ��";
			}
			else if (title.length() > 50) {
				lb_error.setText("���������ⳤ�Ȳ��ܳ���50���ַ�");
			}
			else if (SpecialCharValider.Valid(title)) {
				errorMsg = "���������Ʋ��ܰ�����Щ�ַ���" + SpecialCharValider.getSpacialCharStr();
			}
			else if(StringUtils.isEmpty(folderTitle)) {	
				errorMsg = "δѡ��Ŀ¼";
			}
			else {//�ж���ͬĿ¼���Ƿ����ͬ���Ŀ�����
				Object selObj = flowFolderTree.getSelection();
				ControlFlowFolderBean folderBean = (ControlFlowFolderBean)selObj;
				if(folderBean == null) {
					errorMsg = "δѡ��Ŀ¼";
				}
				else {
					try {
						IControlFlowStorage storage = provider.getControlFlowStorage(folderBean.getSchemeGuid());
						ControlFlowBean[] flows = storage.getChildControlFlows(folderBean.getGuid());
						if(flows != null && flows.length > 0) {
							for(ControlFlowBean bean : flows) {
								if(bean.getTitle().equalsIgnoreCase(title)) {
									errorMsg = "�����ƵĿ������Ѿ�����";
									break;
								}
							}
						}
					} catch (StorageException e1) {
						e1.printStackTrace();
					}
					
				}
			}
			
			if(StringUtils.isEmpty(errorMsg)) {
				btnOK.setEnabled(true);
				lb_error.setText("");
			}
			else {
				btnOK.setEnabled(false);
				lb_error.setText(errorMsg);
			}
		}

	}
}
