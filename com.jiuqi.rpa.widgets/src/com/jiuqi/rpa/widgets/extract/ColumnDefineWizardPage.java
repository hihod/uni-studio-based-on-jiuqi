package com.jiuqi.rpa.widgets.extract;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ColumnDefineWizardPage extends WizardPage {
	private Text name, controltitle;
	private java.util.List<ColumnEntity> allParams;
	private String nameStr, titleStr;

	protected ColumnDefineWizardPage(String pageName, String pageTitle, ImageDescriptor titleImage) {
		super(pageName, pageTitle, titleImage);
	}

	@Override
	public void dispose() {
		super.dispose();
		setControl(null);
	}

	@Override
	public void createControl(Composite parent) {
		allParams = ((TableSelectorWizardDialog) this.getContainer()).getColumnEntityList();

		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(2, false);
		
		gridLayout.marginTop = 20;
		gridLayout.marginBottom = 20;
		gridLayout.marginRight = 20;
		gridLayout.marginLeft = 20;
		gridLayout.verticalSpacing = 30;
		composite.setLayout(gridLayout);
		setControl(composite);

		final Label tipTextLabel = new Label(composite, SWT.NONE);
		tipTextLabel.setText("请定义指定列。");
		tipTextLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		final Label _name = new Label(composite, SWT.NONE);
		_name.setText("名称：");
		name = new Text(composite, SWT.BORDER);
		name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		name.addModifyListener(new PageModifyListener());

		//计算默认的列名称
		for (int i = 1;; i++) {
			int j = 0;
			for (; j < allParams.size(); j++) {
				if (allParams.get(j).getName().equalsIgnoreCase("Column" + String.valueOf(i))) {
					break;
				}
			}

			if (j == allParams.size()) {
				nameStr = "Column" + String.valueOf(i);
				break;
			}
		}
		name.setText(nameStr);

		final Label _title = new Label(composite, SWT.NONE);
		_title.setText("标题：");
		controltitle = new Text(composite, SWT.BORDER);
		controltitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		//计算默认的列标题
		for (int i = 1;; i++) {
			int j = 0;
			for (; j < allParams.size(); j++) {
				if (allParams.get(j).getTitle().equalsIgnoreCase("列" + String.valueOf(i))) {
					break;
				}
			}

			if (j == allParams.size()) {
				titleStr = "列" + String.valueOf(i);
				break;
			}
		}
		controltitle.setText(titleStr);
	}

	public String getName() {
		return name.getText();
	}

	public String getTitle() {
		return controltitle.getText();
	}

	public boolean canFlipToNextPage() {
		return getNextPage() != null && checkPageFinish();
	}

	private boolean checkPageFinish() {

		if (name.getText() == null || name.getText().length() == 0) {
			setErrorMessage(null);
			return false;
		} else {
			String pname = name.getText();
			if (!pname.matches("[a-zA-Z_@][a-zA-Z0-9_@]*")) {
				setErrorMessage("非法的列名称【" + pname + "】，列名称只能包含字母、数字、下划线以及'@'符，并且不能以数字开头");
				return false;
			} else {
				boolean b = true;
				for (ColumnEntity p : allParams) {
					if (p.getName().equalsIgnoreCase(pname)) {
						b = false;
						break;
					}
				}

				if (!b) {
					setErrorMessage("列名称【" + pname + "】已经存在");
					return false;
				} else {
					setErrorMessage(null);
				}
			}
		}
		return true;
	}

	private class PageModifyListener implements ModifyListener {

		public void modifyText(ModifyEvent e) {
			setPageComplete(checkPageFinish());
		}

	}

}
