package com.ptit.iot.dto;

import com.ptit.iot.constant.SystemConstant;
import com.ptit.iot.model.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataDto {
    private Long id;
    private Float temperature;
    private Float humidity;
    private Float gasSensor;
    private Float flameSensor;
    private Integer ledState;
    private Integer buzzerState;
    private String time;

    public DataDto(Data data)
    {
        BeanUtils.copyProperties(data, this);
        if(!ObjectUtils.isEmpty(data))
        {
            this.time = SystemConstant.sdfNormal.format(new Date());
        }
    }
}
