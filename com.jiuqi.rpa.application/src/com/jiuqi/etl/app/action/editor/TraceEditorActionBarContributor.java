/*
 * 2010-9-20
 *
 * Õı–«”Ó
 */
package com.jiuqi.etl.app.action.editor;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * @author Õı–«”Ó
 *
 */
public class TraceEditorActionBarContributor extends EditorActionBarContributor {
	
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		RefreshAction refreshAction = new RefreshAction(getPage().getWorkbenchWindow());
		AutoRefreshAction autoRefreshAction = new AutoRefreshAction(getPage().getWorkbenchWindow());
		toolBarManager.add(new TraceAction(getPage().getWorkbenchWindow(), refreshAction, autoRefreshAction));
		toolBarManager.add(refreshAction);
		toolBarManager.add(autoRefreshAction);
	}
}
