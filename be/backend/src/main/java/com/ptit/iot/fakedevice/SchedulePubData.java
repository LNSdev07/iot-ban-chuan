package com.ptit.iot.fakedevice;

import com.ptit.iot.mqtt.MqttGateway;
import com.google.gson.Gson;
import com.ptit.iot.model.Data;
import lombok.AllArgsConstructor;
//import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@AllArgsConstructor
@Log4j2
public class SchedulePubData {

    private final MqttGateway mqttGateway;

    @Scheduled(fixedDelay = 2000)
    public void fakeDataDeviceSendBroker(){

        // Tạo ngẫu nhiên nhiệt độ từ -10°C đến 40°C
        float temperature = new Random().nextFloat() * 50 - 10;

        // Tạo ngẫu nhiên độ ẩm từ 0% đến 100%
        float humidity = new Random().nextFloat() * 100;
        float gas = new Random().nextFloat() * 100;

        int randomNumber = new Random().nextInt(2); // Sẽ trả về 0 hoặc 1

//        Data data = Data.builder()
//                        .gas(gas)
//                        .humidity(temperature)
//                        .temperature(humidity)
//                        .hornStatus(randomNumber)
//                        .waterStatus(randomNumber)
//                        .time(System.currentTimeMillis())
//                        .build();
//        log.info("send fake data to broker");
//        mqttGateway.sendToMqtt(new Gson().toJson(data), "dataSensorOutput");
    }
}
