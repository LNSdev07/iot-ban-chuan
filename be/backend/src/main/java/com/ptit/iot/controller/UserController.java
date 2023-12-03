package com.ptit.iot.controller;

import com.ptit.iot.auth.CheckPermissionUser;
import com.ptit.iot.auth.Payload;
import com.ptit.iot.auth.Role;
import com.ptit.iot.constant.Definition;
import com.ptit.iot.dto.req.RegisterReq;
import com.ptit.iot.exceptions.ResErrorCode;
import com.ptit.iot.exceptions.ResException;
import com.ptit.iot.service.AuthService;
import com.ptit.iot.service.UserService;
import com.ptit.iot.utils.DaoUtils;
import com.ptit.iot.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CheckPermissionUser checkPermissionUser;

    @GetMapping("/find-user")
    public ResponseEntity findAllUser(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "sortBy", required = false, defaultValue = "modifiedTime") String sortBy,
                                      @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                      @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                      @RequestParam(value = "status", required = false) Integer status,
                                      @RequestParam(value = "role", required = false) Integer role,
                                      @RequestParam(value = "begin", required = false) Long begin,
                                      @RequestParam(value = "end", required = false) Long end,
                                      @RequestAttribute(name = Definition.PAYLOAD, required = false) Payload payload) {
        ValidateUtils.validatePayload(payload);
        if (!checkPermissionUser.isPermissionUser(payload.getUserId(), Set.of(
                new Role(Role.ADMIN),
                new Role(Role.SUPER_ADMIN)))
        ) {
            throw new ResException(ResErrorCode.PERMISSION_DENIED);
        }
        Pageable pageable = DaoUtils.buildPageable(pageIndex, pageSize, sortBy, direction);
        return userService.findAllUser(pageable, keyword, status, role, begin, end);
    }

    @PostMapping("/create-user")
    public ResponseEntity createUser(@RequestBody RegisterReq req,
                                     @RequestAttribute(name = Definition.PAYLOAD, required = false) Payload payload) {
        ValidateUtils.validatePayload(payload);
        if (!checkPermissionUser.isPermissionUser(payload.getUserId(), Set.of(new Role(Role.SUPER_ADMIN)))
        ) {
            throw new ResException(ResErrorCode.PERMISSION_DENIED);
        }
        return authService.register(req);
    }

    @PutMapping("/update-user")
    public ResponseEntity updateUser(@RequestBody RegisterReq req,
                                     @RequestAttribute(name = Definition.PAYLOAD, required = false) Payload payload){
        ValidateUtils.validatePayload(payload);
        if (!checkPermissionUser.isPermissionUser(payload.getUserId(), Set.of(new Role(Role.SUPER_ADMIN)))
        ) {
            throw new ResException(ResErrorCode.PERMISSION_DENIED);
        }
        if(Objects.nonNull(req.getPassword()) && !req.getPassword().isEmpty()){
            req.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        return userService.updateUser(req);
    }

    @GetMapping("/get-user")
    public  ResponseEntity getUserDetail(@RequestParam("username") String username,
                                         @RequestAttribute(name = Definition.PAYLOAD, required = false) Payload payload){
        ValidateUtils.validatePayload(payload);
        if (!checkPermissionUser.isPermissionUser(payload.getUserId(), Set.of(new Role(Role.SUPER_ADMIN)))
        ) {
            throw new ResException(ResErrorCode.PERMISSION_DENIED);
        }
        return userService.getDetailUser(username);
    }

}
