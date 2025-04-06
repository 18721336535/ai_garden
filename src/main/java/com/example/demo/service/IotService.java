package com.example.demo.service;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IotService {
    public void sendMsgToMqttServer(String topic, String msg) throws MqttException;
}
