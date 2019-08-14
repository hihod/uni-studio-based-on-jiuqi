package com.jiuqi.rpa.action;

import com.jiuqi.rpa.lib.Point;
import com.jiuqi.rpa.lib.Rect;

/**
 * ���λ��
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class CursorPosition {
	private int offsetX = 0;
	private int offsetY = 0;
	private Position position = Position.CENTER;

	/**
	 * ��ȡX��ƫ��
	 * 
	 * @return ����X��ƫ��
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * ����X��ƫ��
	 * 
	 * @param offsetX X��ƫ��
	 */
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	/**
	 * ��ȡY��ƫ��
	 * 
	 * @return ����Y��ƫ��
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * ����Y��ƫ��
	 * 
	 * @param offsetY Y��ƫ��
	 */
	public void setOffsetY(int offsetY) {
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
			p.x = rect.x + rect.w / 2 + posotion.getOffsetX();
			p.y = rect.y + rect.h / 2 + posotion.getOffsetY();
			break;
		case TOPLEFT:
			p.x = rect.x + posotion.getOffsetX();
			p.y = rect.y + posotion.getOffsetY();
			break;
		case TOPRIGHT:
			p.x = rect.x + rect.w + posotion.getOffsetX();
			p.y = rect.y + posotion.getOffsetY();
			break;
		case BOTTOMLEFT:
			p.x = rect.x + posotion.getOffsetX();
			p.y = rect.y + rect.h + posotion.getOffsetY();
			break;
		case BOTTOMRIGHT:
			p.x = rect.x + rect.w + posotion.getOffsetX();
			p.y = rect.y + rect.h + posotion.getOffsetY();
			break;
		}
		return p;
	}
}
