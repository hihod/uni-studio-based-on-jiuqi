package com.jiuqi.etl.rpa.execute;

import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

/**
 * ENV访问辅助类
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public final class RPAEnvHelper {

	/**
	 * 获取上下文对象
	 * 
	 * @param env ETL环境对象
	 * @return 返回上下文对象
	 */
	public Context getContext(Env env) {
		ContextResource resource = (ContextResource) env.getKeyResource(ContextResource.ID);
		if (resource == null)
			return null;
		
		return resource.getContext();
	}
	
	/**
	 * 获取元素
	 * 
	 * @param env ETL环境对象
	 * @param keyName 元素资源名称
	 * @return 返回元素
	 * @throws Exception 
	 */
	public IUIHandler getElement(Env env, String keyName) throws ETLEngineException {
		ElementResource resource = (ElementResource) env.getKeyResource(keyName);
		if (resource == null)
			throw new ETLEngineException("未找到可用的对象变量[" + keyName + "]");
		
		return resource.getElement();
	}
	
	/**
	 * 存放元素
	 * 
	 * @param env ETL环境对象
	 * @param keyName 元素资源名称
	 * @param element 要存放的元素
	 */
	public void putElement(Env env, String keyName, IUIHandler element) {
		Env pEnv = env.getParent();
		if (pEnv == null)
			return;
		
		ElementResource resource = new ElementResource(keyName, element);
		pEnv.putResource(resource);
	}
	
}
