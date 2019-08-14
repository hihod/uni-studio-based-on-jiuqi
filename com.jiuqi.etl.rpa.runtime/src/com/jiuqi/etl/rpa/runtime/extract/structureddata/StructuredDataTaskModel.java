package com.jiuqi.etl.rpa.runtime.extract.structureddata;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.model.ControlFlowModel;
import com.jiuqi.etl.model.ETLModelException;
import com.jiuqi.etl.model.IModel;
import com.jiuqi.etl.model.TaskModel;
import com.jiuqi.etl.model.graph.Context;
import com.jiuqi.etl.model.problem.IProblems;
import com.jiuqi.etl.model.problem.Problem;
import com.jiuqi.etl.model.problem.Problem.Level;
import com.jiuqi.etl.rpa.runtime.TaskTarget;
import com.jiuqi.rpa.lib.find.Path;

/**
 * 结构化数据提取
 * 
 * @author liangxiao01
 */
public class StructuredDataTaskModel extends TaskModel {
	
	private String outputParamName;
	private String extractMetaData;
	private TaskTarget nextLinkSelector = new TaskTarget();
	private String delayBetweenPagesMS = "300";
	private String maxNumberOfResult = "100";
	private List<OutputField> fields = new ArrayList<OutputField>();
	
	private final String ATTR_OUTPUT_PARAM_NAME = "PARAM_NAME";
	private final String ATTR_TARGET_ELEMENT = "TARGET_ELEMENT";
	private final String ATTR_TARGET_PATH = "TARGET_PATH";
	private final String ATTR_TARGET_TIMEOUT = "TARGET_TIMEOUT";
	private final String ATTR_EXTRACT_META_DATA = "EXTRACT_META_DATA";
	private final String ATTR_NEXT_LINK_SELECTOR = "NEXT_LINK_SELECTOR";
	private final String ATTR_DELAY_BETWEEN_PAGES_MS = "DELAY_BETWEEN_PAGES_MS";
	private final String ATTR_MAX_NUMBER_OF_RESULT = "MAX_NUMBER_OF_RESULT";
	private final String ATTR_CONFIG_FIELDS = "CONFIG_FIELDS";
	

