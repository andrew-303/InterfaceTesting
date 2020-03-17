package com.biyl.emmatesting.util;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供生成接口测试依赖数据的前置条件的工具类
 * @author HZFI_DC
 *
 */
public class PreInterfaceTestUtil {

	/**
	 * 从配置文件中获取手机号与密码
	 */
	public static Map<String,String> getMobileAndPass() {
		String dataPrefPath ="src/main/resources/config/data_prepare.properties";//直接使用testng运行时路径
		//String dataPrefPath = "classes/config/data_prepare.properties";	//使用maven运行时路径
		String mobile = PropertiesDataProvider.getTestData(dataPrefPath, "mobile");//手机号
		String password = PropertiesDataProvider.getTestData(dataPrefPath,"password");//密码
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

		//String strJson = "{\r\n\t\"params\":{\r\n\t\t\"password\":\"C9196DB6D1CD1C1DDB60D56217AE967F\",\r\n\t\t\"mobile\":\"15068796557\"\r\n\t}\t\r\n}";
		String strJson = "{\r\n\t\"params\":{\r\n\t\t\"password\":\""+password+"\",\r\n\t\t\"mobile\":\""+mobile+"\"\r\n\t}\t\r\n}";
		JSONObject jsonObject = new JSONObject(strJson);
		String userToken = null;
		try {
			HttpResponse<String> response = Unirest.post("https://test.iconntech.com/gateway/system/login")
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
}
