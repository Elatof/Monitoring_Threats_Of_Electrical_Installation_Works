package com.korbiak.service.service;

import com.korbiak.service.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getCurrentUser();

    UserDto getUserById(int id);

    List<UserDto> getAdmins();

    List<UserDto> getAll();

    UserDto addNewUser(UserDto user);

    UserDto addNewAdmin(UserDto admin);

    UserDto updateUser(UserDto userDto);

    void removeUser(int id);
}
