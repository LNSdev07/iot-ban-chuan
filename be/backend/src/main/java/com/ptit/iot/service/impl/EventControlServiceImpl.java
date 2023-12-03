package com.ptit.iot.service.impl;

import com.google.gson.Gson;
import com.ptit.iot.constant.BaseResponse;
import com.ptit.iot.dto.req.ConfigAlertDeviceReq;
import com.ptit.iot.dto.req.ConfigAutoAlertReq;
import com.ptit.iot.dto.req.EventControlReq;
import com.ptit.iot.model.Action;
import com.ptit.iot.model.InformationDevice;
import com.ptit.iot.mqtt.MqttGateway;
import com.ptit.iot.repository.ActionRepository;
import com.ptit.iot.repository.InformationDeviceRepository;
import com.ptit.iot.service.EventControlService;
import com.ptit.iot.utils.DaoUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class EventControlServiceImpl implements EventControlService {


    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private  MqttGateway mqttGateway;

    @Autowired
    private InformationDeviceRepository informationDeviceRepository;

    @Override
    public ResponseEntity changeStatusDevice(EventControlReq req) {
        log.info("request success: req = {}", req);
        String jsonControl  = new Gson().toJson(req);
        mqttGateway.sendToMqtt(jsonControl, "controlDevice");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity configAlertDevice(ConfigAlertDeviceReq req) {
        log.info("request success: req = {}", req);
        InformationDevice device = null;
        switch (req.getDeviceType()){
            case TEMPERATURE -> {
                device = informationDeviceRepository.findById(2L).orElse(new InformationDevice());
            }
            case HUMIDITY -> {
                device = informationDeviceRepository.findById(3L).orElse(new InformationDevice());
            }
            case GAS -> {
                device = informationDeviceRepository.findById(4L).orElse(new InformationDevice());
            }
        }
        assert device != null;
        device.setMin(Long.valueOf(req.getMin()));
        device.setMax(Long.valueOf(req.getMax()));
        device.setUpdatedTime(new Date());
        informationDeviceRepository.save(device);
        String jsonControl = new Gson().toJson(req);
        mqttGateway.sendToMqtt(jsonControl, "minMaxDevice");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity configAutoAlert(ConfigAutoAlertReq req) {
        log.info("request = "+ req);
        String jsonControl = new Gson().toJson(req);
        // config ban đầu cho các giá trị device
        List<InformationDevice> deviceList = informationDeviceRepository.findAll();
        for(InformationDevice device: deviceList){
            ConfigAlertDeviceReq reqDevice = new ConfigAlertDeviceReq();
            Integer min = Integer.parseInt(device.getMin().toString());
            Integer max = Integer.parseInt(device.getMax().toString());
            reqDevice.setMin(min);
            reqDevice.setMax(max);
            if(device.getId().equals(2L)){
               reqDevice.setDeviceType(EventControlReq.DeviceType.TEMPERATURE);
            }
            else if(device.getId().equals(3L)){
                reqDevice.setDeviceType(EventControlReq.DeviceType.HUMIDITY);
            }
            else if(device.getId().equals(4L)){
                reqDevice.setDeviceType(EventControlReq.DeviceType.GAS);
            }
            String jsonControlMinMax = new Gson().toJson(reqDevice);
            mqttGateway.sendToMqtt(jsonControlMinMax, "minMaxDevice");
        }
        mqttGateway.sendToMqtt(jsonControl, "automaticControl");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity findAllData() {
        BaseResponse response = new BaseResponse();
       try{
           Pageable pageable = DaoUtils.buildPageable(1, 100, "updatedTime", "DESC");
           Page<InformationDevice> page = informationDeviceRepository.findAll(pageable);
           response.setData(page.getContent());
           response.setStatus(200);
           response.setMessage("success");
           return new ResponseEntity(response, HttpStatus.OK);
       }catch (Exception e){
           response.setData(null);
           response.setStatus(HttpStatus.BAD_REQUEST.value());
           response.setMessage("failed");
           return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
       }
    }
}
