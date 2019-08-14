package com.jiuqi.etl.rpa.toolkits.welcom.editor;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.jiuqi.etl.rpa.toolkits.welcom.bean.ProcessTemplateBean;


/**
* @author 作者：houzhiyuan 2019年6月18日 下午2:09:12
*/
public class ProcessLabelProvider  implements ILabelProvider {
	public static final String PLUGIN_ID = "com.jiuqi.etl.rpa.toolkits";
	public final static String IMG_CHILD = "icons/welcom/process-item.png";
	public final static String IMG_GROUP = "icons/welcom/process-group.png";
	
	private ImageRegistry imgReg;
	public ProcessLabelProvider() {
		this.imgReg = new ImageRegistry();
		this.imgReg.put(IMG_CHILD, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_CHILD));
		this.imgReg.put(IMG_GROUP, AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, IMG_GROUP));
	}
	
	public void addListener(ILabelProviderListener listener) {
	}
	public void dispose() {
	}
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
	public void removeListener(ILabelProviderListener listener) {
	}
	public Image getImage(Object element) {
		if (element instanceof ProcessTemplateBean) {
			ProcessTemplateBean bean = (ProcessTemplateBean)element;
			if(bean.isParent()) {
				return this.imgReg.get(IMG_GROUP);
			}
			else {
				return this.imgReg.get(IMG_CHILD);
			}
			
		}
		return null;
	}
	public String getText(Object element) {
		if (element instanceof ProcessTemplateBean) {
			ProcessTemplateBean bean = (ProcessTemplateBean)element;
			return bean.getTitle();
		}
		return null;
	}
}
