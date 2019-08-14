package com.jiuqi.etl.rpa.ui;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.jiuqi.rpa.widgets.editableselect.EditableComboBoxLabelProvider;
import com.jiuqi.rpa.widgets.editableselect.EditableComboCellEditor;

/**
 * 属性视图多选下拉
 * @author liangxiao01
 * <br/>
 * @用法		EditableComboPropertyDescriptor multiCombo = new EditableComboPropertyDescriptor("NAME", "NAME", String[]{"1","2","3"});
 */
public class EditableComboPropertyDescriptor extends PropertyDescriptor {


    private String[] labels;

    public EditableComboPropertyDescriptor(Object id, String displayName,
            String[] labelsArray) {
        super(id, displayName);
        labels = labelsArray;
    }

    public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new EditableComboCellEditor(parent, labels);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }

    public ILabelProvider getLabelProvider() {
        if (isLabelProviderSet()) {
			return super.getLabelProvider();
		}
		return new EditableComboBoxLabelProvider(labels);
    }
}
