package com.jiuqi.etl.rpa.toolkits.template.dialog;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Button;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.model.bean.ControlFlowBean;
import com.jiuqi.etl.rpa.toolkits.template.TemplateManager;
import com.jiuqi.etl.rpa.toolkits.welcom.bean.ProcessTemplateBean;
import com.jiuqi.etl.rpa.toolkits.welcom.editor.NewControlFlowShell;
import com.jiuqi.etl.rpa.toolkits.welcom.editor.ProcessContentProvider;
import com.jiuqi.etl.rpa.toolkits.welcom.editor.ProcessLabelProvider;
import com.jiuqi.etl.rpa.toolkits.welcom.manager.WelcomContentManager;
import com.jiuqi.etl.rpa.toolkits.welcom.manager.WelcomControlflowManager;
import com.jiuqi.etl.rpa.toolkits.welcom.utils.MessageBoxUtils;
import com.jiuqi.etl.rpa.toolkits.welcom.utils.ShellUtils;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;

/**
 * @author 作者：houzhiyuan 2019年7月1日 上午11:02:31
 */
public class TemplateDialog extends Shell {
	private Text txtSearch;
	private TreeViewer templateTree;
	private Button btnUp;
	private Button btnDown;
	
	private List<ProcessTemplateBean> templateBeans;

	public TemplateDialog(Display display) {
		super(display, SWT.MIN);
		setText("通用流程管理");
		setSize(593, 434);
		createControl();
		ShellUtils.centerShell(display, getShell());
	}

	protected void checkSubclass() {
		// 解决org.eclipse.swt.SWTException: Subclassing not allowed
	}
	/**
	 * 更新模板排序
	 */
	public void updateOrder() {
		
	}

	private void createControl() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		setLayout(gridLayout);
		Composite compositeTop = new Composite(this, SWT.NONE);
		GridLayout gl_compositeTop = new GridLayout(1, false);
		gl_compositeTop.marginRight = 85;
		compositeTop.setLayout(gl_compositeTop);

		compositeTop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		txtSearch = new Text(compositeTop, SWT.BORDER);
		txtSearch.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text tmpText = (Text) e.getSource();
				if (tmpText == null) {
					return;
				}

