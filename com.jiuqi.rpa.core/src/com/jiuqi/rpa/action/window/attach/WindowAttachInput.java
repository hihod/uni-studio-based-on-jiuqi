package com.jiuqi.rpa.action.window.attach;

import com.jiuqi.rpa.action.IActionInput;
import com.jiuqi.rpa.lib.find.Path;

/**
 * @author liangxiao01
 */
public class WindowAttachInput implements IActionInput {
	private Path path;
	private int timeout = 30000;

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
