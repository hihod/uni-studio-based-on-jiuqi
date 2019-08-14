package com.jiuqi.etl.app;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.jiuqi.bi.database.DatabaseManager;
import com.jiuqi.bi.database.IDatabase;

public class ApplicationPlugin extends AbstractUIPlugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "com.jiuqi.rpa.app";
	public static final String IMG_LOGIN = "icons/login.gif";
	
	// The shared instance
	private static ApplicationPlugin plugin;
	
	/**
	 * The constructor
	 */
	public ApplicationPlugin()
	{
	}
	
	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static ApplicationPlugin getDefault()
	{
		return plugin;
	}
	
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
//		plugin.getPreferenceStore().setDefault("A", "D");
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_MEMORY_MONITOR, 
				true);
		loadDatabaseExtension();
	}
	
	private void loadDatabaseExtension() {
		final String DATABASE_PLUGIN_ID = "com.jiuqi.bi";
		IExtensionRegistry extReg = Platform.getExtensionRegistry();
		IExtensionPoint databaseExtPoint = extReg.getExtensionPoint(DATABASE_PLUGIN_ID, "database");
		IExtension[] extensions = databaseExtPoint.getExtensions();
		
		for (IExtension ext : extensions) {
			IConfigurationElement[] configElements = ext.getConfigurationElements();
			for(IConfigurationElement element: configElements) {
				try {
					IDatabase database = (IDatabase) element.createExecutableExtension("class");
					DatabaseManager.getInstance().regDatabase(database);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		super.stop(context);
	}
	
	@Override
	protected void initializeImageRegistry(ImageRegistry reg)
	{
		reg.put(IMG_LOGIN, imageDescriptorFromPlugin(IMG_LOGIN));
	}
	
	public static ImageDescriptor imageDescriptorFromPlugin(String imageFilePath)
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, imageFilePath);
	}
}
