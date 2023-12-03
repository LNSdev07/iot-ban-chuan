package com.ptit.iot.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payload {

    private Long userId;
    private String phone;
    private String email;
    private String token;
}
