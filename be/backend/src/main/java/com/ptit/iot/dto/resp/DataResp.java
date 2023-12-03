package com.ptit.iot.dto.resp;

import com.ptit.iot.model.Data;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@lombok.Data
public class DataResp {
    private Long id;
    private Float temperature;
    private Float humidity;
    private Float gasSensor;
    private Float flameSensor;
    private Integer ledState;
    private Integer buzzerState;
    private String time;

    public DataResp(Data data){
        BeanUtils.copyProperties(data, this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.time = simpleDateFormat.format(data.getTime());
    }
}
