package com.ptit.iot.dto.req;

import lombok.Data;

@Data
public class ConfigAlertDeviceReq {
    private EventControlReq.DeviceType deviceType;
    private Integer max;
    private Integer min;
}
