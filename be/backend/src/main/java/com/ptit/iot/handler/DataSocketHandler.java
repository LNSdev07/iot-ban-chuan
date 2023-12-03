package com.ptit.iot.handler;

import com.ptit.iot.dto.DataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class DataSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(DataSocketHandler.class);
    private final SimpMessageSendingOperations messageTemplates;
    @Autowired
    public DataSocketHandler(SimpMessageSendingOperations messageTemplates)
    {
        this.messageTemplates = messageTemplates;
    }

    @EventListener
    public void handlerDataSocketConnection(SessionConnectedEvent event)
    {
        logger.info("Đã nhận kết nối từ client");
    }

    public void publishData(DataDto newData)
    {
        this.messageTemplates.convertAndSend("/topic/data-receive", newData);
    }

}
