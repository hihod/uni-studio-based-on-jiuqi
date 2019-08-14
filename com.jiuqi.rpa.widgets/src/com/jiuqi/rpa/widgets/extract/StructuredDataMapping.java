package com.jiuqi.rpa.widgets.extract;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.json.JSONException;
import org.json.JSONObject;
import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.runtime.ExtractedData;
import com.jiuqi.etl.rpa.runtime.extract.structureddata.OutputField;
import com.jiuqi.etl.rpa.runtime.extract.structureddata.StructuredDataTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.widgets.Activator;
import com.jiuqi.widgets.linkedtable.DragableLinkedTable;
import com.jiuqi.widgets.linkedtable.LinkChangeEvent;
import com.jiuqi.widgets.linkedtable.LinkChangeListener;
import com.jiuqi.widgets.linkedtable.LinkProvider;
import com.jiuqi.widgets.linkedtable.LinkTestProvider;

public class StructuredDataMapping extends WizardPage{
	private DragableLinkedTable linkedTable;
	private TableViewer tv_left;
	private TableViewer tv_right;
	private StructuredDataTaskModel model;
	private Button btn_autoMapping;
	private Button btn_cancel;
	private Combo paramNameCombo;// 参数名称下拉框
	private List<ParameterModel> paramList = new ArrayList<ParameterModel>();
	private List<DataTableField> inputFields = new ArrayList<DataTableField>();;
	public StructuredDataMapping(String pageName, TaskContext context,StructuredDataTaskModel cloned_model) {
		super(pageName);
		if(cloned_model ==null){
			this.model = ((StructuredDataTaskModel) context.getTaskModel()).clone();
		}else{
			this.model = cloned_model;			
		}
		try{
			inputFields.clear();
			String jsonStr = model.getExtractMetaData();
			if(StringUtils.isNotEmpty(jsonStr)){
				JSONObject jsonObj = new JSONObject(jsonStr);
				ExtractedData exData = new ExtractedData();
				exData.calInfo(jsonObj);
				inputFields.addAll(exData.getColumnsList());
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		ParameterModel[] paramArray = context.getParameters(new DataType[] { DataType.DATATABLE });
		for (ParameterModel paramModel : paramArray) {
			paramList.add(paramModel);
		}

	}
	public void createControl(Composite parent) {
		setTitle("字段映射");
		setDescription("结构化数据映射到指定参数中");
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		setControl(container);
		String[] paramNameSet = new String[paramList.size()];
		for (int i = 0; i < paramList.size(); i++) {
			paramNameSet[i] = paramList.get(i).getName();
		}

		Composite composite = new Composite(container, SWT.NONE);

		Label label = new Label(composite, SWT.NONE);
		label.setLocation(10, 0);
		label.setSize(60, 27);
		label.setText("参数名称：");
		paramNameCombo = new Combo(composite, SWT.READ_ONLY);
		paramNameCombo.setLocation(70, 0);
		paramNameCombo.setSize(294, 25);
		paramNameCombo.setItems(paramNameSet);
		paramNameCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String selection = paramNameCombo.getText();
				for (ParameterModel parameter : paramList) {
					if (parameter.getName().equals(selection)) {
						if (parameter.getName().equals(model.getOutputParamName())) {
							return;
						}
						model.setOutputParamName(parameter.getName());
						model.getFields().clear();
						for (DataTableField f : parameter.getFields()) {
							OutputField fd = new OutputField();
							fd.setName(f.getName());
							fd.setTitle(f.getTitle());
							fd.setType(f.getType());
							model.getFields().add(fd);

						}
						tv_right.setInput(model.getFields());
						tv_right.refresh();
						linkedTable.finishLink();
					}
				}
				//validate();
			}
		});
		new Label(container, SWT.NONE);
		final Label separatorLabel = new Label(container, SWT.HORIZONTAL | SWT.SEPARATOR);
		GridData separatorLabelData = new GridData(GridData.FILL_HORIZONTAL);
		separatorLabelData.horizontalSpan = 3;
		separatorLabel.setLayoutData(separatorLabelData);

