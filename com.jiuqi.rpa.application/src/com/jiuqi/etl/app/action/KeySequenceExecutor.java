/*
 * 2010-9-16
 *
 * ������
 */
package com.jiuqi.etl.app.action;

import org.eclipse.ui.IWorkbenchWindow;

/**
 * @author ������
 *
 */
public interface KeySequenceExecutor {

	/**
	 * ���ؼ���ö������ַ���
	 * @return
	 */
	String getActivationString();
	
	/**
	 * ִ�ж���
	 * @param window
	 */
	void run(IWorkbenchWindow window);
	
	/**
	 * �ͷŶ�������������Դ
	 */
	void dispose();
}
