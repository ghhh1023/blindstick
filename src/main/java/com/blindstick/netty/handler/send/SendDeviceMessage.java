//package com.blindstick.netty.handler.send;
//
//
//
//import com.blindstick.netty.handler.channel.ConnectManager;
//import com.blindstick.utils.HexUtil;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//
//@Component
//@EnableScheduling // 1.开启定时任务
//@EnableAsync // 2.开启多线程
//public class SendDeviceMessage {
//
//
//    @Async//定时任务
//    @Scheduled(cron = "0 0/2 0/1 * * ?") // 每1分钟执行一次
//    public void sendMessage() throws InterruptedException {
//
//        List<String> addrs = ConnectManager.getDeviceAll();
//
//        for (String addr : addrs) {
//            String result = "hello";
//            //下发指令
//            ConnectManager.sendMessage(addr, HexUtil.convertStringToHex(result));
//        }
//
//
//    }
//
//
//}
