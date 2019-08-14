package com.jiuqi.etl.rpa.runtime.extract.structureddata;
import org.jdom.Element;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.etl.model.ETLModelException;

/**
 * 参数输出适配器的输出字段定义类
 * 
 * @author liangxiao01
 */

public class OutputField implements Cloneable {
	
	private String name;
	private String title;
	private DataType type;
	private String mapping;
	
	/**
	 * 获取字段名称
	 * @return 返回字段名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置字段名称
	 * @param 字段名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取字段标题
	 * @return 返回字段标题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置字段标题
	 * @param 字段标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取字段类型
	 * @return 返回字段类型
	 */
	public DataType getType() {
		return type;
	}
	/**
	 * 设置字段类型
	 * @param 字段类型
	 */
	public void setType(DataType type) {
		this.type = type;
	}

	/**
	 * 获取映射字段
	 * @return 返回映射字段
	 */
	public String getMapping() {
		return mapping;
	}
	/**
	 * 设置映射字段
	 * @param 映射字段
	 */
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)	// 同一个对象返回true
			return true;
		if (obj == null || !(obj instanceof OutputField))
			return false;
		if (this.name == null)
			return false;
		return this.name.equals(((OutputField)obj).getName());
	}
	
	@Override
	public int hashCode() {
		if (this.name == null)
			return 0;
		return this.name.hashCode();
	}

	
	private static final String ATTR_NAME = "name";
	private static final String ATTR_TITLE = "title";
	private static final String ATTR_TYPE = "type";
	private static final String ATTR_MAPPING = "mapping";

	/**
	 * 保存字段对象
	 * @param e 保存节点
	 * @throws ETLModelException 保存失败抛出模型异常
	 */
	public void save(Element e) throws ETLModelException {
		if(name != null)
			e.setAttribute(ATTR_NAME, name);
		if(title != null)
			e.setAttribute(ATTR_TITLE, title);
		if(type != null)
			e.setAttribute(ATTR_TYPE, String.valueOf(type.value()));

		if(mapping != null)
			e.setAttribute(ATTR_MAPPING, mapping);
	}
	
	
	/**
	 * 加载字段对象
	 * @param e 加载节点
	 * @throws ETLModelException 加载失败抛出模型异常
	 */
	public void load(Element e) throws ETLModelException {
		this.name = e.getAttributeValue(ATTR_NAME);
		this.title = e.getAttributeValue(ATTR_TITLE);
		String type = e.getAttributeValue(ATTR_TYPE);
		this.type = type != null ? 
				DataType.valueOf(Integer.parseInt(type)) : DataType.UNKNOWN;
		this.mapping = e.getAttributeValue(ATTR_MAPPING);	
	
	}
	
	@Override
	public String toString() {
		StringBuilder	buffer = new StringBuilder();
		
		buffer.append(name).append('=').append(mapping);
		
		return buffer.toString();		
	}
	public JSONObject toJson() throws JSONException {
		JSONObject returnObj = new JSONObject();
		
		returnObj.putOpt(ATTR_NAME, name);
		returnObj.putOpt(ATTR_TITLE, title);
		returnObj.putOpt(ATTR_TYPE, String.valueOf(type.value()));
		returnObj.putOpt(ATTR_MAPPING, mapping);

		return returnObj;
	}
	public void fromJson(JSONObject optJSONObject) throws JSONException {
		this.name = optJSONObject.optString(ATTR_NAME);
		this.title = optJSONObject.optString(ATTR_TITLE);
		String type = optJSONObject.optString(ATTR_TYPE);
		this.type = type != null ? 
				DataType.valueOf(Integer.parseInt(type)) : DataType.UNKNOWN;
		this.mapping = optJSONObject.optString(ATTR_MAPPING);	
	
		
	}
	public DataTableField toDataTableField() {
		DataTableField f = new DataTableField();
		f.setName(this.name);
		f.setTitle(this.title);
		f.setType(this.type);
		return f;
	}
}
