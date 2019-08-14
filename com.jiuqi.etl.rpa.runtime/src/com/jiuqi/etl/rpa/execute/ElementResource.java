package com.jiuqi.etl.rpa.execute;

import com.jiuqi.etl.env.IKeyResource;
import com.jiuqi.rpa.lib.IUIHandler;

/**
 * Ԫ����Դ
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class ElementResource implements IKeyResource {
	private String keyName;
	private IUIHandler element;
	private boolean closed;
	
	/**
	 * ������
	 * 
	 * @param keyName ��Դ����
	 * @param element Ԫ��
	 */
	public ElementResource(String keyName, IUIHandler element) {
		this.keyName = keyName;
		this.element = element;
	}
	
	/**
	 * ��ȡԪ��
	 * 
	 * @return ����Ԫ��
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
