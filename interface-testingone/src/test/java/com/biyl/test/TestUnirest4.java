package com.biyl.test;



import org.json.JSONObject;
import org.testng.annotations.Test;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;





public class TestUnirest4 {
	
	
	@Test
	public void testHomepage1(){
		//请求的主体,将String转成JSONObject,需要使用org.json包，如果使用net.sf.json.JSONObject会导致unirest请求时调用body报错
		String strJson = "{\n\t\"token\":\"6a95b2e141188d8ff1324ea3feb08879\",\n\t\"client\":\"android\",\n\t\"params\":{\n\t\t\"version\":\"8.0.0\",\n\t\t\"areaId\":\"144\",\n\t\t\"stagePosition\":\"01\",\n\t\t\"realVersion\":\"1.2.5\"\n\t}\n\n}";
		JSONObject jsonObject = new JSONObject(strJson);
		System.out.println("jsonObject:" + jsonObject);
		
		
		//=========================================================================================================
		try {
			HttpResponse<String> response = Unirest.post("https://test.iconntech.com/gateway/appPortal/getAppStageByNewVersion")
					  .header("Content-Type", "application/json")
					  .header("Cache-Control", "no-cache")
					  .header("Postman-Token", "16fa1026-3ae9-2f5b-f9a4-4e1ca561b48d")
					  //.body("{\n\t\"token\":\"6a95b2e141188d8ff1324ea3feb08879\",\n\t\"client\":\"android\",\n\t\"params\":{\n\t\t\"version\":\"8.0.0\",\n\t\t\"areaId\":\"144\",\n\t\t\"stagePosition\":\"01\",\n\t\t\"realVersion\":\"1.2.5\"\n\t}\n\n}")					  
					  .body(jsonObject)
					  .asString();
			
			//System.out.println("response:" + response);
			System.out.println("response.getBody:" + response.getBody());
			System.out.println("response.getStatus:" + response.getStatus());
			System.out.println("response.toString:" + response.toString());
			System.out.println("response.getStatusText:" + response.getStatusText());
			System.out.println("response.getClass:" + response.getClass());
			System.out.println("response.getHeaders:" + response.getHeaders());
			System.out.println("response.getRawBody:" + response.getRawBody());
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}

