package com.ptit.iot.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class LoginResponse {

    private String token;
    private long userId;
    private String name;
    private String phone;
    private String email;
    private String role;
}