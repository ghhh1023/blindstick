package com.blindstick.service;



import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttInfoService {



    int insert() throws MqttException;

    int remove(String id) throws MqttException;

    int pub(String eid,String topic,String msg);

    int issue(String msg);

    int issue(String topic,String msg);
}
