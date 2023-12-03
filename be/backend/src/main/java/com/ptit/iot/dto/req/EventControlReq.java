package com.ptit.iot.dto.req;

import lombok.Data;

@Data
public class EventControlReq
{
    private DeviceAlert deviceAlert;
    private Boolean status;

    public enum DeviceType{
        TEMPERATURE, HUMIDITY, GAS
    }
}


enum DeviceAlert{
    LED, BUZZER
}
