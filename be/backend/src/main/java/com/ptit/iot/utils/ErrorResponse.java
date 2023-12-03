package com.ptit.iot.utils;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ErrorResponse<T> implements Serializable {
    private ResponseStatus status;
    private T data;

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public void setData(T data) {
        this.data = data;
    }
}