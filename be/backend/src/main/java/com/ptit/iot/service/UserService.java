package com.ptit.iot.service;

import com.ptit.iot.dto.req.RegisterReq;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails getUserDetail(Long userId);

    ResponseEntity findAllUser(Pageable pageable, String keyword, Integer status, Integer role, Long begin, Long end);

    ResponseEntity updateUser(RegisterReq req);

    ResponseEntity getDetailUser(String username);
}
