package com.ptit.iot.configuration;

import com.ptit.iot.exceptions.ResErrorCode;
import com.ptit.iot.exceptions.ResException;
import com.ptit.iot.utils.ErrorResponse;
import com.ptit.iot.utils.ResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResException.class)
    public Object handleAppException(HttpServletRequest request, ResException re)
            throws IOException {
        String id = exitsCode(re.getCode()) ? "" : getId();
        ErrorResponse<Object> errorResponse = new ErrorResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setCode(re.getCode());
        responseStatus.setLabel(re.getLabel() + id);
        responseStatus.setMessage(re.getMessage() + id);
        errorResponse.setStatus(responseStatus);
        errorResponse.setData(re.getData());

        log.error("Error: ", re);
        return new ResponseEntity<>(errorResponse, re.getStatus());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public Object handleAccessDeniedException( AccessDeniedException re)
            throws IOException {
        ErrorResponse<Object> errorResponse = new ErrorResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setCode(ResponseStatus.PERMISSION_DENIED_CODE);
        responseStatus.setLabel(ResponseStatus.PERMISSION_DENIED_LABEL);
        responseStatus.setMessage(ResponseStatus.PERMISSION_DENIED_MESSAGES);
        errorResponse.setStatus(responseStatus);

        log.error("Error: ", re);

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value = Exception.class)
    public Object handleException(HttpServletRequest request, Exception re)
            throws IOException {
        String id = getId();
        ErrorResponse<Object> errorResponse = new ErrorResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setCode(ResErrorCode.GENERAL_ERROR.code());
        responseStatus.setLabel(ResErrorCode.GENERAL_ERROR.label() + id);
        responseStatus.setMessage("Có lỗi xảy ra, xin vui lòng thử lại sau ít phút" + id);
        errorResponse.setStatus(responseStatus);

        log.error("Error: ", re);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Object handleMissingParamException(HttpServletRequest request, MissingServletRequestParameterException re)
            throws IOException {
        String id = getId();
        ErrorResponse<Object> errorResponse = new ErrorResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        responseStatus.setCode(ResErrorCode.MISS_PARAM.code());
        responseStatus.setLabel(ResErrorCode.MISS_PARAM.label() + id);
        responseStatus.setMessage("Miss params" + id);
        errorResponse.setStatus(responseStatus);

        log.error("Error: ", re);

        return new ResponseEntity<>(errorResponse, ResErrorCode.MISS_PARAM.status());
    }

    private String getId() {
        return " (" + RandomStringUtils.randomAlphabetic(6) + ")";
    }

    public boolean exitsCode(String code){
        List<String> resCode = Arrays.asList(ResErrorCode.ACCOUNT_BLOCKED.code(), ResErrorCode.PASS_NOT_EQUAL.code(), ResErrorCode.SAME_PASSWORD.code()
                                            , ResErrorCode.OLD_PASSWORD_NOT_VALID.code(), ResErrorCode.INVALID_USER_PASS.code());
        if(resCode.contains(code))
            return true;
        return false;
    }

}
