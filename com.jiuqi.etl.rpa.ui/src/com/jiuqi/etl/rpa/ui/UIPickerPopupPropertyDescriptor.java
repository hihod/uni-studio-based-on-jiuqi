package com.jiuqi.etl.rpa.ui;

import org.eclipse.swt.widgets.Composite;

import com.jiuqi.etl.rpa.runtime.TaskTarget;
import com.jiuqi.etl.widgets.descriptor.PopupPropertyDescriptor;
import com.jiuqi.rpa.lib.find.Path;
import com.jiuqi.rpa.widgets.uipicker.ClearableDialogCellEditor;
import com.jiuqi.rpa.widgets.uipicker.PickerDialogEditor;

/**
 * UIÑ¡ÔñÆ÷ÃèÊö·û
 * 
 * @author liangxiao01 <br/>
 * @ÓÃ·¨ UIPickerPopupPropertyDescriptor picker = new
 *     UIPickerPopupPropertyDescriptor("NAME", "NAME", null);
 */
public class UIPickerPopupPropertyDescriptor extends PopupPropertyDescriptor {
	private Path path = null;
	private boolean toFindWindow;

	public UIPickerPopupPropertyDescriptor(String id, String displayName, TaskTarget target) {
		this(id, displayName, target.getPath(), false);
	}
	
	public UIPickerPopupPropertyDescriptor(String id, String displayName, TaskTarget target, boolean findWindow) {
		this(id, displayName, target.getPath());
		this.toFindWindow = findWindow;
	}
	
	public UIPickerPopupPropertyDescriptor(String id, String displayName, Path path) {
		this(id, displayName, path, false);
	}
	
	public UIPickerPopupPropertyDescriptor(String id, String displayName, Path path, boolean findWindow) {
		super(id, displayName);
		this.path = path == null ? new Path() : path;
		this.toFindWindow = findWindow;
	}

	protected ClearableDialogCellEditor createCellEditor(Composite parent) {
		return new PickerDialogEditor(parent, path, toFindWindow);
	}

}
