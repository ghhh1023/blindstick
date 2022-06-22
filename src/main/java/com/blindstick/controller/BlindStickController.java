package com.blindstick.controller;


import com.blindstick.common.RetJson;
import com.blindstick.service.serviceImpl.MqttInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class BlindStickController {

@Autowired
MqttInfoServiceImpl mqttInfoService;


    @PostMapping("/connect")
    public RetJson connect(){
        mqttInfoService.insert();
        return RetJson.success(2,"true");
    }

    @GetMapping("/pub")
    public RetJson pub(){
        mqttInfoService.pub("62a30d99538e623c42866677_blind_stick_0_0_2022062007","$oc/devices/62a30d99538e623c42866677_blind_stick/user/message","123");
        return RetJson.success(5,"ook");
    }

    @PostMapping("/getMsg")
    public RetJson getMsg( HttpServletRequest req){
        try {
            System.out.println(req.getReader().readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RetJson.success(0,"成功");
    }

    @RequestMapping("/sendMsg")
    public RetJson sendMsg2(String msg){
        mqttInfoService.issue("{\"cmd\":"+"\"soundPlay\""+",\"msg\""+":"+"\""+msg+"\""+"}");

        return RetJson.success(0,"发送成功");
    }

}
