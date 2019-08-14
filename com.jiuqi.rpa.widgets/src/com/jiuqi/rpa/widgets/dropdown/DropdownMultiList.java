package com.jiuqi.rpa.widgets.dropdown;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
/**
 * ֧�ֶ�ѡ�������б� ͨ��setValue����ѡֵ�������θ��죩
 */
public class DropdownMultiList extends Composite {

	private Composite self;
	private CLabel label;
	private Color labelBackground;		// label��ԭʼ����ɫ����label��ȡ����������Ҫ�ͷš�
	private Button button;
	private Object selection;
	private Shell popup;
	private TreeViewer tree;
	private int showLines = 5;
	private ILabelProvider textProvider;	// ѡ��������ʾ���ı����е������ṩ��������Ϊ��
	
	/**
	 * ������������
	 * @param parent
	 * @param style ��ʽ������{@link SWT#READ_ONLY}, {@link SWT#BORDER}
	 */
	public DropdownMultiList(Composite parent, int style) {
		super(parent, style);
		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		this.self = this;
		
		createComposite(parent, style);
		createPopup(parent, style);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (label != null) {
			label.setEnabled(enabled);
			if (enabled) {
				label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
				setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			} else {
				label.setBackground(labelBackground);
				setBackground(labelBackground);
			}
		}
		if (button != null) {
			button.setEnabled(enabled);
		}
	}
	
	private void createComposite(Composite parent, int style) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.numColumns = 2;
		setLayout(gridLayout);
		
		label = new CLabel(this, SWT.NONE);
		labelBackground = label.getBackground();
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		final GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, true);
		gd_label.horizontalIndent = 2;
		label.setLayoutData(gd_label);
		label.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {
				button.notifyListeners(SWT.Selection, null);
			}
			public void mouseDoubleClick(MouseEvent e) {}
		});

		button = new Button(this, SWT.ARROW | SWT.DOWN);
		button.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Point location = self.toDisplay(0,0);
				int dropdownHeight = tree.getTree().getItemHeight() * showLines + self.getBorderWidth()*2*showLines;
				Point dropdownSize = new Point(self.getBounds().width, dropdownHeight);
				Rectangle displayRect = Display.getDefault().getBounds();
				System.out.println(displayRect.height);
				System.out.println(location.y);
				if(displayRect.height>=location.y+dropdownHeight){
					location.y = location.y + self.getBounds().height + self.getBorderWidth();
				}else{
					location.y = location.y - dropdownHeight - self.getBorderWidth();
				}
				popup.setLocation(location);
				popup.setSize(dropdownSize);
				popup.layout();
				popup.setVisible(true);
				popup.setFocus();
			}
		});
	}
	
	private void createPopup(Composite parent, int style) {
		popup = new Shell(parent.getShell(), SWT.NO_TRIM);
		popup.setLayout(new FillLayout());
		popup.setVisible(false);
		popup.addShellListener(new ShellAdapter() {
			public void shellDeactivated(ShellEvent e) {
				popup.setVisible(false);
			}
		});
		
		tree = new TreeViewer(popup, SWT.CHECK |SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		tree.getTree().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//����ѡ�е��м�Ϊ��ѡ
				TreeItem item = (TreeItem)e.item;
				if(e.detail !=SWT.CHECK){
					item.setChecked(!item.getChecked());					
				}
				//�Զ���ѡ,��������
				ITreeSelection selection = (ITreeSelection) tree.getSelection();
				Object selectedObject = selection.getFirstElement();
				
				DropdownMultiList.this.selection = selectedObject;
				if (textProvider != null) {
					setText(textProvider.getText(selectedObject));
				} else {
					setText(tree.getTree().getSelection()[0].getText());
				}
			}
		});
	}

	/**
	 * �����ı�����ʾ�������ṩ��
	 * @param textProvider �����ṩ��
	 */
	public void setTextProvider(ILabelProvider textProvider) {
		this.textProvider = textProvider;
	}

	/**
	 * ��ȡTreeViewer
	 * @return �����б��е���
	 */
	public TreeViewer getTreeViewer() {
		return tree;
	}
	
	/**
	 * ���������ʾ������,���5�У�����5�н�������Ϊ5�� ���ֹ�����
	 * @param lines ����
	 */
	public void setShowLines(int lines) {
		if (lines < 5) {
			lines = 5;
		}
		this.showLines = lines;
	}
	
	/**
	 * @return
	 */
	public int getShowLines() {
		return this.showLines;
	}
	

	/**
	 * ��ȡѡ���Ķ���
	 * @return ѡ���Ķ���
	 */
	public Object getSelection() {
		return this.selection;
	}
	
	/**
	 * ����ѡ���Ķ���
	 * �����õĶ���Ϊnullʱ������ı���������ѡ��ڵ㡣���ҵ�������δ����textProviderʱ���ı�����ʾΪ�ա�
	 * ���������ΪnullʱҪ����������ʾ��������textProvider������textProvider�е�getText�����н��д���
	 * @param selection ѡ���Ķ���
	 */
	public void setSelection(Object selection) {
		this.selection = selection;
		if (this.textProvider != null) {
			setText(textProvider.getText(selection));
		} else if (this.tree.getLabelProvider() != null
				&& this.tree.getLabelProvider() instanceof ILabelProvider) {
			if (selection == null)
				setText("");
			
			ILabelProvider provider = (ILabelProvider) this.tree.getLabelProvider();
			String string = provider.getText(selection);
			
			setText(string);
		}
		if (selection != null) {
			this.tree.setSelection(new StructuredSelection(selection));
		}
	}
	
	/**
	 * @param string ��ʾ���ַ���,����null�����ᴥ��
	 */
	public void setText(String string) {
		if (string == null)
			return;
		label.setText(string);
	}
	
	
	/**
	 * ��ȡ��������ʾ���ı�����
	 * @return ��ʾ���ı�
	 */
	public String getText() {
		return label.getText();
	}

	public void dispose() {
		super.dispose();
		if (label != null && label.isDisposed()) {
			label.dispose();
		}
		if (!popup.isDisposed()) {
			popup.dispose();
		}
	}
	
	public void setInput(Object obj) {
		this.tree.setInput(obj);
	}

}
