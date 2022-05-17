package com.blindstick.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class TestController {
    @RequestMapping("/test")
    public String test(){
//        List<String> addrs = ConnectManager.getDeviceAll();
//
//        for (String addr : addrs) {
//            String msg= "你!好";
//
//            String message1="{\"cmd\":"+"\"soundPlay\""+",\"msg\""+":"+"\""+msg+"\""+"}";
//            byte[] bytes=null;
//            try {
//                bytes=message1.getBytes("GBK");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            //下发指令
//            System.out.println(message1);
//            ConnectManager.sendMessage(addr,HexUtil.bytes2HexString(bytes));
////            ConnectManager.sendMessage(addr,msg);
//        }
        return "111";
    }

    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//        Calendar calendar = Calendar.getInstance();
//        Date date = sdf.parse("2022-04-01");
//        calendar.setTime(date);
//        System.out.println(calendar.get(1));
//        String tempStr="[我的,你的]";
//        System.out.println(tempStr);
//        FileUtil.saveToImgFile(FileUtil.readToString("C:/Users/guhao/Desktop/image.txt"),"C:/Users/guhao/Desktop/image.jpeg");
    }
}
