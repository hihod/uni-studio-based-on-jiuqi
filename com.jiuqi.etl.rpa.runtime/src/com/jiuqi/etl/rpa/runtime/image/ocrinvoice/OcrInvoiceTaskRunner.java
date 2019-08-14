package com.jiuqi.etl.rpa.runtime.image.ocrinvoice;

import java.io.File;
import com.jiuqi.etl.engine.ETLEngineException;
import com.jiuqi.etl.engine.ITaskRunner;
import com.jiuqi.etl.engine.TaskConditionException;
import com.jiuqi.etl.env.ETLEnvException;
import com.jiuqi.etl.env.Env;

/**
 * 
 * @author lpy
 *
 */
public class OcrInvoiceTaskRunner implements ITaskRunner {

	private OcrInvoiceTaskModel taskModel;

	public OcrInvoiceTaskRunner(OcrInvoiceTaskModel taskModel) {
		this.taskModel = taskModel;
	}

	@Override
	public void run(Env env) throws TaskConditionException, ETLEngineException {

		String app_key = taskModel.getAppKey();
		String timestamp = System.currentTimeMillis() / 1000 + "";
		String token = MD5Util.encrypt(app_key + "+" + timestamp + "+" + taskModel.getAppSecret());
		String image_url = taskModel.getImageUrl();
		String image_file = taskModel.getImageFilePath();
		String image_data = taskModel.getImageBase64Data();

		HttpRequest request = HttpRequest.post("https://fapiao.glority.cn/v1/item/get_item_info");
		request.part("app_key", app_key);
		request.part("timestamp", timestamp);
		request.part("token", token);
		
		if(!image_url.equals(""))
		{
			request.part("image_url", image_url);
		}
		
		else if(!image_file.equals(""))
		{
			request.part("image_file", image_file, new File(image_file));
		}
		
		else if(!image_data.equals(""))
		{
			request.part("image_data", image_data);
		}
		
		
		String response = request.body();
		
		try {
			env.setValue(taskModel.getOcrResult(), response);
		} catch (ETLEnvException e) {
			e.printStackTrace();
		}
	}

}
