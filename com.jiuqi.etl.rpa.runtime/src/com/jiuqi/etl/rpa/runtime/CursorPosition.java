package com.jiuqi.etl.rpa.runtime;

import com.jiuqi.rpa.action.Position;
import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;

/**
 * ���λ��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class CursorPosition {
	private String offsetX = "0";
	private String offsetY = "0";
	private Position position = Position.CENTER;

	/**
	 * ��ȡX��ƫ��
	 * 
	 * @return ����X��ƫ��
	 */
	public String getOffsetX() {
		return offsetX;
	}

	/**
	 * ����X��ƫ��
	 * 
	 * @param offsetX X��ƫ��
	 */
	public void setOffsetX(String offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * ��ȡY��ƫ��
	 * 
	 * @return ����Y��ƫ��
	 */
	public String getOffsetY() {
		return offsetY;
	}

	/**
	 * ����Y��ƫ��
	 * 
	 * @param offsetY Y��ƫ��
	 */
	public void setOffsetY(String offsetY) {
		this.offsetY = offsetY;
	}

	/**
	 * ��ȡ��λ
	 * 
	 * @return ���ط�λ
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * ���÷�λ
	 * 
	 * @param position ��λ
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
