package com.jiuqi.rpa.lib;

/**
 * ·½¿ò
 * 
 * @author zhouxubo.jiuqi.com.cn
 */
public class Rect {
	public int x;
	public int y;
	public int w;
	public int h;
	
	public Rect() {
	}
	
	public Rect(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Rect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	@Override
	public boolean equals(Object obj) {
		Rect tar = (Rect) obj;
		return (tar.x == this.x && tar.y == this.y && tar.w == this.w && tar.h == this.h);
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + w + ", " + h + ")";
	}
}
