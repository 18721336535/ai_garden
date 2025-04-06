package com.example.demo.service;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IotServiceImpl implements IotService {
    @Autowired
    private MQTTListener mqtTListener;
    @Override
    public void sendMsgToMqttServer(String topic, String msg) throws MqttException {
        mqtTListener.getServerClient().pub(topic, msg);
    }
}
