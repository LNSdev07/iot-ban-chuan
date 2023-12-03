package com.ptit.iot.auth;

import com.ptit.iot.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
public class CheckPermissionUser {

    @Autowired
    private UserRepository userRepo;

    public boolean isPermissionUser(Long userId, Set<Role> setRoles){
        Set<Role> roles = userRepo.findById(userId).orElse(new User()).getAuthorities();
        for(Role role: setRoles){
            if(roles.contains(role)) return true;
        }
        log.warn("user not permission!");
        return false;
    }
}
