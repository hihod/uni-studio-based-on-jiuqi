package com.jiuqi.rpa.widgets.uipicker;

import com.jiuqi.rpa.lib.find.WEBElement;
/**
 * web端一级节点（页签）对象
 * @author liangxiao01
 *
 */
public class WebTreeNode {
	private Long browserId;
	private String title;
	private String handle;
	private WEBElement element;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getBrowserId() {
		return browserId;
	}
	public void setBrowserId(Long browserId) {
		this.browserId = browserId;
	}
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public WEBElement getElement() {
		return element;
	}
	public void setElement(WEBElement element) {
		this.element = element;
	}
}
