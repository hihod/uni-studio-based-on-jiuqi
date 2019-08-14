package com.jiuqi.rpa.script;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.openqa.selenium.remote.RemoteWebDriver;
/**
 * web脚本初始化工具
 * @author wangshanyu
 *
 */
public class ScriptInitor {
	
	public static void initScript(RemoteWebDriver driver,String jsFileName) {
//		jsFileName="GetHtmlFromPoint.js";
		InputStream is = ScriptInitor.class.getResourceAsStream(jsFileName);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String funStr = "";

		int n = 0;
		byte[] bytes = new byte[1000];
		try {
			while ((n = is.read(bytes)) != -1) {
				os.write(bytes, 0, n);
			}
			funStr = os.toString("utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		driver.executeScript(funStr);
	}
}
