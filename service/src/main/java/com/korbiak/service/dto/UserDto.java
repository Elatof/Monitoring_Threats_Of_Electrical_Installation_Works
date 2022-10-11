package com.korbiak.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String firstName;
    private String secondName;
    private int isAdmin;
    private String password;
    private String email;
    private String phone;
    private Date dayOfBirth;
    private String title;
    private CompanyDto company;
}
