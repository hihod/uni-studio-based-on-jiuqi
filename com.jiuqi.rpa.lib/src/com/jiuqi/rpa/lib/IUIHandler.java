package com.jiuqi.rpa.lib;

/** 
 * UI���
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IUIHandler {
	
	/**
	 * ��ȡ�����ʶ
	 * @return ���ؾ����ʶ
	 */
	long getId();
	
	/**
	 * �ͷž��
	 */
	void release() throws LibraryException;
}