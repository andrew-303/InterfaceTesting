package com.interfacetest.util;

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

	/**
	 * 从data_prepare.properties数据准备的配置文件中取出关键字对应的value
	 * @param key
	 * @return
	 */
	public static String getDataPrepare(String key) {
		//读取配置文件
		String dataPrefPath ="src/main/resources/config/data_prepare.properties";//直接用TestNG运行或者maven集成Allure运行时
		//String dataPrefPath = "classes/config/data_prepare.properties";	//使用maven运行时路径

		return getTestData(dataPrefPath,key);
	}
}
