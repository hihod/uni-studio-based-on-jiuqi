package com.jiuqi.etl.rpa.execute;

import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.env.IKeyResource;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IInterruptable;

/**
 * �����Ķ�����Դ
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
class ContextResource implements IKeyResource {
	public static final String ID = "com.jiuqi.etl.rpa.execute.ContextResource";

	private Context context;
	private boolean closed;

	public ContextResource(final Env env) {
		context = new Context(new IInterruptable() {
			public boolean isInterrupted() {
				return env.isInterrupted();
			}
		});
	}
	
	/**
	 * ��ȡ�����Ķ���
	 * 
	 * @return ���������Ķ���
	 */
	public Context getContext() {
		return context;
	}

	public String getKeyName() {
		return ID;
	}

	public void close() throws Exception {
		context.close();
		closed = true;
	}

	public boolean isClosed() throws Exception {
		return closed;
	}

	@Override
	public String toString() {
		return "RPA��������Դ";
	}
	
}
