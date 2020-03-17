package com.biyl.emmatesting.testcase.hzbs;

import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.biyl.emmatesting.util.DesUtil;
import com.biyl.emmatesting.util.JdbcUtil;
import com.biyl.emmatesting.util.PreInterfaceTestUtil;
import com.biyl.emmatesting.util.UnirestUtil;
import com.biyl.emmatesting.base.BaseParpare;

public class getAppointLists_001_test extends BaseParpare {
	static Logger logger = Logger.getLogger(getAppointLists_001_test.class.getName());


	@Test(dataProvider = "testData")
	public void testCase(Map<String, String> data) {
		/**
		 * 自动化测试用例生成后，需要手动添加
		 * 通过获取上一个接口的响应结果中的token， 替换当前接口中的请求参数中变量{token}
		 */
		String newParameters  = data.get("parameters").replace("{token}", newToken);
		data.put("parameters", newParameters);//更新验证码
		String pre_condition = data.get("pre_condition");
		/********************************************************************************
		 *********************** 用例中的pre_condition列不为空时，执行相应的SQL语句********************
		 ********************************************************************************/
		if (pre_condition.length() > 0) {
			logger.info("**********************************正在执行数据准备操作*********************************");
			String[] Array = pre_condition.split(";\n");
			for (int i = 0; i < Array.length; i++) {
				JdbcUtil.executeNonQuery(Array[i]);
			}
			logger.info("**********************************执行数据准备操作完成*********************************");
		}
		/******************************************************************************** 
		 *********************************** 进行接口测试，并验证测试结果*************************** 
		 ********************************************************************************/
		UnirestUtil.InterfaceTest(data);
	}
}