package com.jiuqi.etl.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.jiuqi.etl.app.ApplicationPlugin;

public class AppUtils {

	public static String getVersion() throws IOException {
		Bundle bundle = Platform.getBundle(ApplicationPlugin.PLUGIN_ID);  
		URL url = bundle.getResource("/about.mappings");  
		InputStream is = FileLocator.toFileURL(url).openStream();
		Properties properties = new Properties();
		properties.load(is);
		
		return properties.getProperty("0");
	}
	
}
