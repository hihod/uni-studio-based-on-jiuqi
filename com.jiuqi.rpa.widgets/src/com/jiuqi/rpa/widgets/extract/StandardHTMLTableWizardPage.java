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
		label1.setText("��ѡ�����һ����׼Html�����Ҫ��ȡ��������");
		
		//��ȡ����
		button1=new Button(composite, SWT.RADIO | SWT.LEFT);
		button1.setText("��ȡ����");
		
		//��ȡָ����
		button2=new Button(composite, SWT.RADIO | SWT.LEFT);
		button2.setText("��ȡָ����");
		
		//Ĭ��ѡ���һ������ȡ����
		button1.setSelection(true);
		fullTable=true;
		((TableSelectorWizardDialog)getContainer()).setFullTable(true);
		
		button1.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//���ڸ�����һ����ť�Ƿ�enable
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
		//��дdispose����ʹcontrol==null���Ա���page�л�ʱ����ִ��createControlˢ����������
		super.dispose();
		setControl(null);
	}
	
	public boolean canFlipToNextPage()
    {
		return button1.getSelection()||button2.getSelection()&&getNextPage() != null;
    }
}
