package com.ptit.iot.dto.resp;

import com.ptit.iot.auth.Role;
import com.ptit.iot.auth.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String phone;
    private String name;
    private String mail;
    private Integer status;
    private String role;
    private Long createdDate;
    private Long modifiedDate;

    public UserResponse(User user){
        BeanUtils.copyProperties(user, this);
        this.createdDate = user.getCreatedTime().getTime();
        this.modifiedDate = user.getModifiedTime().getTime();
        this.role = getRoleToString(user.getAuthorities());
    }

    private String getRoleToString(Set<Role> set){
        return set.stream().map(Role::getAuthority).collect(Collectors.joining(", "));
    }
}