		Group group = new Group(container, SWT.NONE);
		group.setText("参数映射");
		group.setLayout(new GridLayout(2, false));
		GridData gd_group = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_group.heightHint = 350;
		gd_group.widthHint = 810;
		group.setLayoutData(gd_group);

		linkedTable = new DragableLinkedTable(group, SWT.NONE | SWT.LEFT);
		linkedTable.setWeights(40, 15, 45);
		linkedTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		setLinkProviderAndLinstener();

		tv_left = linkedTable.getLeftTableViewer();
		tv_right = linkedTable.getRightTableViewer();

		setLeftTable();
		setRightTable();

		Composite c_center = new Composite(group, SWT.NONE);
		GridLayout gl_c_center = new GridLayout(1, false);
		gl_c_center.marginTop = 10;
		c_center.setLayout(gl_c_center);
		c_center.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, true, 1, 1));

		btn_autoMapping = new Button(c_center, SWT.NONE);
		btn_autoMapping.setImage(AUTOMAPPING);
		btn_autoMapping.setToolTipText("自动映射名称一致的字段");

		btn_cancel = new Button(c_center, SWT.NONE);
		btn_cancel.setImage(CANCEL);
		btn_cancel.setToolTipText("取消映射");

		new Label(container, SWT.NONE);

		addButtonListener();
		linkedTable.finishLink();
		validate();
	}

	private void setLeftTable() {
		Table table = tv_left.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// 设置表头
		TableColumn tc_name = new TableColumn(table, SWT.LEFT);
		tc_name.setText("字段名称");
		tc_name.setWidth(160);

		TableColumn tc_title = new TableColumn(table, SWT.LEFT);
		tc_title.setText("字段标题");
		tc_title.setWidth(60);

		tv_left.setContentProvider(new IStructuredContentProvider() {

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			public void dispose() {
			}

			public Object[] getElements(Object inputElement) {
				return inputElement == null ? new Object[0] : ((List<?>) inputElement).toArray();
			}
		});
		tv_left.setLabelProvider(new ITableLabelProvider() {

			public void removeListener(ILabelProviderListener listener) {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void dispose() {
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public String getColumnText(Object element, int columnIndex) {
				DataTableField field = (DataTableField) element;
				switch (columnIndex) {
				case 0:
					return field.getName();
				case 1:
					return field.getTitle();
				default:
					return null;
				}
			}

			public Image getColumnImage(Object element, int columnIndex) {
				if (columnIndex == 0) {
					return FIELD;
				} else {
					return null;
				}
			}
		});
		tv_left.setInput(inputFields);
	}

	private void setRightTable() {
		Table table = tv_right.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// 设置表头
		TableColumn tc_name = new TableColumn(table, SWT.LEFT);
		tc_name.setText("字段名称");
		tc_name.setWidth(160);

		TableColumn tc_type = new TableColumn(table, SWT.LEFT);
		tc_type.setText("字段类型");
		tc_type.setWidth(60);

		TableColumn tc_mapping = new TableColumn(table, SWT.LEFT);
		tc_mapping.setText("映射字段");
		tc_mapping.setWidth(80);

		TableColumn tc_comment = new TableColumn(table, SWT.LEFT);
		tc_comment.setText("备注");
		tc_comment.setWidth(60);

		tv_right.setContentProvider(new IStructuredContentProvider() {

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			public void dispose() {
			}

			public Object[] getElements(Object inputElement) {
				return inputElement == null ? new Object[0] : ((List<?>) inputElement).toArray();
			}
		});
		tv_right.setLabelProvider(new ITableLabelProvider() {

			public void removeListener(ILabelProviderListener listener) {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void dispose() {
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public String getColumnText(Object element, int columnIndex) {
				OutputField field = (OutputField) element;
				switch (columnIndex) {
				case 0:
					return field.getName();
				case 1:
					return field.getType().toString();
				case 2:
					return field.getMapping();
				case 3:
					return field.getTitle();
				default:
					return null;
				}
			}

			public Image getColumnImage(Object element, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return FIELD;
				default:
					return null;
				}
			}
		});
		if (!StringUtils.isEmpty(model.getOutputParamName())) {
			paramNameCombo.setText(model.getOutputParamName());
			// 刷新表格数据
			List<OutputField> prepareRefreshFields = new ArrayList<OutputField>();
			// 判断当前的参数是否存在
			Boolean existCurrentSelect = false;
			for (int i = 0; i < paramList.size(); i++) {
				if (paramList.get(i).getName().equals(model.getOutputParamName())) {
					existCurrentSelect = true;
					List<DataTableField> thisFields = paramList.get(i).getFields();
					for (DataTableField dataTableField : thisFields) {
						OutputField o = new OutputField();
						o.setName(dataTableField.getName());
						o.setTitle(dataTableField.getTitle());
						o.setType(dataTableField.getType());
						prepareRefreshFields.add(o);
					}
					break;
				}
			}
			if (existCurrentSelect) {

			} else {
				setErrorMessage("找不到参数【" + model.getOutputParamName() + "】");
			}
			for (OutputField o : model.getFields()) {
				for (int i = 0; i < prepareRefreshFields.size(); i++) {
					if (o.getName().equals(prepareRefreshFields.get(i).getName())) {
						prepareRefreshFields.get(i).setMapping(o.getMapping());
					}
				}
			}
			model.getFields().clear();
			model.getFields().addAll(prepareRefreshFields);
			tv_right.setInput(model.getFields());
			tv_right.refresh();

		} else {
			tv_right.setInput(null);
		}
	}

	private void addButtonListener() {
		btn_autoMapping.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				List<OutputField> fields = model.getFields();
				for (int i = 0; i < fields.size(); i++) {
					String fieldName = fields.get(i).getName();
					if (StringUtils.isEmpty(fields.get(i).getMapping())) {
						for (int j = 0; j < inputFields.size(); j++) {
							if (fieldName.equalsIgnoreCase(inputFields.get(j).getName())) {
								fields.get(i).setMapping(inputFields.get(j).getName());
								break;
							}
						}
					}
				}

				// title 配对
				for (int i = 0; i < fields.size(); i++) {
					String titleName = fields.get(i).getTitle();
					if (titleName == null)
						continue;
					if (StringUtils.isEmpty(fields.get(i).getMapping())) {
						for (int j = 0; j < inputFields.size(); j++) {
							if (titleName.equalsIgnoreCase(inputFields.get(j).getTitle())) {
								fields.get(i).setMapping(inputFields.get(j).getName());
								break;
							}
						}
					}
				}

				// title->name ,name->title
				for (int i = 0; i < fields.size(); i++) {
					String fieldName = fields.get(i).getName();
					String titleName = fields.get(i).getTitle();

					if (titleName == null) {
						if (StringUtils.isEmpty(fields.get(i).getMapping())) {
							for (int j = 0; j < inputFields.size(); j++) {
								String inputTitle = inputFields.get(j).getTitle();
								if (fieldName.equalsIgnoreCase(inputTitle)) {
									fields.get(i).setMapping(inputFields.get(j).getName());
									break;
								}
							}
						}
					} else {
						if (StringUtils.isEmpty(fields.get(i).getMapping())) {
							for (int j = 0; j < inputFields.size(); j++) {
								String inputName = inputFields.get(j).getName();
								String inputTitle = inputFields.get(j).getTitle();
								if (titleName.equalsIgnoreCase(inputName) || fieldName.equalsIgnoreCase(inputTitle)) {
									fields.get(i).setMapping(inputFields.get(j).getName());
									break;
								}
							}
						}
					}
				}

				linkedTable.finishLink();
				tv_right.refresh();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		btn_cancel.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection s = (IStructuredSelection) tv_right.getSelection();
				if (!s.isEmpty()) {
					for (
					Iterator<?> it = s.iterator(); it.hasNext();) {
						OutputField field = (OutputField) it.next();
						field.setMapping(null);
						tv_right.update(field, null);
					}
					linkedTable.finishLink();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private boolean testLinkType(DataTableField left, OutputField right) {
		return true;
		/*
		DataType leftType = left.getType();
		DataType rightType = right.getType();
		if (leftType == DataType.UNKNOWN) {
			return false;
		} else if (rightType == DataType.UNKNOWN) {
			return false;
		} else {
			return leftType.canConvertTo(rightType);
		}
		*/
	}
	private int getMappingIndex(String s){
		for (DataTableField f :inputFields) {
			if (f.getName().equals(s)) {
				return 1;
			}
		}
		return 0;
	}
	private void setLinkProviderAndLinstener() {
		linkedTable.setLinkProvider(new LinkProvider() {

			public boolean hasLink(Object left, Object right) {
				DataTableField lField = (DataTableField) left;
				OutputField rField = (OutputField) right;
				String mapping = rField.getMapping();
				if (getMappingIndex(mapping) != 0) {
					String name = lField.getName();
					if (name.equalsIgnoreCase(mapping)) {
						if (testLinkType(lField, rField)) {
							return true;
						} else {
							rField.setMapping(null);
							tv_right.update(rField, null);
						}
					}

				} else {
					rField.setMapping(null);
					tv_right.update(rField, null);
				}
				return false;
			}
		});
		linkedTable.setLinkTestProvider(new LinkTestProvider() {

			public boolean testLink(Object source, Object target) {
				DataTableField lField = (DataTableField) source;
				OutputField rField = (OutputField) target;
				return testLinkType(lField, rField);
			}
		});
		linkedTable.addLinkLinstener(new LinkChangeListener() {

			public void linkDelete(LinkChangeEvent e) {
				e.doit = true;
				OutputField field = (OutputField) e.rightObject;
				field.setMapping(null);
				tv_right.update(field, null);
				validate();
			}

			public void linkCreate(LinkChangeEvent e) {
				DataTableField lField = (DataTableField) e.leftObject;
				OutputField rField = (OutputField) e.rightObject;
				if (StringUtils.isEmpty(rField.getMapping())) {
					e.doit = true;
					rField.setMapping(lField.getName());
					tv_right.update(rField, null);
					validate();
				} else {
					if (MessageDialog.openQuestion(null, "数据库输出适配器",
							"右表字段" + rField.getName() + "已经连到左表字段" + rField.getMapping() + "，是否要删除已存在的连线？")) {
						e.doit = true;
						rField.setMapping(lField.getName());
						tv_right.update(rField, null);
					} else {
						e.doit = false;
					}
				}
			}
		});
	}

	private void validate() {
		if (model.getOutputParamName() == null || "".equals(model.getOutputParamName())) {
			setErrorMessage("未选择数据表参数");
			setPageComplete(false);
			return;
		}
		if (model.getFields().size() == 0) {
			setErrorMessage("请配置输出字段");
			setPageComplete(false);
			return;
		} else {
			List<OutputField> fields = model.getFields();
			boolean isExsit = false;
			for (int i = 0; i < fields.size(); i++) {
				if (StringUtils.isNotEmpty(fields.get(i).getMapping())) {
					isExsit = true;
					break;
				}
			}
			if (!isExsit) {
				setErrorMessage("请配置字段映射");
				setPageComplete(false);
				return;
			}
		}
		setErrorMessage(null);
		setPageComplete(true);
	}

	private static final Image AUTOMAPPING = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_AUTOMAPPING);
	private static final Image CANCEL = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_CANCELMAPPING);
	private static final Image FIELD = Activator.getDefault().getImageRegistry().get(Activator.IMAGE_FIELD);
	public boolean canFinish() {
		validate();
		return true;
	}

	public StructuredDataTaskModel getTaskModel() {
		return model;
	}
	public void buildTableAndLink() {
		inputFields.clear();
		String jsonStr = model.getExtractMetaData();
		try {
			if(StringUtils.isNotEmpty(jsonStr)){
				JSONObject jsonObj = new JSONObject(jsonStr);
				ExtractedData exData = new ExtractedData();
				exData.calInfo(jsonObj);
				inputFields.addAll(exData.getColumnsList());
			}
			tv_left.setInput(inputFields);
			linkedTable.finishLink();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean canFlipToNextPage(){
        return false;
    }
}
