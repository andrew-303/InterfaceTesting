package com.interfacetest.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

/**
 * 接口测试完成后置处理逻辑
 */

public class PostInterfaceTestUtil {
    static Logger logger = Logger.getLogger(PostInterfaceTestUtil.class);

    /**
     * 提取接口响应内容
     */
    public static Boolean extractResult(String responseBody) {

        Boolean resDataEmpty = false;

        JSONObject jsonResponseObject = JSON.parseObject(responseBody);
        Object jsonResultData = jsonResponseObject.get("data");

        //判断json响应中的data内容是否为空,如果结果为空也算程序异常
        if (jsonResultData instanceof JSONArray) {
            resDataEmpty = jsonResponseObject.getJSONArray("data").isEmpty();
            logger.info("接口响应结果中的data数组内容是否为空：" + resDataEmpty);
        } else if (jsonResultData instanceof JSONObject) {
            resDataEmpty = jsonResponseObject.getJSONObject("data").isEmpty();
            logger.info("接口响应结果中的data对象内容是否为空：" + resDataEmpty);
        }
        return resDataEmpty;
    }
}
