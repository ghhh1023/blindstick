package com.blindstick.controller;

import com.blindstick.netty.handler.channel.ConnectManager;
import com.blindstick.utils.FileUtil;
import com.blindstick.utils.HexUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
public class TestController {
    @RequestMapping("/test")
    public String test(){
        List<String> addrs = ConnectManager.getDeviceAll();

        for (String addr : addrs) {
            String message=HexUtil.convertStringToUTF8("{"+"\"msg\""+":"+"\"你好！\""+"}");
            String msg=HexUtil.convertStringToHex("{"+"\"msg\""+":"+"\""+HexUtil.convertStringToUTF8("你好")+"\""+"}");
            System.out.println(msg);
            //下发指令
//            ConnectManager.sendMessage(addr,message);
            ConnectManager.sendMessage(addr,msg);
        }
        return "111";
    }

    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//        Calendar calendar = Calendar.getInstance();
//        Date date = sdf.parse("2022-04-01");
//        calendar.setTime(date);
//        System.out.println(calendar.get(1));
        FileUtil.saveToImgFile(FileUtil.readToString("/tmp/images/image.txt"),"/tmp/images/image.jpeg");
    }
}
