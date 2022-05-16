package com.blindstick.utils;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author 许金涛
 * 与百度云文字识别相关的东西
 */
@Component
public class BaiduAPI {
    //设置APPID/AK/SK
    @Value("${Baidu.OCR.APP_ID}")
    public  String APP_ID;
    @Value("${Baidu.OCR.API_KEY}")
    public  String API_KEY;
    @Value("${Baidu.OCR.SECRET_KEY}")
    public  String SECRET_KEY;

    public static void main(String[] args) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr("26247076", "70aze5kdt0FfsepK4qSXwCvF", "x9rrX6IPElIraaidFHVIv7kYHnxdQWPj");

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_language", "true");


        // 参数为本地图片路径
        String image = "C://Users//lenovo//Pictures//Feedback//test.jpg";
        JSONObject res = client.webImage(image, options);
        System.out.println(res.get("words_result"));
    }

    /**
     * 根据本地路径提取对应图片中的文字信息
     */
    public String getWordMessage(String path){
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        // 是否检测图像朝向
        options.put("detect_direction", "true");
        // 是否检测语言
        options.put("detect_language", "true");

        // 参数为本地图片路径
        JSONObject res = client.webImage(path, options);
        return res.get("words_result").toString();

    }
}
