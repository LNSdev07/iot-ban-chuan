package com.ptit.iot.service;

import com.ptit.iot.dto.req.LoginReq;
import com.ptit.iot.dto.req.RegisterReq;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity login(LoginReq req);

    ResponseEntity register(RegisterReq req);
}
