package com.biyl.test;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TestUnirest1 {
	private static Logger log = Logger.getLogger(TestUnirest1.class.getName());
	
	private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";

    public static String sendGet(String url) {
        try {
            HttpResponse<String> response = Unirest.get(url).header("User-Agent", USER_AGENT).asString();
            return response.getBody();
        } catch (UnirestException e) {
            log.error("HTTP Get Error:{}",e);
        }
        return "";
    }

    public static String sendPost(String url, String bodyParams,String contentType,String cookie) {
        try {
            HttpResponse<String> response = Unirest.post(url)
                    .header("User-Agent", USER_AGENT)
                    .header("Connection", "Keep-Alive")
                    .header("Content-Type", contentType)
                    .header("Cookie",cookie)
                    .body(bodyParams)
                    .asString();
            String responseBody = response.getBody();
            System.out.println("responseBody:" + responseBody);
            return responseBody;
        } catch (UnirestException e) {
            log.error("HTTP Get Error:{}",e);
        }
        return "";
    }

    public static String sendPost(String url, String bodyParams,String contentType) {
    	 System.out.println("responseBody1111");
        try {
        	System.out.println("responseBody2222");
            HttpResponse<String> response = Unirest.post(url)
                    .header("User-Agent", USER_AGENT)
                    .header("Connection", "Keep-Alive")
                    .header("Content-Type", contentType)
                    .body(bodyParams)
                    .asString();
            System.out.println("responseBody3333");
            String responseBody = response.getBody();
           
            System.out.println("responseBody1:" + response);
            log.info("ResponseBody2:" + responseBody);
            return response.getBody();
            
        } catch (UnirestException e) {
            log.error("HTTP Get Error:{}",e);
        }
        return "";
    }
    
    @Test
	public static void job_MidwaySendDataToFundPlatform() {
    	String thirdcode =null;
    	String itemcode =null;
		String url = "http://10.139.105.205:8080/taskInvoker.do";
		String postData = "invokeParam={\"beanName\": \"fundDataSendTask\",\"method\": \"run\",\"parameterTypes\":[\"java.lang.String\",\"java.lang.String\",\"java.lang.String\"],\"parameterValues\":[\""
				+ thirdcode + "\",\"" + itemcode + "\",\"\"],\"callbackUrl\":\"\"}";
		TestUnirest1.sendPost(url, postData, "application/x-www-form-urlencoded");
	}
}

