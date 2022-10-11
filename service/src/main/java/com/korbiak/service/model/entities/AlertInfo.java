package com.korbiak.service.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "alert_info")
@Data
public class AlertInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_time")
    private Timestamp dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
