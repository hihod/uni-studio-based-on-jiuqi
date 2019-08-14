package com.jiuqi.etl.rpa.ui;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jiuqi.etl.rpa.ui.Activator;
import com.jiuqi.etl.widgets.descriptor.PopupPropertyDescriptor;

public class MultipleContentPropertyDescriptor extends PopupPropertyDescriptor {
	private String dialogTitleStr = "";
	private String typeNoticeStr = "";
	public MultipleContentPropertyDescriptor(Object id, String displayName,String dialogTitle,String typeNotice) {
		super(id, displayName);
		this.dialogTitleStr =  dialogTitle;
		this.typeNoticeStr = typeNotice;
	}

	protected DialogCellEditor createCellEditor(Composite parent) {
		return new ContentDialogEditor(parent);
	}

	private class ContentDialogEditor extends DialogCellEditor {

		public ContentDialogEditor(Composite parent) {
			super(parent);
		}
		
		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			String str = (String) getValue();
			str = str.replaceAll(",", "\r\n");
			ContentDialog d = new ContentDialog(cellEditorWindow.getShell(), str);
			int returnValue = d.open();
			if (returnValue == Window.OK) {
				return d.getText();
			}
			
			return null;
		}
	}
	
	private class ContentDialog extends Dialog {

		private String str;
		private Text text;
		protected ContentDialog(Shell parentShell, String value) {
			super(parentShell);
			this.str = value;
		}

		protected Control createDialogArea(Composite parent) {
			final Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			final GridLayout gridLayout = new GridLayout();
			gridLayout.verticalSpacing = 0;
			gridLayout.marginWidth = 0;
			gridLayout.horizontalSpacing = 0;
			gridLayout.marginHeight = 0;
			composite.setLayout(gridLayout);
			
			final Composite headComp = new Composite(composite, SWT.NONE);
			RowLayout rLayout = new RowLayout(SWT.VERTICAL);
			rLayout.marginTop = 10;
			rLayout.marginLeft = 0;
			headComp.setLayout(rLayout);
			headComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			
			Composite topContainer = new Composite(headComp, SWT.NONE);
			GridLayout gl_topContainer = new GridLayout(2, false);
			gl_topContainer.verticalSpacing = 0;
			gl_topContainer.marginWidth = 0;
			gl_topContainer.marginHeight = 0;
			gl_topContainer.horizontalSpacing = 0;
			topContainer.setLayout(gl_topContainer);
			topContainer.setLayoutData(new RowData(471, SWT.DEFAULT));
			Composite labelContainer = new Composite(topContainer, SWT.NONE);
			labelContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			RowLayout rl_labelContainer = new RowLayout(SWT.VERTICAL);
			rl_labelContainer.marginTop = 0;
			rl_labelContainer.marginRight = 0;
			rl_labelContainer.marginLeft = 10;
			rl_labelContainer.marginBottom = 0;
			labelContainer.setLayout(rl_labelContainer);
			Label dialogTitle = new Label(labelContainer, SWT.WRAP);
			dialogTitle.setText(dialogTitleStr);
			FontData newFontData = dialogTitle.getFont().getFontData()[0];
			newFontData.setStyle(SWT.BOLD);
			newFontData.setHeight(10);
			Font newFont = new Font(this.getShell().getDisplay(), newFontData);
			dialogTitle.setFont(newFont);
			Label describ = new Label(labelContainer, SWT.NONE);
			describ.setText(typeNoticeStr);
			//·Ö¸ô·û
			Label label = new Label(headComp, SWT.SEPARATOR | SWT.HORIZONTAL);
			label.setLayoutData(new RowData(514, 2));
			
			Composite composite_1 = new Composite(composite, SWT.NONE);
			FillLayout fl_composite_1 = new FillLayout(SWT.HORIZONTAL);
			fl_composite_1.marginWidth = 5;
			fl_composite_1.marginHeight = 5;
			composite_1.setLayout(fl_composite_1);
			composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
			text = new Text(composite_1, SWT.BORDER | SWT.MULTI|SWT.V_SCROLL);
			if (str != null)
				text.setText(str);
			
			headComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			topContainer.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			dialogTitle.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			labelContainer.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			describ.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			
			Composite imageContainer = new Composite(topContainer, SWT.NONE);
			imageContainer.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
			GridLayout gl_imageContainer = new GridLayout(1, false);
			gl_imageContainer.marginWidth = 0;
			gl_imageContainer.marginHeight = 0;
			gl_imageContainer.horizontalSpacing = 0;
			gl_imageContainer.verticalSpacing = 0;
			imageContainer.setLayout(gl_imageContainer);
			Label imageLabel = new Label(imageContainer, SWT.NONE);
			imageLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1));
			imageLabel.setImage(Activator.getDefault().getImageRegistry().get(Activator.IMAGE_NORMAL_WIDGET_HEAD));
			imageContainer.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			
			return composite;
		}
		
		protected Point getInitialSize() {
			return new Point(480, 400);
		}

		protected void okPressed() {
			this.str = text.getText();
			super.okPressed();
		}
		
		public String getText() {
			return str.replaceAll("\r\n",",");
		}
		
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText(dialogTitleStr);
		}
	}
}
