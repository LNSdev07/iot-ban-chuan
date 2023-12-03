package com.ptit.iot.restcontroller;

import com.ptit.iot.model.Action;
import com.ptit.iot.service.ActionService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/action/")
public class ActionRestController {
    @Autowired
    private ActionService actionService;

}
