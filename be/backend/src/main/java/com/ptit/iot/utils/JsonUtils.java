package com.ptit.iot.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);


    public static <R> Optional<R> jsonToObject(String jsonString, Class<R> returnClass) {
        if (jsonString == null)
            return Optional.empty();
        try {
            return Optional.of(objectMapper.readValue(jsonString, returnClass));
        } catch (Exception e) {
            log.error("Parse json error: ", e);
            return Optional.empty();
        }
    }


    public static <T> Optional<String> objectToJson(T object) {
        try {
            return Optional.of(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            log.error("Parse json error: ", e);
//            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static <R> List<R> jsonToListObject(String jsonString, Class<R> returnClass) {
        if (StringUtils.isEmpty(jsonString))
            return Collections.emptyList();
        try {
            Class<R[]> arrayClass = (Class<R[]>) Class.forName("[L" + returnClass.getName() + ";");
            return Arrays.asList(objectMapper.readValue(jsonString, arrayClass));
        } catch (Exception e) {
            log.error("Parse json to Object error: ", e);
            return Collections.emptyList();
        }

    }


}
