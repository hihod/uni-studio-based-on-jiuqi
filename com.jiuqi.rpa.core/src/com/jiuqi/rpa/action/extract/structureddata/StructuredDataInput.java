package com.jiuqi.rpa.action.extract.structureddata;

import java.util.ArrayList;

import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.lib.find.Path;

public class StructuredDataInput implements IActionInput {

	private Target nextLinkSelector;
	private int delayBetweenPagesMS;
	private int maxNumberOfResult;

	private ArrayList<DataTableField> ColumnsList=new ArrayList<DataTableField>();
	private Path rowPath=new Path();
	private ArrayList<Path> ColumnsPath=new ArrayList<Path>();

	public ArrayList<DataTableField> getColumnsList() {
		return ColumnsList;
	}

	public Path getRowPath() {
		return rowPath;
	}

	public void setRowPath(Path rowPath) {
		this.rowPath = rowPath;
	}

	public ArrayList<Path> getColumnsPath() {
		return ColumnsPath;
	}

	public int getDelayBetweenPagesMS() {
		return delayBetweenPagesMS;
	}

	public void setDelayBetweenPagesMS(int delayBetweenPagesMS) {
		this.delayBetweenPagesMS = delayBetweenPagesMS;
	}

	public int getMaxNumberOfResult() {
		return maxNumberOfResult;
	}

	public void setMaxNumberOfResult(int maxNumberOfResult) {
		this.maxNumberOfResult = maxNumberOfResult;
	}

	public Target getNextLinkSelector() {
		return nextLinkSelector;
	}

	public void setNextLinkSelector(Target nextLinkSelector) {
		this.nextLinkSelector = nextLinkSelector;
	}

}
