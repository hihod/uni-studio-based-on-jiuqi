package com.jiuqi.rpa.widgets.extract;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class StandardHTMLTableWizardPage extends WizardPage {

	static private Label label1;
	static private Button button1;
	static private Button button2;
	static boolean fullTable=true;
	
	
	protected StandardHTMLTableWizardPage(String pageName, String title,ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		this.setPageComplete(false);
		
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginTop = 20;
		gridLayout.marginBottom = 20;
		gridLayout.marginRight = 20;
		gridLayout.marginLeft = 20;
		gridLayout.verticalSpacing=50;
		composite.setLayout(gridLayout);
		setControl(composite);
		
		label1 = new Label(composite, SWT.NONE);
		label1.setText("您选择的是一个标准Html表格，需要提取所有列吗？");
		
		//提取整表
		button1=new Button(composite, SWT.RADIO | SWT.LEFT);
		button1.setText("提取整表");
		
		//提取指定列
		button2=new Button(composite, SWT.RADIO | SWT.LEFT);
		button2.setText("提取指定列");
		
		//默认选择第一个：提取整表
		button1.setSelection(true);
		fullTable=true;
		((TableSelectorWizardDialog)getContainer()).setFullTable(true);
		
		button1.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//用于更新下一步按钮是否enable
				getContainer().updateButtons();
			}
		});
		
		button2.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				fullTable=false;
				((TableSelectorWizardDialog)getContainer()).setFullTable(false);
				getContainer().updateButtons();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}
	
	public boolean isFullTable()
	{
		return fullTable;
	}
	
	@Override
	public void dispose() {
		//重写dispose方法使control==null，以便在page切换时重新执行createControl刷新数据内容
		super.dispose();
		setControl(null);
	}
	
	public boolean canFlipToNextPage()
    {
		return button1.getSelection()||button2.getSelection()&&getNextPage() != null;
    }
}
