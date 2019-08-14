package com.jiuqi.rpa.action.image.snapshot;

import com.jiuqi.rpa.action.IActionOutput;

public class SnapShotOutput implements IActionOutput {

	private byte[] image;

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
}
