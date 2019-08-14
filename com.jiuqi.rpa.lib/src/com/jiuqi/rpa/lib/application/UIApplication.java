package com.jiuqi.rpa.lib.application;

import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * Ӧ��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIApplication implements IUIHandler {
	private long id;

	/**
	 * ������
	 * 
	 * @param id UIӦ�ñ�ʶ
	 */
	public UIApplication(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

	public void release() {
		try {
			JQUIA.releaseById(id);
		} catch (JQUIAException e) {
			e.printStackTrace();
		}
	}

}
