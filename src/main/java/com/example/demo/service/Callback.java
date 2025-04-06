package com.example.demo.service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 常规MQTT回调函数
 *
 * @author
 * @since
 */
public class Callback implements MqttCallback {
    private static final Logger log = LoggerFactory.getLogger(Callback.class);


    public Callback() {}
    /**
     * MQTT 断开连接会执行此方法
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("断开了MQTT连接 ：{}", throwable.getMessage());
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
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        //  TODO    此处可以将订阅得到的消息进行业务处理、数据存储
        String msg = new String(message.getPayload());
        log.info("收到来自 " + topic + " 的消息：{}", msg);
        String value = msg.split(":")[1];
        //redisCache.setCacheObject("L001T01",new String(message.getPayload()),180*24, TimeUnit.HOURS);
        if(MQTTListener.SOILMOISTURE_TOPIC.equals(topic)) MQTTListener.sensorData.put(MQTTListener.SOILMOISTURE_TOPIC,value);
        if(MQTTListener.LIGHTTENSE_TOPIC.equals(topic)) MQTTListener.sensorData.put(MQTTListener.LIGHTTENSE_TOPIC,value);
    }
}
