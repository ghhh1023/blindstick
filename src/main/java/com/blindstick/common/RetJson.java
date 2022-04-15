package com.blindstick.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class RetJson {
    int code;
    String msg;
    Map data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public static RetJson success(Map map){
        RetJson retJson=new RetJson();
        retJson.setCode(0);
        retJson.setData(map);
        return retJson;
    }

    public static RetJson success(String key,String value){
        Map<String,Object> map=new HashMap<>();
        map.put(key,value);
        return success(map);
    }

    public static RetJson success(String key,Object value){
        Map<String,Object> map=new HashMap<>();
        map.put(key,value);
        return success(map);
    }

    public static RetJson success(int code,String msg){
        RetJson retJson=new RetJson();
        retJson.setCode(code);
        retJson.setMsg(msg);
        return retJson;
    }

    public static RetJson fail(int code,String msg){
        RetJson retJson=new RetJson();
        retJson.setCode(code);
        retJson.setMsg(msg);
        return retJson;
    }

    @Override
    public String toString() {
        ObjectMapper mapper=new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(this);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }
}
