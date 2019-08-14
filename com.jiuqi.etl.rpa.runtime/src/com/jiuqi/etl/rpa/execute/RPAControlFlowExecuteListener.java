package com.jiuqi.etl.rpa.execute;

import com.jiuqi.etl.engine.IFinishListener;
import com.jiuqi.etl.engine.controlflow.ControlFlowExecuteListener;
import com.jiuqi.etl.engine.controlflow.ControlFlowExecutor;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.env.SchemeEnv;
import com.jiuqi.etl.model.ControlFlowModel;

/**
 * ������ָ��������
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class RPAControlFlowExecuteListener implements ControlFlowExecuteListener {
	private IWindowMinimizer windowMinimizer;
	
	public void setWindowMinimizer(IWindowMinimizer windowMinimizer) {
		this.windowMinimizer = windowMinimizer;
	}

	public void onFlowStart(Env env, ControlFlowModel model, ControlFlowExecutor executor) {
		//���ڶ������������
		if (!(env.getParent() instanceof SchemeEnv))
			return;
		
		//�������̵���������Դ
		env.putResource(new ContextResource(env));
		
		//���̿�ʼ����С������
		if (windowMinimizer != null)
			windowMinimizer.setMinimize(true);
		
		//���̽�������ԭ����
		executor.addFinishListener(new IFinishListener() {
			@Override
			public void flowFinish(Env env) {
				if (windowMinimizer != null)
					windowMinimizer.setMinimize(false);
			}
		});
	}

}
