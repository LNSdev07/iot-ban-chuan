package com.ptit.iot.model;

import lombok.Data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_action")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * thoi gian tao
     */
    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "description")
    private String description;

}
