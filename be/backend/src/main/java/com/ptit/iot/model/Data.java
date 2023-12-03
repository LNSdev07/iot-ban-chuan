package com.ptit.iot.model;

import jakarta.persistence.*;
import jakarta.persistence.metamodel.StaticMetamodel;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tbl_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "temperature" )
    private Float temperature;
    @Column(name = "humidity")
    private Float humidity;
    @Column(name = "gas_sensor")
    private Float gasSensor;
    @Column(name = "flame_sensor")
    private Float flameSensor;
    @Column(name = "led_state")
    private Integer ledState;
    @Column(name = "buzzer_state")
    private Integer buzzerState;
    @Column(name = "time")
    private Date time;

    @StaticMetamodel(Data.class)
    public abstract class Data_{
        public static final String ID_DATA = "id";
        public static final String TEMPERATURE ="temperature";
        public static final String HUMIDITY = "humidity";
        public static final String GAS = "gasSensor";
        public static final String WATER_STATUS ="ledState";
        public static final String HORN_STATUS ="buzzerState";
        public static final String TIME = "time";
    }
}
