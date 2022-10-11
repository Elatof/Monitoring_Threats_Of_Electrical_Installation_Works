package com.korbiak.service.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "user_info")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "is_admin")
    private int isAdmin;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "day_of_birth")
    private Date dayOfBirth;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
