package com.ptit.iot.service.impl;

import com.ptit.iot.auth.Role;
import com.ptit.iot.auth.User;
import com.ptit.iot.constant.BaseResponse;
import com.ptit.iot.dto.req.RegisterReq;
import com.ptit.iot.dto.resp.DataResp;
import com.ptit.iot.dto.resp.UserResponse;
import com.ptit.iot.exceptions.ResErrorCode;
import com.ptit.iot.exceptions.ResException;
import com.ptit.iot.model.Data;
import com.ptit.iot.repository.UserRepository;
import com.ptit.iot.service.UserService;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails getUserDetail(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public ResponseEntity findAllUser(Pageable pageable, String keyword, Integer status, Integer role, Long begin, Long end) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Page<User> page = userRepository.findAll(makeQueryUser(keyword.trim(), status, role, begin, end), pageable);
            Long totalElements = page.getTotalElements();
            Integer totalPages = page.getTotalPages();
            List<UserResponse> dataResp = page.getContent().stream()
                    .filter(user -> role == null || CollectionUtils.containsAny(user.getAuthorities(), mapRole(Set.of(role))))
                    .map(UserResponse::new)
                    .collect(Collectors.toList());

            BaseResponse.PageResponse pageResponse = BaseResponse.PageResponse
                    .builder()
                    .totalPages(totalPages)
                    .totalElements(totalElements)
                    .data(dataResp)
                    .build();
            baseResponse = BaseResponse
                    .builder()
                    .message("success")
                    .status(HttpStatus.OK.value())
                    .data(pageResponse)
                    .build();
            return ResponseEntity.ok(baseResponse);
        }catch (Exception e){
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("failed");
            baseResponse.setData(null);
            log.error("can not get data user {}", e.getMessage());
            return ResponseEntity.badRequest().body(baseResponse);
        }
    }

    @Override
    public ResponseEntity updateUser(RegisterReq req) {
        User user = userRepository.findByUsername(req.getUsername()).orElse(null);
        if(Objects.isNull(user)){
            throw new ResException(ResErrorCode.ENTITY_NOT_EXISTS);
        }
        if(req.getPassword().isEmpty()) {
            req.setPassword(user.getPassword());
        }
        BeanUtils.copyProperties(req, user);
        user.setModifiedTime(new Date());
        Set<Role> newRoles = mapRole(new HashSet<>(req.getRole()));
        user.setAuthorities(newRoles);
        user = userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity getDetailUser(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null){
            throw new ResException(ResErrorCode.ENTITY_NOT_EXISTS);
        }
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    private Specification<User> makeQueryUser(String keyword, Integer status, Integer role, Long begin, Long end){
       return((root, query, criteriaBuilder) -> {
           List<Predicate> predicates = new ArrayList<>();
           if(Objects.nonNull(keyword)){
               log.info("keyword = {}", keyword);
               List<Predicate> orPredicates = new ArrayList<>();
               orPredicates.add(criteriaBuilder.like(root.get(User.User_.USER_NAME), "%" + keyword.trim() + "%"));
               orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(User.User_.NAME)),
                       "%" + keyword.trim().toLowerCase()+"%"));
               predicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])));
           }
           if(Objects.nonNull(status)){
               predicates.add(criteriaBuilder.equal(root.get(User.User_.STATUS), status));
           }
           if(Objects.nonNull(begin) && Objects.nonNull(end)){
               predicates.add(criteriaBuilder.between(root.get(User.User_.MODIFIED_TIME),
                       new Date(begin), new Date(end)));
           }
           if(Objects.nonNull(role)){
//                Set<Role> rolesSearch = mapRole(Set.of(role));
//               for (Role roleTmp : rolesSearch) {
//                   Join<User, Role> authoritiesJoin = root.join(User.User_.AUTHOR);
//                   predicates.add(criteriaBuilder.isMember(roleTmp, authoritiesJoin));
//               }
           }
           return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
       });
    }

    public Set<Role> mapRole(Set<Integer> roles) {
        Set<Role> setRoles = new HashSet<>();
        for (Integer intRole : roles) {
            switch (intRole) {
                case 2 -> setRoles.add(new Role(Role.SUPER_ADMIN));
                case 1 -> setRoles.add(new Role(Role.ADMIN));
                case 0 -> setRoles.add(new Role(Role.VISITOR));
                default -> log.warn("role not support for search");
            }
        }
        return setRoles;
    }
}
