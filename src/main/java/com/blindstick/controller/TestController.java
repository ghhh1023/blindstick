package com.blindstick.controller;

import com.blindstick.netty.handler.channel.ConnectManager;
import com.blindstick.utils.HexUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @RequestMapping("/test")
    public String test(){
        List<String> addrs = ConnectManager.getDeviceAll();

        for (String addr : addrs) {
            String message=HexUtil.convertStringToHex("hello");
            //下发指令
            ConnectManager.sendMessage(addr, message);
        }
        return "111";
    }
}
