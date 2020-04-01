package com.interfacetest.testcase.appoint; 

import java.util.Map;

import com.interfacetest.util.PreInterfaceTestUtil;
import com.interfacetest.util.PropertiesDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import com.interfacetest.util.JdbcUtil;
import com.interfacetest.util.UnirestUtil;
import com.interfacetest.base.BaseParpare;

@Epic("appoint")
public class AppointAndcancelAppoint_001_test extends BaseParpare { 
 static Logger logger = Logger.getLogger(AppointAndcancelAppoint_001_test.class.getName());

	/**
	 * 1、获取地点列表
	 * url：接口请求地址
	 * parameters:接口请求入参
	 */
	public int getAppointAreaList() {
		String uri = "gateway/appoint/getAppointAreaList";
		String url = PropertiesDataProvider.getDataPrepare("envdomain") + uri;
		String parameters = "{\"params\":{},\"token\":\""+ newToken +"\"}";
		//调用前置处理器，发起接口请求，获取前一个接口的响应结果
		JSONObject preResponseBody = PreInterfaceTestUtil.getPreResponseBody(url, parameters);
		//获取地点返回的是JSONArray,这里调用getJSONArray
		JSONArray jsonArray = preResponseBody.getJSONArray("data");
		//获取JSONArray中索引为0的JSONObject对象的值
		int areaId= jsonArray.getJSONObject(0).getInt("id");
//		String areaIdStr = String.valueOf(areaId);
		logger.info("获取地点列表.areaId: " + areaId);
		return areaId;
	}

	/**
	 * 2、根据地点获取预约类型列表,依赖方法1.获取地点列表接口响应中的areaId
	 * url：接口请求地址
	 * parameters:接口请求入参
	 */
	public int getAppointCatalogList() {
		String uri = "gateway/appoint/getAppointCatalogList";
		String url = PropertiesDataProvider.getDataPrepare("envdomain") + uri;
		String parameters = "{\"params\":{\"areaId\":"+getAppointAreaList()+"},\"token\":\""+ newToken+"\"}";
		//调用前置处理器，发起接口请求，获取前一个接口的响应结果
		JSONObject preResponseBody = PreInterfaceTestUtil.getPreResponseBody(url, parameters);
		//获取预约类型列表返回的是JSONArray,这里调用getJSONArray
		JSONArray jsonArray = preResponseBody.getJSONArray("data");
		//获取JSONArray中索引为0的JSONObject对象的值
		int catalogId= jsonArray.getJSONObject(0).getInt("catalogId");
		logger.info("根据地点获取预约类型列表.catalogId: " + catalogId);
		return catalogId;
	}

	/**
	 * 3、根据类型获取事项列表,依赖方法2.根据地点获取预约类型列表接口响应中的catalogId
	 * url：接口请求地址
	 * parameters:接口请求入参
	 */
	public int getAppointItemList() {
		String uri = "gateway/appoint/getAppointItemList";
		String url = PropertiesDataProvider.getDataPrepare("envdomain") + uri;
		String parameters = "{\"params\":{\"catalogId\":"+getAppointCatalogList()+"},\"token\":\""+ newToken+"\"}";
		//调用前置处理器，发起接口请求，获取前一个接口的响应结果
		JSONObject preResponseBody = PreInterfaceTestUtil.getPreResponseBody(url, parameters);
		//获取事项列表返回的是JSONArray,这里调用getJSONArray
		JSONArray jsonArray = preResponseBody.getJSONArray("data");
		//获取JSONArray中索引为0的JSONObject对象的值
		int appointItemId= jsonArray.getJSONObject(0).getInt("appointItemId");
		logger.info("根据类型获取事项列表.appointItemId: " + appointItemId);
		return appointItemId;
	}

	/**
	 * 4、根据id获取预约事项,依赖方法3.根据类型获取事项列表接口响应中的appointItemId
	 * url：接口请求地址
	 * parameters:接口请求入参
	 */
	public int getAppointItem() {
		String uri = "gateway/appoint/getAppointItem";
		String url = PropertiesDataProvider.getDataPrepare("envdomain") + uri;
		String parameters = "{\"params\":{\"appointItemId\":"+getAppointItemList()+"},\"token\":\""+ newToken+"\"}";
		//调用前置处理器，发起接口请求，获取前一个接口的响应结果
		JSONObject preResponseBody = PreInterfaceTestUtil.getPreResponseBody(url, parameters);
		//获取预约事项返回的是JSONObject,这里调用getJSONObject
		JSONObject jsonObject = preResponseBody.getJSONObject("data");
		//直接获取JSONObject中的值
		int groupId= jsonObject.getInt("groupId");
		logger.info("根据id获取预约事项.groupId: " + groupId);
		return groupId;
	}

	/**
	 * 5、进行预约,依赖方法3中的appointItemId，方法4中的groupId
	 * url：接口请求地址
	 * parameters:接口请求入参
	 */
	public int doAppoint() {
		String uri = "gateway/appoint/appoint";
		String url = PropertiesDataProvider.getDataPrepare("envdomain") + uri;
		String parameters = "{\"params\":{\"groupId\":\""+ getAppointItem() +"\",\"telephone\":\"14112340001\",\"sfzh\":\"3****************2\",\"userId\":\"449\",\"appointItemId\":\""+getAppointItemList() +"\",\"dateId\":\"168062\"},\"token\":\""+newToken+"\"}";
		//调用前置处理器，发起接口请求，获取前一个接口的响应结果
		JSONObject preResponseBody = PreInterfaceTestUtil.getPreResponseBody(url, parameters);
		int appointId = 0;
		appointId = preResponseBody.getJSONObject("data").getInt("id");
		logger.info("预约成功后，预约的事项id为：" + appointId);
		return appointId;
	}
 
	@Test(dataProvider = "testData") 
	@Description("取消预约(先调用预约接口，再根据预约id取消)") 
	public void AppointAndcancelAppoint(Map<String, String> data) { 
 
		/*------- Manual added test code begin ------*/


		/**
		 * 替换变量参数
		 */
		String newAppointId = String.valueOf(doAppoint());
		String newParameters = null;
		newParameters = data.get("parameters").replace("{appointId}", newAppointId).replace("{token}", newToken);
		data.put("parameters", newParameters);//更新参数

		/*------- Manual added test code end ------*/ 
 
		/*------- Auto generated test code begin ------*/ 
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