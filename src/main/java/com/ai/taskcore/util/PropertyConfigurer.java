package com.ai.taskcore.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Decrypt the password to the properties file
 */
public class PropertyConfigurer extends
		org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	protected void loadProperties(Properties props) throws IOException {
		super.loadProperties(props);
	}

}