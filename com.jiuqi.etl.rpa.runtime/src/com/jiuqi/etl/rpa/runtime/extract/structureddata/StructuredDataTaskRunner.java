package com.jiuqi.etl.rpa.runtime.extract.structureddata;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.bi.dataset.BlobValue;
import com.jiuqi.bi.dataset.Column;
import com.jiuqi.bi.dataset.DataRow;
import com.jiuqi.bi.dataset.MemoryDataSet;
import com.jiuqi.bi.dataset.Metadata;
import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.DataType;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.ETLEnvException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.env.datatable.DataTableValue;
import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.etl.rpa.runtime.ExtractedData;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.extract.structureddata.StructuredDataAction;
import com.jiuqi.rpa.action.extract.structureddata.StructuredDataInput;
import com.jiuqi.rpa.action.extract.structureddata.StructuredDataOutput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class StructuredDataTaskRunner implements ITaskRunner {
	private StructuredDataTaskModel taskModel;
	private Env env;
	public StructuredDataTaskRunner(StructuredDataTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		StructuredDataInput actionInput = toActionInput(taskModel);
		StructuredDataAction action = new StructuredDataAction(actionInput);
		int dateCount = 300;
		try{
			dateCount = Integer.parseInt(taskModel.getMaxNumberOfResult());
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getMaxNumberOfResult());
			if(paramModel!=null){
				dateCount = ((int) env.getValue(taskModel.getMaxNumberOfResult()));
			}
		}
		try {
			Context context = new RPAEnvHelper().getContext(env);
			StructuredDataOutput output = (StructuredDataOutput)action.run(context);
			MemoryDataSet<DataTableField> dataTable = output.getDataTable();
			ParameterModel paramModel = env.getParamModel(taskModel.getOutputParamName());
			if(paramModel==null){
				throw new ETLEngineException("未找到指定输出数据表参数");
			}
			//进行数据映射
			MemoryDataSet<DataTableField> paramTable = new MemoryDataSet<DataTableField>();
			Metadata<DataTableField> metadata = paramTable.getMetadata();
			for (int i = 0; i < taskModel.getFields().size(); i++) {
				DataTableField f = taskModel.getFields().get(i).toDataTableField();
				Column<DataTableField> column = new Column<DataTableField>(f.getName(), f.getType().value());
				column.setTitle(f.getTitle());
				column.setInfo(f);
				metadata.addColumn(column);
			}
			for (DataRow dataRow : dataTable) {
				DataRow row = paramTable.add();
				for (int i = 0; i < metadata.getColumnCount(); i++) {
					//定位到索引
					int index = dirDataTableIndex(dataTable.getMetadata(),taskModel.getFields().get(i).getMapping());
					if(index>-1){
						setConvertValue(row,i,metadata.getColumn(i).getDataType(),dataRow.getString(index));
					}else{
						row.setNull(i);
					}
				}
				if(paramTable.size()>=dateCount)
					break;
			}
			
			DataTableValue dt = new DataTableValue(paramModel, paramTable);
			env.setValue(taskModel.getOutputParamName(), dt);
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(),e);
		} catch (ETLEnvException e) {
			e.printStackTrace();
		}
	}
	private String booeleanStr = "NO否0false";
	private void setConvertValue(DataRow row, int i, int datatype, String string) {
		switch (DataType.valueOf(datatype)) {
		case BOOLEAN:
			row.setBoolean(i, booeleanStr.indexOf(string.trim())==-1);
			break;
		case INTEGER:
			row.setInt(i, Integer.parseInt(string));
			break;
		case DOUBLE:
			row.setDouble(i, Double.parseDouble(string));
			break;
		case BIGDECIMAL:
			row.setBigDecimal(i, BigDecimal.valueOf(Long.parseLong(string)));
			break;
		case STRING:
			row.setString(i, string);
			break;
		case DATETIME:
			String strFormater = "yyyy-MM-dd";
			if(string.indexOf("/")!=-1){
				strFormater = "yyyy/MM/dd";
			}
			SimpleDateFormat sdf= new SimpleDateFormat(strFormater);
			Date date = null;
			try {
				date = sdf.parse(string);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			row.setDate(i, calendar);
			break;
		case BLOB:
			row.setBlob(i, new BlobValue(string.getBytes()));
			break;
		default:
			row.setNull(i);
			break;
		}
	}

	private int dirDataTableIndex(Metadata<DataTableField> metadata, String name) {
		for (int i = 0; i < metadata.getColumnCount(); i++) {
			if(metadata.getColumn(i).getName().equals(name)){
				return i;
			}
		}
		return -1;
	}

	private StructuredDataInput toActionInput(StructuredDataTaskModel model) throws ETLEngineException{
		StructuredDataInput input = new StructuredDataInput();
		try{
			input.setDelayBetweenPagesMS(Integer.parseInt(model.getDelayBetweenPagesMS()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(model.getDelayBetweenPagesMS());
			if(paramModel!=null){
				input.setDelayBetweenPagesMS((int) env.getValue(model.getDelayBetweenPagesMS()));
			}
		}
		try{
			input.setMaxNumberOfResult(Integer.parseInt(model.getMaxNumberOfResult()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(model.getMaxNumberOfResult());
			if(paramModel!=null){
				input.setMaxNumberOfResult((int) env.getValue(model.getMaxNumberOfResult()));
			}
		}
		Target nextLink = new Target();
		String nextLinkElementName = taskModel.getNextLinkSelector().getElement();
		if (StringUtils.isNotEmpty(nextLinkElementName)) {
			IUIHandler element = new RPAEnvHelper().getElement(env, nextLinkElementName);
			nextLink.setElement(element);
		}
		nextLink.setPath(taskModel.getNextLinkSelector().getPath());
		try{
			nextLink.setTimeout(Integer.parseInt(taskModel.getNextLinkSelector().getTimeout()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getNextLinkSelector().getTimeout());
			if(paramModel!=null){
				nextLink.setTimeout((int) env.getValue(taskModel.getNextLinkSelector().getTimeout()));
			}
		}
		input.setNextLinkSelector(nextLink);
		
		ExtractedData ex = new ExtractedData();
		try {
			ex.calInfo(new JSONObject(model.getExtractMetaData()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		input.setRowPath(ex.getRowPath());
		input.getColumnsList().addAll(ex.getColumnsList());
		input.getColumnsPath().addAll(ex.getColumnsPath());
		return input;
	}
	
}
