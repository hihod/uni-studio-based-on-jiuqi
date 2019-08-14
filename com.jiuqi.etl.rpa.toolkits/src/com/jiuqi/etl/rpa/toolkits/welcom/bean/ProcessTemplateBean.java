package com.jiuqi.etl.rpa.toolkits.welcom.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
* @author 作者：houzhiyuan 2019年6月18日 下午2:27:41
*/
public class ProcessTemplateBean implements Comparable<ProcessTemplateBean>{
	private String id;
	private String title;
	private String pId;
	private List<ProcessTemplateBean> children = new ArrayList<ProcessTemplateBean>();
	private long order;
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
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public void setChildren(List<ProcessTemplateBean> children) {
		this.children = children;
	}
	public List<ProcessTemplateBean> getChildren() {
		return this.children;
	}
	public long getOrder() {
		return this.order;
	}
	public void setOrder(long order) {
		this.order = order;
	}
	
	public boolean isParent() {
		return this.children.size() > 0;
	}
	public int compareTo(ProcessTemplateBean bean) {
		//return this.title.compareTo(bean.getTitle());
		return this.order > bean.getOrder()?-1:1;
	}
	public boolean equals(Object obj) {
		if(obj instanceof ProcessTemplateBean) {
			ProcessTemplateBean tar = (ProcessTemplateBean)obj;
			return getId().equals(tar.getId());
		}
		return false;
	}
}
