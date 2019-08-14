package com.jiuqi.rpa.widgets.extract;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;

public class WelcomeWizardPage extends WizardPage{
	static private Label label1;
	static private Label label2;
	private boolean isSecondWelcomePage;
	private boolean isStandardHTMLTable=true;
	private boolean isRowValidate=true;
	
	protected WelcomeWizardPage(String pageName, String title,ImageDescriptor titleImage,boolean isSecondWelcomePage) {
		super(pageName, title, titleImage);
		this.isSecondWelcomePage=isSecondWelcomePage;
	}

	@Override
	public void createControl(Composite parent) {
		this.canFlipToNextPage();
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
		label1.setText(isSecondWelcomePage?"请在要提取的数据字段上，选择另一个样本数据。":"请在要提取的数据字段上，选择一个样本数据。");
		
		label2 = new Label(composite, SWT.NONE);
		label2.setText("点击“下一步”开始");
		
		
	}
	
	public void setIsStandardHTMLTable(boolean flag)
	{
		isStandardHTMLTable=flag;
	}
	
	public boolean isStandardHTMLTable()
	{
		return isStandardHTMLTable;
	}
	
	public void setIsRowValidate(boolean flag)
	{
		isRowValidate=true;
	}
	
	public boolean isRowValidate()
	{
		return isRowValidate;
	}
	
    public boolean canFlipToNextPage()
    {
        return getNextPage() != null;
    }
}