	protected void saveToJson(JSONObject e, Context context) throws ETLModelException {
		try {
			e.putOpt(ATTR_OUTPUT_PARAM_NAME, outputParamName);
			e.putOpt(ATTR_EXTRACT_META_DATA, extractMetaData);
			e.putOpt(ATTR_DELAY_BETWEEN_PAGES_MS, delayBetweenPagesMS);
			e.putOpt(ATTR_MAX_NUMBER_OF_RESULT, maxNumberOfResult);
			JSONArray fieldsArray = new JSONArray();
			for (OutputField f : fields) {
				fieldsArray.put(f.toJson());
			}
			e.putOpt(ATTR_CONFIG_FIELDS, fieldsArray);
			JSONObject nextLinkObj = new JSONObject();
			if(nextLinkSelector.getElement() != null){
				nextLinkObj.putOpt(ATTR_TARGET_ELEMENT, nextLinkSelector.getElement());
			}
			if(nextLinkSelector.getPath() != null){
				nextLinkObj.putOpt(ATTR_TARGET_PATH, nextLinkSelector.getPath().toJson().toString());
			}
			nextLinkObj.putOpt(ATTR_TARGET_TIMEOUT, nextLinkSelector.getTimeout());
			e.put(ATTR_NEXT_LINK_SELECTOR, nextLinkObj);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	protected void loadFromJson(JSONObject e, Context context) throws ETLModelException {
		JSONObject obj = e;
		this.outputParamName = obj.optString(ATTR_OUTPUT_PARAM_NAME);
		this.extractMetaData = obj.optString(ATTR_EXTRACT_META_DATA);
		JSONObject nextLinkObj = obj.optJSONObject(ATTR_NEXT_LINK_SELECTOR);
		this.nextLinkSelector.setTimeout(nextLinkObj.optString(ATTR_TARGET_TIMEOUT));
		if(nextLinkObj.has(ATTR_TARGET_ELEMENT)){
			this.nextLinkSelector.setElement(nextLinkObj.optString(ATTR_TARGET_ELEMENT));
		}
		if(nextLinkObj.has(ATTR_TARGET_PATH)){
			Path path = new Path();
			try {
				path.fromJson(new JSONObject(nextLinkObj.optString(ATTR_TARGET_PATH)));
				this.nextLinkSelector.setPath(path);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		this.delayBetweenPagesMS = obj.optString(ATTR_DELAY_BETWEEN_PAGES_MS);
		this.maxNumberOfResult = obj.optString(ATTR_MAX_NUMBER_OF_RESULT);
		JSONArray fieldsJsonArr= obj.optJSONArray(ATTR_CONFIG_FIELDS);
		this.fields.clear();
		for (int i = 0; fieldsJsonArr!=null && i < fieldsJsonArr.length(); i++) {
			OutputField o = new OutputField();
			try {
				o.fromJson(fieldsJsonArr.optJSONObject(i));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			this.fields.add(o);
		}
		
	}
	
	//TODO
	public StructuredDataTaskModel clone() {
		StructuredDataTaskModel cloned = (StructuredDataTaskModel) super.clone();
		cloned.nextLinkSelector = new TaskTarget();
		cloned.nextLinkSelector.setElement(nextLinkSelector.getElement());
		cloned.nextLinkSelector.setPath(nextLinkSelector.getPath());
		cloned.nextLinkSelector.setTimeout(nextLinkSelector.getTimeout());
		cloned.fields = new ArrayList<OutputField>();
		cloned.fields.addAll(fields);
		return cloned;
	}

	@Override
	public String getId() {
		return StructuredDataTaskFactory.ID;
	}
	
	public String getOutputParamName() {
		return outputParamName;
	}
	public void setOutputParamName(String outputParamName) {
		this.outputParamName = outputParamName;
	}

	public List<OutputField> getFields() {
		return fields;
	}

	public String getMaxNumberOfResult() {
		return maxNumberOfResult;
	}

	public void setMaxNumberOfResult(String maxNumberOfResult) {
		this.maxNumberOfResult = maxNumberOfResult;
	}

	public String getDelayBetweenPagesMS() {
		return delayBetweenPagesMS;
	}

	public void setDelayBetweenPagesMS(String delayBetweenPagesMS) {
		this.delayBetweenPagesMS = delayBetweenPagesMS;
	}


	public String getExtractMetaData() {
		return extractMetaData;
	}

	public void setExtractMetaData(String extractMetaData) {
		this.extractMetaData = extractMetaData;
	}
	@Override
	public boolean validate(IProblems problems, IModel parent) {
		boolean result =  super.validate(problems, parent);
		if (!(parent instanceof ControlFlowModel)) {
			Problem problem = new Problem();
			problem.setLevel(Level.ERROR);
			problem.setParent(parent);
			problem.setSource(this);
			problem.setDescription("无法验证适配器的正确性");
			problems.add(problem);
			return false;
		}
		if(StringUtils.isEmpty(extractMetaData)) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置提取元数据");
			problems.add(p);
			result &= false;
		} 
		if(delayBetweenPagesMS.equals("0")) {
			Problem p = new Problem();
			p.setLevel(Level.ERROR);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置翻页间隔");
			problems.add(p);
			result &= false;
		} 
		if(nextLinkSelector.getPath()==null && StringUtils.isEmpty(nextLinkSelector.getElement())) {
			Problem p = new Problem();
			p.setLevel(Level.WARNNING);
			p.setParent(parent);
			p.setSource(this);
			p.setDescription("没有设置下一页选择器");
			problems.add(p);
			result &= true;
		} 
		return result;
	}

	public TaskTarget getNextLinkSelector() {
		return nextLinkSelector;
	}

	public void setNextLinkSelector(TaskTarget nextLinkSelector) {
		this.nextLinkSelector = nextLinkSelector;
	}
}
