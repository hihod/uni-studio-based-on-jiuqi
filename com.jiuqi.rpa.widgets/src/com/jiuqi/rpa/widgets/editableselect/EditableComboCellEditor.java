package com.jiuqi.rpa.widgets.editableselect;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * 用于创建多选下拉的CellEditor
 * 
 * @author liangxiao01
 * @use
 *      <p>在MultiComboPropertyDescriptor#createPropertyEditor中创建CellEditor</p>
 *      <p>CellEditor editor = new MultiComboCellEditor(parent,labels,SWT.READ_ONLY | SWT.MULTI);</p>
 *      <p>if(getValidator() != null) {</p>
 *      <p>editor.setValidator(getValidator());</p>
 *      <p>}</p>
 *      <p>return editor;</p>
 * 
 */
public class EditableComboCellEditor extends CellEditor {
	private String[] items;
	EditableCCombo comboBox;
	private String initString = "";
	private static final int defaultStyle = SWT.NONE;

	public EditableComboCellEditor() {
		setStyle(defaultStyle);
		
	}

	public EditableComboCellEditor(Composite parent, String[] items) {
		this(parent, items, defaultStyle);
	}

	public EditableComboCellEditor(Composite parent, String[] items, int style) {
		super(parent, style);
		setItems(items);
		comboBox.addListener(SWT.FocusOut, new Listener() {
			public void handleEvent(Event event) {
				// 输入框失去焦点时侯将文本同步到下拉选项中
				String multiStr = comboBox.getText().trim();
				for (int i = 0; i < items.length; i++) {
					if(multiStr.equals(items[i])){
						comboBox.select(i);
					}
				}
				if(getValidator()!=null && getValidator().isValid(multiStr)!=null){
					comboBox.clearSelection();
					comboBox.setText(initString);
				}
				
			}
		});
	}

	public String[] getItems() {
		return this.items;
	}

	public void setItems(String[] items) {
		Assert.isNotNull(items);
		this.items = items;
		populateComboBoxItems();
	}

	protected Control createControl(Composite parent) {

		comboBox = new EditableCCombo(parent, getStyle());
		comboBox.setFont(parent.getFont());
		populateComboBoxItems();
		return comboBox;
	}

	protected Object doGetValue() {
		int i = comboBox.getSelectionIndex();
		if(i>=0){
			return items[i];
		}
		return comboBox.getText();
	}

	protected void doSetFocus() {
		comboBox.setFocus();
	}

	protected void focusLost() {
		fireApplyEditorValue();
		deactivate();
	}

	protected void doSetValue(Object value) {
		Assert.isTrue(comboBox != null);
		initString = (String)value;
		comboBox.setText((String)value);
		comboBox.select((String) value);
	}

	private void populateComboBoxItems() {
		if (comboBox != null && items != null) {
			comboBox.removeAll();
			for (int i = 0; i < items.length; i++) {
				comboBox.add(items[i], i);
			}
			setValueValid(true);
		}
	}
}
