package com.jiuqi.etl.rpa.execute;

import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

/**
 * ENV���ʸ�����
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public final class RPAEnvHelper {

	/**
	 * ��ȡ�����Ķ���
	 * 
	 * @param env ETL��������
	 * @return ���������Ķ���
	 */
	public Context getContext(Env env) {
		ContextResource resource = (ContextResource) env.getKeyResource(ContextResource.ID);
		if (resource == null)
			return null;
		
		return resource.getContext();
	}
	
	/**
	 * ��ȡԪ��
	 * 
	 * @param env ETL��������
	 * @param keyName Ԫ����Դ����
	 * @return ����Ԫ��
	 * @throws Exception 
	 */
	public IUIHandler getElement(Env env, String keyName) throws ETLEngineException {
		ElementResource resource = (ElementResource) env.getKeyResource(keyName);
		if (resource == null)
			throw new ETLEngineException("δ�ҵ����õĶ������[" + keyName + "]");
		
		return resource.getElement();
	}
	
	/**
	 * ���Ԫ��
	 * 
	 * @param env ETL��������
	 * @param keyName Ԫ����Դ����
	 * @param element Ҫ��ŵ�Ԫ��
	 */
	public void putElement(Env env, String keyName, IUIHandler element) {
		Env pEnv = env.getParent();
		if (pEnv == null)
			return;
		
		ElementResource resource = new ElementResource(keyName, element);
		pEnv.putResource(resource);
	}
	
}
