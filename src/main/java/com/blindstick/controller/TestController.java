package com.blindstick.controller;

import com.blindstick.netty.handler.channel.ConnectManager;
import com.blindstick.utils.HexUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @RequestMapping("/test")
    public String test(){
        List<String> addrs = ConnectManager.getDeviceAll();

        for (String addr : addrs) {
            String message=HexUtil.convertStringToUTF8("{"+"\"msg\""+":"+"\"你好！\""+"}");
            String msg=HexUtil.convertStringToHex("{"+"\"msg\""+":"+"}");
            System.out.println(message);
            System.out.println(msg);
            //下发指令
            ConnectManager.sendMessage(addr,message);
            ConnectManager.sendMessage(addr,msg);
        }
        return "111";
    }
}
