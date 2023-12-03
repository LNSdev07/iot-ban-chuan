package com.ptit.iot.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.processing.Generated;

@Data
@Entity
@Table(name = "user", schema = "public")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "token")
    @JsonIgnore
    private String token;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "name", nullable = false)
    private String name;

    @Convert(converter = SetRoleJpaConverter.class)
    @Column(name = "roles")
    private Set<Role> authorities = new HashSet<>();

    @Column(name = "phone")
    private String phone;

    @Column(name = "mail")
    private String mail;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "modified_time")
    private Date modifiedTime;


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }



    @Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
    @StaticMetamodel(User.class)
    public abstract class User_{
        public static final String ID = "id";
        public static final String USER_NAME = "username";
        public static final String STATUS = "status";
        public static final String AUTHOR = "authorities";
        public static final String CREATED_TIME = "createdTime";
        public static final String MODIFIED_TIME = "modifiedTime";
        public static final String PHONE = "phone";
        public static final String NAME = "name";
    }

}
