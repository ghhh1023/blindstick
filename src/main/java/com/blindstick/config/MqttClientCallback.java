package com.blindstick.config;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * MQTT回调函数
 */
@Slf4j
public class MqttClientCallback implements MqttCallback {

    /**
     * 系统的mqtt客户端id
     */
    private String mqttClientId;

    public MqttClientCallback(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }


    /**
     * MQTT 断开连接会执行此方法
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("断开了MQTT连接 ：{}", throwable.getMessage());
        log.error(throwable.getMessage(), throwable);
    }

    /**
     * publish发布成功后会执行到这里
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("发布消息成功");
    }

    /**
     * subscribe订阅后得到的消息会执行到这里
     */
    @Override
    public void messageArrived(String topic, MqttMessage message)  {
        log.info("收到来自 " + mqttClientId + "====" + topic + " 的消息：{}", new String(message.getPayload()));
        JSONObject object = JSONObject.parseObject(message.toString());

    }
}