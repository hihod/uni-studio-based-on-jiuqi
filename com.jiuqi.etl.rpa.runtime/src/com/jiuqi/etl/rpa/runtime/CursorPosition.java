package com.jiuqi.etl.rpa.runtime;

import com.jiuqi.rpa.action.Position;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;

/**
 * 光标位置
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class CursorPosition {
	private String offsetX = "0";
	private String offsetY = "0";
	private Position position = Position.CENTER;

	/**
	 * 获取X轴偏移
	 * 
	 * @return 返回X轴偏移
	 */
	public String getOffsetX() {
		return offsetX;
	}

	/**
	 * 设置X轴偏移
	 * 
	 * @param offsetX X轴偏移
	 */
	public void setOffsetX(String offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * 获取Y轴偏移
	 * 
	 * @return 返回Y轴偏移
	 */
	public String getOffsetY() {
		return offsetY;
	}

	/**
	 * 设置Y轴偏移
	 * 
	 * @param offsetY Y轴偏移
	 */
	public void setOffsetY(String offsetY) {
		this.offsetY = offsetY;
	}

	/**
	 * 获取方位
	 * 
	 * @return 返回方位
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * 设置方位
	 * 
	 * @param position 方位
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	public static Point directPoint(Rect rect, CursorPosition posotion) {
		Point p = new Point();
		switch (posotion.getPosition()) {
		case CENTER:
			p.x = rect.x + rect.w / 2 + Integer.parseInt(posotion.getOffsetX());
			p.y = rect.y + rect.h / 2 + Integer.parseInt(posotion.getOffsetY());
			break;
		case TOPLEFT:
			p.x = rect.x + Integer.parseInt(posotion.getOffsetX());
			p.y = rect.y + Integer.parseInt(posotion.getOffsetY());
			break;
		case TOPRIGHT:
			p.x = rect.x + rect.w + Integer.parseInt(posotion.getOffsetX());
			p.y = rect.y + Integer.parseInt(posotion.getOffsetY());
			break;
		case BOTTOMLEFT:
			p.x = rect.x + Integer.parseInt(posotion.getOffsetX());
			p.y = rect.y + rect.h + Integer.parseInt(posotion.getOffsetY());
			break;
		case BOTTOMRIGHT:
			p.x = rect.x + rect.w + Integer.parseInt(posotion.getOffsetX());
			p.y = rect.y + rect.h + Integer.parseInt(posotion.getOffsetY());
			break;
		}
		return p;
	}
}
