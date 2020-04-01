package com.interfacetest.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口测试之前的预处理逻辑
 * 提供生成接口测试依赖数据的前置条件的工具类
 * @author HZFI_DC
 *
 */
public class PreInterfaceTestUtil {
	static Logger logger = Logger.getLogger(PreInterfaceTestUtil.class);

	/**
	 * 从配置文件中获取手机号与密码
	 */
	public static Map<String,String> getMobileAndPass() {

		String mobile = PropertiesDataProvider.getDataPrepare( "mobile");//手机号
		String password = PropertiesDataProvider.getDataPrepare("password");//密码
		Map<String, String> mobilepassMap= new HashMap<>();
		mobilepassMap.put("mobile",mobile);
		mobilepassMap.put("password",password);
		return mobilepassMap;
	}

	/** 	 
 	 * 通过调用手机号密码登录接口，获取用户token，得到token以后传递给下一个接口使用
 	 * 接口入参中的password是加密后的结果
 	 * @return
	*/
	public static String getTokenByMobile(String mobile,String password) {

		// 请求的主体,将String转成JSONObject,需要使用org.json包，如果使用net.sf.json.JSONObject会导致unirest请求时调用body报错
//		String strJson = "{\r\n\t\"params\":{\r\n\t\t\"password\":\""+password+"\",\r\n\t\t\"mobile\":\""+mobile+"\"\r\n\t}\t\r\n}";
		String parameters = PropertiesDataProvider.getDataPrepare("parameters");//接口入参
		String interfaceUrl = PropertiesDataProvider.getDataPrepare("interfaceurl");//接口url

		//处理从配置文件读取出来，前后带中括号的问题
		String strJson = StringUtils.strip(parameters, "[]");
		JSONObject jsonObject = new JSONObject(strJson);

		String userToken = null;
		try {
			HttpResponse<String> response = Unirest.post(interfaceUrl)
					.header("Content-Type", "application/json").header("Cache-Control", "no-cache").body(jsonObject)
					.asString();
			JSONObject responseObject = new JSONObject(response.getBody());
			userToken = responseObject.getString("data");
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userToken;

	}

	public static JSONObject getPreResponseBody(String url,String parameters) {
		String strJson = parameters;
		JSONObject jsonObject = new JSONObject(strJson);
		JSONObject responseObject = null;

		// =========================================================================================================
		try {
			HttpResponse<String> response = Unirest.post(url)
					.header("Content-Type", "application/json").header("Cache-Control", "no-cache").body(jsonObject)
					.asString();
			logger.info("调用前置接口后得到的response为：" + response.getBody());
			responseObject = new JSONObject(response.getBody());

		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseObject;
	}

}
