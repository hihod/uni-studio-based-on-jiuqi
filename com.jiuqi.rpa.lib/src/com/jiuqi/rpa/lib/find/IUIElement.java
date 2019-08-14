package com.jiuqi.rpa.lib.find;

import com.jiuqi.rpa.lib.IUIHandler;
import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;

/**
 * UIԪ��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IUIElement extends IUIHandler {

	/**
	 * ��ȡԪ�����
	 * 
	 * @return ����UIԪ�����
	 * @throws LibraryException
	 */
	Rect getRect() throws LibraryException;

	/**
	 * ��ȡԪ��·��
	 * 
	 * @return ����Ԫ��·��
	 * @throws LibraryException
	 */
	Path getPath() throws LibraryException;

	/**
	 * �������ɼ�
	 * 
	 * @throws LibraryException
	 */
	void scrollIntoView() throws LibraryException;
	
	/**
	 * ���ý���
	 * 
	 * @throws LibraryException
	 */
	void setFocus() throws LibraryException;

	/**
	 * Ԫ���ǹ�����ѡ
	 * 
	 * @return ����Ԫ���Ƿ񱻹�ѡ
	 * @throws LibraryException
	 */
	boolean isChecked() throws LibraryException;

	/**
	 * ����Ԫ�ع�ѡ
	 * 
	 * @param checked �Ƿ�ѡ
	 * @throws LibraryException
	 */
	void setChecked(boolean checked) throws LibraryException;

	/**
	 * ����Ԫ��ѡ��״̬
	 * 
	 * @see CheckState
	 * @return ����ѡ��״̬
	 * @throws LibraryException
	 */
	int getCheckState() throws LibraryException;

	/**
	 * ��ȡԪ���ı�
	 * 
	 * @return ����Ԫ���ı�
	 * @throws LibraryException
	 */
	String getText() throws LibraryException;

	/**
	 * ����Ԫ���ı�
	 * 
	 * @param text Ҫ���õ�Ԫ�ص��ı�
	 * @throws LibraryException
	 */
	void setText(String text) throws LibraryException;

	/**
	 * Ԫ���Ƿ�Ϊ����ؼ�
	 * 
	 * @return �����Ƿ�Ϊ����ؼ�
	 * @throws LibraryException
	 */
	boolean isPassword() throws LibraryException;

	/**
	 * ��յ�ǰ��ѡ��״̬
	 * 
	 * @throws LibraryException
	 */
	void clearSelection() throws LibraryException;

	/**
	 * ѡ��ָ������Ŀ
	 * 
	 * @param items Ҫѡ�е���Ŀ����
	 * @throws LibraryException
	 */
	void selectItems(String[] items) throws LibraryException;

	/**
	 * ���Ԫ���ı�
	 * 
	 * @throws LibraryException
	 */
	void clearText() throws LibraryException;

	/**
	 * ģ��¼���ı�
	 * 
	 * @param text Ҫ¼����ı�
	 * @throws LibraryException
	 */
	void simulateTypeText(String text) throws LibraryException;

	/**
	 * �����ȼ�
	 * 
	 * @param hotkey �ȼ�
	 * @param maskkeys ��ϼ�
	 * @throws LibraryException
	 */
	void sendHotkey(int hotkey, int[] maskkeys) throws LibraryException;

	/**
	 * ��ȡ����ֵ
	 * 
	 * @param attrName ��������
	 * @return ��������ֵ
	 * @throws LibraryException
	 */
	String getAttributeValue(String attrName) throws LibraryException;
	
	/**
	 * �ж�Ԫ���Ƿ����
	 * 
	 * @return ����Ԫ���Ƿ����
	 * @throws LibraryException
	 */
	boolean enable() throws LibraryException;
	
	/**
	 * �ж�Ԫ���Ƿ�ɼ�
	 * 
	 * @return ����Ԫ���Ƿ�ɼ�
	 * @throws LibraryException
	 */
	boolean visible() throws LibraryException;
	
	/**
	 * �Ƿ��׼Table
	 * 
	 * @return �����Ƿ��׼Table
	 * @throws LibraryException
	 */
	boolean isTable() throws LibraryException;
	
	/**
	 * ��ȡ��ҳ����
	 * 
	 * @param columns ��Ԫ��·������
	 * @return ���ص�ҳ����
	 * @throws LibraryException
	 */
	String getPageData(Path[] columns) throws LibraryException;
}
