package com.ptit.iot.controller;

import com.ptit.iot.mqtt.MqttGateway;
import com.ptit.iot.service.ActionService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
public class DataSocketController {
    private final ActionService actionService;
    private final MqttGateway mqttGateway;;

    @Autowired
    public DataSocketController(ActionService actionService,
                                MqttGateway mqttGateway)
    {
        this.actionService = actionService;
        this.mqttGateway = mqttGateway;
    }

    @MessageMapping("/data.led-control")
    @SendTo("topic/control-led-result")
    public ResponseEntity<Object> sendLedControl(@Payload String ledControllMessage)
    {
//        System.out.println(ledControllMessage);
//        JsonObject jsonObject = new Gson().fromJson(ledControllMessage, JsonObject.class);
        //lưu sự kiện bấm nút này
//        this.actionService.saveAction(jsonObject);
        log.error("receive action value = {}", ledControllMessage);

//        this.mqttGateway.sendToMqtt(ledControllMessage, "ledControlInput");
        return ResponseEntity.ok("ok");
    }
}
