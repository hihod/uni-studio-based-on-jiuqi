package com.jiuqi.etl.rpa.ui.keyboard.typesecuretext;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class SecureTextLabelProvider implements ILabelProvider {

	public void addListener(ILabelProviderListener listener) {}
	public void dispose() {}
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
	public void removeListener(ILabelProviderListener listener) {}
	public Image getImage(Object element) {return null;}

	public String getText(Object element) {
		if("".equals((String)element)){
			return "";
		}else{
			return "¡ñ¡ñ¡ñ¡ñ¡ñ¡ñ";			
		}
	}

}