				String inputValue = tmpText.getText();
				try {
					List<ProcessTemplateBean> resutls = WelcomContentManager.getInstance()
							.getProcessTemplateBeans(inputValue);
					templateTree.setInput(resutls);
					templateTree.expandAll();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		txtSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		templateTree = new TreeViewer(composite, SWT.BORDER);
		templateTree.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		initTemplateTree(templateTree);

		Composite composite_2 = new Composite(composite, SWT.NONE);
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_composite_2.widthHint = 80;
		composite_2.setLayoutData(gd_composite_2);
		FillLayout fl_composite_2 = new FillLayout(SWT.VERTICAL);
		fl_composite_2.spacing = 5;

		composite_2.setLayout(fl_composite_2);

		Button btnAdd = new Button(composite_2, SWT.NONE);
		btnAdd.setText("\u6DFB  \u52A0");
		btnAdd.addSelectionListener(new btnAdd_SelectionListener());

		Button btnDelete = new Button(composite_2, SWT.NONE);
		btnDelete.setText("\u5220  \u9664");
		btnDelete.addSelectionListener(new btnDelete_SelectionListener());

		Button btnApply = new Button(composite_2, SWT.NONE);
		btnApply.setText("\u5E94\u7528\u5230");
		btnApply.addSelectionListener(new btnApply_SelectionListener());
		
		btnUp = new Button(composite_2, SWT.NONE);
		btnUp.setText("\u4E0A  \u79FB");
		btnUp.addSelectionListener(new btnUp_SelectionListener());
		btnUp.setVisible(false);

		btnDown = new Button(composite_2, SWT.NONE);
		btnDown.setText("\u4E0B  \u79FB");
		btnDown.addSelectionListener(new btnDown_SelectionListener());
		btnDown.setVisible(false);

		

		Composite composite_1 = new Composite(this, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_composite_1.heightHint = 35;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setLayout(new GridLayout(1, false));

		Button btnCancel = new Button(composite_1, SWT.NONE);
		GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_1.widthHint = 80;
		btnCancel.setLayoutData(gd_btnNewButton_1);
		btnCancel.setText("\u5173  \u95ED");
		btnCancel.addSelectionListener(new btnCancel_SelectionListener());
	}

	private void initTemplateTree(TreeViewer treeViewer) {
		try {
			// 获取所有分组
			WelcomContentManager welcomManager = new WelcomContentManager();
			List<ProcessTemplateBean> beans = welcomManager.getProcessTemplateBeans();
			templateBeans = beans;
			treeViewer.setContentProvider(new ProcessContentProvider());
			treeViewer.setLabelProvider(new ProcessLabelProvider());
			
			treeViewer.setInput(templateBeans);
			treeViewer.expandAll();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class btnAdd_SelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			Shell shell = getShell();
			FileDialog dd = new FileDialog(shell);
			dd.setFilterExtensions(new String[] { "*.zip" });
			String dir = dd.open();
			if (dir != null) {
				try {
					upLoadTemplate(shell, dir);
				} catch (Exception e2) {
					MessageBoxUtils.showError("添加失败");
				}
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

	}

	private void upLoadTemplate(Shell shell, String filePath) throws Exception {
		try {
			byte[] fileByte = getBytes(filePath);
			boolean isExist = isExistTemplate(fileByte);
			boolean isUpload = false;
			if (isExist) {
				MessageBox msg = new MessageBox(shell, SWT.YES | SWT.NO);
				msg.setText("提示");
				msg.setMessage("文件已存在，继续将覆盖已存在文件，是否继续？");
				if (msg.open() == SWT.YES) {
					isUpload = true;
				}
			} else {
				isUpload = true;
			}
			if (isUpload) {
				TemplateManager manager = new TemplateManager();

				try {
					manager.add(fileByte);
					//重新赋值内容，记住上次展开
					
					/*Object[] expanded = templateTree.getExpandedElements();
					templateTree.getControl().setRedraw(false);*/
					List<ProcessTemplateBean> resutls = WelcomContentManager.getInstance().getProcessTemplateBeans();
					templateBeans = resutls;
					templateTree.setInput(resutls);
					/*templateTree.setExpandedElements(expanded);
					templateTree.getControl().setRedraw(true);*/
				} catch (Exception e) {
					throw new Exception("上传文件失败，" + e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("上传文件失败," + e.getMessage().replace("java.lang.Exception: ", ""));
		}

	}

	private boolean isExistTemplate(byte[] bytes) throws Exception {
		try {
			TemplateManager mgr = new TemplateManager();
			return mgr.exists(bytes);
		} catch (Exception e) {
			throw new Exception(e);
		}

	}

	public static byte[] getBytes(String filePath) {
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

	private class btnDelete_SelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			IStructuredSelection sel = (IStructuredSelection) templateTree.getSelection();

			ProcessTemplateBean selBean = (ProcessTemplateBean) sel.getFirstElement();
			if (selBean == null) {
				MessageBoxUtils.showWarnning("请选择一个节点进行删除");
			}
			int result = -1;
			if (selBean.isParent()) {
				result = MessageBoxUtils.showOKAndCancel("确认删除流程分组【" + selBean.getTitle() + "】，及其所有流程？");
			} else {
				result = MessageBoxUtils.showOKAndCancel("确认删除流程包【" + selBean.getTitle() + "】？");
			}

			if (result == SWT.OK) {
				TemplateManager manager = new TemplateManager();
				boolean isOK = false;
				if (!selBean.isParent()) {
					isOK = manager.remove(selBean.getId());
					if (!isOK) {
						MessageBoxUtils.showError("删除失败");
						return;
					}
					ProcessTemplateBean parent = findGroup(selBean.getpId());
					parent.getChildren().remove(selBean);
				} else {
					String errors = "";
					for (int i=selBean.getChildren().size() -1; i>0; i--) {
						ProcessTemplateBean bean = selBean.getChildren().get(i);
						isOK = manager.remove(bean.getId());
						if (!isOK) {
							errors += "\r\n" + bean.getTitle();
						}
						else {
							selBean.getChildren().remove(bean);
						}
					}
					if (!StringUtils.isEmpty(errors)) {
						MessageBoxUtils.showError("删除失败:" + errors);
						return;
					}
				}

				// 重新加载列表
				try {
					templateTree.refresh();
					MessageBoxUtils.showTips("删除成功");
				} catch (Exception e2) {
					e2.printStackTrace();
					MessageBoxUtils.showError("删除失败");
				}
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

	}

	private class btnUp_SelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			IStructuredSelection selection = (IStructuredSelection)templateTree.getSelection();
			Object obj = selection.getFirstElement();
			ProcessTemplateBean selBean = (ProcessTemplateBean)obj;
			if(selBean == null) {
				return;
			}
			int index = -1;
			List<ProcessTemplateBean> beans;
			if(selBean.isParent()) {
				beans = templateBeans;
				index = templateBeans.indexOf(selBean);
			}
			else {
				ProcessTemplateBean group = findGroup(selBean.getpId());
				if(group == null) {
					return;
				}
				beans = group.getChildren();
				index = beans.indexOf(selBean);
			}
			if(index > 0) {
				exchange(beans, index, index - 1);
			}
			templateTree.refresh();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

	}

	private class btnDown_SelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			IStructuredSelection selection = (IStructuredSelection)templateTree.getSelection();
			Object obj = selection.getFirstElement();
			ProcessTemplateBean selBean = (ProcessTemplateBean)obj;
			if(selBean == null) {
				return;
			}
			int index = -1;
			List<ProcessTemplateBean> beans;
			if(selBean.isParent()) {
				beans = templateBeans;
				index = templateBeans.indexOf(selBean);
			}
			else {
				ProcessTemplateBean group = findGroup(selBean.getpId());
				if(group == null) {
					return;
				}
				beans = group.getChildren();
				index = beans.indexOf(selBean);
			}
			if(index >= 0 && index < beans.size() - 1) {
				exchange(beans, index, index + 1);
			}
			templateTree.refresh();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

	}
	private void exchange(List<ProcessTemplateBean> list, int index1, int index2) {
		if (index1 < 0 || index1 > list.size() || index2 < 0
				|| index2 > list.size())
			return;
		
		ProcessTemplateBean o1 = list.get(index1);
		ProcessTemplateBean o2 = list.get(index2);
		
		list.set(index1, o2);
		list.set(index2, o1);
	}
	private ProcessTemplateBean findGroup(String groupId) {
		for(ProcessTemplateBean parent: templateBeans) {
			if(parent.getId().equals(groupId)) {
				return parent;
			}
		}
		return null;
	}

	private class btnApply_SelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			IStructuredSelection sel = (IStructuredSelection) templateTree.getSelection();

			final ProcessTemplateBean selBean = (ProcessTemplateBean) sel.getFirstElement();
			if (selBean == null || selBean.isParent()) {
				MessageBoxUtils.showWarnning("请选择流程节点");
			}
			Display display = Display.getDefault();
			final NewControlFlowShell shell  = new NewControlFlowShell(display, "添加到控制流",
					selBean.getTitle());
			shell.addDisposeListener(new DisposeListener() {
				
				public void widgetDisposed(DisposeEvent e) {
					if(shell.isOK()) {
						ControlFlowBean flowBean = shell.getRestult();
						try {
							WelcomControlflowManager.addToControlFlow(selBean, flowBean,
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

		public void widgetDefaultSelected(SelectionEvent e) {
		}

	}
	private class btnCancel_SelectionListener implements SelectionListener {
		public void widgetSelected(SelectionEvent e) {
			dispose();
			
		}
		public void widgetDefaultSelected(SelectionEvent e) {
		}
		
	}
}
