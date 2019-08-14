package com.jiuqi.etl.rpa.ui.keyboard.typesecuretext;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class SecureTextPropertyDescriptor extends PropertyDescriptor {

    public SecureTextPropertyDescriptor(Object id, String displayName) {
        super(id, displayName);
    }

    public CellEditor createPropertyEditor(Composite parent) {
        CellEditor editor = new SecureTextCellEditor(parent);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }
    public ILabelProvider getLabelProvider() {
        if (isLabelProviderSet()) {
			return super.getLabelProvider();
		}
		return new SecureTextLabelProvider();
    }
}
