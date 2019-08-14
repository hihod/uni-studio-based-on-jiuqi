package com.jiuqi.rpa.lib.find;

import com.jiuqi.rpa.lib.Rect;

/**
 * UIA·½¿ò
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class UIARect extends Rect {
	public boolean isBrowserClient;
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UIARect other = (UIARect) obj;
		if (isBrowserClient != other.isBrowserClient)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + "isBrowserClient=" + isBrowserClient;
	}
}
