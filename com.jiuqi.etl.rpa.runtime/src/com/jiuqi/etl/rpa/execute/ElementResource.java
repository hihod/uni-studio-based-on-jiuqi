package com.jiuqi.etl.rpa.execute;

import com.jiuqi.etl.env.IKeyResource;
import com.jiuqi.rpa.lib.IUIHandler;

/**
 * 元素资源
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class ElementResource implements IKeyResource {
	private String keyName;
	private IUIHandler element;
	private boolean closed;
	
	/**
	 * 构造器
	 * 
	 * @param keyName 资源名称
	 * @param element 元素
	 */
	public ElementResource(String keyName, IUIHandler element) {
		this.keyName = keyName;
		this.element = element;
	}
	
	/**
	 * 获取元素
	 * 
	 * @return 返回元素
	 */
	public IUIHandler getElement() {
		return element;
	}

	public void close() throws Exception {
		if (element == null)
			return;
		
		element.release();
		closed = true;
	}

	public boolean isClosed() throws Exception {
		return closed;
	}

	public String getKeyName() {
		return keyName;
	}

}
