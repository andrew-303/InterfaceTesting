package com.biyl.emmatesting.testcase.hzbs; 

import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.Test;
import com.biyl.emmatesting.util.JdbcUtil;
import com.biyl.emmatesting.util.UnirestUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.biyl.emmatesting.base.BaseParpare;

public class getAppointListByUser_001_test extends BaseParpare { 
 static Logger logger = Logger.getLogger(getAppointListByUser_001_test.class.getName());
 
 	/**
 	 * 此段代码是在自动生成代码后，手动添加的部分
 	 * 通过调用手机号密码登录接口，获取用户token，得到token以后传递给下一个接口使用
 	 * 接口入参中的password是加密后的结果
 	 * @return
	*/
	public String testLoginByMobile() {

		// 请求的主体,将String转成JSONObject,需要使用org.json包，如果使用net.sf.json.JSONObject会导致unirest请求时调用body报错
		
		String strJson = "{\r\n\t\"params\":{\r\n\t\t\"password\":\"C9196DB6D1CD1C1DDB60D56217AE967F\",\r\n\t\t\"mobile\":\"15068796557\"\r\n\t}\t\r\n}";
		JSONObject jsonObject = new JSONObject(strJson);
		String verificationToken = null;
		try {
			HttpResponse<String> response = Unirest.post("https://test.iconntech.com/gateway/system/login")
					.header("Content-Type", "application/json").header("Cache-Control", "no-cache").body(jsonObject)
					.asString();
			JSONObject responseObject = new JSONObject(response.getBody());
			verificationToken = responseObject.getString("data");
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return verificationToken;

	}
 	
 	
	@Test(dataProvider = "testData") 
	public void testCase(Map<String, String> data) {
		/******************************************************************************** 
		 ***********************通过获取上一个接口的响应结果中的token， 替换当前接口中的请求参数中变量{token}******************** 
		 ********************************************************************************/
		getAppointListByUser_001_test alb = new getAppointListByUser_001_test();
		String newParameters = null;
		newParameters = data.get("parameters").replace("{token}", alb.testLoginByMobile());
		data.put("parameters", newParameters);//更新验证码
		System.out.println("new data is:" + data.toString());
		
		
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