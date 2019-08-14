package com.jiuqi.rpa.lib.find;

import java.util.List;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;

/**
 * ���Ҳ�����
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public interface IFindLibary {
	/**
	 * ����ָ��λ�û�ȡUIԪ��
	 * 
	 * @param point ָ����λ�� 
	 * @return ����UIԪ�ض���
	 * @throws LibraryException
	 */
	IUIElement get(Point point) throws LibraryException;
	
	/**
	 * ����ָ��λ�û�ȡ��ӦԪ�صķ���
	 * 
	 * @param point ָ����λ��
	 * @return ����UIԪ�صķ���
	 * @throws LibraryException
	 */
	Rect getRect(Point point) throws LibraryException;
	
	/**
	 * �ж�ָ��·����UIԪ���Ƿ����
	 * 
	 * @param path ·��
	 * @return ���ڷ���true�����򷵻�false
	 * @throws LibraryException
	 */
	boolean exists(Path path) throws LibraryException;
	
	/**
	 * ��ȡָ��·����Ԫ�أ�������ڶ�������ص�һ��
	 * 
	 * @param path ·��
	 * @return ����UIԪ�ض���
	 * @throws LibraryException
	 */
	IUIElement findFirst(Path path) throws LibraryException;
	
	/**
	 * ��ȡָ��·����Ԫ�أ�������ڶ�������ص�һ��
	 * 
	 * @param path ·��
	 * @param runtimeIdList ����ʱ��ʶ�б�
	 * @return ����UIԪ�ض���
	 * @throws LibraryException
	 */
	IUIElement findFirst(Path path, List<String> runtimeIdList) throws LibraryException;
	
	/**
	 * ��ȡָ��·����Ԫ�ؼ���
	 * 
	 * @param path ·��
	 * @return ����UIԪ�ض��󼯺�
	 * @throws LibraryException
	 */
	IUIElement[] findAll(Path path) throws LibraryException;
	
}
