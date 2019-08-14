package com.jiuqi.rpa.lib.tree;

import java.util.Properties;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.find.IUIElement;

/**
 * ���β�����
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface ITreeWalker {

	/**
	 * ��ȡ��UIԪ��
	 * 
	 * @return ���ظ�UIԪ��
	 * @throws LibraryException
	 */
	IUIElement getRoot() throws LibraryException;
	
	/**
	 * ��ȡ��UIԪ�ؼ���
	 * 
	 * @param parent ��UIԪ��
	 * @return ������UIԪ�ؼ��� 
	 * @throws LibraryException
	 */
	IUIElement[] getChildren(IUIElement parent) throws LibraryException;
	
	
	/**
	 * ��ȡ��UIԪ��
	 * 
	 * @param child ��UIԪ��
	 * @return ���ظ�UIԪ��
	 * @throws LibraryException
	 */
	IUIElement getParent(IUIElement child) throws LibraryException;
	
	/**
	 * ��ȡָ��UIԪ�صĽڵ��ı�
	 * 
	 * @param uiElement UIԪ��
	 * @return ����UIԪ�صĽڵ��ı�
	 * @throws LibraryException
	 */
	String getText(IUIElement uiElement) throws LibraryException;
	
	/**
	 * ��ȡָ��UIԪ�ص������б�
	 * 
	 * @param uiElement UIԪ��
	 * @return ����UIԪ�ص������б�
	 * @throws LibraryException
	 */
	Properties getProperties(IUIElement uiElement) throws LibraryException;
}
