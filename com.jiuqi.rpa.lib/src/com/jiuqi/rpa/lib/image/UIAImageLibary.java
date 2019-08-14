package com.jiuqi.rpa.lib.image;

import com.jiuqi.rpa.lib.LibraryException;
import com.jiuqi.rpa.lib.Rect;
import com.jiuqi.rpa.uiadll.JQUIA;
import com.jiuqi.rpa.uiadll.JQUIAException;

/**
 * UIA图片操作库
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIAImageLibary {
	/**
	 * 执行截屏
	 * 
	 * @param rect 屏幕上的指定区域，null表示截全屏
	 * @return 返回图片数据
	 * @throws LibraryException
	 */
	public byte[] doScreenshot(Rect rect) throws LibraryException {
		try {
			return JQUIA.image_doScreenShot(rect);
		} catch (JQUIAException e) {
			throw new LibraryException(e);
		}
	}
	
}
