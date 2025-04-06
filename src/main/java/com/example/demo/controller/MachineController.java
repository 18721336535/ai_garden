package com.example.demo.controller;

import com.example.demo.dto.AjaxResult;
import com.example.demo.dto.EnvDto;
import com.example.demo.service.IotService;
import com.example.demo.service.MQTTListener;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage/iot/machine")
public class MachineController{
    private static final Logger log = LoggerFactory.getLogger(MachineController.class);
    @Autowired
    IotService iotService;
    @GetMapping(value = "/{cmd}")
    public AjaxResult getInfo(@PathVariable("cmd") String cmd) throws MqttException {
        log.info("收到来自ui 的cmd  ：{}", cmd);
        iotService.sendMsgToMqttServer("machine/control",cmd);
        return AjaxResult.success();
    }


    @GetMapping(value = "/getSensorData")
    public AjaxResult getTemperature() {
        EnvDto envdto = new EnvDto();
        String  soilMoisture =MQTTListener.sensorData.get(MQTTListener.SOILMOISTURE_TOPIC);
        envdto.setSoilMoisture(StringUtils.isNotEmpty(soilMoisture) ? soilMoisture.replaceAll("\\\\","").replaceAll("\"",""): "1.5");
        String  lightIntensity =MQTTListener.sensorData.get(MQTTListener.LIGHTTENSE_TOPIC);
        envdto.setLightIntensity(StringUtils.isNotEmpty(lightIntensity) ? lightIntensity.replaceAll("\\\\","").replaceAll("\"",""): "3");
        return AjaxResult.success(envdto);
    }

}
