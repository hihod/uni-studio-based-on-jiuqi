package com.jiuqi.rpa.action.extract.structureddata;

import com.jiuqi.bi.dataset.MemoryDataSet;
import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.rpa.action.IActionOutput;
/**
 * 
 * @author liangxiao01
 */
public class StructuredDataOutput implements IActionOutput {
	private MemoryDataSet<DataTableField> dataTable;

	public MemoryDataSet<DataTableField> getDataTable() {
		return dataTable;
	}

	public void setDataTable(MemoryDataSet<DataTableField> dataTable) {
		this.dataTable = dataTable;
	}

}
