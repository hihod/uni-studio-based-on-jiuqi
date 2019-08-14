package com.jiuqi.etl.app.preference;

import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.jiuqi.etl.ui.editor.ControlFlowEditor;
import com.jiuqi.etl.ui.editor.DataFlowEditor;
import com.jiuqi.etl.ui.editor.ETLEditorPlugin;
import com.jiuqi.etl.ui.editor.ext.AdapterDefinition;
import com.jiuqi.etl.ui.editor.ext.DefinitionGroup;
import com.jiuqi.etl.ui.editor.ext.TaskDefinition;

public class CustomOrderPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	
	private List<DefinitionGroup> taskgroups;
	private List<DefinitionGroup> adaptergroups;
	private boolean isDirty = false;
	
	TabFolder tabFolder;
	TreeViewer taskTreeViewer;
	TreeViewer adapterTreeViewer;
	
	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createControls(composite);
		applyDialogFont(composite);
		return composite;
	}
	
	private void createControls(Composite parent) {		
		tabFolder = new TabFolder(parent, SWT.NONE);
		GridData tabFolderGridData = new GridData(GridData.FILL_BOTH);
		tabFolderGridData.widthHint = 300;
		tabFolderGridData.heightHint = 300;
		tabFolder.setLayoutData(tabFolderGridData);
		
		//树
		TabItem taskTab = new TabItem(tabFolder, SWT.NONE);
		taskTab.setText("任务");				
		Composite taskContainer = new Composite(tabFolder, SWT.NONE);
		taskTab.setControl(taskContainer);
		buildTaskTree(taskContainer);
		TabItem adapterTab = new TabItem(tabFolder, SWT.NONE);
		adapterTab.setText("适配器");
		Composite adapterContainer = new Composite(tabFolder, SWT.NONE);
		adapterTab.setControl(adapterContainer);
		buildAdapterTree(adapterContainer);
		//按钮
		Composite btnComposite = new Composite(parent, SWT.NONE);
		btnComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		buildBtns(btnComposite);
	}
	
	private void buildBtns(Composite btnComposite) {
		btnComposite.setLayout(new GridLayout());
		
		new Label(btnComposite, SWT.NONE);//占位
		
		Button moveupBtn = new Button(btnComposite, SWT.NONE);
		moveupBtn.setText("上移");
		moveupBtn.addSelectionListener(new SelectionListener() {			
			public void widgetSelected(SelectionEvent e) {
				move(true);
			}
						
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		Button movedownBtn = new Button(btnComposite, SWT.NONE);
		movedownBtn.setText("下移");
		movedownBtn.addSelectionListener(new SelectionListener() {			
			public void widgetSelected(SelectionEvent e) {
				move(false);
			}
						
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	private void move(boolean isUp) {
		List<DefinitionGroup> groups;
		TreeViewer treeViewer = null;
		if (tabFolder.getSelectionIndex() == 0) {
			groups = taskgroups;
			treeViewer = taskTreeViewer;
		} else {
			groups = adaptergroups;
			treeViewer = adapterTreeViewer;
		}
		IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
		if (selection == null || selection.size() == 0)
			return;
		
		Object obj = selection.getFirstElement();
		if (obj instanceof DefinitionGroup) {			
			int curIdx = groups.indexOf(obj);
			if ((isUp && curIdx > 0) || (!isUp && curIdx < groups.size() - 1)) {
				int tarIdx = isUp ? curIdx - 1 : curIdx + 1;
				Object temp = groups.get(tarIdx);
				groups.set(tarIdx, (DefinitionGroup) obj);
				groups.set(curIdx, (DefinitionGroup) temp);
				treeViewer.refresh();
				
				isDirty = true;
			}
		} else if (obj instanceof TaskDefinition){
			TaskDefinition taskDefinition = (TaskDefinition)obj;
			DefinitionGroup definitionGroup = find(taskgroups, taskDefinition.group);
			int curIdx = definitionGroup.children.indexOf(obj);
			if ((isUp && curIdx > 0) || (!isUp && curIdx < definitionGroup.children.size() - 1)) {
				int tarIdx = isUp ? curIdx - 1 : curIdx + 1;
				Object temp = definitionGroup.children.get(tarIdx);
				definitionGroup.children.set(tarIdx, obj);
				definitionGroup.children.set(curIdx, temp);
				taskTreeViewer.refresh();
				
				isDirty = true;
			}
		} else if (obj instanceof AdapterDefinition){
			AdapterDefinition adapterDefinition = (AdapterDefinition)obj;
			DefinitionGroup definitionGroup = find(adaptergroups, adapterDefinition.group);
			int curIdx = definitionGroup.children.indexOf(obj);
			if ((isUp && curIdx > 0) || (!isUp && curIdx < definitionGroup.children.size() - 1)) {
				int tarIdx = isUp ? curIdx - 1 : curIdx + 1;
				Object temp = definitionGroup.children.get(tarIdx);
				definitionGroup.children.set(tarIdx, obj);
				definitionGroup.children.set(curIdx, temp);
				adapterTreeViewer.refresh();
				
				isDirty = true;
			}
		}
	}

	private void buildTaskTree(Composite parent) {
		parent.setLayout(new FillLayout());
		taskTreeViewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
				
		taskTreeViewer.setContentProvider(new ITreeContentProvider() {
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof DefinitionGroup)
					return ((DefinitionGroup)parentElement).children.toArray();
				
				return null;
			}			

			public boolean hasChildren(Object element) {								
				return (element instanceof DefinitionGroup);
			}

			public Object[] getElements(Object inputElement) {
				return taskgroups.toArray();
			}

			public Object getParent(Object element) {return null;}
			public void dispose() {}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
			
		});
		
		taskTreeViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DefinitionGroup)
					return ((DefinitionGroup)element).group;
				else if (element instanceof TaskDefinition) {
					TaskDefinition taskDefinition = (TaskDefinition)element;
					return taskDefinition.name;
				}
					
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				if (element instanceof DefinitionGroup)
					return ETLEditorPlugin.getDefault().getImageRegistry().get(ETLEditorPlugin.IMG_CONTROL_FLOW);
				else if (element instanceof TaskDefinition) {
					ImageDescriptor smallIco = ((TaskDefinition)element).smallIcon;
					if (smallIco != null)
						return smallIco.createImage();
				}
				
				return null;
			}
		});
		
		taskTreeViewer.getTree().addKeyListener(new KeyListener() {			
			public void keyPressed(KeyEvent e) {
				if (e.stateMask != SWT.SHIFT && e.stateMask != SWT.CTRL)
					return;
				
				switch (e.keyCode) {
					case SWT.ARROW_UP: {
						move(true);
						e.doit = false;
						break;
					}
					case SWT.ARROW_DOWN: {
						move(false);
						e.doit = false;
						break;
					}	
					default:;
				}
			}
			public void keyReleased(KeyEvent e) {}
		});
		
		taskTreeViewer.setInput(taskgroups);		
	}
	
	private void buildAdapterTree(Composite parent) {
		parent.setLayout(new FillLayout());		
		adapterTreeViewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);		
				
		adapterTreeViewer.setContentProvider(new ITreeContentProvider() {
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof DefinitionGroup)
					return ((DefinitionGroup)parentElement).children.toArray();
				
				return null;
			}			

			public boolean hasChildren(Object element) {								
				return (element instanceof DefinitionGroup);
			}

			public Object[] getElements(Object inputElement) {
				return adaptergroups.toArray();
			}

			public Object getParent(Object element) {return null;}
			public void dispose() {}
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
			
		});
		
		adapterTreeViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof DefinitionGroup)
					return ((DefinitionGroup)element).group;
				else if (element instanceof AdapterDefinition) {
					AdapterDefinition adapterDefinition = (AdapterDefinition)element;
					return adapterDefinition.name;
				}
					
				return null;
			}
			
			@Override
			public Image getImage(Object element) {
				if (element instanceof DefinitionGroup)
					return ETLEditorPlugin.getDefault().getImageRegistry().get(ETLEditorPlugin.IMG_CONTROL_FLOW);
				else if (element instanceof AdapterDefinition) {
					ImageDescriptor smallIco = ((AdapterDefinition)element).smallIcon;
					if (smallIco != null)
						return smallIco.createImage();
				}
				return null;
			}
		});
		
		adapterTreeViewer.getTree().addKeyListener(new KeyListener() {			
			public void keyPressed(KeyEvent e) {
				if (e.stateMask != SWT.SHIFT && e.stateMask != SWT.CTRL)
					return;
				
				switch (e.keyCode) {
					case SWT.ARROW_UP: {
						move(true);
						e.doit = false;
						break;
					}
					case SWT.ARROW_DOWN: {
						move(false);
						e.doit = false;
						break;
					}	
					default:;
				}
			}
			public void keyReleased(KeyEvent e) {}
		});
		
		adapterTreeViewer.setInput(adaptergroups);
	}

	public void init(IWorkbench workbench) {
		taskgroups = DefinitionGroup.loadTaskGroup();
		adaptergroups = DefinitionGroup.loadAdapterGroup();		
	}
	
	private DefinitionGroup find(List<DefinitionGroup> definitionGroups, String group) {
		for (DefinitionGroup definitionGroup: definitionGroups) {
			if (group.equals(definitionGroup.group))
				return definitionGroup;
		}
		
		return null;
	}

	@Override
	public boolean performOk() {
		if (isDirty) {
			DefinitionGroup.saveTaskGroup(taskgroups);
			DefinitionGroup.saveAdapterGroup(adaptergroups);
			
			ControlFlowEditor.clearPaletteRoot();
			DataFlowEditor.clearPaletteRoot();
		}
		
		return super.performOk();
	}
	
	@Override
	protected void performDefaults() {
		taskgroups = DefinitionGroup.loadDefaultTaskGroup();
		adaptergroups = DefinitionGroup.loadDefaultAdapterGroup();
		
		taskTreeViewer.refresh();
		adapterTreeViewer.refresh();
		
		super.performDefaults();
	}
}
