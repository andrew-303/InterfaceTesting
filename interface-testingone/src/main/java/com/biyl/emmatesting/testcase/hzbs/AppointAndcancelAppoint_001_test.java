package com.biyl.emmatesting.testcase.hzbs;

import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.Test;
import com.biyl.emmatesting.util.JdbcUtil;
import com.biyl.emmatesting.util.PreInterfaceTestUtil;
import com.biyl.emmatesting.util.UnirestUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.biyl.emmatesting.base.BaseParpare;
/**
 * 预约和取消预约接口测试
 * @author Andrew
 *
 */
public class AppointAndcancelAppoint_001_test extends BaseParpare {
	static Logger logger = Logger.getLogger(AppointAndcancelAppoint_001_test.class.getName());

	@Test(dataProvider = "testData")
	public void testCase(Map<String, String> data) {

		/**
		 * 1、预约
		 */
		// 请求的主体,将String转成JSONObject,需要使用org.json包，如果使用net.sf.json.JSONObject会导致unirest请求时调用body报错
		String strJson = "{\"params\":{\"groupId\":\"475\",\"telephone\":\"15068791025\",\"sfzh\":\"3****************2\",\"userId\":\"449\",\"appointItemId\":\"165\",\"dateId\":\"168062\"},\"token\":\""+newToken+"\"}";
		JSONObject jsonObject = new JSONObject(strJson);
		int appointId = 0;

		// =========================================================================================================
		try {
			HttpResponse<String> response = Unirest.post("https://test.iconntech.com/gateway/appoint/appoint")
					.header("Content-Type", "application/json").header("Cache-Control", "no-cache").body(jsonObject)
					.asString();
			//logger.info("调用预约接口后得到的response为：" + response.getBody());
			JSONObject responseObject = new JSONObject(response.getBody());
			appointId = responseObject.getJSONObject("data").getInt("id");
			//logger.info("appointId值为:"+ appointId);
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 替换变量参数
		 */
		String newAppointId = String.valueOf(appointId);

		String newParameters = null;
		newParameters = data.get("parameters").replace("{appointId}", newAppointId).replace("{token}", newToken);
		data.put("parameters", newParameters);//更新验证码

		//以下是自动生成的代码：
		//=========================================================================================================
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