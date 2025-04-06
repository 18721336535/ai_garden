package com.example.demo.service;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目启动 监听主题
 *
 * @author
 * @since
 */
@Component
public class MQTTListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger log = LoggerFactory.getLogger(MQTTListener.class);
    private final MQTTConnect serverClient;
    public static Map<String,String> sensorData = new ConcurrentHashMap();
    public static final String SOILMOISTURE_TOPIC = "melon/soilMoisture";
    public static final String LIGHTTENSE_TOPIC = "melon/lightTense";
    @Autowired
    public MQTTListener(MQTTConnect serverClient) {
        this.serverClient = serverClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            serverClient.setMqttClient("melon", "password2", new Callback());
            serverClient.sub("melon/soilMoisture");//home/sensor/data    melon/Wendu
            serverClient.sub("melon/lightTense");//home/sensor/data    melon/Wendu
        } catch (MqttException e) {
            log.error(e.getMessage(), e);
        }
    }

    public MQTTConnect getServerClient() {
        return serverClient;
    }
}
