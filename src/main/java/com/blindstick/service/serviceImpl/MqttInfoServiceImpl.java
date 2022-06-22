package com.blindstick.service.serviceImpl;


import com.blindstick.config.MqttClientCallback;
import com.blindstick.config.MqttClientConnect;
import com.blindstick.service.MqttInfoService;
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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: MqttInfoServiceImpl
 * @Description: TODO
 * @Date: 2021/9/26 16:05
 */
@Service
@Slf4j
public class MqttInfoServiceImpl implements MqttInfoService {

    String ak = "F8OPULFT0UV6WTZWURGA";
    String sk = "OYJA2LYoyZvQ0VRWji9wRbRs89BPAFDsOHzZBCAS";

    @Transactional
    public int insert() {
        try {
            MqttClientConnect mqttClientConnect = new MqttClientConnect();
            mqttClientConnect.setMqttClientId("62a30d99538e623c42866677_blind_stick_0_0_2022062007");
            mqttClientConnect.setMqttClient("tcp://44cd68df75.iot-mqtts.cn-north-4.myhuaweicloud.com:1883", "62a30d99538e623c42866677_blind_stick_0_0_2022062007", "62a30d99538e623c42866677_blind_stick", "d68a678ce71f87fea7325a4483268523dcee85193a4f763c9e579aceb4a11741", true, new MqttClientCallback("62a30d99538e623c42866677_blind_stick_0_0_2022062007"));
            mqttClientConnect.sub(StringUtils.commaDelimitedListToStringArray("$oc/devices/62a30d99538e623c42866677_blind_stick/user/message"));
            MqttClientConnect.mqttClients.put("62a30d99538e623c42866677_blind_stick_0_0_2022062007", mqttClientConnect);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("新增连接失败,host--->" + "tcp://44cd68df75.iot-mqtts.cn-north-4.myhuaweicloud.com:1883");
            return 0;
        }
        log.info("新增连接成功,host--->" + "tcp://44cd68df75.iot-mqtts.cn-north-4.myhuaweicloud.com:1883");
        return 1;
    }


    @Transactional
    public int remove(String eid) {
        try {
            ConcurrentHashMap<String, MqttClientConnect> mqttClients = MqttClientConnect.mqttClients;
            MqttClientConnect mqttClientConnect = mqttClients.get(eid);
            mqttClientConnect.close();
        } catch (MqttException e) {
            e.printStackTrace();
            log.error("断开连接失败,id--->" + eid);
            return 0;
        }
        log.info("断开连接成功,id--->" + eid);
        return 1;
    }

    @Override
    public int pub(String eid, String topic, String msg) {
        ConcurrentHashMap<String, MqttClientConnect> mqttClients = MqttClientConnect.mqttClients;
        MqttClientConnect mqttClientConnect = mqttClients.get(eid);
        try {
            mqttClientConnect.pub(topic, msg);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //向默认主题下发消息
    @Override
    public int issue(String msg) {
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
        body.withMessage(msg);
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
        return 0;
    }


    @Override
    public int issue(String topic, String msg) {
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
        body.withTopic(topic);
        body.withMessage(msg);
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
        return 0;
    }


}
