package com.jiuqi.rpa.lib;

/**
 * �������ṩ�����ɸ��������ѡ֧��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class ContextProvider {
	private Context context;

	/**
	 * ������
	 * 
	 * @param context �������
	 */
	public ContextProvider(Context context) {
		this.context = context;
	}
	
	/**
	 * ��ȡ�������
	 * 
	 * @return ���ػ������
	 */
	protected Context getContext() {
		return context;
	}
	
}
