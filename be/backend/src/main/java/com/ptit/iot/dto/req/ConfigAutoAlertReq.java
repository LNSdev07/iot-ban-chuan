package com.ptit.iot.dto.req;

import lombok.Data;

@Data
public class ConfigAutoAlertReq {
   private String alert;
   private boolean value;
}
