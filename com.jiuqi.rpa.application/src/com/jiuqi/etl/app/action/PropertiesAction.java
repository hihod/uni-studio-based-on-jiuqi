/*
 * ��������������޹�˾ ��Ȩ����(c) 2009
 * �ļ������ࣺPropertiesAction
 * ���ߣ�	������ 	����о�Ժ�����
 * �޸ļ�¼��
 * 2009-5-8 	������ 	�����ļ�
 */
package com.jiuqi.etl.app.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.jiuqi.etl.app.ApplicationPlugin;

/**
 * ��������ʾ��������
 */
public class PropertiesAction extends Action implements IWorkbenchAction
{
	private static final String IMG	= "icons/action/property.gif";
	
	private IWorkbenchAction propertyAction;
	
	public PropertiesAction(IWorkbenchWindow window)
    {
		propertyAction = ActionFactory.PROPERTIES.create(window);
		propertyAction.setImageDescriptor(ApplicationPlugin.imageDescriptorFromPlugin(IMG));
		propertyAction.addPropertyChangeListener(new IPropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent event)
            {
				if(event.getProperty().equals(IAction.ENABLED))
				{
					setEnabled((Boolean)event.getNewValue());
				}
            }
		});
    }
	
	@Override
	public String getId()
	{
	    return propertyAction.getId();
	}
	
	@Override
	public int getStyle()
	{
	    return propertyAction.getStyle();
	}
	
	@Override
	public String getText()
	{
	    return propertyAction.getText();
	}
	
	@Override
	public String getToolTipText()
	{
	    return propertyAction.getToolTipText();
	}
	
	@Override
	public int getAccelerator()
	{
	    return propertyAction.getAccelerator();
	}
	
	@Override
	public ImageDescriptor getImageDescriptor()
	{
	    return propertyAction.getImageDescriptor();
	}
	
	@Override
	public ImageDescriptor getDisabledImageDescriptor()
	{
	    return propertyAction.getDisabledImageDescriptor();
	}
	
	@Override
	public String getActionDefinitionId()
	{
	    return propertyAction.getActionDefinitionId();
	}
	
	@Override
	public String getDescription()
	{
	    return propertyAction.getDescription();
	}
	
	@Override
	public IMenuCreator getMenuCreator()
	{
	    return propertyAction.getMenuCreator();
	}
	
	@Override
	public void run()
	{
		propertyAction.run();
	}
	
	public void dispose()
    {
		propertyAction.dispose();
    }
}
