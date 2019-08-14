package com.jiuqi.etl.rpa.toolkits.welcom.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.jiuqi.etl.rpa.toolkits.welcom.bean.ProcessTemplateBean;


/**
* @author 作者：houzhiyuan 2019年6月18日 下午2:06:54
*/
public class ProcessContentProvider implements ITreeContentProvider{
	
	public Object[] getElements(Object inputElement) {
		@SuppressWarnings("unchecked")
		List<ProcessTemplateBean> beans = (List<ProcessTemplateBean>)inputElement;
		if(beans != null) {
			return beans.toArray();
		}
		return null;
	}
	

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getChildren(Object parentElement) {
		ProcessTemplateBean parent = (ProcessTemplateBean)parentElement;
		return parent.getChildren().toArray();
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		ProcessTemplateBean b = (ProcessTemplateBean)element;
		if(b != null) {
			return b.getChildren().size() > 0;
		}
		else {
			return false;
		}
	}

}
