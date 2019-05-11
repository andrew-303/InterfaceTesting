package com.biyl.emmatesting.base;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.biyl.emmatesting.util.ExcelDataProvider;
import com.biyl.emmatesting.util.LogConfiguration;

/**
 * 自动化测试基类，供测试类继承
 * @author Administrator
 *
 */
public class BaseParpare {
	//输出本页面的日志  初始化
	static Logger logger = Logger.getLogger(BaseParpare.class);
	public static String moduleName = null;	//模块的名字
	public static String functionName = null;	//功能的名字
	public static String caseNum = null;	//用例编号
	
	@BeforeTest
	public void beforeTest(){
		LogConfiguration.initLog(getModuleName(), this.getClass().getSimpleName());		
		logger.info("====================================接口测试开始====================================");
	}
	
	/**接口测试前准备工作*/
	@BeforeClass
	public void startTest(){
		moduleName = getModuleName();
		functionName = getFunctionName();
		caseNum = getCaseNum();
		logger.info(moduleName + "模块" + functionName + "接口" + caseNum + "用例测试开始");
	}
	
	/**接口测试结束后需要做的相关工作*/
	@AfterClass
	public void endTest(){
		logger.info(moduleName + "模块"  + functionName + "接口"  + caseNum + "用例测试结束");
	}
	
	/**接口测试结束后需要做的相关工作*/
	@AfterTest
	public void afterTest(){
		logger.info("====================================接口测试完毕====================================");
	}
	
	/**测试数据提供者 - 方法*/
	@DataProvider(name="testData")
	public Iterator<Object[]> dataFortestMethod(){
		//将模块、接口和用例名称传递给ExcelDataProvider，读取excel中的测试数据
		return new ExcelDataProvider(moduleName +"/" + functionName,caseNum);
	}
	/**
	 * 获取模块名
	 */
	public String getModuleName(){
		String className = this.getClass().getName();
		String moduleName , startStr;
		int lastDotIndexNum ,secondLastDotIndexNum;
		startStr = "testcase.";
		lastDotIndexNum = className.lastIndexOf(".");	//取得最后一个.的index
		secondLastDotIndexNum = className.indexOf(startStr)+startStr.length();
		moduleName = className.substring(secondLastDotIndexNum, lastDotIndexNum);	//取的模块的名称
		return moduleName;
		
	}
	
	/**
	 * 获取接口名
	 * @return
	 */
	public String getFunctionName(){
		String className = this.getClass().getName();		
		int underlineIndexNum = className.indexOf("_");//取得第一个"_"的index
		if(underlineIndexNum>0){
			functionName = className.substring(className.lastIndexOf(".")+1, underlineIndexNum);
		}
		return functionName;
		
	}
	
	/**
	 * 获取用例编号
	 */
	public String getCaseNum(){
		String className = this.getClass().getName();
		int underlineIndexNum = className.indexOf("_");	//取得第一个"_"的index
		if(underlineIndexNum > 0){
			caseNum = className.substring(underlineIndexNum+1,underlineIndexNum+4);	//取得用例编号
		}
		return caseNum;		
	}
}
