package com.jiuqi.etl.rpa.ui.extract.structureddata;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.rpa.runtime.ExtractedData;
import com.jiuqi.etl.rpa.runtime.extract.structureddata.StructuredDataTaskModel;
import com.jiuqi.etl.ui.editor.ext.TaskContext;
import com.jiuqi.rpa.widgets.extract.TableSelectorWizard;
import com.jiuqi.rpa.widgets.extract.TableSelectorWizardDialog;

/**
 * UIÑ¡ÔñÆ÷Ãæ°å
 * 
 * @author liangxiao01
 */
public class MetaDataDialogEditor extends DialogCellEditor {
	TaskContext context;
	public MetaDataDialogEditor(Composite parent, TaskContext context) {
		super(parent);
		this.context = context;
	}
	

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		String metaData = ((StructuredDataTaskModel)(context.getTaskModel())).getExtractMetaData();
		ExtractedData exData = new ExtractedData();
		if(StringUtils.isNotEmpty(metaData)){
			try{
				JSONObject metadataJsonObj = new JSONObject(metaData);
				exData.calInfo(metadataJsonObj);	
				metaData = ExtractedData.getXMLText(exData.getColumnsList(), exData.getRowPath(), exData.getColumnsPath());
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		TableSelectorWizardDialog dlg = new TableSelectorWizardDialog(cellEditorWindow.getShell(), StringUtils.isEmpty(metaData)? new TableSelectorWizard(context): new TableSelectorWizard(new ExtractedData(metaData),context));
		int windowReturnCode = dlg.open();
		if(windowReturnCode==Window.OK){
			return ((StructuredDataTaskModel)(context.getTaskModel())).getExtractMetaData();
		}else{
			return null;
		}
		
	}

	@Override
	protected void updateContents(Object value) {
		if (value == null || "<Î´ÅäÖÃ>".equals(value) || "".equals(value)) {
			super.updateContents("<Î´ÅäÖÃ>");
		} else if ("<ÒÑÅäÖÃ>".equals(value)) {
			super.updateContents("<ÒÑÅäÖÃ>");
		} else {
			super.updateContents("<ÒÑÅäÖÃ>");
		}

	}

}