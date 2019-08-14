package com.jiuqi.rpa.action;

import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.find.Path;

/**
 * 操作目标
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Target {
	private Path path;
	private IUIHandler element;
	private int timeout = 30000;

	/**
	 * 获取元素
	 * 
	 * @return 返回元素
	 */
	public IUIHandler getElement() {
		return element;
	}
	
	/**
	 * 设置元素标识
	 * 
	 * @param element 元素
	 */
	public void setElement(IUIHandler element) {
		this.element = element;
	}

	/**
	 * 获取路径
	 * 
	 * @return 返回路径
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * 设置路径
	 * 
	 * @param path 路径
	 */
	public void setPath(Path path) {
		this.path = path;
	}

	/**
	 * 获取超时时间
	 * 
	 * @return 返回超时时间
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * 设置超时时间
	 * 
	 * @param timeout 超时时间
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
