package com.korbiak.service.mapper;

import com.korbiak.service.dto.UserDto;
import com.korbiak.service.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<UserDto, User> {
    private final CompanyMapper companyMapper;

    @Override
    public UserDto toDto(User input) {
        return UserDto.builder()
                .id(input.getId())
                .firstName(input.getFirstName())
                .secondName(input.getSecondName())
                .password(input.getPassword())
                .title(input.getTitle())
                .email(input.getEmail())
                .isAdmin(input.getIsAdmin())
                .phone(input.getPhone())
                .dayOfBirth(input.getDayOfBirth())
                .company(companyMapper.toDto(input.getCompany()))
                .build();
    }

    @Override
    public User toEntity(UserDto input) {
        User entity = new User();
        entity.setId(input.getId());
        entity.setFirstName(input.getFirstName());
        entity.setSecondName(input.getSecondName());
        entity.setPassword(input.getPassword());
        entity.setTitle(input.getTitle());
        entity.setEmail(input.getEmail());
        entity.setIsAdmin(input.getIsAdmin());
        entity.setPhone(input.getPhone());
        entity.setDayOfBirth(input.getDayOfBirth());
        entity.setCompany(companyMapper.toEntity(input.getCompany()));

        return entity;
    }
}
