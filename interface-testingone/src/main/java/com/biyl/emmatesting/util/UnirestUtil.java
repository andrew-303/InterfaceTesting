package com.biyl.emmatesting.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;





public class UnirestUtil {
	public static Logger logger = Logger.getLogger(UnirestUtil.class);

	/**
	 * 此方法支持http协议的get和post方法
	 */
	public static String InterfaceTest(Map<String,String> data){
		String responseBody = null;
		HttpResponse<String> jsonResponse = null;
		HttpResponse<JsonNode> kapiResponse = null;
		boolean flag = true;
		String case_desc = data.get("case_desc");
		String url = data.get("interface_url");
		String expect = data.get("expect");
		String parameters = data.get("parameters");		
		logger.info(case_desc+"接口post请求数据开始");
		
		JSONObject para;
		try {
			
			para = new JSONObject(parameters);
			//jsonResponse = Unirest.post(url).header("accept","*/*").fields(para).asString();
			jsonResponse = Unirest.post(url)
					.header("Content-Type", "application/json")					
					.body(para).asString();
			responseBody = jsonResponse.getBody();
			logger.info("请求参数：" + para);
			logger.info("post请求数据完毕");
			/* ***********************************************************************************************************
             ********************************* 执行数据校验：*************************************************************
             * 1、判断是否包含select语句**********************************************************************************
             * 判断是否包含select count(1)********************************************************************************
             * a.包含，执行SQL查询，结果为1表示pass，0表示fail************************************************************
             * b.不包含，执行SQL查询，将结果和Response内容比较************************************************************
             * 2、未包含select语句****************************************************************************************
             * 直接和Response内容比较*************************************************************************************
             *************************************************************************************************************/
			if(expect.toLowerCase().contains("select")){
				if(expect.toLowerCase().contains("count(1)")){
					ResultSet qrs1 = JdbcUtil.executeQuery(expect);
					try {
						while(qrs1.next()){
							String num = qrs1.getString(1);//获取结果集中的第一列
							if(num.equals("1")){
								flag = true;
								logger.info("接口测试， post请求正常响应，数据正确返回");
								logger.info("Response Body:" + responseBody);
								Assert.assertTrue(flag);
							}else{
								flag = false;
								logger.info("Response Body:" + responseBody);
								logger.error("接口测试，数据校验失败");
								Assert.assertTrue(flag);
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}else{
					JSONObject sqlJson = null;
					sqlJson = JdbcUtil.sqlToJson(expect);	//将sql结果存到json中
					logger.info("sqlJson:" + sqlJson);
					logger.info("查询数据库的结果："+ sqlJson);
					jsonCompare(new JSONObject(responseBody), sqlJson);					
				}								
			}else{
				if(responseBody.contains(expect)){
					flag = true;
					logger.info("接口测试，post请求正常响应，数据正确返回");
					logger.info("ResponseBody:" + responseBody);
					Assert.assertTrue(flag);					
				}else{
					flag = false;
					logger.error("接口测试，接口发生异常或存在异常数据进行接口注入，预期结果为："+ expect + "\n,而实际结果为："+responseBody.toString());
					Assert.assertTrue(flag);
				}
			}
		} catch (UnirestException e) {
			flag = false;
			logger.error("接口测试发生异常，请求连接失败");
			responseBody = null;
			Assert.assertTrue(flag);
		}
		return responseBody;
		
	}

	/**
     * @param
     * @return void
     * @throws
     * @Description: 使用Jackson序列化json, 当执行asObject(Class)或者.body(Object)之前，
     * 需要一个ObjectMapper的定制实现，在最初就要运行
     */
	public static void Serialization(){
		Unirest.setObjectMapper(new ObjectMapper(){
			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
						
			@Override
			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}				
			}
			
			@Override
			public String writeValue(Object value) {				
				try {
					return jacksonObjectMapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}			
			}			
		});
	}
	
	/**
	 * 废弃，用net.sf.json.JSONObject转换以后，使用unirest调用接口时，传入的fields(para)会导致解析json失败
     * 将json字符串转换成JSONObject对象
     *
     * @param parameters
     * @return
     */
	/*public static JSONObject convertJson(String parameters) {
		JSONObject jsonObj = JSONObject.fromObject(parameters);
		return jsonObj;
	}*/
	
	/**
	 * 使用org.json.JSONObject
	 * 将json字符串转换成JSONObject对象
	 * @param parameters
	 * @return
	 */
	public static JSONObject convertJson(String parameters) {
		JSONObject jsonObj = new JSONObject(parameters);
		return jsonObj;
	}
	
	
	/* ***********************************************************************************************************
     * 比较两个json数据的一致性，支持全比较、部分比较、含复杂list的json比较***************************************
     * ****判断sortJson里面是否包含json数组***********************************************************************
     * *******a、包含，则进行两个json里面的数组的比较(支持全比较、部分比较)***************************************
     * *******b、不包含，则进行两个json里面数据的比较(支持全比较、部分比较)***************************************
     * **********判断longJson里面是否包含sortJson相应的key********************************************************
     * *************b1、包含，则进而比较相应key对应的value********************************************************
     * ****************最终结果全为true，则校验成功，若有一个为flase，则比较结果为失败****************************
     * *************b2、不包含，则从data节点进行解析校验**********************************************************
     *  ***************判断longJson的data节点里面是否包含sortJson相应的key****************************************
     * *******************b21、包含，则进而比较相应key对应的value*************************************************
     * *******************b22、最终结果全为true，则校验成功，若有一个为flase，则比较结果为失败********************
     *
     * @param longJson 即Response内容
     * @param sqlJson 即SQL查询结果转换的json数据
     */
	public static void jsonCompare(JSONObject longJson, JSONObject sqlJson) {
		boolean flag = true;
		if(sqlJson.toString().contains("[")){
			jsonArrayValueCompare(longJson,sqlJson);
		}else{
			Iterator<?> iter = sqlJson.keys();
			while(iter.hasNext()){
				System.out.println("进入jsonCompare方法");
				String key = (String) iter.next();
				String value = (String) sqlJson.get(key);
				if(longJson.has(key)){	//包含相应的key的情况
					if(longJson.get(key).equals(value)){	//相应的key对应的value相等
						flag = true;
					}else{//相应的key对应的value不相等
						flag = false;
						logger.error("接口测试数据校验失败，预期结果为：" + longJson + "\n，而实际结果为："+sqlJson);
						Assert.assertTrue(flag);
						break;
					}
				}else{	// 不包含相应的key的情况,则从data节点进行解析校验
					JSONObject newJson = longJson.getJSONObject("data");
					if(newJson.has(key)){//data节点包含相应的key的情况
						if(newJson.get(key).equals(value)){//相应的key对应的value相等
							flag = true;
						}else{	//相应的key对应的value不等
							flag = false;
							logger.error("接口测试数据校验失败，预期结果为："+longJson+"，实际结果为："+sqlJson);
							Assert.assertTrue(flag);
							break;
						}
					}else{
						JSONObject newJson2 = newJson.getJSONObject("resultObject");
						if(newJson2.has(key)){//resultObject节点包含相应key的情况
							if(newJson2.get(key).equals(value)){//相应的key对应的value相等
								flag = true;
							}else{// resultObject节点相应的key对应的value不相等
								flag = false;
								logger.error("接口测试数据校验失败,预期结果为:"+sqlJson+",实际结果为:"+longJson);
								Assert.assertTrue(flag);
								break;
							}
						}else{
							flag = false;
                            logger.error("接口测试数据校验失败,预期结果为:" + sqlJson
                                    + "\n,而实际结果为:" + longJson);
                            Assert.assertTrue(flag);
                            break;
						}
					}
				}
			}
			if(flag == true){
				logger.info("接口测试数据正常返回,数据一致");
                Assert.assertTrue(true);
			}
		}
		
	}

	/**
     * @param @param longJson 如：{"resultObject": {"courseid": "1059","lessonList": [{"lessonId": "1157","title": "第一节"},{"lessonId": "1158","title": "第二节"}],
     *               "teacherList": [{"teacherid": "1","name": "苏强"},{"teacherid": "2","name": "胡仔"}]}}
     * @param @param sqlJson 如：{"array":[{"lessonId":"1157","title":"第一节"},{"lessonId":"1158","title":"第二节"}]}
     * @return void  包含则返回true，否则返回false
     * @throws
     * @Description: TODO 判断一个json数组中对应的key、value是否包含在另外一个json数组中，包含则返回true，否则返回false
     */
	public static void jsonArrayValueCompare(JSONObject longJson, JSONObject sqlJson) {
			String[] array = StringUtils.substringsBetween(longJson.toString(), "[", "]");
			String[] array2 = StringUtils.substringsBetween(sqlJson.toString(), "[", "]");
			int Count = 0;
			int arrLength = array2.length;
			for (int i = 0; i < array.length; i++) {
				JSONObject longObj = new JSONObject(array[i]);
				for (int j = 0; j < arrLength; j++) {
					JSONObject sqlObj = new JSONObject(array2[i]);
					if(jsonFieldCompare(longObj,sqlObj)){
						if(jsonValueCompare(longObj, sqlObj)){
							Count = Count + 1;
						}
					}
				}
			}
			if(Count == arrLength){
				logger.info("接口测试数据正常返回，数据一致");
				Assert.assertTrue(true);
			}else{
				logger.error("接口测试数据校验失败，预期结果为：" + sqlJson +",而实际结果是：" + longJson);
				Assert.assertTrue(false);
			}
	}

	/**
     * @param @param  longJson 如：{"lessonId":"1157","title":"第一节:你所不知道的木兰辞","name":"李白"}
     * @param @param  sortJson 如：{"lessonId":"1157","title":"第一节:你所不知道的木兰辞"}
     * @param @return
     * @return boolean  包含则返回true，否则返回false
     * @throws
     * @Description: TODO 判断一个json数据中对应的key、value是否包含在另外一个json中，包含则返回true，否则返回false
     */
	private static boolean jsonValueCompare(JSONObject longJson, JSONObject sqlJson) {
		boolean flag = true;
		Iterator<?> iter = sqlJson.keys();
		while(iter.hasNext()){
			System.out.println("进入jsonValueCompare方法");
			String key = (String) iter.next();
			String value = (String) sqlJson.get(key);
			if(longJson.has(key)){//包含相应的key的情况
				if(longJson.get(key).equals(value)){	//相应的key对应的value相等
					flag = true;
				}else{	//相应的key对应的value不相等
					flag = false;
				}
			}else{//不包含相应的key
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
     * @param @param  longJson 如：{"lessonId":"1157","title":"第一节:你所不知道的木兰辞","name":"李白"}
     * @param @param  sortJson 如：{"lessonId":"1157","title":"第一节:你所不知道的木兰辞"}
     * @param @return
     * @return boolean  包含则返回true，否则返回false
     * @throws
     * @Description: TODO 判断一个json数据中是否包含另外一个json的所有字段，包含则返回true，否则返回false
     */
	public static boolean jsonFieldCompare(JSONObject longJson, JSONObject sqlJson) {
		boolean flag = true;
		Iterator<?> iter = sqlJson.keys();
		while(iter.hasNext()){
			System.out.println("进入jsonFieldCompare方法");
			String key = (String) iter.next();
			if(((Map<String, String>) longJson).containsKey(key)){
				flag = true;
			}else{
				flag = false;
				break;
			}
		}
		return flag;
	}

	
}
