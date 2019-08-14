package com.jiuqi.rpa.action.extract.getposition;

import com.jiuqi.rpa.action.IActionOutput;
import com.jiuqi.rpa.lib.Rect;
/**
 * 
 * @author liangxiao01
 */
public class GetPositionOutput implements IActionOutput {
	private Rect rect;

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}


}
