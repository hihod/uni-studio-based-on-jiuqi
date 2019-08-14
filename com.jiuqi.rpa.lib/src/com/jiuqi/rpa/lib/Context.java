package com.jiuqi.rpa.lib;

import java.util.Map;

import java.util.HashMap;

/**
 * �������
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Context {
	private Map<Long, IUIHandler> map = new HashMap<Long, IUIHandler>();
	private IInterruptable interruptable;

	/**
	 * ������
	 */
	public Context() {
	}
	
	/**
	 * ������
	 * 
	 * @param interruptable ���ж϶���
	 */
	public Context(IInterruptable interruptable) {
		this.interruptable = interruptable;
	}
	
	/**
	 * ��UI�����ӵ�������
	 * 
	 * @param uiHandler
	 * @return ������ӵ�UI�������
	 */
	public IUIHandler add(IUIHandler uiHandler) {
		map.put(uiHandler.getId(), uiHandler);
		return uiHandler;
	}
	
	/**
	 * ��ȡָ��id��UI���
	 * 
	 * @param id UI�����ʶ  
	 * @return ����UI���
	 */
	public IUIHandler get(long id) {
		return map.get(id);
	}
	
	/**
	 * �ͷ�ָ����ʶ��UI���
	 * 
	 * @param id UI�����ʶ
	 */
	public void release(long id) {
		IUIHandler uiHandler = get(id);
		if (uiHandler != null) {
			try {
				uiHandler.release();
			} catch (LibraryException e) {
				e.printStackTrace();
			}
			
			map.remove(id);
		}
	}
	
	/**
	 * �ͷ�ָ����ʶ��UI�������
	 * 
	 * @param ids UI�����ʶ����
	 */
	public void release(long[] ids) {
		//ͳһ�ͷ�ID�б�
		for (long id: ids) {
			IUIHandler uiHandler = get(id);
			if (uiHandler == null)
				continue;
			
			try {
				uiHandler.release();
			} catch (LibraryException e) {
			}
		}

		//�Ƴ�ID�б�
		for (long id: ids)
			map.remove(id);
	}
	
	/**
	 * �ر�������
	 */
	public void close() {
		int i = 0;
		long[] ids = new long[map.keySet().size()];
		for (Long id: map.keySet())
			ids[i++] = id;
		
		release(ids);
	}
	
	public boolean isInterrupted() {
		if (interruptable != null)
			return interruptable.isInterrupted();
		
		return false;
	}
	
}
