package com.jiuqi.etl.rpa.execute;

import com.jiuqi.etl.engine.IFinishListener;
import com.jiuqi.etl.engine.controlflow.ControlFlowExecuteListener;
import com.jiuqi.etl.engine.controlflow.ControlFlowExecutor;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.env.SchemeEnv;
import com.jiuqi.etl.model.ControlFlowModel;

/**
 * 控制流指定监听器
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class RPAControlFlowExecuteListener implements ControlFlowExecuteListener {
	private IWindowMinimizer windowMinimizer;
	
	public void setWindowMinimizer(IWindowMinimizer windowMinimizer) {
		this.windowMinimizer = windowMinimizer;
	}

	public void onFlowStart(Env env, ControlFlowModel model, ControlFlowExecutor executor) {
		//仅在顶层控制流处理
		if (!(env.getParent() instanceof SchemeEnv))
			return;
		
		//整个流程的上下文资源
		env.putResource(new ContextResource(env));
		
		//流程开始：最小化窗体
		if (windowMinimizer != null)
			windowMinimizer.setMinimize(true);
		
		//流程结束：还原窗体
		executor.addFinishListener(new IFinishListener() {
			@Override
			public void flowFinish(Env env) {
				if (windowMinimizer != null)
					windowMinimizer.setMinimize(false);
			}
		});
	}

}
