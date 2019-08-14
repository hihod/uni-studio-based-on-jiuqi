/*
 * 2010-9-16
 *
 * 王星宇
 */
package com.jiuqi.etl.app.action;

import org.eclipse.ui.IWorkbenchWindow;

/**
 * @author 王星宇
 *
 */
public interface KeySequenceExecutor {

	/**
	 * 返回激活该动作的字符串
	 * @return
	 */
	String getActivationString();
	
	/**
	 * 执行动作
	 * @param window
	 */
	void run(IWorkbenchWindow window);
	
	/**
	 * 释放动作所产生的资源
	 */
	void dispose();
}
