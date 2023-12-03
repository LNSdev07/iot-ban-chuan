package com.ptit.iot.dto.resp;

import lombok.Data;

@Data
public class BaseResponse<T> {
    private String message;
    private Integer httpCode;
    private T data;
}
