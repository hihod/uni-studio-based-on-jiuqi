package com.jiuqi.rpa.widgets.inputdialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

class InputDialog extends Shell {
	public String result = "";
	public Boolean okPress = false;
	public String title;
	public String label;
	public Boolean password;
	public String[] options;
	private Text inputType1;
	private Button radioType2A;
	private Button radioType2B;
	private Button radioType2C;
	private Combo comboType3;
	Composite mainComposite;// 主体内容
	Button confirmBtn;
	Button cancelBtn;
	protected InputDialog(Display display, String title, String label, Boolean password, String[] options) {
		super(display, SWT.DIALOG_TRIM|SWT.ON_TOP);
		this.title = title;
		this.label = label;
		this.password = password;
		this.options = options;
		createContents();
		centerShell(InputDialog.this.getDisplay(), InputDialog.this.getShell());// 控制界面居中
	}

	public void centerShell(Display display, Shell shell) {
		Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
		Rectangle shellBounds = shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;
		shell.setLocation(x, y);
	}

	public String getResult() {
		return this.result;
	}

	protected void createContents() {
		setSize(350, 180);
		setText(this.title);
		this.setLayout(new GridLayout(1, false));
		GridLayout gridLayout = (GridLayout) this.getLayout();
		gridLayout.marginTop = 20;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		// 上部label
		Composite topComposite = new Composite(this, SWT.NONE);
		topComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		FillLayout topLayout = new FillLayout(SWT.HORIZONTAL);
		topLayout.marginWidth = 10;
		topComposite.setLayout(topLayout);
		Label topLabel = new Label(topComposite, SWT.NONE);
		topLabel.setText(this.label);
		// 中部主体
		if (options.length <= 1) {
			mainComposite = new Composite(this, SWT.NONE);
			mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			inputType1 = new Text(mainComposite, this.password ? (SWT.BORDER | SWT.PASSWORD) : (SWT.BORDER));
			if (options.length == 1) {
				inputType1.setText(options[0]);
			}
			inputType1.setBounds(10, 20, 306, 23);
			inputType1.addKeyListener(new KeyListener() {
				public void keyReleased(KeyEvent e) {
					
				}
				public void keyPressed(KeyEvent e) {
					if(e.keyCode==13 || e.keyCode==16777296){
						Event ent = new Event();
						ent.widget = confirmBtn;
						confirmBtn.notifyListeners(SWT.Selection, ent);
					}
				}
			});
		} else if (options.length <= 3) {
			Composite mainComposite = new Composite(this, SWT.NONE);
			mainComposite.setLayout(new GridLayout(3, false));
			mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			radioType2A = new Button(mainComposite, SWT.RADIO);
			radioType2A.setText(options[0]);
			radioType2B = new Button(mainComposite, SWT.RADIO);
			radioType2B.setText(options[1]);
			if (options.length == 3) {
				radioType2C = new Button(mainComposite, SWT.RADIO);
				radioType2C.setText(options[2]);
			}
		} else {
			mainComposite = new Composite(this, SWT.NONE);
			mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			comboType3 = new Combo(mainComposite, SWT.READ_ONLY);
			comboType3.setItems(options);
			comboType3.select(0);
			comboType3.setBounds(10, 20, 272, 25);
		}
		// 下部按钮
		Composite buttonsComposite = new Composite(this, SWT.NONE);
		RowLayout rl_buttonsComposite = new RowLayout(SWT.HORIZONTAL);
		rl_buttonsComposite.spacing = 20;
		rl_buttonsComposite.marginWidth = 5;
		rl_buttonsComposite.marginRight = 20;
		rl_buttonsComposite.marginBottom = 10;
		buttonsComposite.setLayout(rl_buttonsComposite);
		buttonsComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1));
		confirmBtn = new Button(buttonsComposite, SWT.CENTER);
		confirmBtn.setLayoutData(new RowData(60, SWT.DEFAULT));
		confirmBtn.setText("确认");
		cancelBtn = new Button(buttonsComposite, SWT.NONE);
		cancelBtn.setLayoutData(new RowData(60, SWT.DEFAULT));
		cancelBtn.setText("取消");
		confirmBtn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				buildResult();
				okPress = true;
				InputDialog.this.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		cancelBtn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				InputDialog.this.result = "";
				okPress = false;
				InputDialog.this.dispose();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	protected void checkSubclass() {
	}

	public void dispose() {
		super.dispose();
	}

	public void buildResult() {
		switch (this.options.length) {
		case 0:
		case 1:
			this.result = inputType1.getText();
			break;
		case 2:
		case 3:
			if (radioType2A.getSelection())
				this.result = radioType2A.getText();
			else if (radioType2B.getSelection())
				this.result = radioType2B.getText();
			else if (radioType2C.getSelection())
				this.result = radioType2C.getText();
			break;
		default:
			if (comboType3.getText() != null) {
				this.result = comboType3.getText();
			}
			break;
		}
	}
}
