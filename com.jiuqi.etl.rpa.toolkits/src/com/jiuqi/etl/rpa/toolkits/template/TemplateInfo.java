package com.jiuqi.etl.rpa.toolkits.template;

import java.util.Properties;

/**
 * 通用模板信息
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class TemplateInfo {
	private String id;
	private String title;
	private String category;
	private String order;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getOrder() {
		return this.order;
	}
	
	public void setOrder(String order) {
		this.order = order;
	}
	
	public Properties save() {
		Properties properties = new Properties();
		properties.put("id", id);
		properties.put("title", title);
		properties.put("category", category);
		properties.put("order", order);
		return properties;
	}
	
	public void load(Properties properties) {
		this.id = properties.getProperty("id");
		this.title = properties.getProperty("title");
		this.category = properties.getProperty("category");
		if(properties.containsKey("order")) {
			this.order = properties.getProperty("order");
		}
	}

}
