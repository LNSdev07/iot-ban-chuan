package com.ptit.iot.service.impl;

import com.ptit.iot.constant.SystemConstant;
import com.ptit.iot.dto.DataDto;
import com.ptit.iot.model.Action;
import com.ptit.iot.repository.ActionRepository;
import com.ptit.iot.service.ActionService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ActionServiceImpl implements ActionService {
    Logger logger = LoggerFactory.getLogger(ActionServiceImpl.class);
    private final ActionRepository actionRepository;
    private final EntityManager entityManager;
    @Autowired
    public ActionServiceImpl(ActionRepository actionRepository,
                             EntityManager entityManager)
    {
        this.actionRepository = actionRepository;
        this.entityManager = entityManager;
    }

    @Autowired

    @Override
    public List<Action> getAllActions() {
        return this.actionRepository.getAllAction();
    }
}
