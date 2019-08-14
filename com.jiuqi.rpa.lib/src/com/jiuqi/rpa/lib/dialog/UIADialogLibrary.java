package com.jiuqi.rpa.lib.dialog;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA�Ի��������
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIADialogLibrary {
	private static IInputDialogProvider inputDialogProvider;
	
	public static void setInputDialogProvider(IInputDialogProvider inputDialogProvider) {
		UIADialogLibrary.inputDialogProvider = inputDialogProvider;
	}
	
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
	public String inputDialog(String title, String label, String[] items, boolean isPassword) throws LibraryException {
		if (inputDialogProvider == null)
			throw new LibraryException("δע������Ի����ṩ��");
		
		return inputDialogProvider.open(title, label, items, isPassword);
			
		/*
		try {
			return JQUIA.dialog_inputDialog(title, label, items, isPassword);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
		*/
	}
	
	/**
	 * ����Ϣ�Ի���
	 * 
	 * @param title ����
	 * @param message ��ǩ
	 * @param btnSuite ��ť��
	 * @return
	 * @throws LibraryException
	 */
	public String messageDialog(String title, String message, long btnSuite) throws LibraryException {
		try {
			return JQUIA.dialog_messageDialog(title, message, btnSuite);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * ���ļ��Ի���
	 * 
	 * @param initialDir Ĭ��Ŀ¼
	 * @param filters �ļ��������б��׸�ΪĬ�Ϲ�����
	 * @return �����ļ�·��
	 * @throws LibraryException
	 */
	public String fileDialog(String initialDir, String[] filters) throws LibraryException {
		try {
			StringBuffer buffer = new StringBuffer();
			for (String filter: filters)
				buffer.append(filter);
			return JQUIA.dialog_fileDialog(initialDir, buffer.toString());
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
	/**
	 * Ŀ¼�Ի���
	 * 
	 * @param initialDir Ĭ��Ŀ¼
	 * @return ����Ŀ¼·��
	 * @throws JQUIAException
	 */
	public String directoryDialog(String initialDir) throws LibraryException {
		try {
			return JQUIA.dialog_directoryDialog(initialDir);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
}
