package com.biyl.emmatesting.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 从.properties文件中读取相关数据
 * @author Administrator
 *
 */
public class PropertiesDataProvider {

	public static String getTestData(String configFilePath,String key){
		Configuration config = null;
		try {			
			config = new PropertiesConfiguration(configFilePath);			
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return String.valueOf(config.getProperty(key));
	}
}
