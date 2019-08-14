package com.jiuqi.rpa.widgets;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.jiuqi.rpa.widgets";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(IMAGE_ADD,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/add.gif"));
		reg.put(IMAGE_DELETE,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/delete.gif"));
		reg.put(IMAGE_CHECKED,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/checked.gif"));
		reg.put(IMAGE_UNCHECKED,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/unchecked.gif"));
		reg.put(IMAGE_FIELD,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/field.gif"));
		reg.put(IMAGE_HSWITCH,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/HSwitch.png"));
		reg.put(IMAGE_ESWITCH,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/ESwitch.png"));
		reg.put(IMAGE_VSWITCH,
				imageDescriptorFromPlugin(PLUGIN_ID, "icons/VSwitch.png"));
		reg.put(IMAGE_FIELD_TABLE,imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_FIELD_TABLE));
		reg.put(IMAGE_AUTOMAPPING,imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_AUTOMAPPING));
		reg.put(IMAGE_CANCELMAPPING,imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_CANCELMAPPING));
		reg.put(IMAGE_STATUS_WARNING,imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_STATUS_WARNING));
		reg.put(IMAGE_STATUS_ERROR,imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_STATUS_ERROR));
		reg.put(IMAGE_STATUS_SUCCESS,imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_STATUS_SUCCESS));
		reg.put(IMAGE_STATUS_SUCCESS,imageDescriptorFromPlugin(PLUGIN_ID, IMAGE_STATUS_SUCCESS));
	}

	public static final String IMAGE_ADD = "ADD";
	public static final String IMAGE_DELETE = "DELETE";
	public static final String IMAGE_CHECKED = "CHECKED";
	public static final String IMAGE_UNCHECKED = "UNCHECKED";
	public static final String IMAGE_FIELD = "FIELD";
	public static final String IMAGE_HSWITCH = "HSWITCH";
	public static final String IMAGE_VSWITCH = "VSWITCH";
	public static final String IMAGE_ESWITCH = "ESWITCH";
	public static final String IMAGE_FIELD_TABLE = "icons/field_table.gif";
	public static final String IMAGE_CANCELMAPPING = "icons/cancelMapping.gif";
	public static final String IMAGE_AUTOMAPPING = "icons/autoMapping.gif";
	public static final String IMAGE_STATUS_WARNING = "icons/STATUS_WARNING.jpg";
	public static final String IMAGE_STATUS_ERROR = "icons/STATUS_ERROR.jpg";
	public static final String IMAGE_STATUS_SUCCESS = "icons/STATUS_SUCCESS.jpg";

}
