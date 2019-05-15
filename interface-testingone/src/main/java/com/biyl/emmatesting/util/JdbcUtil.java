package com.biyl.emmatesting.util;

import java.sql.*;

import org.apache.log4j.Logger;

/**
 * 该类为数据库操作类，实现数据库的增删改查等操作
 * @author Administrator
 *
 */
public class JdbcUtil {
	static Logger logger = Logger.getLogger(JdbcUtil.class.getName());//输出本页面的日志  初始化
	
	/**
	 * 此方法为获取数据库连接
	 */
	public static Connection getConnection(){
		Connection conn = null;
		//直接使用testng运行时，路径为"src/main/resources/config/db.properties"
		String dbConfPath = "classes/config/db.properties";	//使用maven运行时路径
		try {
			String driver = PropertiesDataProvider.getTestData(dbConfPath, "mysqlDriver");//数据库驱动
			String url = PropertiesDataProvider.getTestData(dbConfPath, "mysqlURL");//数据库
			String user = PropertiesDataProvider.getTestData(dbConfPath, "mysqlUser");//用户名
			String password = PropertiesDataProvider.getTestData(dbConfPath, "mysqlPwd");//密码			
			Class.forName(driver);//加载数据库驱动
			if(null == conn){
				conn = DriverManager.getConnection(url, user, password);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can't find the Driver!");
		}	
		catch (Exception e) {
			System.out.println("Database connection failed!");
		}				
		return conn;		
	}
	
	/**
     * 增删改【Add、Del、Update】
     *
     * @param sql
     * @return
     */
	
}
