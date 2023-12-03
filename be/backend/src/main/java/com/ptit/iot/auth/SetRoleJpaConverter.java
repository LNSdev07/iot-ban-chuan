package com.ptit.iot.auth;

import com.ptit.iot.utils.JsonUtils;
import jakarta.persistence.AttributeConverter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetRoleJpaConverter implements AttributeConverter<Set<Role>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Role> meta) {
        return JsonUtils.objectToJson(meta).orElse(null);
    }

    @Override
    public Set<Role> convertToEntityAttribute(String dbData) {
        if ("null".equalsIgnoreCase(dbData)) return Collections.emptySet();
        try {
            List<Role> roles = JsonUtils.jsonToListObject(dbData, Role.class);
            return new HashSet<>(roles);
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }
}