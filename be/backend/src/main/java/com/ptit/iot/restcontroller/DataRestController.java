package com.ptit.iot.restcontroller;

import com.ptit.iot.service.DataService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataRestController {
    private final DataService dataService;

    @Autowired
    public DataRestController(DataService dataService)
    {
        this.dataService = dataService;
    }

}
