package com.ptit.iot.utils;

import com.ptit.iot.auth.Payload;
import com.ptit.iot.exceptions.ResErrorCode;
import com.ptit.iot.exceptions.ResException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class ValidateUtils {
    public static void validatePayload(Payload payload){
        if(payload ==null || StringUtils.isBlank(payload.getToken()) || payload.getUserId() ==null){
            log.warn("payload invalid");
            throw new ResException(ResErrorCode.INVALID_REQUEST);
        }
    }
}
