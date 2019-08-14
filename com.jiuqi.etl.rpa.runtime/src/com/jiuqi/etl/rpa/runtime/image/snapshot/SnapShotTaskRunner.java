package com.jiuqi.etl.rpa.runtime.image.snapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jiuqi.bi.util.StringUtils;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.ETLEnvException;
import com.jiuqi.etl.env.Env;
import com.jiuqi.etl.model.ParameterModel;
import com.jiuqi.etl.rpa.execute.RPAEnvHelper;
import com.jiuqi.rpa.action.ActionException;
import com.jiuqi.rpa.action.Delay;
import com.jiuqi.rpa.action.Target;
import com.jiuqi.rpa.action.image.snapshot.SnapShotAction;
import com.jiuqi.rpa.action.image.snapshot.SnapShotInput;
import com.jiuqi.rpa.action.image.snapshot.SnapShotOutput;
import com.jiuqi.rpa.lib.Context;
import com.jiuqi.rpa.lib.IUIHandler;

public final class SnapShotTaskRunner implements ITaskRunner {
	private SnapShotTaskModel taskModel;
	private Env env;

	public SnapShotTaskRunner(SnapShotTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	public void run(Env env) throws TaskConditionException, ETLEngineException {
		this.env = env;
		SnapShotInput actionInput = toActionInput(taskModel);
		SnapShotAction action = new SnapShotAction(actionInput);
		try {
			Context context = new RPAEnvHelper().getContext(env);
			SnapShotOutput output = (SnapShotOutput) action.run(context);
			writeLocalFile(env.parseExpr(taskModel.getSavePath()),output.getImage());
			if(StringUtils.isNotEmpty(taskModel.getOutputImageData())){
				env.setValue(taskModel.getOutputImageData(), output.getImage());				
			}
		} catch (ActionException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		} catch (ETLEnvException e) {
			e.printStackTrace();
			throw new ETLEngineException(e.getMessage(), e);
		}
	}

	private void writeLocalFile(String filePath, byte[] bs) {
		filePath = filePath.replaceAll("\\\\", "/");
		FileOutputStream foStream = null;
	
		try {
			File f = new File(filePath);
			if(f.isDirectory()&&false == f.exists()){
				f.mkdir();
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String dateTime = df.format(new Date());
			File image = new File(filePath.indexOf('.')>=0?filePath:(filePath+"/snapshot_"+dateTime+".bmp"));
			if(false == image.exists()){
				image.createNewFile();
			}
			foStream = new FileOutputStream(image);
			foStream.write(bs);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				foStream.flush();
				foStream.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private SnapShotInput toActionInput(SnapShotTaskModel model) throws ETLEngineException {
		SnapShotInput input = new SnapShotInput();

		Target target = new Target();
		String elementName = taskModel.getTaskTarget().getElement();
		if (StringUtils.isNotEmpty(elementName)) {
			IUIHandler element = new RPAEnvHelper().getElement(env, elementName);
			target.setElement(element);
		}
		target.setPath(taskModel.getTaskTarget().getPath());
		target.setTimeout(Integer.valueOf(taskModel.getTaskTarget().getTimeout()));
		input.setTarget(target);
		Delay delay = new Delay();
		try{
			delay.setBefore(Integer.parseInt(taskModel.getDelay().getBefore()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getDelay().getBefore());
			if(paramModel!=null){
				delay.setBefore((int) env.getValue(taskModel.getDelay().getBefore()));
			}
		}
		try{
			delay.setAfter(Integer.parseInt(taskModel.getDelay().getAfter()));
		}catch (Exception e) {
			ParameterModel paramModel = env.getParamModel(taskModel.getDelay().getAfter());
			if(paramModel!=null){
				delay.setAfter((int) env.getValue(taskModel.getDelay().getAfter()));
			}
		}
		input.setDelay(delay);
		input.setSavePath(model.getSavePath());
		return input;
	}
}
