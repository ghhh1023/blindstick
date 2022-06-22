package com.blindstick.controller;

import com.blindstick.common.RetJson;
import com.blindstick.netty.handler.channel.ConnectManager;
import com.blindstick.service.MqttInfoService;
import com.blindstick.utils.HexUtil;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.CreateMessageRequest;
import com.huaweicloud.sdk.iotda.v5.model.CreateMessageResponse;
import com.huaweicloud.sdk.iotda.v5.model.DeviceMessageRequest;
import com.huaweicloud.sdk.iotda.v5.region.IoTDARegion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

@RestController
public class TestController {
    @Autowired
    private MqttInfoService mqttInfoService;

    @RequestMapping("/test")
    public String test(){
        List<String> addrs = ConnectManager.getDeviceAll();

        for (String addr : addrs) {
            String msg= "你!好";

            String message1="{\"cmd\":"+"\"findCane\""+"}";
            byte[] bytes=null;
            try {
                bytes=message1.getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //下发指令
            System.out.println(message1);
            ConnectManager.sendMessage(addr, HexUtil.bytes2HexString(bytes));
//            ConnectManager.sendMessage(addr,msg);
        }
        return "111";
    }

    @RequestMapping("/sendMsg0")
    public RetJson sendMsg(String msg){
        List<String> addrs = ConnectManager.getDeviceAll();
        for (String addr : addrs) {
            String message1="{\"cmd\":"+"\"soundPlay\""+",\"msg\""+":"+"\""+msg+"\""+"}";
            byte[] bytes=null;
            try {
                bytes=message1.getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //下发指令
            System.out.println(message1);
            ConnectManager.sendMessage(addr, HexUtil.bytes2HexString(bytes));
//            ConnectManager.sendMessage(addr,msg);
        }
        return RetJson.success(0,"发送成功");
    }
    @RequestMapping("/sendMsg1")
    public RetJson sendMsg1(){
        List<String> addrs = ConnectManager.getDeviceAll();
        for (String addr : addrs) {
            String message1="{\"cmd\":"+"\"soundPlay\""+",\"msg\""+":"+"\""+"你好"+"\""+"}";
            byte[] bytes=null;
            try {
                bytes=message1.getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //下发指令
            System.out.println(message1);
            ConnectManager.sendMessage(addr, HexUtil.bytes2HexString(bytes));
//            ConnectManager.sendMessage(addr,msg);
        }
        return RetJson.success(0,"发送成功");
    }
    @RequestMapping("/sendMsg2")
    public RetJson sendMsg2(){
        mqttInfoService.issue("{\"cmd\":"+"\"soundPlay\""+",\"msg\""+":"+"\""+"你好"+"\""+"}");

        return RetJson.success(0,"发送成功");
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
        String ak = "F8OPULFT0UV6WTZWURGA";
        String sk = "OYJA2LYoyZvQ0VRWji9wRbRs89BPAFDsOHzZBCAS";

        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        IoTDAClient client = IoTDAClient.newBuilder()
                .withCredential(auth)
                .withRegion(IoTDARegion.valueOf("cn-north-4"))
                .build();
        CreateMessageRequest request = new CreateMessageRequest();
        request.withDeviceId("62a30d99538e623c42866677_blind_stick");
        DeviceMessageRequest body = new DeviceMessageRequest();
        body.withTopic("message");
        body.withMessage("{\"con\":123}");
        request.withBody(body);
        try {
            CreateMessageResponse response = client.createMessage(request);
            System.out.println(response.toString());
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            e.printStackTrace();
            System.out.println(e.getHttpStatusCode());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorMsg());
        }
    }
}
