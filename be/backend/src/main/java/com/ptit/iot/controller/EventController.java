package com.ptit.iot.controller;

import com.ptit.iot.auth.CheckPermissionUser;
import com.ptit.iot.auth.Payload;
import com.ptit.iot.auth.Role;
import com.ptit.iot.constant.Definition;
import com.ptit.iot.dto.req.ConfigAlertDeviceReq;
import com.ptit.iot.dto.req.ConfigAutoAlertReq;
import com.ptit.iot.dto.req.EventControlReq;
import com.ptit.iot.exceptions.ResErrorCode;
import com.ptit.iot.exceptions.ResException;
import com.ptit.iot.service.EventControlService;
import com.ptit.iot.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/event-control")
@CrossOrigin("*")
public class EventController {

    @Autowired
    private EventControlService eventControlService;

    @Autowired
    private CheckPermissionUser checkPermissionUser;

    @PostMapping("/change-status-sensor")
    public ResponseEntity changeStatusDevice(@RequestBody EventControlReq req,
                                             @RequestAttribute(name = Definition.PAYLOAD, required = false) Payload payload){
        ValidateUtils.validatePayload(payload);
        if(!checkPermissionUser.isPermissionUser(payload.getUserId(), Set.of(new Role(Role.ADMIN), new Role(Role.SUPER_ADMIN)))){
            throw new ResException(ResErrorCode.PERMISSION_DENIED);
        }
        return eventControlService.changeStatusDevice(req);
    }

    @PostMapping("/config-alert-for-device")
    public ResponseEntity configAlertForDevice(@RequestBody ConfigAlertDeviceReq req,
                                               @RequestAttribute(name = Definition.PAYLOAD, required = false) Payload payload){
        ValidateUtils.validatePayload(payload);
        if(!checkPermissionUser.isPermissionUser(payload.getUserId(), Set.of(new Role(Role.ADMIN), new Role(Role.SUPER_ADMIN)))){
            throw new ResException(ResErrorCode.PERMISSION_DENIED);
        }
        return eventControlService.configAlertDevice(req);
    }

    @GetMapping("/find-all-action")
    public ResponseEntity findAllAction( @RequestAttribute(name = Definition.PAYLOAD, required = false) Payload payload){
        ValidateUtils.validatePayload(payload);
        if(!checkPermissionUser.isPermissionUser(payload.getUserId(), Set.of(new Role(Role.ADMIN), new Role(Role.SUPER_ADMIN)))){
            throw new ResException(ResErrorCode.PERMISSION_DENIED);
        }
        return eventControlService.findAllData();
    }

    @PostMapping("/config-automatic-alert")
    public ResponseEntity configAutomaticAlert(@RequestBody ConfigAutoAlertReq req,
                                               @RequestAttribute(name = Definition.PAYLOAD, required = false) Payload payload){
        ValidateUtils.validatePayload(payload);
        if(!checkPermissionUser.isPermissionUser(payload.getUserId(), Set.of(new Role(Role.ADMIN), new Role(Role.SUPER_ADMIN)))){
            throw new ResException(ResErrorCode.PERMISSION_DENIED);
        }
        return eventControlService.configAutoAlert(req);
    }

}
