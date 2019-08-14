package com.jiuqi.rpa.action.dialog.file;

import com.jiuqi.rpa.action.IActionOutput;
/**
 * 
 * @author liangxiao01
 */
public class SelectFileOutput implements IActionOutput {
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


}
