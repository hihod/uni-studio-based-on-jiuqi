/*
 * 2010-9-15
 *
 * 王星宇
 */
package com.jiuqi.etl.app.action.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;

import com.jiuqi.widgets.trace.TraceComposite;

/**
 * 一个现实连接跟踪的只读编辑器
 * @author 王星宇
 *
 */
public class TraceEditor extends EditorPart {
	
	public static final String ID = "com.jiuqi.etl.app.trace";
	
	private TraceComposite traceComposite;
	
	public TraceEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}
	
	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}
	
	@Override
	public void setTitleToolTip(String toolTip) {
		super.setTitleToolTip(toolTip);
	}
	
	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		traceComposite = new TraceComposite(parent, SWT.NONE);
		traceComposite.setDelay(AutoRefreshAction.getDelay());
	}

	@Override
	public void setFocus() {
	}

	public void refresh() {
		traceComposite.refresh();
	}
	
	public void setDelay(int delay) {
		traceComposite.setDelay(delay);
	}
}
