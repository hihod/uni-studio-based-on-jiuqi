package com.jiuqi.etl.rpa.runtime.extract.structureddata;
import org.jdom.Element;
import org.json.JSONException;
import org.json.JSONObject;

import com.jiuqi.etl.DataType;
import com.jiuqi.etl.model.DataTableField;
import com.jiuqi.etl.model.ETLModelException;

/**
 * �������������������ֶζ�����
 * 
 * @author liangxiao01
 */

public class OutputField implements Cloneable {
	
	private String name;
	private String title;
	private DataType type;
	private String mapping;
	
	/**
	 * ��ȡ�ֶ�����
	 * @return �����ֶ�����
	 */
	public String getName() {
		return name;
	}
	/**
	 * �����ֶ�����
	 * @param �ֶ�����
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * ��ȡ�ֶα���
	 * @return �����ֶα���
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * �����ֶα���
	 * @param �ֶα���
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * ��ȡ�ֶ�����
	 * @return �����ֶ�����
	 */
	public DataType getType() {
		return type;
	}
	/**
	 * �����ֶ�����
	 * @param �ֶ�����
	 */
	public void setType(DataType type) {
		this.type = type;
	}

	/**
	 * ��ȡӳ���ֶ�
	 * @return ����ӳ���ֶ�
	 */
	public String getMapping() {
		return mapping;
	}
	/**
	 * ����ӳ���ֶ�
	 * @param ӳ���ֶ�
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
		if (this == obj)	// ͬһ�����󷵻�true
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
	 * �����ֶζ���
	 * @param e ����ڵ�
	 * @throws ETLModelException ����ʧ���׳�ģ���쳣
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
	 * �����ֶζ���
	 * @param e ���ؽڵ�
	 * @throws ETLModelException ����ʧ���׳�ģ���쳣
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
