package com.ptit.iot.dto.req;

import com.ptit.iot.auth.Role;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RegisterReq {
    private String name;
    private String username;
    private String password;
    private Integer status;
    private List<Integer> role;
}
