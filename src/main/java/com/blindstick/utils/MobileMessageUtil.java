package com.blindstick.utils;

import com.blindstick.config.Client;

public class MobileMessageUtil {
    public static Client.Request sendIdentifyingCode(String mobile, String code) {
        Client client = new Client();
        client.setAppId("hw_10511");     //开发者ID，在【设置】-【开发设置】中获取
        client.setSecretKey("5354e599062278d422667efbd779655e");    //开发者密钥，在【设置】-【开发设置】中获取
        client.setVersion("1.0");

        /**
         *   json格式可在 bejson.com 进行校验
         */
        Client.Request request = new Client.Request();
        request.setBizContent("{\"mobile\":[\"" + mobile + "\"],\"type\":0,\"template_id\":\"ST_2020101100000005\",\"sign\":\"智能导航盲杖\",\"send_time\":\"\",\"params\":{\"code\":" + code + "}}");  // 这里是json字符串，send_time 为空时可以为null, params 为空时可以为null,短信签名填写审核后的签名本身，不需要填写签名id
        request.setMethod("sms.message.send");
        System.out.println( client.execute(request) );
        return request;
    }
}
