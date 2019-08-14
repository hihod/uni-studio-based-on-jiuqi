package com.jiuqi.rpa.widgets.editableselect;

import org.eclipse.jface.viewers.LabelProvider;
/**
 * 用于创建多选下拉的ILabelProvider
 * @author liangxiao01
 * @use <p>在MultiComboPropertyDescriptor#getLabelProvider中创建MultiComboBoxLabelProvider</p>
        <p>if (isLabelProviderSet()) {</p>
		<p>return super.getLabelProvider();</p>
		<p>}</p>
		<p>return new MultiComboBoxLabelProvider(labels);</p>
    }
 */
public class EditableComboBoxLabelProvider extends LabelProvider {

    private String[] values;

    public EditableComboBoxLabelProvider(String[] values) {
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String getText(Object element) {
        
        return (String)element; 
    }
}
