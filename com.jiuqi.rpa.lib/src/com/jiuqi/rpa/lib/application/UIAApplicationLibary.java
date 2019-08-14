package com.jiuqi.rpa.lib.application;

import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.ContextProvider;
import com.jiuqi.rpa.lib.LibraryException;

/**
 * UIAӦ�ò�����
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAApplicationLibary extends ContextProvider {
	/**
	 * ������
	 * 
	 * @param context �������
	 */
	public UIAApplicationLibary(Context context) {
		super(context);
	}

	/**
	 * ��ʼ����
	 * 
	 * @param applicationPath Ӧ�õĳ���·��
	 * @param args ����Ӧ��ʱ��Ҫ���ݵĲ��������ַ���
	 * @return ���ؽ���Id
	 * @throws LibraryException
	 */
	public long startProcess(String applicationPath, String args) throws LibraryException {
		try {
			return JQUIA.application_startProcess(applicationPath, args);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * ���ݽ���Id��ȡ����Ԫ��
	 * 
	 * @param pId ����Id
	 * @return ����Ӧ��������Ԫ��
	 * @throws LibraryException
	 */
	public long getApplicationWindow(long pId) throws LibraryException {
		try {
			return JQUIA.application_getApplicationWindow(pId);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * ���ҽ���
	 * 
	 * @param processName ������
	 * @return ���ؽ��̱�ʶ����
	 * @throws LibraryException
	 */
	public long[] findProcess(String processName) throws LibraryException {
		try {
			return JQUIA.application_findProcess(processName);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	public void closeApplication(Long id) throws LibraryException {
		try {
			JQUIA.application_closeApplication(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}

	public void killProcess(Long id) throws LibraryException {
		try {
			JQUIA.application_killProcess(id);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
}
