package com.ptit.iot.controller;

import com.ptit.iot.dto.req.LoginReq;
import com.ptit.iot.dto.req.RegisterReq;
import com.ptit.iot.service.AuthService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Log4j2
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginReq req){
         log.info("user login");
         return authService.login(req);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterReq req){
        log.info("register user");
        return authService.register(req);
    }
}
