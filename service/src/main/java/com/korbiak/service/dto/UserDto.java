package com.korbiak.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String secondName;
    private int isAdmin;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    @NotNull
    private Date dayOfBirth;
    @NotBlank
    private String title;
    @NotNull
    private CompanyDto company;
}
