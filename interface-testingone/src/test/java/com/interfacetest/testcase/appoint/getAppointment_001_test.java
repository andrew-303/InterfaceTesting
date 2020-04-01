package com.interfacetest.testcase.appoint; 

import java.util.Map;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.interfacetest.util.JdbcUtil;
import com.interfacetest.util.UnirestUtil;
import com.interfacetest.base.BaseParpare;

@Epic("appoint")
public class getAppointment_001_test extends BaseParpare { 
 static Logger logger = Logger.getLogger(getAppointment_001_test.class.getName());
 
	@Test(dataProvider = "testData") 
	@Description("根据主键获取预约详情（需要依赖登录接口获取token）") 
	public void getAppointment(Map<String, String> data) { 
 
		/*------- Manual added test code start ------*/
		String newParameters  = data.get("parameters").replace("{token}", newToken);
		data.put("parameters", newParameters);//更新新参数
		/*------- Manual added test code end ------*/ 
 
		/*------- Auto generated test code start ------*/ 
		String pre_condition = data.get("pre_condition"); 
 
		//用例中的pre_condition列不为空时，执行相应的SQL语句 
		if (pre_condition.length() > 0) { 
		logger.info("----------------------正在执行数据准备操作---------------------"); 
			String[] Array = pre_condition.split(";\n"); 
			for (int i = 0; i < Array.length; i++) { 
				JdbcUtil.executeNonQuery(Array[i]); 
			} 
		logger.info("----------------------执行数据准备操作完成----------------------"); 
		} 
		//进行接口测试，并验证测试结果 
		UnirestUtil.InterfaceTest(data);  
 
		/*------- Auto generated test code end ------*/ 
	}
}