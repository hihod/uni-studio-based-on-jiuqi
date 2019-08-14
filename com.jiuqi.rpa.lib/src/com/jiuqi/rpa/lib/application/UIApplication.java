package com.jiuqi.rpa.lib.application;

import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * 应用
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIApplication implements IUIHandler {
	private long id;

	/**
	 * 构造器
	 * 
	 * @param id UI应用标识
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
