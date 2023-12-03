package com.ptit.iot.auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    public static final String ADMIN = "ADMIN";
    public static final String VISITOR = "USER";
    public static final String SUPER_ADMIN ="SUPER_ADMIN";

    private String authority;

}