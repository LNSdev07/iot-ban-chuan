package com.ptit.iot.service;

import com.ptit.iot.dto.req.ConfigAlertDeviceReq;
import com.ptit.iot.dto.req.ConfigAutoAlertReq;
import com.ptit.iot.dto.req.EventControlReq;
import org.springframework.http.ResponseEntity;

public interface EventControlService {

    ResponseEntity changeStatusDevice(EventControlReq req);

    ResponseEntity configAlertDevice(ConfigAlertDeviceReq req);

    ResponseEntity configAutoAlert(ConfigAutoAlertReq req);

    ResponseEntity findAllData();
}
