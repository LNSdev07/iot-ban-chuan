package com.ptit.iot.service.impl;

import com.ptit.iot.auth.JwtUtil;
import com.ptit.iot.auth.Role;
import com.ptit.iot.auth.User;
import com.ptit.iot.dto.req.LoginReq;
import com.ptit.iot.dto.req.RegisterReq;
import com.ptit.iot.dto.resp.LoginResponse;
import com.ptit.iot.exceptions.ResErrorCode;
import com.ptit.iot.exceptions.ResException;
import com.ptit.iot.repository.UserRepository;
import com.ptit.iot.service.AuthService;
import com.ptit.iot.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public ResponseEntity login(LoginReq req) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    req.getUsername(), req.getPassword()
                            )
                    );
            User user = (User) authenticate.getPrincipal();
            return makeLoginResponse(user);
        }
        catch (Exception e){
             throw new ResException(ResErrorCode.BAD_REQUEST, "error login");
        }

    }

    @Override
    public ResponseEntity register(RegisterReq req) {
        validateRequestRegister(req);
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setToken(null);
        user.setName(req.getName());
        Set<Role> roleSet = userService.mapRole(new HashSet<>(req.getRole()));
        user.setAuthorities(roleSet);
        user.setStatus(req.getStatus());
        user.setCreatedTime(new Date());
        user.setModifiedTime(new Date());
        user.setPhone(user.getPhone());
        user.setMail(user.getMail());
        user = userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    private ResponseEntity makeLoginResponse(User entity) {
        entity = userRepository.findById(entity.getId()).orElse(null);
        if(entity ==null){
            throw new ResException(ResErrorCode.ENTITY_NOT_EXISTS);
        }
        if(entity.getStatus() ==0){
            throw new ResException(ResErrorCode.USER_INACTIVE);
        }
        LoginResponse loginResponse = new LoginResponse();
        Map<String, Object> info = new HashMap<>();
        info.put("phone",entity.getPhone());
        info.put("email", entity.getMail());
        info.put("user_id", entity.getId());
        String token = jwtUtil.generateToken(entity.getId(),
                entity.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toList()),
                info);
        loginResponse.setToken(token);
        loginResponse.setName(entity.getName());
        loginResponse.setUserId(entity.getId());
        StringBuilder roleSb = new StringBuilder();
        entity.getAuthorities().stream().forEach( item ->{
            roleSb.append(item.getAuthority()).append(", ");
        });
        loginResponse.setRole(roleSb.substring(0, roleSb.length()-2));

        // set token for user
        entity.setToken(token);
        userRepository.save(entity);
        return ResponseEntity.ok(loginResponse);
    }

    public void validateRequestRegister(RegisterReq req){
//         validatePhone(req.getPhone());
//         validateEmail(req.getEmail());
         validatePassword(req.getPassword());
         if(userRepository.existsByUsername(req.getUsername())){
             throw new ResException(ResErrorCode.ENTITY_EXISTED, "username existed");
         }
//         if(req.getPhone() != null && userRepository.existsByPhone(req.getPhone())){
//             throw new ResException(ResErrorCode.ENTITY_EXISTED, "phone existed");
//         }
//         if(req.getEmail() != null && userRepository.existsByMail(req.getEmail())){
//             throw new ResException(ResErrorCode.ENTITY_EXISTED, "mail existed");
//         }
    }

    public void validateEmail(String email) {
        if(email != null) {
            Pattern pattern = Pattern.compile("^[0-9]+$");
            Matcher matcher = pattern.matcher(email);
            if(!matcher.matches()){
                log.error("The [{}] email is malformed.", email);
                throw new ResException(ResErrorCode.INVALID_EMAIL_FORMAT);
            }
        }
    }

    public void validatePhone(String phone) {
        if(phone != null) {
            Pattern pattern = Pattern.compile("^(.+)@(\\\\S+)$");
            Matcher matcher = pattern.matcher(phone);
            if(!matcher.matches()){
                log.error("The [{}] mobile number is malformed.", phone);
                throw new ResException(ResErrorCode.INVALID_MOBILE_NUMBER_FORMAT);
            }
        }
    }

    public void validatePassword(String password){
        if(password == null || password.length() < 6){
          log.info("password invalid");
            throw new ResException(ResErrorCode.INVALID_USER_PASS);
        }
    }
}
