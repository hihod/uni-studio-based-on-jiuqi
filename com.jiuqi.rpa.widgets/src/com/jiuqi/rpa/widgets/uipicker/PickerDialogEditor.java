package com.jiuqi.rpa.widgets.uipicker;

import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.json.JSONException;

import com.jiuqi.rpa.lib.find.Path;

/**
 * UI选择器面板
 * 
 * @author liangxiao01
 */
public class PickerDialogEditor extends ClearableDialogCellEditor {
	Path path = null;
	Boolean saveMoment = false;
	PickerShell pickerShell = null;
	private boolean toFindWindow;
	
	public PickerDialogEditor(Composite parent, Path path) {
		this(parent, path, false);
		this.toFindWindow = false;
	}
	
	public PickerDialogEditor(Composite parent, Path path, boolean toFindWindow) {
		super(parent);
		this.path = path;
		this.toFindWindow = toFindWindow;
	}


	protected Object openDialogBox(Control cellEditorWindow) {
		if(path==null || path.getElements().size()==0){
			return openNonePathPicker(cellEditorWindow);
		}else{
			return openPickerShell(cellEditorWindow);
		}
	}
	
	protected Object clearBtnFunc() {
		path.getElements().clear();
		return null;
	}

	protected void updateContents(Object value) {
		if (value == null || "<未配置>".equals(value) || "".equals(value)) {
			super.updateContents("<未配置>");
		} else if ("<已配置>".equals(value)) {
			super.updateContents("<已配置>");
		} else {
			super.updateContents("<已配置>");
		}
	}

	private Object openNonePathPicker(Control cellEditorWindow) {
		NonePathPicker oncePicker = new NonePathPicker();
		
		getControl().getShell().setMinimized(true);
		oncePicker.startPicker(toFindWindow,path);
		getControl().getShell().setMinimized(false);
		
		if (path.getElements().size() == 0) {
			return null;
		} else {
			try {
				return path.toJson().toString();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	private Object openPickerShell(Control cellEditorWindow) {
		Display display = cellEditorWindow.getDisplay();
		pickerShell = new PickerShell(cellEditorWindow.getShell(), path, toFindWindow);
		pickerShell.addShellListener(new PickerShellListener());
		pickerShell.open();
		pickerShell.layout();
		
		// 实现等待办法
		pickerShell.getParent().setEnabled(false);
		while (!pickerShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		if (!saveMoment) {
			return null;
		}
		
		if (path.getElements().size() == 0) {
			return null;
		} else {
			try {
				return path.toJson().toString();
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	
	class PickerShellListener extends ShellAdapter {
		public void shellClosed(ShellEvent e) {
			if (pickerShell.callSaveDialog()) {
				e.doit = true;// 重点
				saveMoment = pickerShell.isSaveMode();
				if (saveMoment) {
					path = pickerShell.getPickerPath();
				}
				pickerShell.releasePicker();
				pickerShell.getParent().setEnabled(true);
			} else {
				e.doit = false;// 重点
			}
		}
	}


}