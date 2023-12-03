package com.ptit.iot.service;


import com.ptit.iot.model.Action;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActionService {
    List<Action> getAllActions();
}
