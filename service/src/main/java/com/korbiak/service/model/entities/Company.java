package com.korbiak.service.model.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "company")
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "logo_path")
    private String logoPath;
}
