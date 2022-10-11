package com.korbiak.service.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "weather_condition")
@Data
public class WeatherConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "weather_id")
    private double weatherId;

    @Column(name = "main")
    private String main;

    @Column(name = "description")
    private String description;

    @Column(name = "icon")
    private String icon;

    @Column(name = "wind_speed")
    private double windSpeed;

    @Column(name = "date_time")
    private Timestamp dateTime;

    @Column(name = "threat_level")
    private int threatLevel;

    @ManyToOne
    @JoinColumn(name = "coordinate_id")
    private Coordinate coordinate;
}
