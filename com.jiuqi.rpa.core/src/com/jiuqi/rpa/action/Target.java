package com.jiuqi.rpa.action;

import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.find.Path;

/**
 * ����Ŀ��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Target {
	private Path path;
	private IUIHandler element;
	private int timeout = 30000;

	/**
	 * ��ȡԪ��
	 * 
	 * @return ����Ԫ��
	 */
	public IUIHandler getElement() {
		return element;
	}
	
	/**
	 * ����Ԫ�ر�ʶ
	 * 
	 * @param element Ԫ��
	 */
	public void setElement(IUIHandler element) {
		this.element = element;
	}

	/**
	 * ��ȡ·��
	 * 
	 * @return ����·��
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * ����·��
	 * 
	 * @param path ·��
	 */
	public void setPath(Path path) {
		this.path = path;
	}

	/**
	 * ��ȡ��ʱʱ��
	 * 
	 * @return ���س�ʱʱ��
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * ���ó�ʱʱ��
	 * 
	 * @param timeout ��ʱʱ��
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
