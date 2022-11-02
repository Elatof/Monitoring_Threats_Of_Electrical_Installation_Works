package com.korbiak.service.service.impl;

import com.korbiak.service.dto.UserDto;
import com.korbiak.service.mapper.UserMapper;
import com.korbiak.service.model.entities.User;
import com.korbiak.service.repos.UserRepo;
import com.korbiak.service.security.jwt.JwtUser;
import com.korbiak.service.service.UserService;
import com.korbiak.service.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    public UserDto getCurrentUser() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byId = userRepo.findById(jwtUser.getId());
        return byId.map(userMapper::toDto).orElse(null);
    }

    @Override
    public UserDto getUserById(int id) {
        String authority = ServiceUtils.getAuthority();
        if (authority.equals("MAIN_ADMIN") || authority.equals("ADMIN")) {
            Optional<User> user = userRepo.findById(id);
            if (user.isPresent()) {
                return userMapper.toDto(user.get());
            } else {
                throw new IllegalArgumentException("User not found");
            }
        } else {
            throw new IllegalArgumentException("User is not MAIN_ADMIN");
        }
    }

    @Override
    public List<UserDto> getAdmins() {
        List<User> users;
        String authority = ServiceUtils.getAuthority();
        if (authority.equals("MAIN_ADMIN")) {
            users = userRepo.findAllByIsAdmin(2);
        } else {
            users = new ArrayList<>();
        }
        return userMapper.toDtos(users);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users;
        String authority = ServiceUtils.getAuthority();
        if (authority.equals("MAIN_ADMIN")) {
            users = userRepo.findAll();
        } else if (authority.equals("ADMIN")) {
            JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            users = userRepo.findAllByCompanyIdAndIsAdmin(jwtUser.getCompanyId(), 1);
        } else {
            users = new ArrayList<>();
        }
        return userMapper.toDtos(users);
    }

    @Override
    public UserDto addNewUser(UserDto user) {
        if (ServiceUtils.getAuthority().equals("ADMIN")) {
            JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user.getCompany().setId(jwtUser.getCompanyId());
            user.setIsAdmin(1);
            User entity = userMapper.toEntity(user);
            userRepo.save(entity);
            return userMapper.toDto(entity);
        } else {
            throw new IllegalArgumentException("User is not ADMIN of department");
        }
    }

    @Override
    public UserDto addNewAdmin(UserDto admin) {
        if (ServiceUtils.getAuthority().equals("MAIN_ADMIN")) {
            admin.setIsAdmin(2);
            User entity = userMapper.toEntity(admin);
            userRepo.save(entity);
            return userMapper.toDto(entity);
        } else {
            throw new IllegalArgumentException("User is not MAIN_ADMIN");
        }
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        if (ServiceUtils.getAuthority().equals("MAIN_ADMIN") || ServiceUtils.getAuthority().equals("ADMIN")) {
            User entity = userMapper.toEntity(userDto);
            userRepo.save(entity);
            return userMapper.toDto(entity);
        } else {
            throw new IllegalArgumentException("User is not MAIN_ADMIN or ADMIN");
        }
    }

    @Override
    public void removeUser(int id) {
        if (ServiceUtils.getAuthority().equals("MAIN_ADMIN") || ServiceUtils.getAuthority().equals("ADMIN")) {
            userRepo.deleteById(id);
        } else {
            throw new IllegalArgumentException("User is not MAIN_ADMIN or ADMIN");
        }
    }
}
