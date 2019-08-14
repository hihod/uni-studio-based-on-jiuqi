package com.jiuqi.rpa.lib.dialog;

import com.jiuqi.rpa.lib.LibraryException;

public interface IInputDialogProvider {

	/**
	 * ��¼��Ի���
	 * 
	 * @param title ����
	 * @param label ��ǩ
	 * @param items ѡ���
	 * @param isPassword �Ƿ�����
	 * @return ����¼���ֵ
	 * @throws LibraryException
	 */
	public String open(String title, String label, String[] items, boolean isPassword);
	
}
