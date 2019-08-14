package com.jiuqi.rpa.lib;

/**
 * 上下文提供器：由各操作库可选支持
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class ContextProvider {
	private Context context;

	/**
	 * 构造器
	 * 
	 * @param context 活动上下文
	 */
	public ContextProvider(Context context) {
		this.context = context;
	}
	
	/**
	 * 获取活动上下文
	 * 
	 * @return 返回活动上下文
	 */
	protected Context getContext() {
		return context;
	}
	
}
